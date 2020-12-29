package com.bol.game.kalaha;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import com.bol.game.kalaha.service.KalahaGameService;

@SpringBootApplication
@EnableCaching
public class KalahaGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(KalahaGameApplication.class, args);
	}

}
