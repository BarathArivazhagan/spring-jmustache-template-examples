package com.barath.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class CodeGenerator {
	
	private TemplateRenderer templateRenderer;
	
	public CodeGenerator(TemplateRenderer templateRenderer){
		this.templateRenderer=templateRenderer;
	}
	
	public void write(File target, String templateName, Map<String, Object> model) {
		String body = templateRenderer.process(templateName, model);
		writeText(target, body);
	}

	private void writeText(File target, String body) {
		try (OutputStream stream = new FileOutputStream(target)) {
			StreamUtils.copy(body, Charset.forName("UTF-8"), stream);
		}
		catch (Exception e) {
			throw new IllegalStateException("Cannot write file " + target, e);
		}
	}
	
	public String execute(String templateName, Map<String, Object> model) {
		return templateRenderer.process(templateName, model);
	}

}
