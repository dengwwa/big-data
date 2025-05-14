package com.rabbitmq.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.utils.ConnectionUtil;

public class Producer2 {

    public static void main(String[] args) throws Exception {

        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        String exchangeName = "my_direct";

        // 创建交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, null);

        // 创建队列
        String queue1Name = "my_direct_queue";

        // 声明（创建）队列
        channel.queueDeclare(queue1Name, true, false, false, null);

        // 队列绑定交换机
        // 队列1绑定error
//        channel.queueBind(queue1Name, exchangeName, "v1");

        String message = "日志信息：张三调用了delete方法.错误了,日志级别warning";

        // 发送消息
        channel.basicPublish(exchangeName, "v1", null, message.getBytes());
        channel.basicPublish(exchangeName, "", null, message.getBytes());
        System.out.println(message);

        // 释放资源
        channel.close();
        connection.close();

    }

}