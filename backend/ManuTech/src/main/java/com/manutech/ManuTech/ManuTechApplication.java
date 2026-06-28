package com.manutech.ManuTech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// Adicione o (exclude = SecurityAutoConfiguration.class) aqui:
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ManuTechApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManuTechApplication.class, args);
    }
}