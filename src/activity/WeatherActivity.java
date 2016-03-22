package activity;

import com.coolweather.app.R;

import model.City;
import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{

	private LinearLayout weatherInfoLayout;
	/**
	* 用于显示城市名

	*/
	private TextView cityNameText;
	/**
	* 用于显示发布时间
	*/
	private ProgressBar icon;
	/**
	* 用于显示天气描述信息
	*/
	private TextView weatherDespText;
	/**
	* 用于显示气温1
	*/
	private TextView temp1Text;
	/**
	* 用于显示气温2
	*/
	private TextView temp2Text;
	/**
	* 用于显示当前日期
	*/
	private TextView currentDateText;
	/**
	* 切换城市按钮
	*/
	private Button switchCity;
	/**
	* 更新天气按钮
	*/
	private Button refreshWeather;
	
	
	/**
	* 选中的城市
	*/
	private City selectCity;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		// 初始化各控件
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		icon = (ProgressBar) findViewById(R.id.icon);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		
		//Intent countyCode = getIntent()。getSerializable("City");
		Intent intent = this.getIntent(); 
		selectCity=(City)intent.getSerializableExtra("selectCity");
		String countyCode = null;
		
		if(selectCity!=null){
		 countyCode = Integer.toString(selectCity.getArea_id());
		 }
		
		
		SharedPreferences prefs = PreferenceManager.
				getDefaultSharedPreferences(this);
		
		String area_id =prefs.getString("area_id", "");
		
		if (!TextUtils.isEmpty(area_id)) {
			// 有县级代号时就去查询天气
		
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
		}
			if(countyCode!=null ){
				 queryWeatherCode(countyCode);
			}else{
				 queryWeatherCode(area_id);
			}
		  
		
		
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
		
			SharedPreferences prefs = PreferenceManager.
			getDefaultSharedPreferences(this);
			String area_id = prefs.getString("area_id", "");
			if (!TextUtils.isEmpty(area_id)) {
			queryWeatherCode(area_id);
			}
			break;
			
			
		default:
			break;
		}
	}
	
	/**
	* 查询县级代号所对应的天气代号。
	*/
	private void queryWeatherCode(String countyCode) {

	queryFromServer("cityid",countyCode);
	}
	
	/**
	* 根据传入的地址和类型去向服务器查询天气代号或者天气信息。
	*/
	
	private void queryFromServer(final String type,final String typeArg){
	
		HttpUtil.sendHttpRequest( "http://apis.baidu.com/apistore/weatherservice/recentweathers",type,typeArg, new HttpCallbackListener() {

			
			
			@Override
			public void onFinish(String response) {
   				// TODO Auto-generated method stub
	       
				boolean result =false;
				
				
				
				Utility.handleWeatherResponse(WeatherActivity.this, response, selectCity);
				
						runOnUiThread(new Runnable() {
						@Override
						public void run() {
						showWeather();
						}
						});
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
		
		
	}
	
	/**
	* 从SharedPreferences文件中读取存储的天气信息，并显示到界面上。
	* editor.putString("date", info.getDate());
		editor.putString("name_cn", info.getName_cn());
		editor.putString("week", info.getWeek());
		editor.putString("curTemp",info.getCurTemp());
		editor.putString("aqi", info.getAqi());
		editor.putString("fengxiang", info.getFengxiang());
		editor.putString("fengli", info.getFengli());
		editor.putString("lowtemp", info.getLowtemp());
		editor.putString("type", info.getType());
		
	*/
	private void showWeather() {
	SharedPreferences prefs = PreferenceManager.
	getDefaultSharedPreferences(this);
	cityNameText.setText( prefs.getString("name_cn", ""));
	temp1Text.setText(prefs.getString("lowtemp", ""));
	temp2Text.setText(prefs.getString("hightemp", ""));
	//weatherDespText.setText(prefs.getString("weather_desp", ""));
	//publishText.setText("今天" + prefs.getString("date", "") + "发布");
	currentDateText.setText(prefs.getString("date", ""));
	weatherInfoLayout.setVisibility(View.VISIBLE);
	cityNameText.setVisibility(View.VISIBLE);
	
	
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
	}
	
	
   @Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	// TODO Auto-generated method stub
	   if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
           dialog();
	   }
        return false;
	
}
   protected void dialog() { 
       AlertDialog.Builder builder = new Builder(WeatherActivity.this); 
       builder.setMessage("确定要退出吗?"); 
       builder.setTitle("提示"); 
       builder.setPositiveButton("确认", 
               new android.content.DialogInterface.OnClickListener() { 
             

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				
					WeatherActivity.this.finish(); 
				} 
               }); 
               builder.setNegativeButton("取消", 
               new android.content.DialogInterface.OnClickListener() { 
                   @Override
                   public void onClick(DialogInterface dialog, int which) { 
                       dialog.dismiss(); 
                   } 
               }); 
       builder.create().show(); 
   } 
}
