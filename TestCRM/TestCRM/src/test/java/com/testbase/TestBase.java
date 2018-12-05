package com.testbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class TestBase {
	
	private static File file = new File("src/test/resources/env-config.properties");
	public static String getConfig() {
		
		String env = getEnv();
		Properties prop = readProperties();
		
		if(StringUtils.equalsIgnoreCase("test", env)) {
			return prop.getProperty("http_url_test");
		}
		
		if(StringUtils.equalsIgnoreCase("prepub", env)) {
			return prop.getProperty("http_url_prepub");
		}
		
		return null;
	}
	private static Properties readProperties() {
		
		InputStreamReader in = null;
		Properties prop = new Properties();
		try{
			in = new InputStreamReader(new FileInputStream(file));			
		}catch (FileNotFoundException e1){
			e1.printStackTrace();
		}
		try{
			prop.load(in);
		}catch (IOException e){
			e.printStackTrace();
		}

		return prop;
	}
	
	public static String getEnv() {
		Properties prop = readProperties();
		return prop.getProperty("env");
	}
}
