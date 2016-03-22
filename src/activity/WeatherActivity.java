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
	* ������ʾ������

	*/
	private TextView cityNameText;
	/**
	* ������ʾ����ʱ��
	*/
	private ProgressBar icon;
	/**
	* ������ʾ����������Ϣ
	*/
	private TextView weatherDespText;
	/**
	* ������ʾ����1
	*/
	private TextView temp1Text;
	/**
	* ������ʾ����2
	*/
	private TextView temp2Text;
	/**
	* ������ʾ��ǰ����
	*/
	private TextView currentDateText;
	/**
	* �л����а�ť
	*/
	private Button switchCity;
	/**
	* ����������ť
	*/
	private Button refreshWeather;
	
	
	/**
	* ѡ�еĳ���
	*/
	private City selectCity;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		// ��ʼ�����ؼ�
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
		
		//Intent countyCode = getIntent()��getSerializable("City");
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
			// ���ؼ�����ʱ��ȥ��ѯ����
		
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
	* ��ѯ�ؼ���������Ӧ���������š�
	*/
	private void queryWeatherCode(String countyCode) {

	queryFromServer("cityid",countyCode);
	}
	
	/**
	* ���ݴ���ĵ�ַ������ȥ���������ѯ�������Ż���������Ϣ��
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
	* ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ�������ϡ�
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
	//publishText.setText("����" + prefs.getString("date", "") + "����");
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
       builder.setMessage("ȷ��Ҫ�˳���?"); 
       builder.setTitle("��ʾ"); 
       builder.setPositiveButton("ȷ��", 
               new android.content.DialogInterface.OnClickListener() { 
             

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				
					WeatherActivity.this.finish(); 
				} 
               }); 
               builder.setNegativeButton("ȡ��", 
               new android.content.DialogInterface.OnClickListener() { 
                   @Override
                   public void onClick(DialogInterface dialog, int which) { 
                       dialog.dismiss(); 
                   } 
               }); 
       builder.create().show(); 
   } 
}
