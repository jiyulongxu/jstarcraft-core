package com.jstarcraft.core.event.mqtt;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.junit.After;
import org.junit.Before;

import com.jstarcraft.core.codec.ContentCodec;
import com.jstarcraft.core.codec.json.JsonContentCodec;
import com.jstarcraft.core.codec.specification.CodecDefinition;
import com.jstarcraft.core.event.EventBus;
import com.jstarcraft.core.event.EventBusTestCase;
import com.jstarcraft.core.event.EventMode;
import com.jstarcraft.core.event.MockEvent;
import com.jstarcraft.core.utility.StringUtility;

import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;

public class MqttEventBusTestCase extends EventBusTestCase {

    private Vertx vertx;

    private MqttClient session;

    @Before
    public void start() throws Exception {
        vertx = Vertx.vertx();
        session = MqttClient.create(vertx);
        CountDownLatch latch = new CountDownLatch(1);
        session.connect(1883, "localhost", (connect) -> {
            latch.countDown();
        });
        latch.await();
    }

    @After
    public void stop() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        session.disconnect((disconnect) -> {
            latch.countDown();
        });
        latch.await();
        vertx.close();
    }

    @Override
    protected EventBus getEventBus(EventMode mode) {
        if (mode == EventMode.TOPIC) {
            CodecDefinition definition = CodecDefinition.instanceOf(MockEvent.class);
            ContentCodec codec = new JsonContentCodec(definition);
            return new MqttTopicEventBus(session, codec);
        } else {
            return null;
        }
    }

    @Override
    public void testTriggerQueueEvent() throws Exception {
        // TODO 无需测试队列模式
    }

}
