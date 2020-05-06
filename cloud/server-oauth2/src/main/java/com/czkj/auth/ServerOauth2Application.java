package com.czkj.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServerOauth2Application {

    public static void main(String[] args) {
        SpringApplication.run(ServerOauth2Application.class, args);
    }

}
