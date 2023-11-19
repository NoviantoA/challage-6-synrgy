package com.novianto.challange6;

import com.novianto.challange6.controller.fileupload.FileStorageProperties;
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
//@OpenAPIDefinition
@EnableConfigurationProperties({FileStorageProperties.class})
public class Challange6SynrgyApplication {

	public static void main(String[] args) {
		SpringApplication.run(Challange6SynrgyApplication.class, args);
	}

}
