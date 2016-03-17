package util;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.text.TextUtils;
import android.util.JsonWriter;
import model.City;
import model.CoolWeatherDB;
import model.County;
import model.Province;

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
}