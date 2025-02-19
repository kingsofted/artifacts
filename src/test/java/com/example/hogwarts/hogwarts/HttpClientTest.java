package com.example.hogwarts.hogwarts;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HttpClientTest {

    // Get
    @Test
    public void testGet() throws ClientProtocolException, IOException {
        // Create http client
        // HttpClient client = HttpClient.newHttpClient();

        // HttpRequest request = HttpRequest.newBuilder()
        // .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
        // .GET()
        // .build();

        // Create http client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Create http request
        HttpGet httpGet = new HttpGet("http://localhost:8081/api/v1/wizard");

        // Send request
        CloseableHttpResponse response = httpClient.execute(httpGet);

        // Test
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("RESPONSE status: " + statusCode);

        HttpEntity httpEntity = response.getEntity();
        String body = EntityUtils.toString(httpEntity);
        System.out.println("RESPONSE body: " + body);

        // Close
        response.close();
        httpClient.close();

    }

    // Post
    @Test
    public void testPOST() throws Exception {
        // Create http client
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // create http request
        HttpPost httpPost = new HttpPost("http://localhost:8081/api/v1/wizard");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "queen");

        StringEntity stringEntity = new StringEntity(jsonObject.toString());
        stringEntity.setContentEncoding("utf-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);

        // Send request
        CloseableHttpResponse response = httpClient.execute(httpPost);

        // Test
        System.out.println("Post respones: " + response);
        System.out.println("Post respones: " + EntityUtils.toString(response.getEntity()));

        // Close
        response.close();
        httpClient.close();
    }

}
