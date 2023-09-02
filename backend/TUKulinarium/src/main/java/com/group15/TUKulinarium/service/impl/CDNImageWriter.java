package com.group15.TUKulinarium.service.impl;

import com.google.gson.Gson;
import com.group15.TUKulinarium.exception.ImageWriterException;
import com.group15.TUKulinarium.payload.response.CDNErrorResponse;
import com.group15.TUKulinarium.payload.response.CDNResponse;
import com.group15.TUKulinarium.service.ImageWriter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class CDNImageWriter implements ImageWriter {
    private static final Logger logger = LoggerFactory.getLogger(CDNImageWriter.class);

    @Value("${group15.tukulinarium.cdnUploadImageAddress}")
    private String address;

    @Value("${group15.tukulinarium.cdnPermissionToken}")
    private String token;

    private final HttpClient httpClient = HttpClientBuilder.create().build();

    private final Gson gsonDecoder = new Gson();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String WriteImage(MultipartFile multipartFile) throws ImageWriterException {
        File tempFile = null;
        try {
            tempFile = getTemporaryFile(multipartFile);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ImageWriterException("couldn't extract file from multipart body");
        }
        boolean fileDelete = false;
        String serverFileName = "";
        try {
            serverFileName = uploadFileAndGetPath(tempFile);
        } catch (Exception ex){
            logger.error(ex.getMessage());
            throw new ImageWriterException(String.format("couldn't upload image %s", tempFile.getName()));
        } finally {
            fileDelete = tempFile.delete();
        }
        if (!fileDelete){
            logger.error(String.format("couldn't delete file: %s", tempFile.getName()));
            throw new ImageWriterException(String.format("couldn't delete file %s", tempFile.getName()));
        }
        return  serverFileName;
    }

    private File getTemporaryFile(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null) {
            throw new IOException("can't retrieve image name");
        }
        File tempFile = new File("temp/" + fileName);
        //Reading multipart file data
        try {
            OutputStream os = new FileOutputStream(tempFile);
            os.write(multipartFile.getBytes());
            os.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new IOException(
                    String.format("couldn't read the file with filename: %s", fileName));
        }

        return tempFile;
    }

    private String uploadFileAndGetPath(File file) throws IOException {
        HttpPost post = new HttpPost(address);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("file", file);
        builder.addTextBody("token", token);
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        entity = response.getEntity();
        int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
            case 200:
                CDNResponse responseBody = gsonDecoder.fromJson(
                        EntityUtils.toString(entity), CDNResponse.class
                );
                return responseBody.getServerFilename();
            case 408:
                throw new HttpResponseException(statusCode, "CDN server timedout");
            case 500:
                CDNErrorResponse errorResponse = gsonDecoder.fromJson(
                        EntityUtils.toString(entity), CDNErrorResponse.class
                );
                throw new HttpResponseException(statusCode, errorResponse.getError());
            default:
                throw new HttpResponseException(400, "something unexpected happened");
        }
    }
}
