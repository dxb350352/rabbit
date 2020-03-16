package com.dxb.homework;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: dxb
 * Date: 2020/3/15
 * Description: When I wrote this, only God and I understood what I was doing. Now, God only knows
 */
@Configuration
public class HomeworkConfig {

    @Bean
    public TopicExchange homeworkExchange() {
        return new TopicExchange("homework", true, false);
    }

    @Bean
    public Queue homework() {
        return new Queue("homework", true);
    }

    @Bean
    public Binding bindingHomeworkExchange() {
        return BindingBuilder
                .bind(homework())
                .to(homeworkExchange())
                .with("homework");
    }
}
