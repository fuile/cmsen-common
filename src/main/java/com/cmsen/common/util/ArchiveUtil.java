/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ArchiveUtil {
    public static int zip(String targetFolderPath) {
        return zip(new File(targetFolderPath));
    }

    public static int zip(File targetFolderPath) {
        return zip(targetFolderPath, targetFolderPath.getAbsolutePath() + File.separator + targetFolderPath.getName() + ".zip", true);
    }

    public static int zip(String targetFolderPath, String destZipFile) {
        return zip(targetFolderPath, destZipFile, true);
    }

    public static int zip(String targetFolderPath, File destZipFile) {
        return zip(targetFolderPath, destZipFile, true);
    }

    public static int zip(String targetFolderPath, String destZipFile, boolean keepDirStructure) {
        return zip(targetFolderPath, new File(destZipFile), keepDirStructure);
    }

    public static int zip(String targetFolderPath, File destZipFile, boolean keepDirStructure) {
        return zip(targetFolderPath, destZipFile, FileUtil.scanFile(targetFolderPath), keepDirStructure);
    }

    public static int zip(File targetFolderPath, String destZipFile, boolean keepDirStructure) {
        return zip(targetFolderPath.getAbsolutePath(), new File(destZipFile), FileUtil.scanFile(targetFolderPath), keepDirStructure);
    }

    public static int zip(String targetFolderPath, File destZipFile, List<String> scanFilePaths, boolean keepDirStructure) {
        int fileCount = 0;
        try {
            if (!destZipFile.exists()) {
                destZipFile.createNewFile();
            }
            byte[] buf = new byte[1024];
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZipFile));
            for (int i = 0; i < scanFilePaths.size(); i++) {
                String relativePath = scanFilePaths.get(i);
                if (StringUtil.isBlank(relativePath)) {
                    continue;
                }
                File sourceFile = new File(relativePath);
                if (!sourceFile.exists() || (!keepDirStructure && sourceFile.isDirectory())) {
                    continue;
                }
                if (keepDirStructure) {
                    if (sourceFile.isDirectory()) {
                        relativePath = relativePath + File.separator;
                    }
                    String name = FileUtil.transformPath(relativePath, "").replace(FileUtil.transformPath(targetFolderPath), "");
                    //保持目录结构
                    zos.putNextEntry(new ZipEntry(name));
                } else {
                    //直接放到压缩包的根目录
                    zos.putNextEntry(new ZipEntry(sourceFile.getName()));
                }
                if (sourceFile.isFile()) {
                    FileInputStream fis = new FileInputStream(sourceFile);
                    int len;
                    while ((len = fis.read(buf)) > 0) {
                        zos.write(buf, 0, len);
                    }
                    zos.closeEntry();
                    fis.close();
                    fileCount++;
                }
            }
            zos.close();
        } catch (IOException e) {
            if (destZipFile.exists()) {
                destZipFile.delete();
            }
            e.printStackTrace();
        }
        return fileCount;
    }


    public static int unZip(String destZipFile) {
        File file = new File(destZipFile);
        return unZip(file, file.getParent(), StandardCharsets.UTF_8);
    }

    public static int unZip(String destZipFile, Charset charset) {
        File file = new File(destZipFile);
        return unZip(file, file.getParent(), charset);
    }

    public static int unZip(String destZipFile, String outPutPath) {
        return unZip(new File(destZipFile), outPutPath, StandardCharsets.UTF_8);
    }

    public static int unZip(String destZipFile, String outPutPath, Charset charset) {
        return unZip(new File(destZipFile), outPutPath, charset);
    }

    public static int unZip(File destZipFile, String outPutPath, Charset charset) {
        int fileCount = 0;
        try {
            byte[] buf = new byte[1024];
            ZipFile zipFile = new ZipFile(destZipFile, ZipFile.OPEN_READ, charset);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                String zipEntryName = zipEntry.getName();
                boolean zipIsDirectory = zipEntryName.endsWith(File.separator);
                File file = new File(FileUtil.transformPath(outPutPath), zipEntryName);
                if (zipIsDirectory && !file.exists()) {
                    if (!file.mkdirs()) {
                        throw new IOException("Invalid file path");
                    }
                }
                if (!new File(file.getAbsolutePath()).isDirectory()) {
                    FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, len);
                    }
                    inputStream.close();
                    outputStream.close();
                    fileCount++;
                }
            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileCount;
    }

    public static String readFile(String destZipFile, String name) {
        return readFile(destZipFile, name, StandardCharsets.UTF_8);
    }

    public static String readFile(String destZipFile, String name, Charset charset) {
        byte[] bytes = readFile(new File(destZipFile), name, charset);
        return new String(bytes == null ? new byte[0] : bytes);
    }

    public static String readFile(File destZipFile, String name) {
        byte[] bytes = readFile(destZipFile, name, StandardCharsets.UTF_8);
        return new String(bytes == null ? new byte[0] : bytes);
    }

    public static byte[] readFile(File destZipFile, String name, Charset charset) {

        InputStream inputStream = readFileInputStream(destZipFile, name, charset);
        if (inputStream != null) {
            return FileUtil.getBytes(inputStream, 1024);
        }
        return null;
    }

    public static InputStream readFileInputStream(String destZipFile, String name) {
        return readFileInputStream(new File(destZipFile), name);
    }

    public static InputStream readFileInputStream(File destZipFile, String name) {
        return readFileInputStream(destZipFile, name, StandardCharsets.UTF_8);
    }

    public static InputStream readFileInputStream(File destZipFile, String name, Charset charset) {
        try {
            ZipFile zipFile = new ZipFile(destZipFile, ZipFile.OPEN_READ, charset);
            ZipEntry entry = zipFile.getEntry(name);
            if (entry != null) {
                return zipFile.getInputStream(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> readFileLists(String destZipFile) {
        return readFileLists(new File(destZipFile), StandardCharsets.UTF_8);
    }

    public static List<String> readFileLists(File destZipFile) {
        return readFileLists(destZipFile, StandardCharsets.UTF_8);
    }

    public static List<String> readFileLists(String destZipFile, Charset charset) {
        return readFileLists(new File(destZipFile), charset);
    }

    public static List<String> readFileLists(File destZipFile, Charset charset) {
        List<String> lists = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(destZipFile, ZipFile.OPEN_READ, charset)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                lists.add(entries.nextElement().getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lists;
    }

    public static String getComment(String destZipFile) {
        return getComment(new File(destZipFile));
    }

    public static String getComment(File destZipFile) {
        try {
            ZipFile zipFile = new ZipFile(destZipFile);
            String comment = zipFile.getComment();
            zipFile.close();
            return comment;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setComment(String destZipFile, String comment) {
        setComment(new File(destZipFile), comment);
    }

    public static void setComment(File destZipFile, String comment) {
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destZipFile));
            if (StringUtil.isNotBlank(comment)) {
                out.setComment(comment);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
