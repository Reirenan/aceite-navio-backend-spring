package br.com.treinaweb.twjobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
public class TwjobsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwjobsApplication.class, args);
	}

}
