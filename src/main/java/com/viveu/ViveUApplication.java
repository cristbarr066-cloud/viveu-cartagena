package com.viveu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ViveUApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViveUApplication.class, args);
        System.out.println("=================================================");
        System.out.println("  ViveU - Alojamientos Universitarios Cartagena  ");
        System.out.println("  Plataforma Activa: http://localhost:8080      ");
        System.out.println("=================================================");
    }
}
