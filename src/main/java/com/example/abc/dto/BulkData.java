package com.example.abc.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

public class BulkData {
    private String fileName;
    private MultipartFile file;

    public BulkData(String fileName, MultipartFile file) {
        this.fileName = fileName;
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
