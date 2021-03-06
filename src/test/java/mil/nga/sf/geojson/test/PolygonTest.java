package mil.nga.sf.geojson.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import mil.nga.sf.Geometry;
import mil.nga.sf.LinearRing;
import mil.nga.sf.geojson.GeoJsonObject;
import mil.nga.sf.geojson.Polygon;

public class PolygonTest {

	private static String POLYGON = "{\"type\":\"Polygon\",\"coordinates\":[[[100.0,10.0],[101.0,1.0],[101.0,10.0],[100.0,10.0]]]}";
	private static String POLYGON_WITH_ALT = "{\"type\":\"Polygon\",\"coordinates\":[[[100.0,10.0,5.0],[101.0,1.0,10.0],[101.0,10.0,15.0],[100.0,10.0,5.0]]]}";
	private static String POLYGON_WITH_RINGS = "{\"type\":\"Polygon\",\"coordinates\":[[[-100.0,-50.0],[100.0,-50.0],[1.0,50.0],[-100.0,-50.0]],[[-50.0,-25.0],[50.0,-25.0],[-1.0,25.0],[-50.0,-25.0]]]}";

	@Test
	public void itShouldSerializeASFPolygon() throws Exception {
		List<LinearRing> rings = new ArrayList<LinearRing>();
		List<mil.nga.sf.Point> points = new ArrayList<mil.nga.sf.Point>();
		points.add(new mil.nga.sf.Point(100d, 10d));
		points.add(new mil.nga.sf.Point(101d, 1d));
		points.add(new mil.nga.sf.Point(101d, 10d));
		LinearRing ring = new LinearRing(points);
		rings.add(ring);
		mil.nga.sf.Polygon polygon = new mil.nga.sf.Polygon(rings);
		TestUtils.compareAsNodes(polygon, POLYGON);
	}

	@Test
	public void itShouldSerializeASFPolygonWithAltitude() throws Exception {
		List<LinearRing> rings = new ArrayList<LinearRing>();
		List<mil.nga.sf.Point> points = new ArrayList<mil.nga.sf.Point>();
		points.add(new mil.nga.sf.Point(100d, 10d,  5d));
		points.add(new mil.nga.sf.Point(101d,  1d, 10d));
		points.add(new mil.nga.sf.Point(101d, 10d, 15d));
		LinearRing ring = new LinearRing(points);
		rings.add(ring);
		mil.nga.sf.Polygon polygon = new mil.nga.sf.Polygon(rings);
		TestUtils.compareAsNodes(polygon, POLYGON_WITH_ALT);
	}

	@Test
	public void itShouldSerializeASFPolygonWithRings() throws Exception {
		List<LinearRing> rings = new ArrayList<LinearRing>();
		List<mil.nga.sf.Point> positions = new ArrayList<mil.nga.sf.Point>();
		positions.add(new mil.nga.sf.Point(-100d, -50d));
		positions.add(new mil.nga.sf.Point( 100d, -50d));
		positions.add(new mil.nga.sf.Point(   1d,  50d));
		LinearRing ring = new LinearRing(positions);
		rings.add(ring);
		positions = new ArrayList<mil.nga.sf.Point>();
		positions.add(new mil.nga.sf.Point(-50d, -25d));
		positions.add(new mil.nga.sf.Point( 50d, -25d));
		positions.add(new mil.nga.sf.Point( -1d,  25d));
		ring = new LinearRing(positions);
		rings.add(ring);
		mil.nga.sf.Polygon polygon = new mil.nga.sf.Polygon(rings);
		TestUtils.compareAsNodes(polygon, POLYGON_WITH_RINGS);
	}


	@Test
	public void itShouldDeserializeAPolygon() throws Exception {
		GeoJsonObject value = TestUtils.getMapper().readValue(POLYGON, GeoJsonObject.class);
		assertNotNull(value);
		assertTrue(value instanceof Polygon);
		Polygon gjPolygon = (Polygon)value;
		Geometry geometry = gjPolygon.getGeometry();
		assertTrue(geometry instanceof mil.nga.sf.Polygon);
		mil.nga.sf.Polygon polygon = (mil.nga.sf.Polygon)geometry;
		List<LinearRing> rings = polygon.getRings();
		assertTrue(rings.size() == 1);
		LinearRing ring = rings.get(0);
		List<mil.nga.sf.Point> points = ring.getPoints();
		TestUtils.assertPosition(100d, 10d, null, null, points.get(0));
		TestUtils.assertPosition(101d,  1d, null, null, points.get(1));
		TestUtils.assertPosition(101d, 10d, null, null, points.get(2));
		TestUtils.assertPosition(100d, 10d, null, null, points.get(3));
	}

	@Test
	public void itShouldDeserializeAPolygonWithAltitude() throws Exception {
		GeoJsonObject value = TestUtils.getMapper().readValue(POLYGON_WITH_ALT, GeoJsonObject.class);
		assertNotNull(value);
		assertTrue(value instanceof Polygon);
		Polygon gjPolygon = (Polygon)value;
		Geometry geometry = gjPolygon.getGeometry();
		assertTrue(geometry instanceof mil.nga.sf.Polygon);
		mil.nga.sf.Polygon polygon = (mil.nga.sf.Polygon)geometry;
		List<LinearRing> rings = polygon.getRings();
		assertTrue(rings.size() == 1);
		LinearRing ring = rings.get(0);
		List<mil.nga.sf.Point> points = ring.getPoints();
		TestUtils.assertPosition(100d, 10d,  5d, null, points.get(0));
		TestUtils.assertPosition(101d,  1d, 10d, null, points.get(1));
		TestUtils.assertPosition(101d, 10d, 15d, null, points.get(2));
		TestUtils.assertPosition(100d, 10d,  5d, null, points.get(3));
	}

	@Test
	public void itShouldDeserializeAPolygonWithRings() throws Exception {
		GeoJsonObject value = TestUtils.getMapper().readValue(POLYGON_WITH_RINGS, GeoJsonObject.class);
		assertNotNull(value);
		assertTrue(value instanceof Polygon);
		Polygon gjPolygon = (Polygon)value;
		Geometry geometry = gjPolygon.getGeometry();
		assertTrue(geometry instanceof mil.nga.sf.Polygon);
		mil.nga.sf.Polygon polygon = (mil.nga.sf.Polygon)geometry;
		List<LinearRing> rings = polygon.getRings();
		assertTrue(rings.size() == 2);
		mil.nga.sf.LinearRing ring = rings.get(0);
		List<mil.nga.sf.Point> points = ring.getPoints();
		TestUtils.assertPosition(-100d, -50d, null, null, points.get(0));
		TestUtils.assertPosition( 100d, -50d, null, null, points.get(1));
		TestUtils.assertPosition(   1d,  50d, null, null, points.get(2));
		TestUtils.assertPosition(-100d, -50d, null, null, points.get(3));
		ring = rings.get(1);
		points = ring.getPoints();
		TestUtils.assertPosition(-50d, -25d, null, null, points.get(0));
		TestUtils.assertPosition( 50d, -25d, null, null, points.get(1));
		TestUtils.assertPosition( -1d,  25d, null, null, points.get(2));
		TestUtils.assertPosition(-50d, -25d, null, null, points.get(3));
	}
}
