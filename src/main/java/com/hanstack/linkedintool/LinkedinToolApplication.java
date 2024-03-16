package com.hanstack.linkedintool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class LinkedinToolApplication {



    public static void main(String[] args) {
        SpringApplication.run(LinkedinToolApplication.class, args);
    }


//    @Bean
//    public Wait<WebDriver> getWaitDriverInstance() {
//        return new WebDriverWait(getDriverInstance(), Duration.ofSeconds(2));
//    }

}
