package com.dustinredmond.sharepoint;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SharePointRequests {

    protected static String doGet(String url, Token authToken) {
        final String urlPath = "https://" + authToken.getDomain() + ".sharepoint.com/" + url;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(urlPath);
            httpGet.addHeader("Cookie", String.format("%s;%s", authToken.getRtFa(), authToken.getFedAuth()));
            httpGet.addHeader("accept", "application/json;odata=verbose");
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                return inStreamToString(response.getEntity().getContent());
            } else {
                throw new RuntimeException("Error code: " + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String doPost(String path, String data, String formDigestValue, Token authToken) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("https://" + authToken.getDomain() + ".sharepoint.com/" + path);
            post.addHeader("Cookie", authToken.getRtFa() + ";" + authToken.getFedAuth());
            post.addHeader("accept", "application/json;odata=verbose");
            post.addHeader("content-type", "application/json;odata=verbose");
            post.addHeader("X-RequestDigest", formDigestValue);
            post.addHeader("IF-MATCH", "*");

            if (data != null) {
                StringEntity input = new StringEntity(data, StandardCharsets.UTF_8);
                input.setContentType("application/json");
                post.setEntity(input);
            }

            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 204) {
                throw new RuntimeException("Error code: " + response.getStatusLine().getStatusCode());
            }
            if (response.getEntity() == null || response.getEntity().getContent() == null) {
                return null;
            } else {
                return inStreamToString(response.getEntity().getContent());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String doDelete(String path, String formDigestValue, Token authToken) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDelete del = new HttpDelete("https://" + authToken.getDomain() + ".sharepoint.com/" + path);
            del.addHeader("Cookie", authToken.getRtFa() + ";" + authToken.getFedAuth());
            del.addHeader("accept", "application/json;odata=verbose");
            del.addHeader("content-type", "application/json;odata=verbose");
            del.addHeader("X-RequestDigest", formDigestValue);
            del.addHeader("IF-MATCH", "*");
            HttpResponse response = client.execute(del);
            if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 204) {
                throw new RuntimeException("Error code: " + response.getStatusLine().getStatusCode());
            }
            if (response.getEntity() == null || response.getEntity().getContent() == null) {
                return null;
            } else {
                return inStreamToString(response.getEntity().getContent());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String inStreamToString(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        Charset cs = Charset.forName(StandardCharsets.UTF_8.name());
        try (Reader reader = new BufferedReader(new InputStreamReader(in, cs))) {
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
        }
        return sb.toString();
    }
}
