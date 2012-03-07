package com.example.petclinic.ui.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebTestConfig.class)
public class HomePageTest {
	@Autowired
	private WebApp webApp;

	@Test
	public void homepage_title_is_correct() throws Exception {
		System.out.println("url = " + webApp.url());
		webApp.driver().get(webApp.url());
		WebElement title = webApp.driver().findElement(By.tagName("title"));
		assertEquals("Welcome to Mvc", title.getText());
	}
}
