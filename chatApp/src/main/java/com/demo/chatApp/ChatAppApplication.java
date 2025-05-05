package com.demo.chatApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.demo.chatApp")
public class ChatAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatAppApplication.class, args);
		System.out.println("********** ChatApp Starting ************");
	}
}
