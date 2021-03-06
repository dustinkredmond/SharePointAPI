package com.dustinredmond.sharepoint;

import static com.dustinredmond.sharepoint.SharePointRequests.*;

/**
 * Constructs an instance of the SharePointAPI,
 * all public API is available through instance methods of this class
 */
public class SharePointAPI {

    private final Token authToken;

    /**
     * Takes in a token as a parameter and constructs
     * a class through which we access the SharePoint API. Use the
     * SharePointTokenFactory to get an instance of Token
     * @param authToken com.dustinredmond.sharepoint.Token with which to authenticate to SharePoint
     */
    public SharePointAPI(Token authToken) {
        this.authToken = authToken;
    }

    /**
     * Executes a HTTP GET request at the given path.
     * https://youDomain.sharepoint.com/${path goes here}
     * @param path The API endpoint path
     * @return The response as a String
     * @throws RuntimeException If the response's status code is other than 200
     */
    public String get(String path) {
        return doGet(path, authToken);
    }

    /**
     * Executes a HTTP POST request at the given path.
     * @param path The API endpoint path
     * @param data The data
     * @return The response as a String
     * @throws RuntimeException If the response's status code is other than 200
     */
    public String post(String path, String data) {
        return doPost(path, data, authToken);
    }

    /**
     * Executes a HTTP DELETE request at the given path.
     * @param path The API endpoint path
     * @return The response as a String
     * @throws RuntimeException If the response's status code is other than 200
     */
    public String delete(String path) {
        return doDelete(path, authToken);
    }

    /**
     * Gets an instance of the SharePointAPI class.
     * Provided as a convenience method, to avoid having to create
     * a token via SharePointTokenFactory.getToken()
     * @param username The SharePoint username e.g. person@example.com
     * @param password The SharePoint user's password
     * @param domain The subdomain of SharePoint
     * @return An instance of the SharePointAPI for making requests to SharePoint
     */
    public static SharePointAPI getInstance(String username, String password, String domain) {
        return new SharePointAPI(TokenFactory.getToken(username, password, domain));
    }

}
