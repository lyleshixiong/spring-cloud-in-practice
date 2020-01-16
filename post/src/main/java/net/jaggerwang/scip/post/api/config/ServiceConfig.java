package net.jaggerwang.scip.post.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.adapter.service.sync.UserSyncService;
import net.jaggerwang.scip.common.usecase.port.service.sync.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ServiceConfig {
    @Bean
    @LoadBalanced
    public RestTemplate userServiceRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri("lb://spring-cloud-in-practice-user").build();
    }

    @Bean
    public UserService userService(@Qualifier("userServiceRestTemplate") RestTemplate restTemplate,
                                   CircuitBreakerFactory cbFactory,
                                   ObjectMapper objectMapper) {
        return new UserSyncService(restTemplate, cbFactory, objectMapper);
    }
}