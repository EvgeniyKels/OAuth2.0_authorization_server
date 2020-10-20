package kls.oauth.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class AuthServerApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AuthServerApplication.class);
    }

    @PostConstruct
    public void setServerTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}

//https://www.baeldung.com/spring-security-oauth2-jws-jwk