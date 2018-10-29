package com.maybe.sys.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    final static String queueName = "queue.wechat";   //这里是队列的名称，交换机和队列进行绑定有个匹配模式，当producer 发送数据到指定路由键queue.demo或与 绑定的匹配模式进行匹配。
    /**
     * 定义一个队列
     * @return
     */
    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }
    /**
     * 定义一个topic交换机
     * @return
     */
    @Bean
    public TopicExchange topicExchange(){
        TopicExchange exchange=new TopicExchange("spring-boot-topicExchange");
        return exchange;
    }
    /**
     * 让队列和交换机绑定
     * @param queue   参数名称必须是queue，springboot默认使用的时方法名。-->对应上面的queue()方法
     * @param topicExchange 参数必须是topicExchange ,springboot默认使用的是方法名-->对应上面的topicExchange()方法
     * @return
     */
    @Bean
    public Binding bindingQueueAndTopicExchange(Queue queue, TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with("queue.wechat");//路由匹配，这里是路由匹配规则。
    }
}
