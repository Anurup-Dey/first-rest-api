package com.anurup.springboot.firstrestapi.helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
public class HelloWorldResource {

	@RequestMapping("/hello-world")
	// @ResponseBody
	public String helloWorld() {
		return "Hello World";
	}

	@RequestMapping("/hello-world-bean")
	public HelloWorldBean helloWorldBean() {
		return new HelloWorldBean("Hello World");
	}

	// Path variable or Path Param
	// /user/Anurup/todos/1
	@RequestMapping("/hello-world-path-param/{name}")
	public HelloWorldBean helloWorldPathVariable(@PathVariable String name) {
		return new HelloWorldBean("Hello World, " + name);
	}

	@RequestMapping("/hello-world-path-param/{name}/{message}")
	public HelloWorldBean helloWorldMultiplePathVariable(@PathVariable String name,
			@PathVariable String message) {
		return new HelloWorldBean("Hello World " + name + "," + message);
	}
}
