package com.rabbitmq.fanout;

import com.rabbitmq.client.*;
import com.rabbitmq.utils.ConnectionUtil;

import java.io.IOException;

public class Consumer1 {

    public static void main(String[] args) throws Exception {

        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        String queue1Name = "test_fanout_queue1";

        channel.queueDeclare(queue1Name, true, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("body：" + new String(body));
                System.out.println("队列 1 消费者 1 将日志信息打印到控制台.....");

            }

        };

        channel.basicConsume(queue1Name, true, consumer);

    }

}
