package com.example.petclinic.ui.tests;

import org.openqa.selenium.WebDriver;

public class WebApp {

	private final WebDriver webDriver;
	private final String url;

	public WebApp(String url, WebDriver webDriver) {
		this.webDriver = webDriver;
		this.url = url;
	}
	
	public WebDriver driver() {
		return webDriver;
	}
	
	public String url() {
		return this.url;
	}

}
