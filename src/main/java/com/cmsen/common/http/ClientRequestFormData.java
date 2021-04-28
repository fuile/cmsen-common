/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.http;

import java.io.File;

public class ClientRequestFormData {
    String name;
    String filename;
    String fileType;
    Object stream;

    public ClientRequestFormData() {
    }

    public ClientRequestFormData(String name, String filename, String fileType, Object stream) {
        this.name = name;
        this.filename = filename;
        this.fileType = fileType;
        this.stream = stream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Object getStream() {
        return stream;
    }

    public File getStreamFile() {
        return (File) stream;
    }

    public byte[] getStreamBytes() {
        return (byte[]) stream;
    }

    public String getStreamString() {
        return (String) stream;
    }

    public void setStream(File stream) {
        this.stream = stream;
    }

    public void setStream(byte[] stream) {
        this.stream = stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }
}
