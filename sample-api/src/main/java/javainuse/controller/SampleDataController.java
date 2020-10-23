package javainuse.controller;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SampleDataController {

    @RequestMapping({"/userInfo"})
    public ResponseEntity<Map<String, String>>  getUserInfo(
            @RequestHeader Map<String, String> headers) {
        String header = headers.get("authorization");
        header = header.replaceAll("OAuth", "");
        header = header.replaceAll(" ", "");
        System.out.println(header);
        HttpResponse response = null;
        Map<String, String> infoMap = new HashMap<>();
        JSONObject jsonObject = null;
        System.out.println("\n_______________ Lead INSERT _______________");

        String uri = "https://aarnet--aarnetdev2.my.salesforce.com/services/oauth2/userinfo";
        try {

            //Construct the objects needed for the request
            HttpClient httpClient = HttpClientBuilder.create().build();
            Header oauthHeader = new BasicHeader("Authorization", "Bearer " + header);
            HttpGet httpPost = new HttpGet(uri);
            httpPost.addHeader(oauthHeader);
            String getResult = null;
            response = httpClient.execute(httpPost);
            try {
                getResult = EntityUtils.toString(response.getEntity());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                System.out.println(">>"+getResult);
                jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();

                infoMap.put("userId",jsonObject.getString("user_id")) ;
                infoMap.put("email",jsonObject.getString("email")) ;
                infoMap.put("user_type",jsonObject.getString("user_type")) ;

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        return new ResponseEntity<Map<String, String>>(infoMap, HttpStatus.OK);
    }




}