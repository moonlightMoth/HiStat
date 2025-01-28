package edu.moonlightmoth.HiStat.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HiStatApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiStatApplication.class, args);
	}

}

// curl -X POST -F "file=@mini.csv" http://localhost:8080/api/calculate