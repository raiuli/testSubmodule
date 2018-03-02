package se.ltu.ssr.common.data;

public class SSRGeoLocation {
	private double latitude;
	private double longitude;
	private String address;
	
	
	public SSRGeoLocation() {
		super();
	}
	public SSRGeoLocation(long latitude, long longitude, String address) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SSRGeoLocation [latitude=");
		builder.append(latitude);
		builder.append(", longitude=");
		builder.append(longitude);
		builder.append(", address=");
		builder.append(address);
		builder.append("]");
		return builder.toString();
	}
	
	

}
