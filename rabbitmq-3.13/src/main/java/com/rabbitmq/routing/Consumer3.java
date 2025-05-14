package com.rabbitmq.routing;

import com.rabbitmq.client.*;
import com.rabbitmq.utils.ConnectionUtil;

import java.io.IOException;

public class Consumer3 {

    public static void main(String[] args) throws Exception {

        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        String queue2Name = "my_direct_queue";

        channel.queueDeclare(queue2Name, true, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("body：" + new String(body));
                System.out.println("getRoutingKey:" + envelope.getRoutingKey());

            }

        };

        channel.basicConsume(queue2Name, true, consumer);

    }

}