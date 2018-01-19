package org.librehealth.convert.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApacheHttpClientGet {

    private final static ApacheHttpClientGet apacheHttpClientGet = new ApacheHttpClientGet();

    private ApacheHttpClientGet() {

    }

    public static ApacheHttpClientGet getInstance() {
        return apacheHttpClientGet;
    }

    public String executeGet(String url) {
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            //HttpGet getRequest = new HttpGet(
            //		"https://toolkit.librehealth.io/master/ws/rest/v1/person/4088066f-12b4-4fbb-8cf3-f7791dc6e5e6");
            HttpGet getRequest = new HttpGet(
                    url);
            getRequest.addHeader("accept", "application/json");
            getRequest.addHeader("Authorization", "Basic YWRtaW46QWRtaW4xMjM=");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (response.getEntity().getContent())));

            String fhirResource = "";
            String output;
            while ((output = br.readLine()) != null) {
                fhirResource = fhirResource + output;
            }

            httpClient.getConnectionManager().shutdown();
            return fhirResource;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
