package com.n26.challenge.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 *
 */

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan({"com.n26.challenge"})
public class App 
{
    public static void main( String[] args )
    {
    	//Starting application
        SpringApplication.run(App.class, args);
    }
}
