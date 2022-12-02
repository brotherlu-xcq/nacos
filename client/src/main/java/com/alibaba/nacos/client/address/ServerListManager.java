/*
 * Copyright 1999-2022 Alibaba Group Holding Ltd.
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

package com.alibaba.nacos.client.address;

import com.alibaba.nacos.api.exception.NacosException;

import java.util.List;

/**
 * ServerListManager Interface
 * Date 2022/8/30.
 *
 * @author GuoJiangFu
 */
public interface ServerListManager {
    
    /**
     * start to get server address list.
     *
     * @throws NacosException exception
     */
    void start() throws NacosException;
    
    /**
     * get server address list.
     *
     * @return server address list.
     */
    List<String> getServerList();
    
    /**
     * get current server address.
     *
     * @return get current server address to connect.
     */
    String getCurrentServer();
    
    /**
     * get next server address.
     *
     * @return get Next server address to connect
     */
    String getNextServer();
    
    /**
     * stop to get server address.
     */
    void shutdown();
}
