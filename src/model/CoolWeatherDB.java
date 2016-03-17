package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import db.CoolWeatherOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {

	/**
	 * 
	 * 数据库名
	 * 
	 * */
	
	public static final String DB_NAME= "cool_weather";
	
    /**
	 * 数据库版本
	 */
	public static final int VERSION=1;
	
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;
	
	/**
	* 将构造方法私有化
	*/ 

	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper = new  CoolWeatherOpenHelper(context, 
				DB_NAME, null, VERSION);
		//方法以读写方式打开数据库，一旦数据库的磁盘空间满了，数据库就只能读而不能写，
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	* 获取CoolWeatherDB的实例。
	*/
	public synchronized static CoolWeatherDB getInstance(Context context){
		if (coolWeatherDB ==null) {
			coolWeatherDB =new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/**
	* 将Province实例存储到数据库。
	*/
	
	
	public void saveProvince(Province province){
		
		if (province !=null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	/**
	* 从数据库读取全国所有的省份信息。
	*/
	public List<Province> loadProvinces(){
		
		List<Province> list= new ArrayList<Province>();
		Cursor cursor = db.query("Province", null,
				null, null, null, null, null);
		if (cursor.moveToNext()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
			    list.add(province);
				
			} while (cursor.moveToNext());
				
		}
		
		return list;
	}
	
	/**
	* 将City实例存储到数据库。
	*/
	public void saveCity(City city){
		
		if (city !=null) {
			ContentValues values = new ContentValues();
			values.put("province_cn", city.getProvince_cn());
			values.put("district_cn", city.getDistrict_cn());
			values.put("name_cn", city.getName_cn());
			values.put("name_en", city.getName_en());
			values.put("area_id", city.getArea_id());
			
			db.insert("City", null, values);
		}
	}
	
	/**
	* 从数据库读取某省下所有的城市信息。
	*/
	public List<City> loadCities(String province_cn){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_cn =?",
				new String[] { String.valueOf(province_cn) },null,null,null);
		if (cursor.moveToNext()) {
			do {
				
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setProvince_cn(province_cn);
				city.setDistrict_cn(cursor.getString(cursor.getColumnIndex("district_cn")));
				city.setName_cn(cursor.getString(cursor.getColumnIndex("name_cn")));
				city.setName_en(cursor.getString(cursor.getColumnIndex("name_en")));
				city.setArea_id(cursor.getInt(cursor.getColumnIndex("area_id")));
				list.add(city);
			} while (cursor.moveToNext());
		}
		
		for (int i = 0; i < list.size()-1; i++)  //外循环是循环的次数
        {
            for (int j = list.size() - 1 ; j > i; j--)  //内循环是 外循环一次比较的次数
            {

                if (list.get(i).getDistrict_cn().equals(list.get(j).getDistrict_cn()))
                {
                    list.remove(j);
                }

            }
        }
		
		return list;
	}
	
	
	
	/**
	* 将County实例存储到数据库。
	*/
	public void saveCounty(County county) {
		if (county != null) {
		ContentValues values = new ContentValues();
		values.put("county_name", county.getCountyName());
		values.put("county_code", county.getCountyCode());
		values.put("city_id", county.getCityId());
		db.insert("County", null, values);
		}
	}
	
	

	/**
	* 从数据库读取某城市下所有县信息。
	*/
	public List<City> loadCounty(String district_cn){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "district_cn =?",
				new String[] { String.valueOf(district_cn) },null,null,null);
		if (cursor.moveToNext()) {
			do {
				
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setProvince_cn(cursor.getString(cursor.getColumnIndex("province_cn")));
				city.setDistrict_cn(district_cn);
				city.setName_cn(cursor.getString(cursor.getColumnIndex("name_cn")));
				city.setName_en(cursor.getString(cursor.getColumnIndex("name_en")));
				city.setArea_id(cursor.getInt(cursor.getColumnIndex("area_id")));
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}
}
