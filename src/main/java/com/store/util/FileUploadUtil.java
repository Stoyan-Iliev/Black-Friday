package com.store.util;;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class FileUploadUtil {
    private final static String FORWARD_SLASH =  "/";
    private final static String URL_ENCODED_FORWARD_SLASH =  "%2F";

    public static String generateFileName(MultipartFile multiPart) {
        String originalFilename = formatUrl(requireNonNull(multiPart.getOriginalFilename()));
        return UUID.randomUUID() + "-" + originalFilename.replace(" ", "");
    }

    public static String formatUrl(String str) {
        return str.replace(FORWARD_SLASH, URL_ENCODED_FORWARD_SLASH);
    }
}