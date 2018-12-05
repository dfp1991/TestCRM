package com.crm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;


import com.alibaba.fastjson.JSONObject;


//import com.alibaba.fastjson.JSONObject;

public class LoginForPC {
	 private static PoolingHttpClientConnectionManager connMgr;  
	 private static RequestConfig requestConfig;  
	 private static final int MAX_TIMEOUT = 7000;    
	 static CookieStore cookieStore = null;  
	 static HttpClientContext context = null; 

	 
	 /*private static String account;//帐号
	 private static String password;//密码
	 private static String terminal;//客户端
	 private static String verCode;//图片验证码
*/	 
	 static {  
	        // 设置连接池  
	        connMgr = new PoolingHttpClientConnectionManager();  
	        // 设置连接池大小  
	        connMgr.setMaxTotal(100);  
	        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());  
	  
	        RequestConfig.Builder configBuilder = RequestConfig.custom();  
	        // 设置连接超时  
	        configBuilder.setConnectTimeout(MAX_TIMEOUT);  
	        // 设置读取超时  
	        configBuilder.setSocketTimeout(MAX_TIMEOUT);  
	        // 设置从连接池获取连接实例的超时  
	        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);  
  }
	 
	 /*public LoginForPC(String verCode,String account, String password, String terminal){
		    
		    LoginForPC.account = account;
		
		    LoginForPC.password = password;
		    
		    LoginForPC.terminal = terminal;
		    
		    LoginForPC.verCode = verCode;
		
	} */
	 
	 public Map<String,String> login(String url){
		    Map<String, String> map =new HashMap<String, String>();
			//String result="";
		    String code = "";
		    
		    //String jsonStr="{\"verCode\":"+verCode+","+"\"account\":"+account+","+"\"password\":"+password+","+"\"terminal\":"+terminal+"}";
			
			String jsonStr="{\"verCode\":\"\","+"\"account\":\"admin_data\","+"\"password\":\"123456a\","+"\"terminal\":\"PC\"}";
			
			JSONObject jsonObj = JSONObject.parseObject(jsonStr);
					
			map=doPostJson(url, jsonObj);
			String result1=map.get("httpStr");
		    JSONObject jsonResult1 = JSONObject.parseObject(result1);
		    code=jsonResult1.getString("code");
		    
			Map<String, String> map2 =new HashMap<String, String>();
			map2.put("code", code);
			map2.put("cookie", map.get("cookie"));
			return map2;	
		 
	 }
	 
	 public static Map<String,String> doPostJson(String apiUrl, Object json){
			Map<String, String> map =new HashMap<String, String>();
			CloseableHttpClient httpClient = HttpClients.createDefault();  
	        String httpStr = null;  
	        HttpPost httpPost = new HttpPost(apiUrl);  
	        CloseableHttpResponse response = null; 		 

	        try {  
	            httpPost.setConfig(requestConfig);              
	            StringEntity stringEntity = new StringEntity(json.toString(),"UTF-8");//解决中文乱码问题  
	            stringEntity.setContentEncoding("UTF-8");  
	            stringEntity.setContentType("application/json");  
	            httpPost.setEntity(stringEntity);  
	            response = httpClient.execute(httpPost); 
	            
	            HttpEntity entity = response.getEntity();  
	            httpStr = EntityUtils.toString(entity, "UTF-8");  	            
	        
	        }catch (IOException e) {  
	    	   e.printStackTrace();  
	        } finally {  
	        	if (response != null) {  
	        		try {  
	        			EntityUtils.consume(response.getEntity());  
	        		} catch (IOException e) {  
	        			e.printStackTrace();  
	        		}  
	        	}  
	        }  
	        Header[] cookies=response.getAllHeaders();
	        String cookie="";
	        for (Header h : cookies) {
	        	if (h.getName().equals("set-cookie")){
	        		if(StringUtils.contains( h.getValue(), "CRM_LOGIN_ID")){
	        			cookie = h.getValue();
	        		}
					
	        	}
				
				}
	
	        map.put("cookie", cookie);
	        map.put("httpStr", httpStr);
	        return map;  
	 
	 }
	 
	 
	 
	 
	 
	 
	 public static void main(String[] args) {
		 
		 //LoginForPC testlogin = new LoginForPC("","admin_data", "123456a", "PC");
		
		 LoginForPC testlogin = new LoginForPC();		 
		 Map<String, String> map = testlogin.login("http://manager.prepub.xkeshi.net/api/common/login");		
		 if(StringUtils.equalsIgnoreCase("0",map.get("code"))==true)
		 		 
		 {
				System.out.println("登陆成功了！");
		 }
	 }
	 
}
