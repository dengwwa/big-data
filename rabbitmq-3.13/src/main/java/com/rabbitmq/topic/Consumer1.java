package com.rabbitmq.topic;

import com.rabbitmq.client.*;
import com.rabbitmq.utils.ConnectionUtil;

import java.io.IOException;

public class Consumer1 {

    public static void main(String[] args) throws Exception {

        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        String QUEUE_NAME = "test_topic_queue1";

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("body：" + new String(body));

            }

        };

        channel.basicConsume(QUEUE_NAME, true, consumer);

    }

}