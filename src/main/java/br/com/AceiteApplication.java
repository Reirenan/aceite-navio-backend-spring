package br.com.laps.aceite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.laps.aceite")
public class AceiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(AceiteApplication.class, args);
	}
}