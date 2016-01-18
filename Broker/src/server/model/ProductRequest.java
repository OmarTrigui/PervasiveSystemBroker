package server.model;

import java.io.Serializable;

public class ProductRequest implements Serializable {

	private static final long serialVersionUID = 8805413983551141234L;

	private int id;

	public ProductRequest() {
	}

	public ProductRequest(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
