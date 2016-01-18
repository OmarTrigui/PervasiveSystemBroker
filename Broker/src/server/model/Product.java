package server.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product implements Serializable {

	private static final long serialVersionUID = 1113799434508676095L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "category")
	private String category;
	@Column(name = "price")
	private double price;
	@Column(name = "description")
	private String description;
	@Lob
	@Column(name = "thumbnail", length = 1000000)
	private byte[] thumbnail;
	@Lob
	@Column(name = "videodemo", length = 100000000)
	private byte[] videoDemo;
	@Column(name = "longitude")
	private double longitude;
	@Column(name = "latitude")
	private double latitude;

	public Product() {
	}

	public Product(String name, String category, double price,
			String description, byte[] thumbnail, byte[] videoDemo,
			double longitude, double latitude) {

		this.name = name;
		this.category = category;
		this.price = price;
		this.description = description;
		this.thumbnail = thumbnail;
		this.videoDemo = videoDemo;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	public byte[] getVideoDemo() {
		return videoDemo;
	}

	public void setVideoDemo(byte[] videoDemo) {
		this.videoDemo = videoDemo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", category="
				+ category + ", price=" + price + ", description="
				+ description + ", thumbnail=" + thumbnail.length
				+ ", videoDemo=" + videoDemo.length + ", longitude="
				+ longitude + ", latitude=" + latitude + "]";
	}

}
