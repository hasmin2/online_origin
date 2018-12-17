package com.streamsets.stage.origin;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class HttpResponseCmd {
    private long duration = 0;
    private int returnValue =0;
    private final Logger LOG = LoggerFactory.getLogger(HttpResponseCmd.class);

    int runHttpResponseCommand(String ipAddress, int port, String subAddress)  {
        try {
            //Unirest.setTimeouts(networkTimeout, networkTimeout/2);
            long startTime = System.currentTimeMillis();
            HttpResponse<String> response = Unirest.get("http://" + ipAddress + ":" + Integer.toString(port) + subAddress)
                    .header("Cache-Control", "no-cache").asString();
            //System.out.println(response.getStatus());
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            returnValue = response.getStatus();

        }
        catch (UnirestException e) { LOG.info("Destination IP Address '{}:{}' unreached",ipAddress, port); }
        //finally { Unirest.shutdown(); }
        return returnValue;
    }

    long getTimegapLong(){ return duration; }
}
