package com.lee.hellomessagequeue.step2;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 큐 네임을 설정한다.
    public static final String QUEUE_NAME = "WorkQueue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter listenerAdapter
    ) {
        return new SimpleMessageListenerContainer() {{
            setConnectionFactory(connectionFactory);
            setQueueNames(QUEUE_NAME);
            setMessageListener(listenerAdapter);
            setAcknowledgeMode(AcknowledgeMode.AUTO);
        }};
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(WorkQueueConsumer workQueueTask) {
        return new MessageListenerAdapter(workQueueTask, "workQueueTask");
    }

}
