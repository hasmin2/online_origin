package com.streamsets.stage.origin;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;


class HttpResponseCmd {
    private long duration = 0;
    private int returnValue =0;
    int runHttpResponseCommand(String ipAddress, int port, String subAddress, int networkTimeout) throws IOException {
        Unirest.setTimeouts(networkTimeout, networkTimeout/2);
        try {
            long startTime = System.currentTimeMillis();
            HttpResponse<String> response = Unirest.get("http://" + ipAddress + ":" + Integer.toString(port) + subAddress)
                    .header("Cache-Control", "no-cache").asString();
            //System.out.println(response.getStatus());
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            returnValue = response.getStatus();
            Unirest.shutdown();
        } catch (UnirestException | IOException e) {
                Unirest.shutdown();
            e.printStackTrace();
        }

        return returnValue;
    }

    long getTimegapLong(){ return duration; }
}
