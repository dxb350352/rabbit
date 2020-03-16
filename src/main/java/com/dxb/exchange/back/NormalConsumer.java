package com.dxb.exchange.back;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类说明：普通的消费者
 */
public class NormalConsumer {

    public static void main(String[] argv)
            throws IOException, TimeoutException {
        //创建连接、连接到RabbitMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置下连接工厂的连接地址(使用默认端口5672)
        connectionFactory.setHost("127.0.0.1");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();

        //申明一个消费者
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                String message = new String(bytes, "UTF-8");
                System.out.println("Received[" + envelope.getRoutingKey() + "]" + message);
            }
        };
        //TODO 这里第二个参数是自动确认参数，如果是true则是自动确认
        channel.basicConsume("queue_1", true, consumer);
        //申明一个消费者
        channel.basicConsume("queue_2", true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                String message = new String(bytes, "UTF-8");
                System.out.println("Received[" + envelope.getRoutingKey() + "]" + message);
            }
        });

        System.out.println("waiting for message ......");

    }

}
