package com.testutils;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.DataProvider;

import com.testbase.TestBase;

/**
  数据驱动器：为@Test注解的测试方法提供参数
 **/

public class IDataProvider {

	public IDataProvider(){
		
	}
	//此方法调用getDataPorvider
	@DataProvider(name = "CsvDataProvider")
	public static Iterator<?> getDataProvider(Method method){
		return getDataPorvider(method.getDeclaringClass(), method);
		
	}
	
	public static Iterator<?> getDataPorvider(Class<?> cls, Method method) {
		//获取调用此方法的包名【com.customService.templt】
		String path = cls.getPackage().getName();//
		//获取调用此方法的类名【AddTempletTest】
		String clsName = cls.getSimpleName();
		//获取调用此方法的方法名【addTemplet】
		//String name = method.getName();	
		//获取接口访问IP地址及端口号（公共部分地址）
		String env = TestBase.getEnv();
		
		String csvFilePath = null;
		if (StringUtils.equalsIgnoreCase("test", env)){
			csvFilePath = "src/test/resources/" + path + "/" + clsName + "-test.csv";
			}
		else if (StringUtils.equalsIgnoreCase("prepub", env)) {
			csvFilePath = "src/test/resources/" + path + "/" + clsName + "-prepub.csv";
		}
		
		return new DriverDataProvider(cls, method, csvFilePath);
		
		}
}
