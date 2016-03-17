package util;




import com.baidu.apistore.sdk.ApiStoreSDK;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	
	private static Context content;
	
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		super.onCreate();
		ApiStoreSDK.init(this, "cf7dfab043c9cb8dc1998e858327d990");
	   
		content = getApplicationContext();
	}
	
	public static Context getContext() {
		return content;
	}
}
