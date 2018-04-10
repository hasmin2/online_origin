/**
 * Copyright 2015 StreamSets Inc.
 * <p>
 * Licensed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.stage.origin;

import com.streamsets.pipeline.api.BatchMaker;
import com.streamsets.pipeline.api.Field;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.base.BaseSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * This source is an example and does not actually read from anywhere.
 * It does however, generate generate a simple record with one field.
 */
public abstract class OnlineSource extends BaseSource {
    List <IPv4> ipv4List;
    /**
     * Gives access to the UI configuration of the stage provided by the {@link OnlineDSource} class.
     */
    @Override
    protected List<ConfigIssue> init() {
        // Validate configuration values and open any required resources.
        List<ConfigIssue> issues = super.init();
        /*if (getConfig().equals("invalidValue")) {
            issues.add(
                    getContext().createConfigIssue(
                            Groups.SAMPLE.name(), "config", Errors.SAMPLE_00, "Here's what's wrong..."
                    )
            );
        }*/

        // If issues is not empty, the UI will inform the user of each configuration issue in the list.
        ipv4List = new ArrayList<>();
        for( String key : getIPMap().keySet() ){ ipv4List.add(new IPv4(key, getIPMap().get(key))); }
        //System.out.println(ipv4List.size());
        return issues;
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        // Clean up any open resources.
        super.destroy();
    }

    /** {@inheritDoc} */
    @Override
    public String produce(String lastSourceOffset, int maxBatchSize, BatchMaker batchMaker) {
        // Offsets can vary depending on the data source. Here we use an integer as an example only.
        long nextSourceOffset = 0;
        if (lastSourceOffset != null) {
            nextSourceOffset = Long.parseLong(lastSourceOffset);
        }
        int numRecords = 0;

        // TODO: As the developer, implement your logic that reads from a data source in this method.

        // Create records and add to batch. Records must have a string id. This can include the source offset
        // or other metadata to help uniquely identify the record itself.
        try {
            Record record = getContext().createRecord(String.valueOf(nextSourceOffset));
            Map<String, Field> map = new HashMap<>();
            List <Field> list  = new ArrayList<>();
            long startTime = System.currentTimeMillis();
            if(usePing()) {
                ipv4List.forEach((item) -> {
                    for (String eachPing : item.getAvailableIPs(65535)) {
                        map.put("pingResult", Field.create(runPingCommand(eachPing)));
                        list.add(Field.create(map));
                    }
                });
            }

            long endTime = System.currentTimeMillis();
            long interval = getInterval(startTime, endTime);
            record.set(Field.create(list));
            batchMaker.addRecord(record);
            sleep(interval);
            ++nextSourceOffset;
            ++numRecords;
        }
        catch (InterruptedException e) { e.printStackTrace(); }
        finally { return String.valueOf(nextSourceOffset); }
    }
    private long getInterval (long startTime, long endTime){
        long elaspedTime = (endTime - startTime);
        long interval = getPingInterval()*1000 - elaspedTime;
        return interval > 0 ? interval : 0;
    }

    private String runPingCommand(String ip){
        try {
            Process p = new ProcessBuilder("ping", ip).start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            sleep(getPingTimeout());
            if(in.ready()){
                in.readLine();
                inputLine = in.readLine();
                p.destroy();
                in.close();
                return inputLine;
            }
        } catch (IOException | NullPointerException e) {
            System.out.println(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    public abstract Map<String, String> getIPMap();
    public abstract boolean usePing();
    public abstract boolean useWebsocket();
    public abstract boolean useHttpresponse();
    public abstract int getPingTimeout();
    public abstract int getPingInterval();
}
