package activity;

import com.coolweather.app.R;

import model.City;
import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;




import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
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
	private TextView publishText;
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
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		//Intent countyCode = getIntent()。getSerializable("City");
		Intent intent = this.getIntent(); 
		selectCity=(City)intent.getSerializableExtra("selectCity");
		String countyCode = Integer.toString(selectCity.getArea_id());
		
		if (!TextUtils.isEmpty(countyCode)) {
			// 有县级代号时就去查询天气
			publishText.setText("同步中。。");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}
		
	}
	
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
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

}
