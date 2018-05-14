package com.streamsets.stage.origin;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;


public class HttpResponseCmd {
    private HttpResponse<String> response=null;
    private long duration = 0;
    private int returnValue =0;
    int runHttpResponseCommand(String ipAddress, int port, String subAddress, int networkTimeout) {
        Unirest.setTimeouts(networkTimeout, networkTimeout/2);
        try {
            long startTime = System.currentTimeMillis();
            response = Unirest.get("http://"+ipAddress+":"+Integer.toString(port)+subAddress)
                    .header("Cache-Control", "no-cache").asString();
            //System.out.println(response.getStatus());
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            returnValue = response.getStatus();

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
    void shutDown(){
        try {
            Unirest.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    long getTimegapLong(){ return duration; }
}
