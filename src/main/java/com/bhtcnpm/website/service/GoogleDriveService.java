package com.bhtcnpm.website.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GoogleDriveService {
    private static final String APPLICATION_NAME = "BHTWeb";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final java.io.File CREDENTIALS_FOLDER = new java.io.File(System.getProperty("user.home"), "credentials");

    private static final String CLIENT_SECRET_FILE_NAME = "client_key.json";

    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    private static FileDataStoreFactory DATA_STORE_FACTORY;

    private static HttpTransport HTTP_TRANSPORT;

    private static Drive _driveService;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(CREDENTIALS_FOLDER);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static com.google.api.services.drive.model.File _createGoogleFile (String googleFolderIdParent, String contentType, String customFileName, AbstractInputStreamContent uploadStreamContent) throws IOException {
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(customFileName);

        List<String> parents = Arrays.asList(googleFolderIdParent);

        fileMetadata.setParents(parents);

        Drive driveService = getDriveService();

        com.google.api.services.drive.model.File file = driveService.files().create(fileMetadata, uploadStreamContent).setFields("id, webContentLink, webViewLink, parents").execute();

        return file;
    }

    public static com.google.api.services.drive.model.File createGoogleFile(String googleFolderIdParent, String contentType, String customFileName, byte[] uploadData) throws IOException {
        AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentType, uploadData);
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    public static com.google.api.services.drive.model.File createGoogleFile(String googleFolderIdParent, String contentType, //
                                                                            String customFileName, java.io.File uploadFile) throws IOException {

        //
        AbstractInputStreamContent uploadStreamContent = new FileContent(contentType, uploadFile);
        //
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    public static com.google.api.services.drive.model.File createGoogleFile(String googleFolderIdParent, String contentType, //
                                                                            String customFileName, InputStream inputStream) throws IOException {

        //
        AbstractInputStreamContent uploadStreamContent = new InputStreamContent(contentType, inputStream);
        //
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    private static Credential getCredential () throws IOException {
        java.io.File clientSecretFilePath = new java.io.File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME);

        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("Please copy the secret to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
        }

        InputStream in = new FileInputStream(clientSecretFilePath);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER))
                .setAccessType("offline").build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static Drive getDriveService () throws IOException {
        if (_driveService != null) {
            return _driveService;
        }
        Credential credential = getCredential();
        _driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();

        return _driveService;
    }
}
