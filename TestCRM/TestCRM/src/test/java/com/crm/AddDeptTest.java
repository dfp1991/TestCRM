package com.crm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

//import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.testbase.TestBase;
import com.testutils.IDataProvider;

public class AddDeptTest extends TestBase{
	
	HttpURLConnection connection = null;
	BufferedReader reader = null;
	String http_url = getConfig();
	//String Cookie = getCookie();
	
	//试执行时，每次会读取一条CSV记录，每个字段做为一个参数传给@Test下面的测试方法addDept
	@Test(dataProvider = "CsvDataProvider", dataProviderClass = IDataProvider.class)
	public void addDept(String caseId,String caseDesc,String desc,String name,int parentId,String weight,String res,String responsedesc) {
		
		new AddDept(caseId,caseDesc,desc,name,parentId,weight,res,responsedesc).AddDepts();		
  }
	
	class AddDept{
		public String caseId;
		public String caseDesc;
		public String desc;
		public String name;
		public int parentId;
		public String weight;
		public String res;
		public String responsedesc;
		public String jsonStr;
		
		public AddDept(String caseId,String caseDesc, String desc, String name, int parentId, String weight,String res,String responsedesc) {
			this.caseId = caseId;
			this.caseDesc = caseDesc;
			this.desc = desc;
			this.name = name;
			this.parentId = parentId;
			this.weight = weight;
			this.res = res;
			this.responsedesc = responsedesc;
			this.jsonStr = getJsonStr();
		}

		
		public void AddDepts(){
			try{
				URL url = new URL(http_url + "/api/sys/dept/add");
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setInstanceFollowRedirects(true);
				connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "*/*");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
                connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
                connection.setRequestProperty("Content-Length", "232");
                connection.setRequestProperty("App-Type", "x-common");
				//String cookie = Cookie;
                LoginForPC testlogin = new LoginForPC();		 
       		 	Map<String, String> map = testlogin.login(http_url + "/api/common/login");
       		 	String cookie = map.get("cookie");
				connection.setRequestProperty("cookie",cookie);
				connection.connect();
				PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
				
				out.write(jsonStr);
				out.flush();
				out.close();
				
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String lines;
				StringBuffer sbf = new StringBuffer();
				while ((lines = reader.readLine()) != null) {
					lines = new String(lines.getBytes(), "utf-8");
					sbf.append(lines);
				}
				
				JSONObject obj = (JSONObject) JSON.parse(sbf.toString());
				System.out.println("返回code值^^^^："+obj.get("code")+"description值^^^^^："+obj.get("description"));
				
				Assert.assertEquals(obj.get("code"), res);
				Assert.assertEquals(obj.get("description"), responsedesc);			
			}catch(MalformedURLException e){
				
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				try{
					reader.close();
				}catch(IOException e){
					e.printStackTrace();
				}
				connection.disconnect();
			}
		}

		private String getJsonStr() {
			AddDeptRequest adq = new AddDeptRequest();
			adq.setDesc(desc);
			adq.setName(name);
			adq.setParentId(parentId);
			adq.setWeight(weight);
			
			String jsonBody = JSON.toJSONString(adq);
			return jsonBody;
			
		}

		class AddDeptRequest{
			public String desc;
			public String name;
			public int parentId;
			public String weight;
			
			public String getDesc(){
				return desc;
			}
			
			public void setDesc(String desc){
				this.desc = desc;
			}

			public String getName(){
				return name;
			}
			
			public void setName(String name){
				this.name = name;
			}
			
			public int getParentId(){
				return parentId;
			}
			
			public void setParentId(int parentId){
				this.parentId = parentId;
			}

			public String getWeight(){
				return weight;
			}
			
			public void setWeight(String weight2){
				this.weight = weight2;
			}
			
		}

	}
	
}
