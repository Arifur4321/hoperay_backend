package com.mediatica.vouchain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class HomeController {
	@Controller  
	public class HelloController {  
		@RequestMapping("/")  
	    public String display()  
	    {  
			System.out.println("1");
	        return "index";  
	    }
		@RequestMapping("/VouChain")  
	    public String displayHome()  
	    {  
			System.out.println("2");
	        return "index";  
	    }     

	}  
}
