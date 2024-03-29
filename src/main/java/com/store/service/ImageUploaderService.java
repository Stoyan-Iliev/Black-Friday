package com.store.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import com.store.exception.ImageUploaderServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.store.util.FileUploadUtil.formatUrl;
import static com.store.util.FileUploadUtil.generateFileName;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Service
public class ImageUploaderService {
    private final static String METADATA_DOWNLOAD_TOKENS_KEY = "firebaseStorageDownloadTokens";
    private final static String SERVICE_ACCOUNT_JSON_URL = "D:/Java/Black-Friday/src/main/resources/black-friday-store-firebase-adminsdk-zfyrc-3078039d24.json";
    private final static String IMAGE_URL_FORMAT = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media&token=%s";

    private static final String DASH = "-";
    private static final String WHITESPACE = "\\s+";
    private final static String FORWARD_SLASH =  "/";
    private final static String URL_ENCODED_FORWARD_SLASH =  "%2F";

    @Value("${firebase.bucket-name}")
    private String bucketName;

    public Set<String> upload(Collection<MultipartFile> images, String productName) {
        HashSet<BlobId> failedBloIds = new HashSet<>();
        final Storage[] storage = {StorageOptions.newBuilder().build().getService()};
        final Exception[] occurredException = {null};

        Set<String> imageUrls = images.stream().map(imageFile -> {
            String filePath = (formatUrl(productName).replaceAll(WHITESPACE, DASH) + "/" + generateFileName(imageFile))
                    .replace("?", "");
            BlobId blobId = BlobId.of(bucketName, filePath);
            Map<String, String> metadata = new HashMap<>();
            metadata.put(METADATA_DOWNLOAD_TOKENS_KEY, filePath);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(imageFile.getContentType())
                    .setMetadata(metadata)
                    .build();

            String token = "";
            try {
                GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(SERVICE_ACCOUNT_JSON_URL));
                storage[0] = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
                storage[0].create(blobInfo, imageFile.getBytes());
                token = formatUrl(storage[0].get(bucketName, filePath).getMetadata().get(METADATA_DOWNLOAD_TOKENS_KEY));
            } catch (IOException ioe) {
                failedBloIds.add(blobId);
                occurredException[0] = ioe;
            }

            return String.format(IMAGE_URL_FORMAT, bucketName, formatUrl(filePath), formatUrl(token));
        }).collect(Collectors.toSet());

        if (!failedBloIds.isEmpty()) {
            storage[0].delete(failedBloIds);
            throw new ImageUploaderServiceException(buildFailedImageUploadsMessage(failedBloIds), occurredException[0]);
        }

        return imageUrls;
    }


    public void deleteImagesForProduct(Collection<String> imageUrls) {
        if (isNotEmpty(imageUrls)) {
            Set<BlobId> blobIds = imageUrls.stream()
                    .map(url -> {
                        int startIndex = url.indexOf("/o/") + 3;
                        int endIndex = url.indexOf("?");
                        String urlToDelete = formatUrl(url.substring(startIndex, endIndex));
                        return BlobId.of(bucketName, urlToDelete);
                    })
                    .collect(Collectors.toSet());

            GoogleCredentials credentials;
            try {
                credentials = GoogleCredentials.fromStream(new FileInputStream(SERVICE_ACCOUNT_JSON_URL));
            } catch (IOException e) {
                throw new ImageUploaderServiceException("Failed to load google credentials", e);
            }

            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            storage.delete(blobIds);
        }
    }

    private String buildFailedImageUploadsMessage(Collection<BlobId> failedBloIds) {
        StringBuilder builder = new StringBuilder("Failed images:{");
        failedBloIds.stream().map(BlobId::getName).forEach(name -> builder.append(name).append(", "));
        builder.append("}");
        return builder.toString();
    }

    private String formatUrl(String str) {
        return str.replace(FORWARD_SLASH, URL_ENCODED_FORWARD_SLASH);
    }
}
