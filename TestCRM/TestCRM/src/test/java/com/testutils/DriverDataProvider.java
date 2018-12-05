package com.testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVReader;

/**
 * 读取csv文件,提供数据给被@DataProvider注解的getDataPorvider()
 *
 */

public class DriverDataProvider implements Iterator<Object[]> {
	
	CSVReader reader = null;
	private Class<?>[] parameterTypes;
	private Converter[] parameterConverters;
	private static final Log log = LogFactory.getLog(DriverDataProvider.class);
	
	public DriverDataProvider(Class<?> cls,Method method,String csvFilePath){
		
		try{
			File file = new File(csvFilePath);
			FileInputStream in = new FileInputStream(file);
			//InputStreamReader 将字节流转换为字符流
			InputStreamReader isr = new InputStreamReader(in,"utf-8");
			reader = new CSVReader(isr,',','\"',1);
			parameterTypes = method.getParameterTypes();
			int len = parameterTypes.length;
			parameterConverters = new Converter[len];
			for (int i = 0; i < len; i++) {
				parameterConverters[i] = ConvertUtils.lookup(parameterTypes[i]);
			}		
		}catch (RuntimeException e) {
			log.error("CsvFile not Exist!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	private String[] last;
	
	@Override
	public boolean hasNext() {
		if (reader == null){
			return false;
		}
		try{
			last = reader.readNext();
		}catch (IOException e){
			e.printStackTrace();
		}
		return last !=null;		
	}
	
	@Override
	public Object[] next() {
		String[] next;
		if (last != null) {
			next = last;
		} else {
			next = getNextLine();
		}
		
		last = null;
		return parseLine(next);
	}	
	
	private String[] getNextLine() {
		if (last == null) {
			try {
				last = reader.readNext();
			} catch (IOException e) {
				log.error("");
				throw new RuntimeException(e);
			}
		}
		return last;
	}	
	private Object[] parseLine(String[] args) {
		if (args.length != parameterTypes.length) {
			System.out.println("csv文件驱动数据个数：[" + args.length + "], 参数个数：[" +  parameterTypes.length + "]");
			return null;
		}
		
		int len = args.length;
		Object[] result = new Object[len];
		for (int i = 0; i < len; i++) {
			String args2 = args[i];
			result[i] = parameterConverters[i].convert(parameterTypes[i], args2);
		}
		return result;
	}	

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}
	
	public CSVReader getReader() {
		return this.reader;
				
	}	
	
}
