package com.dustinredmond.auth;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SharePointAPI {

    private final Token authToken;


    public SharePointAPI(Token authToken) {
        this.authToken = authToken;
    }

    /**
     * Returns the contextinfo String obtained by sending
     * a GET request to https://yourDomain.sharepoint.com/_api/contextinfo
     * @return SharePoint's /_api/contextinfo as a String
     * @throws RuntimeException If the response's status code is other than 200
     */
    @SuppressWarnings("unused")
    public String contextInfo() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost get = new HttpPost("https://" + this.authToken.getDomain() + ".sharepoint.com/_api/contextinfo");
            get.addHeader("Cookie", String.format("%s;%s", this.authToken.getRtFa(), this.authToken.getFedAuth()));
            get.addHeader("accept", "application/json;odata=verbose");
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                return inStreamToString(response.getEntity().getContent());
            } else {
                throw new RuntimeException("Error code: " + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes a HTTP GET request at the given path.
     * https://youDomain.sharepoint.com/${path goes here}
     * @param path The API endpoint path
     * @return The response as a String
     * @throws RuntimeException If the response's status code is other than 200
     */
    public String get(String path) {
        return getRequest("https://" + this.authToken.getDomain() + ".sharepoint.com/" + path);
    }

    /**
     * Executes a HTTP POST request at the given path.
     * @param path The API endpoint path
     * @param data The data
     * @param formDigestValue The X-RequestDigest value
     * @return The response as a String
     * @throws RuntimeException If the response's status code is other than 200
     */
    @SuppressWarnings("unused")
    public String post(String path, String data, String formDigestValue) {
        return postRequest(path, data, formDigestValue);
    }

    /**
     * Executes a HTTP DELETE request at the given path.
     * @param path The API endpoint path
     * @param formDigestValue The X-RequestDigest value
     * @return The response as a String
     * @throws RuntimeException If the response's status code is other than 200
     */
    @SuppressWarnings("unused")
    public String delete(String path, String formDigestValue) {
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

    private String getRequest(String url) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Cookie", String.format("%s;%s", this.authToken.getRtFa(), this.authToken.getFedAuth()));
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

    private String postRequest(String path, String data, String formDigestValue) {
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

    private String inStreamToString(InputStream in) throws IOException {
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
