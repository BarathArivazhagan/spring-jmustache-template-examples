package com.barath.app;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.barath.app.entity.CodeGenEntity;
import com.barath.app.entity.CodeGenEntityProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;


@RestController
public class TemplateController {
	
	private CodeGenerator codeGenerator;
	private static final ObjectMapper mapper=new ObjectMapper();
	
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

	@PostMapping("/entity")
	public String buildEntity(@RequestBody Map<String,Object> input) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException{

		System.out.println(" input "+input);
		Map<String,CodeGenEntity> entities=(Map<String, CodeGenEntity>) input.get("entities");
		System.out.println("entites   === > "+entities);
		Map<String,Object> models=new HashMap<>();
		entities.entrySet().forEach( entry -> {

			String entityName=entry.getKey();
			Map<String,Object> codeGenEntityMap=(Map<String, Object>) entry.getValue();
			Map<String,Object> entityMap=new HashMap<>();
			Map<String,CodeGenEntityProperty> propertyMap=(Map<String, CodeGenEntityProperty>) codeGenEntityMap.get("properties");
			entityMap.put("className",StringUtils.uncapitalize(entityName));
			entityMap.put("ClassName",StringUtils.capitalize(entityName));
			propertyMap.entrySet().forEach( propEntry -> {

				String propertyName=propEntry.getKey();
				CodeGenEntityProperty codeGenEntityProperty = new CodeGenEntityProperty();
				try {
					String json=mapper.writeValueAsString(propEntry.getValue());
					System.out.println("JSON "+json);
					codeGenEntityProperty = mapper.readValue(json, CodeGenEntityProperty.class);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				Map<String,Object> properties=new HashMap<>();
				properties.put("propertyName", StringUtils.uncapitalize(propertyName));
				properties.put("PropertyName", StringUtils.capitalize(propertyName));
				properties.put("type",codeGenEntityProperty.getType() );
				properties.put("primaryKey", codeGenEntityProperty.isPrimaryKey());
				entityMap.put("properties", properties);
			});

			models.putAll(entityMap);
		});
		System.out.println("models "+models);
		String response=codeGenerator.execute("entity",models);
		System.out.println("response "+response);
		return response;

	}

}
