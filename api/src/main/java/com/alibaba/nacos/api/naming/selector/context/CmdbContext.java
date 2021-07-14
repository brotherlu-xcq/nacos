/*
 *  Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.alibaba.nacos.api.naming.selector.context;

import com.alibaba.nacos.api.cmdb.pojo.Entity;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * The CMDB context is given by the {@link SelectorContextBuilder#build(Object, Object)} and used for the
 * {@link com.alibaba.nacos.api.naming.selector.Selector#select(Object)}.
 *
 * @author chenglu
 * @date 2021-07-09 21:31
 */
public class CmdbContext {
    
    /**
     * consumer is the instance which provide the info.
     */
    private CmdbInstance consumer;
    
    /**
     * the providers which be selected.
     */
    private List<CmdbInstance> providers;
    
    
    public CmdbInstance getConsumer() {
        return consumer;
    }
    
    public void setConsumer(CmdbInstance consumer) {
        this.consumer = consumer;
    }
    
    public List<CmdbInstance> getProviders() {
        return providers;
    }
    
    public void setProviders(List<CmdbInstance> providers) {
        this.providers = providers;
    }
    
    @Override
    public String toString() {
        return "CmdbContext{" + "consumer=" + consumer + ", providers=" + providers + '}';
    }
    
    public static class CmdbInstance<T extends Instance> {
    
        /**
         * the CMDB info of instance
         */
        private Entity entity;
    
        /**
         * the instance.
         */
        private T instance;
    
        public Entity getEntity() {
            return entity;
        }
    
        public void setEntity(Entity entity) {
            this.entity = entity;
        }
    
        public T getInstance() {
            return instance;
        }
    
        public void setInstance(T instance) {
            this.instance = instance;
        }
    
        @Override
        public String toString() {
            return "CmdbInstance{" + "entity=" + entity + ", instance=" + instance + '}';
        }
    }
}
