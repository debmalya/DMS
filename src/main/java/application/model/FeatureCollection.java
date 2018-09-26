package application.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureCollection {
	private String type = "FeatureCollection";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	private Map<String,String> metadata = new HashMap<>();

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}
	
	private List<Feature> features = new ArrayList<>();

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}
	private List<Double> bbox = new ArrayList<>();

	public List<Double> getBbox() {
		return bbox;
	}

	public void setBbox(List<Double> bbox) {
		this.bbox = bbox;
	}
	
	
	
	
	
}
