package com.codecool.stocktrader;

import com.codecool.stocktrader.component.DataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
@EnableEurekaClient
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

	@Bean
	public Docket api(){
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/**"))
				.build();
	}

}
