package com.bhtcnpm.website.constant.business.Doc;

import com.bhtcnpm.website.model.exception.FileExtensionNotAllowedException;

import java.util.HashMap;
import java.util.Map;

public class AllowedUploadExtension {
    //Please only specify allowed extension list for doc upload here.
    private static final Map<String, String> allowedMimeTypesMapping = new HashMap<>();
    private static final Map<String, String> allowedImageTypesMapping = new HashMap<>();

    static {
        //Please only specify allowed extension. Don't specify anything that's not allowed.
        allowedMimeTypesMapping.put("pdf", "application/pdf");
        allowedMimeTypesMapping.put("doc", "application/msword");
        allowedMimeTypesMapping.put("docx", "application/vnd.openxmlformats");
        allowedMimeTypesMapping.put("jpeg", "image/jpeg");
        allowedMimeTypesMapping.put("jpg", "image/jpeg");
        allowedMimeTypesMapping.put("png", "image/png");
        allowedMimeTypesMapping.put("bmp", "image/bmp");
        allowedMimeTypesMapping.put("gif", "image/gif");
    }

    static {
        allowedImageTypesMapping.put("jpeg", "image/jpeg");
        allowedImageTypesMapping.put("jpg", "image/jpeg");
        allowedImageTypesMapping.put("png", "image/png");
        allowedImageTypesMapping.put("bmp", "image/bmp");
    }

    public static String getMimeType(String extension) throws FileExtensionNotAllowedException {
        String mimetype = allowedMimeTypesMapping.get(extension);

        if (mimetype == null) {
            throw new FileExtensionNotAllowedException();
        }

        return mimetype;
    }

    public static String getImageMimeType (String extension) throws FileExtensionNotAllowedException {
        String mimetype = allowedImageTypesMapping.get(extension);

        if (mimetype == null) {
            throw new FileExtensionNotAllowedException();
        }

        return mimetype;
    }
}
