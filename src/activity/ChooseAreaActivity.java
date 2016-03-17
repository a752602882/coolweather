package activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import com.coolweather.app.R;

import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;



import model.City;
import model.CoolWeatherDB;
import model.County;
import model.Province;




import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.provider.DocumentsContract.Document;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity  extends Activity {
	
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY=2;
    
    
    private  ProgressDialog progressDialog;
    private  TextView  titleText;
    private  ListView  listView;
    private  ArrayAdapter<String> adapter;
    private  CoolWeatherDB coolWeatherDB;
    private  List<String> dataList  = new ArrayList<String>();
	
	
    /**
     * 省列表
     * */
    private List<Province> provinceList;
    private List<City> cityList;
    private List<City> countyList;
    
    /**
     * 选中的省份
     * 
     * */
    private Province  selectedProvince;
    private City  selectedCity;
    private City  selectedCity_no_repeat;
    /**
    * 当前选中的级别
    */
    private int currentLevel;
	private Object context;
    
	/**
	* 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
	*/
    private void addProvinceDB(){
    	
    	org.w3c.dom.Document document=null;
    	InputStream inputStream=null;
    	DocumentBuilderFactory factory=null;
    	DocumentBuilder builder=null;
    	
    	factory=DocumentBuilderFactory.newInstance();

    	Province  province = new Province();;
    	try {
    		
    		builder=factory.newDocumentBuilder();
    		inputStream=this.getResources().getAssets().open("province_data.xml");
            document=builder.parse(inputStream);

          //找到根Element
            Element root=document.getDocumentElement();
            NodeList nodes=root.getElementsByTagName("value");

            for (int i = 0; i < nodes.getLength(); i++) {
				String name = root.getElementsByTagName("province").item(i).getFirstChild().getNodeValue();
				String code = root.getElementsByTagName("id").item(i).getFirstChild().getNodeValue();
                province.setProvinceCode(code);
                province.setProvinceName(name);
                coolWeatherDB.saveProvince(province);
                //province=null;
            }
    		
    		
    	
    	} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	queryProvinces();
    	
    }
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView  = (ListView) findViewById(R.id.list_view);
		titleText =  (TextView) findViewById(R.id.title_text);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter);
		
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				// TODO Auto-generated method stub
				if (currentLevel==LEVEL_PROVINCE) {
					selectedProvince =provinceList.get(index);
					queryCities();
					
				}else if(currentLevel==LEVEL_CITY){
					selectedCity = cityList.get(index);
					queryCounties();
				}
				
			}
		});
		
		queryProvinces(); // 加载省级数据
	}
	
	/**
	* 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
	*/
	private void queryProvinces(){
		provinceList = coolWeatherDB.loadProvinces();
		if(provinceList.size()>0){
			dataList.clear();
			for (Province province: provinceList) 
				dataList.add(province.getProvinceName());

			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		}else {
			addProvinceDB();
	        //queryFromServer(null, "province");	
		}
		
	}
	
	
	
	
	/**
	* 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
	*/
	private void queryCities() {
		
		cityList = coolWeatherDB.loadCities(selectedProvince.getProvinceName());
		   if (cityList.size()>0) {
			//   Map<String, Integer> dataMap = new HashMap<String, Integer>();
			   dataList.clear();
			/*
			 for (City city: cityList){ 
				dataMap.put(city.getDistrict_cn(), 0);
			    
			}	
			Iterator iter = dataMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				dataList.add((String) entry.getKey());
			}
			*/
			   for (City city : cityList) {
					dataList.add(city.getName_cn());
					}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel=LEVEL_CITY;
		}else if(currentLevel==LEVEL_CITY&&cityList.size()==0) {
			return;
		}else{
			queryFromServer(selectedProvince.getProvinceName(), "city");
		}
	}
	
	/**
	* 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
	*/
	private void queryCounties() {
		countyList = coolWeatherDB.loadCounty(selectedCity.getDistrict_cn());
		if (countyList.size() > 0) {
		dataList.clear();
		for (City county : countyList) {
		dataList.add(county.getName_cn());
		}
		adapter.notifyDataSetChanged();
		listView.setSelection(0);
		titleText.setText(selectedCity.getDistrict_cn());
		currentLevel = LEVEL_COUNTY;
		} else {
		   //queryFromServer(selectedCity.getCityCode(), "county");
		}
	}
	private void queryFromServer(final String code,final String type){
		String address;
		if (!TextUtils.isEmpty(code)) 
			address= "http://apis.baidu.com/apistore/weatherservice/citylist"+code+".xml";
		else 
			address = "http://apis.baidu.com/apistore/weatherservice/citylist"+"?"+"cityname="+selectedProvince;
		
		showProgressDialog();
		HttpUtil.sendHttpRequest(code, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result =false;
				if("province".equals(type))	
					//result = Utility.handleCityResponse(coolWeatherDB, response, selectedProvince.getId());
					Log.d("s", "ss");
					//result = Utility.handleProvincesResponse(coolWeatherDB, response);
				else if ("city".equals(type)) 
					result = Utility.handleCityResponse(coolWeatherDB, response, selectedProvince.getId());
				else if("county".equals(type))
					result = Utility.handleCityResponse(coolWeatherDB, response, selectedCity.getId());
								
				if (result) {
					// 通过runOnUiThread()方法回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
						   closeProgressDialog();
						   if ("province".equals(type)) 
							  queryProvinces();
						   else if("city".equals(type))
							   queryCities();
						   else if("county".equals(type))
							   queryCounties();
						}
					});
				}
				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					 public void run() {
						 closeProgressDialog();
						 Toast.makeText(ChooseAreaActivity.this,"加载失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		
	}
	
	
	/**
	* 显示进度对话框
	*/
	private void showProgressDialog() {
	if (progressDialog ==null) {
		progressDialog= new ProgressDialog(this);
		progressDialog.setMessage("正在加载。。。");
		progressDialog.setCanceledOnTouchOutside(false);
	   }
	  progressDialog.show();
	}
	/**
	* 关闭进度对话框
	*/
	private void closeProgressDialog() {
	if (progressDialog != null) {
	progressDialog.dismiss();
	  }
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		if (currentLevel == LEVEL_COUNTY) {
			queryCities();
			} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
			} else {
			finish();
			}
	}
	
}
