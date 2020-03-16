package com.dxb.exchange.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类说明：fanout生产者
 */
public class FanoutProducer {

    public final static String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args)
            throws IOException, TimeoutException {
        //创建连接、连接到RabbitMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置下连接工厂的连接地址(使用默认端口5672)
        connectionFactory.setHost("127.0.0.1");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();

        //在信道中设置交换器(在生产者处理)
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        //申明绑定键\消息体
        String[] routeKeys = {"king", "mark", "peter"};
        for (int i = 0; i < 3; i++) {
            String routeKey = routeKeys[i % 3];
            String msg = "Hello,RabbitMQ" + (i + 1);
            //发布消息
            channel.basicPublish(EXCHANGE_NAME, routeKey, null, msg.getBytes());
            System.out.println("Sent:" + routeKey + ":" + msg);
        }
        channel.close();
        connection.close();
    }

}
