package com.barath.app;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;


@RestController
public class TemplateController {
	
	private CodeGenerator codeGenerator;
	
	public TemplateController(CodeGenerator codeGenerator) {
		this.codeGenerator=codeGenerator;
	}
	
	@RequestMapping("/")
	public String home() {
		
		Map<String, Object> model=new HashMap<>();
		
		model.put("message", "Hello World");		
		String response=codeGenerator.execute("home",model);
		System.out.println("response "+response);
		return response;
		
	}

}
