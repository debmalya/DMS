package application.model;

import java.util.HashMap;
import java.util.Map;

public class Feature {
	private String type = "Feature";
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	private Geometry geometry;
	private Map<String,String> properties = new HashMap<>();
	public Geometry getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

}
