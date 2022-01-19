package com.orchestrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsUtil {
	
	private static Properties prop = new Properties();
	
	private static String filePath = "src/main/resources/config/config.properties";
	
	public static Properties getProps() {
		if(prop.isEmpty()) {
			loadProperties(filePath);
		}
		return prop;
		
	}
	
	public static void loadProperties(String path) {
		File file = new File(path);
		if(!file.isFile()) {
			path= filePath;
		}
		
		try (InputStream input = new FileInputStream(path)) {
			
			// load a properties file
			prop.load(input);
			// get the property value and print it out

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
