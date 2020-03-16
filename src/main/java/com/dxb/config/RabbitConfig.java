package com.dxb.config;

import com.dxb.receiver.Receiver;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类说明：
 */
@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String addresses;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Autowired
    private Receiver receiver;


    /**
     * 连接工厂
     *
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(addresses + ":" + port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        //设置消息发送的确认（生产者）
        connectionFactory.setPublisherConfirms(true);

        return connectionFactory;
    }

    /**
     * rabbitAdmin类封装对RabbitMQ的管理操作
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    //TODO 使用Template，给生产者、消费者  方便发消息 5672
    @Bean
    public RabbitTemplate newRabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
//        //回调  发送确认(异步，监听)
        template.setConfirmCallback(confirmCallback());
        //开启路由失败通知
        template.setMandatory(true);
//        //路由失败通知
        template.setReturnCallback(returnCallback());

        return template;
    }

    //===============使用了RabbitMQ系统缺省的交换器==========
    //TODO 申明交换器(topic交换器)
    @Bean
    public TopicExchange Topicexchange() {
        return new TopicExchange("TopicExchange", true, false);
    }

    //TODO 申明交换器(Direct交换器)
    @Bean
    public DirectExchange Directexchange() {
        return new DirectExchange("DirectExchange", true, false);
    }

    //TODO 申明交换器(Fanout交换器)
    @Bean
    public FanoutExchange Fanoutexchange() {
        return new FanoutExchange("FanoutExchange", true, false);
    }


    //TODO 申明队列
    @Bean
    public Queue queue1() {
        return new Queue("queue1", true);
    }

    @Bean
    public Queue queue2() {
        return new Queue("queue2", true);
    }

    @Bean
    public Queue queue3() {
        return new Queue("queue3", true);
    }

    @Bean
    public Queue queue4() {
        return new Queue("queue4", true);
    }


    //TODO 绑定关系 绑定直连direct交换器
    @Bean
    public Binding bindingDirectExchange() {
        return BindingBuilder
                .bind(queue1())
                .to(Directexchange())
                .with("king.jvm");
    }
    //TODO 绑定关系

    @Bean
    public Binding bindingTopicExchange() {
        return BindingBuilder
                .bind(queue2())
                .to(Topicexchange())
                .with("king.*");
    }

    //TODO fanout 广播 绑定关系
    @Bean
    public Binding bindingFanoutExchange1() {
        return BindingBuilder
                .bind(queue3())
                .to(Fanoutexchange());
    }

    @Bean
    public Binding bindingFanoutExchange2() {
        return BindingBuilder
                .bind(queue4())
                .to(Fanoutexchange());
    }


    //===============生产者发送确认==========
    @Bean
    public RabbitTemplate.ConfirmCallback confirmCallback() {
        return new RabbitTemplate.ConfirmCallback() {

            @Override
            public void confirm(CorrelationData correlationData,
                                boolean ack, String cause) {
                if (ack) {
                    System.out.println("发送者确认发送给mq成功");
                } else {
                    //处理失败的消息
                    System.out.println("发送者发送给mq失败,考虑重发:" + cause);
                }
            }
        };
    }

    //===============失败通知==========
    @Bean
    public RabbitTemplate.ReturnCallback returnCallback() {
        return new RabbitTemplate.ReturnCallback() {

            @Override
            public void returnedMessage(Message message,
                                        int replyCode,
                                        String replyText,
                                        String exchange,
                                        String routingKey) {
                System.out.println("无法路由的消息，需要考虑另外处理。");
                System.out.println("Returned replyText：" + replyText);
                System.out.println("Returned exchange：" + exchange);
                System.out.println("Returned routingKey：" + routingKey);
                String msgJson = new String(message.getBody());
                System.out.println("Returned Message：" + msgJson);
            }
        };
    }

    //===============消费者确认==========
//    @Bean
//    public SimpleMessageListenerContainer messageContainer() {
//        SimpleMessageListenerContainer container
//                = new SimpleMessageListenerContainer(connectionFactory());
//        //TODO 绑定了这个队列
//        container.setQueues(queue1());
//        //TODO 手动提交
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        //TODO 消费确认方法
//        container.setMessageListener(receiver);
//        return container;
//    }


}
