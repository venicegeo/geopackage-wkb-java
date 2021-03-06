package mil.nga.sf.geojson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import mil.nga.sf.GeometryType;

import java.util.Map;

@JsonIgnoreProperties({"feature", "geometryType"})
public class Feature extends GeoJsonObject {
	
	private static final long serialVersionUID = -2507073025031506871L;

	private mil.nga.sf.Feature feature = new mil.nga.sf.SimpleFeature();

	private String id;

	@JsonInclude(JsonInclude.Include.ALWAYS)
	public Geometry getGeometry() {

		return GeoJsonObjectFactory.createObject(feature.getGeometry());
	}

	public void setGeometry(GeoJsonObject gjObject) {
		if (gjObject instanceof Point) {
			Point point = (Point)gjObject;
			this.feature.setGeometry(point.getGeometry());
		} else if (gjObject instanceof Geometry) {
			Geometry geometry = (Geometry)gjObject;
			this.feature.setGeometry((geometry == null) ? null : geometry.getGeometry());
		}
	}

	@JsonInclude(JsonInclude.Include.ALWAYS)
	public Map<String, Object> getProperties() {
		return feature.getProperties();
	}

	public void setProperties(Map<String, Object> properties) {
		feature.setProperties(properties);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public <T> T accept(GeoJsonObjectVisitor<T> geoJsonObjectVisitor) {
		return geoJsonObjectVisitor.visit(this);
	}

	public mil.nga.sf.Feature getFeature() {
		return feature;
	}
	
	public GeometryType getGeometryType() {
		try {
			return getGeometry().getGeometry().getGeometryType();
		} catch (NullPointerException exc){
			return null;
		}
	}

	@Override
	public String getType() {
		return "Feature";
	}
}
