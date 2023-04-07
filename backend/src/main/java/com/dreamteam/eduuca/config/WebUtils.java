package com.dreamteam.eduuca.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.URI;

@Log4j2
public class WebUtils {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final OkHttpClient client = new OkHttpClient();

    static {
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    public static <T> T post(URI uri, Object requestBody, Class<T> responseClass) throws IOException {
        log.debug("post() called. URI: {}, requestBody: {}", () -> uri, () -> requestBody);

        String requestBodyString = new ObjectMapper().writeValueAsString(requestBody);
        log.trace("post(). Request JSON: {}", () -> requestBodyString);

        Request request = new Request.Builder()
                .url(uri.toURL().toString())
                .post(RequestBody.create(requestBodyString, MEDIA_TYPE_JSON))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) {
                throw new IOException("Response body is NULL");
            }

            String responseBodyString = response.body().string();
            log.trace("post(). Response JSON: {}", () -> responseBodyString);

            return objectMapper.readValue(responseBodyString, responseClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
