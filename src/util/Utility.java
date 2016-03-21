package util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.JsonWriter;
import model.City;
import model.CoolWeatherDB;
import model.County;
import model.Province;
import model.WeatherInfo;

public class Utility {

	
	/**
	* 解析和处理服务器返回的省级数据
	*/
public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
	
		if (!TextUtils.isEmpty(response)) {
			
		 //String	response1 =new String(response,"UTF-8");
			String[] allProvince = response.split(",");
			if (allProvince !=null && allProvince.length>0) {
				for (String p:allProvince) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
			
		}
		
	return false;
}



	/**
	* 解析和处理服务器返回的市级数据
	*/
public synchronized static boolean handleCityResponse(CoolWeatherDB coolWeatherDB,String response, int provinceId){
	
	if (!TextUtils.isEmpty(response)) {
		
		try {
			JSONObject josn =  new JSONObject(response);
			JSONArray jsonArray = josn.getJSONArray("retData");
			
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject user = (JSONObject) jsonArray.get(i) ;
				String province_cn = (String)user.get("province_cn");
				String district_cn = (String)user.get("district_cn");
				String name_cn= (String)user.get("name_cn");
				String name_en= (String)user.get("name_en");
				int area_id =  user.getInt("area_id");			
				City city = new City();
				city.setProvince_cn(province_cn);
				city.setDistrict_cn(district_cn);
				city.setName_cn(name_cn);
				city.setName_en(name_en);
				city.setArea_id(area_id);
				
				coolWeatherDB.saveCity(city);
				
			}
			
			
//			String[] allProvince = response.split(",");
//			if (allProvince !=null && allProvince.length>0) {
//				for (String p:allProvince) {
//					String[] array = p.split(":");
//					City city = new City();
//					city.setCityCode(array[0]);
//					city.setCityName(array[1]);
//					city.setProvinceId(provinceId);
//					
//					coolWeatherDB.saveCity(city);
//				}
//				return true;
//			}
//			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}
	
    return false;
}



/**
* 解析和处理服务器返回的市级数据
*/
public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response, int cityId){

if (!TextUtils.isEmpty(response)) {
	String[] allProvince = response.split(",");
	if (allProvince !=null && allProvince.length>0) {
		for (String p:allProvince) {
			String[] array = p.split("\\|");
			County county = new County();
			county.setCountyCode(array[0]);
			county.setCountyName(array[1]);
			county.setCityId(cityId);
			
			coolWeatherDB.saveCounty(county);
		}
		return true;
	}
	
}

return false;
}

/**
*  解析服务器返回的JSON 数据，并将解析出的数据存储到本地。
*/
public static void handleWeatherResponse(Context context, String response,City cityInfo) {
		try {
		JSONObject jsonObject = new JSONObject(response);
		JSONObject weatherInfo = jsonObject.getJSONObject("retData").getJSONObject("today");
		
		WeatherInfo  info = new WeatherInfo();
		String dataString =weatherInfo.getString("date");
		String[] strarray=dataString.split("-"); 
		  
		info.setDate(new String(strarray[0]+"年"+strarray[1]+"月"+strarray[2]+"日"));
		info.setName_cn(cityInfo.getName_cn());
		info.setArea_id((cityInfo.getArea_id()));
    	info.setWeek(weatherInfo.getString("week"));
		info.setCurTemp(weatherInfo.getString("curTemp"));
		info.setAqi( weatherInfo.getString("aqi"));
		info.setFengxiang( weatherInfo.getString("fengxiang"));
		info.setFengli( weatherInfo.getString("fengli"));
		info.setHightemp( weatherInfo.getString("hightemp"));
	    info.setLowtemp( weatherInfo.getString("lowtemp"));
	    info.setType( weatherInfo.getString("type"));
		saveWeatherInfo(context, info);
		} catch (JSONException e) {
		e.printStackTrace();
		}
}


/**
*  将服务器返回的所有天气信息存储到SharedPreferences 文件中。
*/
public static void saveWeatherInfo(Context context, WeatherInfo info) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年M 月d 日",
//		Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
		.getDefaultSharedPreferences(context).edit();
		editor.putString("date", info.getDate());
		editor.putString("name_cn", info.getName_cn());
		editor.putString("week", info.getWeek());
		editor.putString("curTemp",info.getCurTemp());
		editor.putString("aqi", info.getAqi());
		editor.putString("fengxiang", info.getFengxiang());
		editor.putString("fengli", info.getFengli());
		editor.putString("lowtemp", info.getLowtemp());
		editor.putString("hightemp", info.getHightemp());
		editor.putString("type", info.getType());
		
		editor.commit();
}

}