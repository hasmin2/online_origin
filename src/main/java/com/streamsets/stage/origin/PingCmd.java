package com.streamsets.stage.origin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Thread.sleep;

public class PingCmd {
    String runPingCommand(String ip, int pingTimeout){
        Process p = null;
        BufferedReader in = null;
        String inputLine="";
        try {
            p = new ProcessBuilder("ping", ip).start();
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            sleep(pingTimeout);
            if(in.ready()){ in.readLine(); inputLine = in.readLine(); }
        }
        catch (IOException | NullPointerException e) { System.out.println(e); }
        catch (InterruptedException e) { e.printStackTrace(); }
        finally {
            try {
                assert p != null; p.destroy();
                assert in != null; in.close();
            } catch (IOException ignored) {}
        }
        return inputLine;
    }
}
