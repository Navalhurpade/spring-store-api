package com.naval.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(StoreApplication.class, args);

    }
}
