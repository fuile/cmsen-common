/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.util;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class FileUtil {
    private static final Map<String, String> mime = new HashMap<>();

    static {
        mime.put(".jpeg", "image/jpeg");
        mime.put(".jpg", "image/jpeg");
        mime.put(".png", "image/png");
        mime.put(".gif", "image/gif");
        mime.put(".pdf", "application/pdf");
        mime.put(".json", "application/json");
        mime.put(".doc", "application/msword");
        mime.put(".xls", "application/vnd.ms-excel");
        mime.put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        mime.put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static void setMime(String type, String value) {
        mime.put(type, value);
    }

    /**
     * 系统临时文件路径
     */
    public static String getTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String getUseDir() {
        return System.getProperty("user.dir");
    }

    public static String getMimeType(String filename) {
        String ext = getExtension(filename);
        String s = mime.get(getExtension(filename));
        if (null == s) {
            return new MimetypesFileTypeMap().getContentType(filename);
        }
        return "";
    }

    /**
     * 获取Mime类型
     *
     * @param file File对象
     * @return MimeType
     */
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
        filename = filename.replace("/", File.separator);
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
        int i = (null == filename ? "" : filename).lastIndexOf(".");
        if (i == -1) {
            return "";
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
     * 获得指定文件的byte数组
     *
     * @param file File对象
     * @return byte[]
     */
    public static byte[] getBytes(File file) {
        return getBytes(file, 1024);
    }

    public static String getResource(String path) {
        String buffer = null;
        InputStream resourceAsStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            resourceAsStream = FileUtil.class.getClassLoader().getResourceAsStream(path);
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

    public static final Kind<Path> WATCH_CREATE = StandardWatchEventKinds.ENTRY_CREATE;
    public static final Kind<Path> WATCH_MODIFY = StandardWatchEventKinds.ENTRY_MODIFY;
    public static final Kind<Path> WATCH_DELETE = StandardWatchEventKinds.ENTRY_DELETE;

    public static void watchFolder(String path, Function<WatchEvent<?>, ?> watchEvent, Kind<?>... events) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path paths = Paths.get(path);
        paths.register(watchService, events);
        while (true) {
            WatchKey watchKey = watchService.take();
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                watchEvent.apply(event);
            }
            watchKey.reset();
        }
    }
}
