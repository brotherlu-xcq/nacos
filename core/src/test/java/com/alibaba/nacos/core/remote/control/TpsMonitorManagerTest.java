/*
 * TpsMonitorManagerTest.java
 * Copyright 2021 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.alibaba.nacos.core.remote.control;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class TpsMonitorManagerTest {
    
    static TpsMonitorManager tpsMonitorManager = new TpsMonitorManager();
    
    @BeforeClass
    public static void setUpBeforeClass() {
        TpsMonitorPoint publish = new TpsMonitorPoint("configPublish");
        tpsMonitorManager.registerTpsControlPoint(publish);
        TpsControlRule rule = new TpsControlRule();
        rule.setPointRule(new TpsControlRule.Rule(5000, TimeUnit.SECONDS, "SUM", "intercept"));
        rule.getMonitorKeyRule()
                .putIfAbsent("testKey:a*b", new TpsControlRule.Rule(500, TimeUnit.SECONDS, "EACH", "intercept"));
        rule.getMonitorKeyRule()
                .putIfAbsent("testKey:*", new TpsControlRule.Rule(2000000, TimeUnit.SECONDS, "SUM", "intercept"));
        
        publish.applyRule(rule);
    }
    
    @Before
    public void setUp() {
        // make sure different case will not effect each other.
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException ignored) {
        }
    }
    
    @Test
    public void testApplyTps() {
        for (int i = 0; i < 100; i++) {
            String value = "atg" + (new Random().nextInt(100) + 2) + "efb";
            boolean pass = tpsMonitorManager
                    .applyTps("configPublish", "testconnectionId", Lists.list(new TestKey(value)));
            assertTrue(pass);
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Test
    public void testApplyTpsWithOverFlow() {
        for (int i = 0; i < 1000; i++) {
            String value = "atg" + (new Random().nextInt(100) + 2) + "efb";
            boolean pass = tpsMonitorManager
                    .applyTps("configPublish", "testconnectionId", Lists.list(new TestKey(value)));
            if (!pass) {
                return;
            }
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Assert.fail("fail to limit.");
    }
    
    class TestKey extends MonitorKey {
        
        public TestKey(String key) {
            setKey(key);
        }
        
        @Override
        public String getType() {
            return "testKey";
        }
    }
}