# SharePointAPI

Small java library to allow programmatically sending REST calls to SharePoint sites.
You can authenticate with a username, password, and provide the subdomain of SharePoint
with which you wish to authenticate, then you can make API calls.

---

### Authenticating

First we need to import the library and provide `SharePointAPI` with an auth token.
The library will handle getting the token and confirming it with the SharePoint domain for us.

Now we can authenticate, and we will be able to make REST requests using the
`SharePointAPI` class's instance methods.
```java
import com.dustinredmond.sharepoint.TokenFactory;
import com.dustinredmond.sharepoint.SharePointAPI;
    
class TestSharePoint {

    public static void main(String[] args) {
    String user, pass, domain;
    user    =      "somebody@example.com";
    pass    =      "someVerySecurePassword";
    domain  =       "myCompany"; // If your site is https://myCompany.sharepoint.com/

    SharePointAPI api = new SharePointAPI(TokenFactory.getToken(user,pass,domain));
    }
}
    
```

---

### Making Requests

Now, through the api variable, we have access to GET, POST, and DELETE methods.

```java
    String invoiceJson = api.get("/Sites/InvoiceRetention/_api/web/lists/GetByTitle('Invoices')/Items(130)");
```

The above code will get Invoice with ID == 130 from the Invoices SharePoint List on the 
InvoiceRetention SharePoint site. Read the Microsoft REST documentation to figure out what
endpoints you need.

We can then use some other library to parse the JSON that is returned.
Please feel free to submit a pull request, or fork the repo. 