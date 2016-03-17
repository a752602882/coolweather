package util;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;

import android.util.Log;

public class HttpUtil {

	public static void sendHttpRequest(final String address,final HttpCallbackListener  listener){
		
		new Thread(new Runnable(){
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
             //   HttpsURLConnection connection = null;
                /*
                try {
                	
                	URL url = new URL(URLEncoder.encode(address, "UTF-8"));
				    
					connection = (HttpsURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setRequestProperty("apikey","cf7dfab043c9cb8dc1998e858327d990");
					
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in= connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
					
					StringBuffer response = new StringBuffer();
					String line;
					while((line =reader.readLine())!=null)
						response.append(line);
					if (listener !=null) // 回调onFinish()方法
						listener.onFinish(response.toString());
							
                } catch (Exception e) {
	
                	
                	// TODO Auto-generated catch bloc
                	e.printStackTrace();
                	listener.onError(e);
                	//Log.d("sss",e());
					
				} finally {
					if (connection != null) {
						connection.disconnect();
						}
				}
				  */
				
                	 Parameters para = new Parameters();
                	//  String cityname = URLEncoder.encode("成都", "UTF-8");
                      para.put("cityname",address);
                   
             		 ApiStoreSDK.execute("http://apis.baidu.com/apistore/weatherservice/citylist", 
             				ApiStoreSDK.GET, 
             				para, 
             				new ApiCallBack() {
                         
                             @Override
                             public void onSuccess(int status, String responseString) {
                             	Log.i("sdkdemo", "onSuccess");
                             	listener.onFinish(responseString);
                             }
                 
                             @Override
                             public void onComplete() {
                             	Log.i("sdkdemo", "onComplete");
                             }
                 
                             @Override
                             public void onError(int status, String responseString, Exception e) {
                             	Log.i("sdkdemo", "onError, status: " + status);
                             	Log.i("sdkdemo", "errMsg: " + (e == null ? "" : e.getMessage()));
                       
                             }
                         
                         });
					
                
			
			   }
		  }
				
	).start();
		
	}
	
}
