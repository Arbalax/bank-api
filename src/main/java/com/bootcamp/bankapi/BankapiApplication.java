package com.bootcamp.bankapi;

import com.bootcamp.bankapi.util.DataSynchronizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BankapiApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(BankapiApplication.class, args);

		context.getBean(DataSynchronizer.class).synchronizeData();
	}


}
