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

package com.alibaba.nacos.naming.selector;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.selector.AbstractCmdbSelector;
import com.alibaba.nacos.api.naming.selector.Selector;
import com.alibaba.nacos.api.naming.selector.context.CmdbContext;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@link AbstractCmdbSelector} mock implement.
 *
 * @author chenglu
 * @date 2021-07-14 19:20
 */
public class MockSelector extends AbstractCmdbSelector<Instance> {
    
    private String key;
    
    private String value;
    
    @Override
    protected List<Instance> doSelect(CmdbContext context) {
        if (context.getProviders() == null) {
            return null;
        }
        return context.getProviders()
                .stream()
                .filter(provider -> {
                    Map<String, String> labels = provider.getEntity().getLabels();
                    if (labels == null) {
                        return false;
                    }
                    return value.equals(labels.get(key));
                })
                .map((Function<CmdbContext.CmdbInstance, Instance>) CmdbContext.CmdbInstance::getInstance)
                .collect(Collectors.toList());
    }
    
    @Override
    public Selector<List<Instance>, CmdbContext, String> parse(String condition) {
        String[] keyValues = condition.split("=");
        key = keyValues[0];
        value = keyValues[1];
        return this;
    }
    
    @Override
    public String getType() {
        return "mock";
    }
}
