package br.com.wg.starwars;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class StarwarsApplication {

	@Value("${apistarwars.urlApi}")
	private String urlApi;


	public static void main(String[] args) {
		SpringApplication.run(StarwarsApplication.class, args);
	}

	@Bean
	WebClient clientApi() {
		return WebClient.create(urlApi);
	}
}
