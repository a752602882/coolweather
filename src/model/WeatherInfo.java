package model;

public class WeatherInfo {
	
	
	private String date;
	
	private String  week;
	private String  curTemp;
	private String  aqi;
	private String  fengxiang;
	private String  fengli;
	private String  hightemp;
	private String  lowtemp;
	private String  type;
	private String  name_cn;
	private String  area_id;

	public String getName_cn() {
		return name_cn;
	}
	public void setName_cn(String name_cn) {
		this.name_cn = name_cn;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getCurTemp() {
		return curTemp;
	}
	public void setCurTemp(String curTemp) {
		this.curTemp = curTemp;
	}
	public String getAqi() {
		return aqi;
	}
	public void setAqi(String aqi) {
		this.aqi = aqi;
	}
	public String getFengxiang() {
		return fengxiang;
	}
	public void setFengxiang(String fengxiang) {
		this.fengxiang = fengxiang;
	}
	public String getFengli() {
		return fengli;
	}
	public void setFengli(String fengli) {
		this.fengli = fengli;
	}
	public String getHightemp() {
		return hightemp;
	}
	public void setHightemp(String hightemp) {
		this.hightemp = hightemp;
	}
	public String getLowtemp() {
		return lowtemp;
	}
	public void setLowtemp(String lowtemp) {
		this.lowtemp = lowtemp;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
