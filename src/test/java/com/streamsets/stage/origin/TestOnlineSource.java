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

import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.sdk.SourceRunner;
import com.streamsets.pipeline.sdk.StageRunner;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestOnlineSource {
    private static final int MAX_BATCH_SIZE = 1;
    private Map <String, String> ipAddressEx;
    @Test
    public void testOrigin() throws Exception {
        ipAddressEx = new HashMap<>();
        String ipAddress = "127.0.0.1";
        ipAddressEx.put(ipAddress, "255.255.255.255");
        int httpPort = 18630;
        ipAddressEx.put("10.100.16.98", "255.255.255.255");
        /*ipAddressEx.put("211.193.192.125", "255.255.255.255");
        ipAddressEx.put("175.202.93.216", "255.255.255.255");
        ipAddressEx.put("218.158.214.85", "255.255.255.255");*/


        SourceRunner runner = new SourceRunner.Builder(OnlineDSource.class)
                .addConfiguration("isHttp", false)
                .addConfiguration("httpPort", httpPort)
                .addConfiguration("isWebsocket", false)
                .addConfiguration("isPing", true)
                .addConfiguration("pingInterval", 1)
                .addConfiguration("pingTimeout", 500)
                .addConfiguration("ipAddress_maskMap", ipAddressEx)
                .addOutputLane("lane")
                .build();

        try {
            runner.runInit();
            runner.runProduce("0", 1);
            final String lastSourceOffset = null;
            StageRunner.Output output = runner.runProduce(lastSourceOffset, MAX_BATCH_SIZE);
            //Assert.assertEquals("1", output.getNewOffset());
            //List<Record> records = output.getRecords().get("lane");
            //Assert.assertEquals(1, records.size());
            //Assert.assertTrue(records.get(0).has("/"));
            //Assert.assertEquals(ipAddress.contains(ipAddress), records.get(0).get().getValue().toString().contains(ipAddress));

        } finally {
            runner.runDestroy();
        }
    }

}
