package com.group15.TUKulinarium.service.impl;

import com.google.gson.Gson;
import com.group15.TUKulinarium.service.ImageDeleter;
import com.group15.TUKulinarium.utils.HttpDeleteWithBody;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CDNImageDeleter implements ImageDeleter {
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

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public Gson getGsonDecoder() {
        return gsonDecoder;
    }

    @Override
    public void DeleteImage(String imageLink) {
        if(imageLink.equals("image.default.jpg")) return;

        var deleteLink = getDeleteLink(imageLink);
        try {
            deleteImageHttp(deleteLink);
        } catch (IOException ignored) {}
    }

    private String getDeleteLink(String imageLink) {
        return getAddress() + "/" + imageLink;
    }

    private void deleteImageHttp(String deleteLink) throws IOException {
        HttpDeleteWithBody delete = new HttpDeleteWithBody(deleteLink);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addTextBody("token", token);
        HttpEntity entity = builder.build();
        delete.setEntity(entity);
        HttpResponse response = httpClient.execute(delete);
        var statusCode = response.getStatusLine().getStatusCode();
        if (statusCode!= 204) {
            logger.warn(String.format("CDN Server Response: %s", EntityUtils.toString(response.getEntity())));
            throw new HttpResponseException(statusCode, "can't delete this file");
        }
    }
}
