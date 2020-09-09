import com.dustinredmond.sharepoint.SharePointAPI;
import com.dustinredmond.sharepoint.TokenFactory;
import com.dustinredmond.sharepoint.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.math.BigDecimal;

public class Test {

    public static void main(String[] args) {

        String username = "aUser"; // e.g. admin@myCompany.com
        String password = "theirPassword";
        // This is the subdomain of SharePoint, if your SharePoint url were
        //      https://myCompany.sharepoint.com/
        // You would supply the domain as 'myCompany'
        String domain = "myCompany";

        // We have to create our Token for authentication
        Token token = TokenFactory.getToken(username, password, domain);
        // We can now access API methods through the SharePointAPI class
        SharePointAPI api = new SharePointAPI(token);

        // We can now make a GET request use our SharePointAPI object
        // The below will get Invoice with ID 130 from the
        // InvoiceRetention SharePoint site's Invoices list
        final String site = "/Sites/InvoiceRetention";
        String invoice = api.get(site+"/_api/web/lists/GetByTitle('Invoices')/Items(130)");

        // We can use Google's Gson library to make our JSON print prettily
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(JsonParser.parseString(invoice));
        System.out.println(prettyJson);

        // Using Gson's JsonParser class, we can get our Invoice as an "object" of sorts
        JsonObject rootElement = JsonParser.parseString(invoice).getAsJsonObject();

        // In this example, the fields we wanted were under the "d" object
        // this could be different in your SharePoint list, this structure is handled
        // internally by SharePoint
        JsonObject dObject = rootElement.getAsJsonObject("d");

        // Our "d" object actually looked like below:
            /*
                "d": {
                    "Title": "5122552",
                    "Date_x0020_Invoiced": "2020-05-22T04:00:00Z",
                    "InvoiceAmount": 43212.08
                }
             */

        // If we wanted to get the InvoiceAmount field, we can choose a type,
        // based off of the JsonObject methods, since this is currency, how
        // about a BigDecimal?
        BigDecimal invoiceAmount = dObject.get("InvoiceAmount").getAsBigDecimal();
        assert invoiceAmount.equals(new BigDecimal("43212.08"));
    }
}
