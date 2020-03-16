package com.dxb.exchange.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 类说明：Topic类型的生产者
 * 假设有交换器 topic_course，
 * 讲课老师有king,mark,peter，
 * 技术专题有kafka,jvm,redis，
 * 课程章节有 A、B、C，
 * 路由键的规则为 讲课老师+“.”+技术专题+“.”+课程章节，如：king.kafka.A。
 * 生产者--生产全部的消息3*3*3=27条消息
 */
public class TopicProducer {

    public final static String EXCHANGE_NAME = "topic_exchange";

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
        // 指定主题交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        /*我们的课程，路由键最终格式类似于：king.kafka.A  king.kafka.B*/
        String[] techers = {"king", "mark", "peter"};
        for (int i = 0; i < 3; i++) {
            String[] modules = {"jvm", "rabbit", "redis"};
            for (int j = 0; j < 3; j++) {
                String[] servers = {"A", "B", "C"};
                for (int k = 0; k < 3; k++) {
                    // 发送的消息
                    String message = "Hello Topic_[" + i + "," + j + "," + k + "]";
                    String routeKey = techers[i % 3] + "." + modules[j % 3]
                            + "." + servers[k % 3];
                    channel.basicPublish(EXCHANGE_NAME, routeKey,
                            null, message.getBytes());
                    System.out.println(" [x] Sent '" + routeKey + ":'"
                            + message + "'");
                }
            }

        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }

}
