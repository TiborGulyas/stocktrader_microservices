package com.codecool.stocktrader;

import com.codecool.stocktrader.component.DataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StocktraderApplication {

	@Autowired
	private DataInitializer dataInitializer;

	public static void main(String[] args) {
		SpringApplication.run(StocktraderApplication.class, args);
		System.out.println("!!!!!!!!!!!!!running!!!!!!!!!!");
	}

	@Bean
	public CommandLineRunner init() {
		return args -> {
			System.out.println("init persistance");
			dataInitializer.initData();
		};
	}

}
