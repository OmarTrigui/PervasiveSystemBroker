package server.model;

import java.io.Serializable;

public class MatchingProductsRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8805413983551146766L;
	
	private String category;
	private double latitude;
	private double longitude;

	public MatchingProductsRequest() {
	}

	public MatchingProductsRequest(String category, double latitude,
			double longitude) {
		this.category = category;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

}
