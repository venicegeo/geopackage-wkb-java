package mil.nga.sf.geojson.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import mil.nga.sf.Geometry;
import mil.nga.sf.LinearRing;
import mil.nga.sf.geojson.GeoJsonObject;
import mil.nga.sf.geojson.MultiPolygon;
import mil.nga.sf.geojson.Position;

public class MultiPolygonTest {

	private static String MULTIPOLYGON = "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[100.0,10.0],[101.0,1.0],[101.0,10.0],[100.0,10.0]]]]}";
	private static String MULTIPOLYGON_WITH_ALT = "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[100.0,10.0,5.0],[101.0,1.0,10.0],[101.0,10.0,15.0],[100.0,10.0,5.0]]]]}";
	private static String MULTIPOLYGON_WITH_RINGS = "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[-100.0,-50.0],[100.0,-50.0],[1.0,50.0],[-100.0,-50.0]],[[-50.0,-25.0],[50.0,-25.0],[-1.0,25.0],[-50.0,-25.0]]]]}";
	private static String MULTIPOLYGON_WITH_MULTI = "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[-100.0,-50.0],[100.0,-50.0],[1.0,50.0],[-100.0,-50.0]]],[[[-50.0,-25.0],[50.0,-25.0],[-1.0,25.0],[-50.0,-25.0]]]]}";

	@Test
	public void itShouldSerializeASFMultiPolygon() throws Exception {
		List<mil.nga.sf.Polygon> polygons = new ArrayList<mil.nga.sf.Polygon>();
		List<LinearRing> rings = new ArrayList<LinearRing>();
		List<mil.nga.sf.Point> points = new ArrayList<mil.nga.sf.Point>();
		points.add(new mil.nga.sf.Point(100d, 10d));
		points.add(new mil.nga.sf.Point(101d, 1d));
		points.add(new mil.nga.sf.Point(101d, 10d));
		LinearRing ring = new LinearRing(points);
		rings.add(ring);
		mil.nga.sf.Polygon polygon = new mil.nga.sf.Polygon(rings);
		polygons.add(polygon);
		mil.nga.sf.MultiPolygon multiPolygon = new mil.nga.sf.MultiPolygon(polygons);
		TestUtils.compareAsNodes(multiPolygon, MULTIPOLYGON);
	}

	@Test
	public void itShouldSerializeASFMultiPolygonWithAltitude() throws Exception {
		List<mil.nga.sf.Polygon> polygons = new ArrayList<mil.nga.sf.Polygon>();
		List<LinearRing> rings = new ArrayList<LinearRing>();
		List<mil.nga.sf.Point> points = new ArrayList<mil.nga.sf.Point>();
		points.add(new mil.nga.sf.Point(100d, 10d,  5d));
		points.add(new mil.nga.sf.Point(101d,  1d, 10d));
		points.add(new mil.nga.sf.Point(101d, 10d, 15d));
		LinearRing ring = new LinearRing(points);
		rings.add(ring);
		mil.nga.sf.Polygon polygon = new mil.nga.sf.Polygon(rings);
		polygons.add(polygon);
		mil.nga.sf.MultiPolygon multiPolygon = new mil.nga.sf.MultiPolygon(polygons);
		TestUtils.compareAsNodes(multiPolygon, MULTIPOLYGON_WITH_ALT);
	}

	@Test
	public void itShouldSerializeASFMultiPolygonWithRings() throws Exception {
		TestUtils.compareAsNodes(TestUtils.getMultiPolygonWithRings(), MULTIPOLYGON_WITH_RINGS);
	}

	@Test
	public void itShouldSerializeASFMultiPolygonWithMulti() throws Exception {
		List<mil.nga.sf.Polygon> polygons = new ArrayList<mil.nga.sf.Polygon>();
		// Polygon 1
		List<LinearRing> rings = new ArrayList<LinearRing>();
		List<mil.nga.sf.Point> points = new ArrayList<mil.nga.sf.Point>();
		points.add(new mil.nga.sf.Point(-100d, -50d));
		points.add(new mil.nga.sf.Point( 100d, -50d));
		points.add(new mil.nga.sf.Point(   1d,  50d));
		LinearRing ring = new LinearRing(points);
		rings.add(ring);
		mil.nga.sf.Polygon polygon = new mil.nga.sf.Polygon(rings);
		polygons.add(polygon);

		// P2R1
		rings = new ArrayList<LinearRing>();
		points = new ArrayList<mil.nga.sf.Point>();
		points.add(new mil.nga.sf.Point(-50d, -25d));
		points.add(new mil.nga.sf.Point( 50d, -25d));
		points.add(new mil.nga.sf.Point( -1d,  25d));
		ring = new LinearRing(points);
		rings.add(ring);
		polygon = new mil.nga.sf.Polygon(rings);
		polygons.add(polygon);
		
		mil.nga.sf.MultiPolygon multiPolygon = new mil.nga.sf.MultiPolygon(polygons);
		TestUtils.compareAsNodes(multiPolygon, MULTIPOLYGON_WITH_MULTI);
	}

	@Test
	public void itShouldDeserializeAMultiPolygon() throws Exception {
		GeoJsonObject value = TestUtils.getMapper().readValue(MULTIPOLYGON, GeoJsonObject.class);
		assertNotNull(value);
		assertTrue(value instanceof MultiPolygon);
		MultiPolygon gjMultiPolygon = (MultiPolygon)value;
		Geometry geometry = gjMultiPolygon.getGeometry();
		assertTrue(geometry instanceof mil.nga.sf.MultiPolygon);
		mil.nga.sf.MultiPolygon multiPolygon = (mil.nga.sf.MultiPolygon)geometry;
		List<mil.nga.sf.Polygon> polygons = multiPolygon.getGeometries();
		assertTrue(polygons.size() == 1);
		mil.nga.sf.Polygon polygon = polygons.get(0);
		List<LinearRing> rings = polygon.getRings();
		assertTrue(rings.size() == 1);
		mil.nga.sf.LinearRing ring = rings.get(0);
		List<mil.nga.sf.Point> points = ring.getPoints();
		TestUtils.assertPosition(100d, 10d, null, null, points.get(0));
		TestUtils.assertPosition(101d,  1d, null, null, points.get(1));
		TestUtils.assertPosition(101d, 10d, null, null, points.get(2));
		TestUtils.assertPosition(100d, 10d, null, null, points.get(3));
	}

	@Test
	public void itShouldDeserializeAMultiPolygonWithAltitude() throws Exception {
		GeoJsonObject value = TestUtils.getMapper().readValue(MULTIPOLYGON_WITH_ALT, GeoJsonObject.class);
		assertNotNull(value);
		assertTrue(value instanceof MultiPolygon);
		MultiPolygon gjMultiPolygon = (MultiPolygon)value;
		Geometry simpleGeometry = gjMultiPolygon.getGeometry();
		assertTrue(simpleGeometry instanceof mil.nga.sf.MultiPolygon);
		mil.nga.sf.MultiPolygon multiPolygon = (mil.nga.sf.MultiPolygon)simpleGeometry;
		List<mil.nga.sf.Polygon> polygons = multiPolygon.getGeometries();
		assertTrue(polygons.size() == 1);
		mil.nga.sf.Polygon polygon = polygons.get(0);
		List<LinearRing> rings = polygon.getRings();
		assertTrue(rings.size() == 1);
		mil.nga.sf.LinearRing ring = rings.get(0);
		List<mil.nga.sf.Point> points = ring.getPoints();
		TestUtils.assertPosition(100d, 10d,  5d, null, points.get(0));
		TestUtils.assertPosition(101d,  1d, 10d, null, points.get(1));
		TestUtils.assertPosition(101d, 10d, 15d, null, points.get(2));
		TestUtils.assertPosition(100d, 10d,  5d, null, points.get(3));
	}

	@Test
	public void itShouldDeserializeAMultiPolygonWithRings() throws Exception {
		GeoJsonObject value = TestUtils.getMapper().readValue(MULTIPOLYGON_WITH_RINGS, GeoJsonObject.class);
		assertNotNull(value);
		assertTrue(value instanceof MultiPolygon);
		MultiPolygon gjMultiPolygon = (MultiPolygon)value;
		Geometry simpleGeometry = gjMultiPolygon.getGeometry();
		assertTrue(simpleGeometry instanceof mil.nga.sf.MultiPolygon);
		mil.nga.sf.MultiPolygon multiPolygon = (mil.nga.sf.MultiPolygon)simpleGeometry;
		List<mil.nga.sf.Polygon> polygons = multiPolygon.getGeometries();
		assertTrue(polygons.size() == 1);
		mil.nga.sf.Polygon polygon = polygons.get(0);
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

	@Test
	public void itShouldDeserializeAMultiPolygonWithMulti() throws Exception {
		GeoJsonObject value = TestUtils.getMapper().readValue(MULTIPOLYGON_WITH_MULTI, GeoJsonObject.class);
		assertNotNull(value);
		assertTrue(value instanceof MultiPolygon);
		MultiPolygon gjMultiPolygon = (MultiPolygon)value;
		Geometry geometry = gjMultiPolygon.getGeometry();
		assertTrue(geometry instanceof mil.nga.sf.MultiPolygon);
		mil.nga.sf.MultiPolygon multiPolygon = (mil.nga.sf.MultiPolygon)geometry;
		List<mil.nga.sf.Polygon> polygons = multiPolygon.getGeometries();
		assertTrue(polygons.size() == 2);
		mil.nga.sf.Polygon polygon = polygons.get(0);
		List<LinearRing> rings = polygon.getRings();
		assertTrue(rings.size() == 1);
		mil.nga.sf.LinearRing ring = rings.get(0);
		List<mil.nga.sf.Point> points = ring.getPoints();
		TestUtils.assertPosition(-100d, -50d, null, null, points.get(0));
		TestUtils.assertPosition( 100d, -50d, null, null, points.get(1));
		TestUtils.assertPosition(   1d,  50d, null, null, points.get(2));
		TestUtils.assertPosition(-100d, -50d, null, null, points.get(3));
		polygon = polygons.get(1);
		rings = polygon.getRings();
		assertTrue(rings.size() == 1);
		ring = rings.get(0);
		points = ring.getPoints();
		TestUtils.assertPosition(-50d, -25d, null, null, points.get(0));
		TestUtils.assertPosition( 50d, -25d, null, null, points.get(1));
		TestUtils.assertPosition( -1d,  25d, null, null, points.get(2));
		TestUtils.assertPosition(-50d, -25d, null, null, points.get(3));
	}
}
