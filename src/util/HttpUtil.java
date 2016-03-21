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

import android.R.string;
import android.util.Log;

public class HttpUtil {

	public static void sendHttpRequest(final String URL,final String address,final String addressAgr,final HttpCallbackListener  listener){
		
		new Thread(new Runnable(){
			
			@Override
			public void run() {
			
            Parameters para = new Parameters();
            para.put(address,addressAgr);
                   
            ApiStoreSDK.execute(URL, 
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
