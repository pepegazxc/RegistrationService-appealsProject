package main.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean public NewTopic userRegisteredTopic(){
        return TopicBuilder.name("user-registered")
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean public NewTopic userLoggedTopic(){
        return TopicBuilder.name("user-logged")
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean public NewTopic adminRequestTopic(){
        return TopicBuilder.name("admin-request")
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean public NewTopic mayorRequestTopic(){
        return TopicBuilder.name("mayor-request")
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean public NewTopic requestResponseTopic(){
        return TopicBuilder.name("request-response")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
