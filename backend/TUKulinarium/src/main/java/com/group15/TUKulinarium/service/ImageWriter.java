package com.group15.TUKulinarium.service;

import com.group15.TUKulinarium.exception.ImageWriterException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageWriter {
    String WriteImage(MultipartFile multipartFile) throws ImageWriterException;
}
