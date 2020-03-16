package com.dxb.exchange.transcation;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类说明：direct类型交换器的生产者,加入事务
 */
public class TransProducer {

    public final static String EXCHANGE_NAME = "direct_exchange";

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
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //持久化的交换器  ，
        // channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.DIRECT,true);
        //autoDelete （自动删除，一般都是false）所有队列或交换器都与本交换器解除绑定，autoDelete=true时，此交换器就会被自动删除
        channel.txSelect();// 事务开始
        try {
            //申明绑定键\消息体
            String[] routeKeys = {"white", "red"};
            for (int i = 0; i < 4; i++) {
                String routeKey = routeKeys[i % 2];
                String msg = "Hello,RabbitMQ" + (i + 1);
                //发布消息
                channel.basicPublish(EXCHANGE_NAME, routeKey, null, msg.getBytes());
                System.out.println("Sent:" + routeKey + ":" + msg);
            }
            //TODO
            //事务提交
            channel.txCommit();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO
            //事务回滚
            channel.txRollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
        channel.close();
        connection.close();

    }

}
