package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 604800)
@SpringBootApplication
@EnableAsync
public class RegistrationServiceAppealsProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistrationServiceAppealsProjectApplication.class, args);
    }

}
