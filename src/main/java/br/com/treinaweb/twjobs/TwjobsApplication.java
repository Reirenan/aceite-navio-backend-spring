package br.com.treinaweb.twjobs;

import me.paulschwarz.springdotenv.DotenvPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@SpringBootApplication
public class TwjobsApplication {
	private static final Logger log = LoggerFactory.getLogger(TwjobsApplication.class);

	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		DotenvPropertySource.addToEnvironment(applicationContext.getEnvironment());
		applicationContext.register(Config.class);
		applicationContext.refresh();
		SpringApplication.run(TwjobsApplication.class, args);

	}

}
