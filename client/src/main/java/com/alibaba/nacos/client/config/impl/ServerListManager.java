/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos.client.config.impl;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.address.AbstractServerListManager;
import com.alibaba.nacos.client.env.NacosClientProperties;
import com.alibaba.nacos.common.lifecycle.Closeable;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.plugin.address.exception.AddressException;

/**
 * the server list manager work in config.
 *  fixme logic compare
 * @author GuoJiangFu
 */
public class ServerListManager extends AbstractServerListManager implements Closeable {
    
    private String namespace = "";
    
    private String tenant = "";
    
    private String contentPath;
    
    private String name;
    
    private static final String PROPERTY_ADDRESS_PLUGIN_NAME = "PropertyAddressPlugin";
    
    private static final String DEFAULT_PLUGIN = "defaultPlugin";
    
    private static final String CUSTOM_PLUGIN = "customPlugin";
    
    boolean isFixed;
    
    public ServerListManager(NacosClientProperties properties) throws NacosException {
        super(properties);
        this.initParam(properties);
        this.initServerName(properties);
    }

    private void initParam(NacosClientProperties properties) {
        this.isFixed = PROPERTY_ADDRESS_PLUGIN_NAME.equals(this.addressPlugin.getPluginName());
        String contentPathTemp = properties.getProperty(PropertyKeyConst.CONTEXT_PATH);
        if (!StringUtils.isBlank(contentPathTemp)) {
            this.contentPath = contentPathTemp;
        }

        String namespaceTemp = properties.getProperty(PropertyKeyConst.NAMESPACE);
        if (!StringUtils.isBlank(contentPathTemp)) {
            this.namespace = namespaceTemp;
            this.tenant = namespaceTemp;
        }
    }
    
    private void initServerName(NacosClientProperties properties) {
        String serverName = "";

        if (properties != null && properties.containsKey(PropertyKeyConst.SERVER_NAME)) {
            serverName = properties.getProperty(PropertyKeyConst.SERVER_NAME);
        } else {
            if (isFixed) {
                serverName = DEFAULT_PLUGIN + "-" + getAddressPluginName()
                        + (StringUtils.isNotBlank(namespace) ? StringUtils.trim(namespace)
                        : "") + "-" + getFixedNameSuffix(getServerList().toArray(new String[0]));
            } else {
                serverName = CUSTOM_PLUGIN + "-" + getAddressPluginName()
                        + (StringUtils.isNotBlank(namespace) ? ("_" + StringUtils.trim(namespace)) : "");
            }
        }

        serverName = serverName.replaceAll("\\/", "_");
        serverName = serverName.replaceAll("\\:", "_");
        this.name = serverName;
    }
    
    public String getAddressPluginName() {
        return addressPlugin.getPluginName();
    }
    
    public String getUrlString() {
        return addressPlugin.getServerList().toString();
    }
    
    @Override
    protected void initAddressPluginListener() throws AddressException {
        addressPlugin.registerListener(serverLists -> {
            NotifyCenter.publishEvent(new ServerlistChangeEvent());
        });
    }
    
    String getFixedNameSuffix(String... serverIps) {
        StringBuilder sb = new StringBuilder();
        String split = "";
        for (String serverIp : serverIps) {
            sb.append(split);
            serverIp = serverIp.replaceAll("http(s)?://", "");
            sb.append(serverIp.replaceAll(":", "_"));
            split = "-";
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "ServerManager-" + name + "-" + getUrlString();
    }
    
    public String getNamespace() {
        return namespace;
    }
    
    public String getTenant() {
        return tenant;
    }
    
    public String getContentPath() {
        return contentPath;
    }
    
    public String getName() {
        return name;
    }
}
