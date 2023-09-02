package com.group15.TUKulinarium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class TuKulinariumApplication {
	public static void main(String[] args) {
		SpringApplication.run(TuKulinariumApplication.class, args);
	}

	@Bean
	public static void tempDirectory(){
		new File("temp/").mkdir();
	}
}