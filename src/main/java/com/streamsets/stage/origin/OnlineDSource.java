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
import com.streamsets.pipeline.api.*;

import java.util.HashMap;
import java.util.Map;

@StageDef(
        version = 1,
        label = "Sample Origin",
        description = "",
        icon = "default.png",
        execution = ExecutionMode.STANDALONE,
        recordsByRef = true,
        onlineHelpRefUrl = ""
)
@ConfigGroups(value = Groups.class)
@GenerateResourceBundle
public class OnlineDSource extends OnlineSource {

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.MAP,
            defaultValue = "",
            label = "IP Address and subnet Mask",
            displayPosition = 10,
            group = "Network",
            description = "Input IP address and subnet mask for checking online status"
    )
    public Map<String, String> ipAddress_maskMap = new HashMap<>();
    @ConfigDef(
            required = true,
            type = ConfigDef.Type.BOOLEAN,
            defaultValue = "true",
            label = "Ping",
            displayPosition = 10,
            group = "Network",
            description = "Online check performed by Ping"
    )
    public boolean isPing;
    @ConfigDef(
            required = true,
            type = ConfigDef.Type.BOOLEAN,
            defaultValue = "false",
            label = "Websocket",
            displayPosition = 10,
            group = "Network",
            description = "Online check performed by Websocket"
    )
    public boolean isWebsocket;
    @ConfigDef(
            required = true,
            type = ConfigDef.Type.BOOLEAN,
            defaultValue = "false",
            label = "Http Response",
            displayPosition = 10,
            group = "Network",
            description = "Online check performed by Http response"
    )
    public boolean isHttp;

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.NUMBER,
            defaultValue = "150",
            label = "Timeout for each Host (MilliSec)",
            displayPosition = 10,
            group = "Ping",
            description = "Max timeout for each Host. Smaller timeout setting may ignore response from host. Bigger timeout setting may cause unreach some hosts",
            dependsOn = "isPing",
            triggeredByValue = "true"
    )
    public int pingTimeout;

    @ConfigDef(
            required = true,
            type = ConfigDef.Type.NUMBER,
            defaultValue = "150",
            label = "Ping batch interval (Sec)",
            displayPosition = 10,
            group = "Ping",
            description = "Max Interval secs for pinging batch job. Smaller setting may ignore response from host. Bigger Setting may not recognize correct status ",
            dependsOn = "isPing",
            triggeredByValue = "true"
    )
    public int pingInterval;


    /** {@inheritDoc} */
    @Override
    public Map<String, String> getIPMap() { return ipAddress_maskMap; }
    @Override
    public boolean usePing(){ return isPing; };
    @Override
    public boolean useWebsocket(){ return isWebsocket; }
    @Override
    public boolean useHttpresponse(){ return isHttp; }
    @Override
    public int getPingTimeout() { return pingTimeout; }
    @Override
    public int getPingInterval() { return pingInterval; }
}
