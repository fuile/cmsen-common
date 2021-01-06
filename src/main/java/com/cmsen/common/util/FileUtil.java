/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.util;

import java.io.*;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;

/**
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class FileUtil {
    /**
     * 系统临时文件路径
     */
    public static String getTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String getUseDir() {
        return System.getProperty("user.dir");
    }

    public static String getRunCurrentDirectory(Class<?> clazz) {
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (System.getProperty("os.name").contains("Windows")) {
            path = path.replace("\\", "/")
                    .replace("file:/", "");
            if (path.substring(0, 1).equals("/")) {
                path = path.replaceFirst("/", "");
            }
        }
        if (path.contains(".jar")) {
            path = path.substring(0, path.lastIndexOf("."));
            return path.substring(0, path.lastIndexOf("/"));
        }
        return path.replace("%20", " ")
                .replace("target/classes/", "")
                .replace("//", "/");
    }

    /**
     * 获取Mime类型
     *
     * @param filename 文件路径或后缀
     * @return MimeType
     */
    @Deprecated
    public static String getMimeType(String filename) {
        return FileMime.get(filename, FileMime.STREAM);
    }

    /**
     * 获取Mime类型
     *
     * @param file File对象
     * @return MimeType
     */
    @Deprecated
    public static String getMimeType(File file) {
        return getMimeType(file.getName());
    }

    /**
     * 获取创建时间
     *
     * @param pathname 路径名
     * @return long
     */
    public static long getCreateTime(String pathname) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(new File(pathname).toPath(), BasicFileAttributes.class);
            return attributes.creationTime().toMillis();
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param dirname 文件夹名
     * @return long
     */
    public static long getDirSize(String dirname) {
        try {
            return Files.walk(new File(dirname).toPath())
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .mapToLong(File::length)
                    .sum();
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * 获取文件名
     *
     * @param filename 文件名
     * @return 扩展名
     */
    public static String getFilename(String filename) {
        if (filename == null) {
            return "";
        }
        filename = filename.replaceAll("[\\/|\\\\]+", Matcher.quoteReplacement(File.separator));
        int i = filename.lastIndexOf(File.separator);
        if (i == -1) {
            return "";
        }
        return filename.substring(i + 1);
    }

    /**
     * 获取扩展名
     *
     * @param filename 文件名
     * @return 扩展名
     */
    public static String getExtension(String filename) {
        return getExtension(filename, false);
    }

    public static String getExtension(String filename, boolean symbol) {
        int i = (null == filename ? "" : filename).lastIndexOf(".");
        if (i == -1) {
            return "";
        }
        if (symbol) {
            i = i + 1;
        }
        return filename.substring(i);
    }

    /**
     * 字节格式化
     *
     * @param length 字节数
     * @return string
     */
    public static String formatByte(double length) {
        return formatByte(length, 2);
    }

    /**
     * 字节格式化
     *
     * @param length    字节数
     * @param precision 小数位
     * @return string
     */
    public static String formatByte(double length, int precision) {
        return formatByte(length, precision, " ");
    }

    /**
     * 字节格式化
     *
     * @param length    字节数
     * @param delimiter 分隔符
     * @param precision 小数位
     * @return string
     */
    public static String formatByte(double length, int precision, String delimiter) {
        if (length <= 0) {
            return "0" + delimiter + "B";
        }
        String[] units = {"B", "KB", "MB", "GB", "TB", "PB"};
        int i = (int) (Math.log10(length) / Math.log10(1024));
        return String.format("%." + precision + "f", length / Math.pow(1024, i)) + delimiter + units[i];
    }

    /**
     * 路径地址修正
     *
     * @param path 路径
     * @return string
     */
    public static String transformPath(String path) {
        return transformPath(path, File.separator);
    }

    public static String transformPath(String path, String separator) {
        // 判断是否已分割符结尾，如果没有测自动补充
        path = path.replaceAll("[\\/|\\\\]+", Matcher.quoteReplacement(separator));
        if (path.lastIndexOf(separator) == -1) {
            path = path + separator;
        }
        return path;
    }

    /**
     * 获得指定文件的byte数组
     *
     * @param file File对象
     * @return byte[]
     */
    public static byte[] getBytes(File file) {
        return getBytes(file, 1024);
    }

    public static InputStream getResourceAsStream(String path) {
        return FileUtil.class.getClassLoader().getResourceAsStream(path);
    }

    public static String getResource(String path) {
        String buffer = null;
        InputStream resourceAsStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            resourceAsStream = getResourceAsStream(path);
            if (null != resourceAsStream) {
                outputStream = new ByteArrayOutputStream(1024);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = resourceAsStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                }
                buffer = outputStream.toString();
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
                if (null != resourceAsStream) {
                    resourceAsStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
        return buffer;
    }

    /**
     * 获得指定文件的byte数组
     *
     * @param file File对象
     * @return byte[]
     */
    public static byte[] getBytes(File file, int size) {
        try {
            return getBytes(new FileInputStream(file), size);
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public static byte[] getBytes(InputStream inputStream, int size) {
        byte[] buffer = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(size);
        try {
            byte[] bytes = new byte[size];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            buffer = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                outputStream.close();
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
        return buffer;
    }

    public static boolean saveFile(InputStream fis, String savePath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(savePath);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > -1) {
                fos.write(buffer, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                if (null != fos) {
                    fos.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
        return false;
    }

    public static String repeatString(String str, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(str);
        }
        return sb.substring(0, sb.length());
    }

    public static final Kind<Path> WATCH_CREATE = StandardWatchEventKinds.ENTRY_CREATE;
    public static final Kind<Path> WATCH_MODIFY = StandardWatchEventKinds.ENTRY_MODIFY;
    public static final Kind<Path> WATCH_DELETE = StandardWatchEventKinds.ENTRY_DELETE;

    public static void watchFolder(String path, Function<WatchEvent<?>, Boolean> watchEvent, Kind<?>... events) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path paths = Paths.get(path);
        paths.register(watchService, events);
        while (true) {
            WatchKey watchKey = watchService.take();
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                if (watchEvent.apply(event)) {
                    break;
                }
            }
            watchKey.reset();
        }
    }

    public static boolean checkFileWritingOn(String fileName) {
        long oldLen = 0;
        long newLen = 0;
        File file = new File(fileName);
        while (true) {
            newLen = file.length();
            if ((newLen - oldLen) > 0) {
                oldLen = newLen;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                return true;
            }
        }
    }

    public static List<String> scanFile(String folderPath) {
        return scanFile(new File(folderPath), new ArrayList<>());
    }

    public static List<String> scanFile(File folderPath) {
        return scanFile(folderPath, new ArrayList<>());
    }

    public static List<String> scanFile(String folderPath, List<String> scanFiles) {
        return scanFile(new File(folderPath), scanFiles);
    }

    public static List<String> scanFile(File folderPath, List<String> scanFiles) {
        if (folderPath.isDirectory()) {
            File[] files = folderPath.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    if (folderPath.isDirectory()) {
                        scanFiles.add(file1.getAbsolutePath());
                        scanFile(file1.getAbsolutePath(), scanFiles);
                    } else {
                        scanFiles.add(file1.getAbsolutePath());
                    }
                }
            }
        }
        return scanFiles;
    }
}
