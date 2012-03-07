package com.example.petclinic.ui.tests;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:environment.properties")
public class WebTestConfig {
	@Autowired
    private Environment env;

	@Bean
	public WebApp webApp() {
		return new WebApp(env.getProperty("app.test.url"), new HtmlUnitDriver());
	}
}
