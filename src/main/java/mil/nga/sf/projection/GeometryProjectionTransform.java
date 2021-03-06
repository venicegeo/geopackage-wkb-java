package mil.nga.sf.projection;

import mil.nga.sf.CircularString;
import mil.nga.sf.CompoundCurve;
import mil.nga.sf.Curve;
import mil.nga.sf.Polygon;
import mil.nga.sf.Geometry;
import mil.nga.sf.SimpleGeometryCollection;
import mil.nga.sf.GeometryType;
import mil.nga.sf.LineString;
import mil.nga.sf.LinearRing;
import mil.nga.sf.MultiLineString;
import mil.nga.sf.MultiPoint;
import mil.nga.sf.MultiPolygon;
import mil.nga.sf.Point;
import mil.nga.sf.PolyhedralSurface;
import mil.nga.sf.Position;
import mil.nga.sf.TIN;
import mil.nga.sf.Triangle;
import mil.nga.sf.util.SFException;

import org.osgeo.proj4j.ProjCoordinate;

/**
 * Geometry Projection Transform
 * 
 * @author osbornb
 * @since 1.1.3
 */
public class GeometryProjectionTransform {

	/**
	 * Projection transform
	 */
	private final ProjectionTransform transform;

	/**
	 * Constructor
	 * 
	 * @param transform
	 */
	public GeometryProjectionTransform(ProjectionTransform transform) {
		this.transform = transform;
	}

	/**
	 * Transform the geometry
	 * 
	 * @param geometry
	 * @return projected geometry
	 */
	public Geometry transform(Geometry geometry) {

		Geometry to = null;

		GeometryType geometryType = geometry.getGeometryType();
		switch (geometryType) {
		case POINT:
			to = transform((Point) geometry);
			break;
		case LINESTRING:
			to = transform((LineString) geometry);
			break;
		case POLYGON:
			to = transform((Polygon) geometry);
			break;
		case MULTIPOINT:
			to = transform((MultiPoint) geometry);
			break;
		case MULTILINESTRING:
			to = transform((MultiLineString) geometry);
			break;
		case MULTIPOLYGON:
			to = transform((MultiPolygon) geometry);
			break;
		case CIRCULARSTRING:
			to = transform((CircularString) geometry);
			break;
		case COMPOUNDCURVE:
			to = transform((CompoundCurve) geometry);
			break;
		case POLYHEDRALSURFACE:
			to = transform((PolyhedralSurface) geometry);
			break;
		case TIN:
			to = transform((TIN) geometry);
			break;
		case TRIANGLE:
			to = transform((Triangle) geometry);
			break;
		case GEOMETRYCOLLECTION:
			SimpleGeometryCollection toCollection = transform((SimpleGeometryCollection) geometry);
			to = toCollection;
			break;
		default:
			throw new SFException("Unsupported Geometry Type: "
					+ geometryType);
		}

		return to;
	}

	/**
	 * Transform the projected position
	 * 
	 * @param from
	 * @return projected from
	 */
	public Position transform(Position from) {

		ProjCoordinate fromCoord;
		if (from.hasZ()) {
			fromCoord = new ProjCoordinate(from.getX(), from.getY(),
					from.getZ() != null ? from.getZ() : Double.NaN);
		} else {
			fromCoord = new ProjCoordinate(from.getX(), from.getY());
		}

		ProjCoordinate toCoord = transform.transform(fromCoord);
		
		Double emm = from.hasM() ? from.getM() : null;
		Double alt = from.hasZ() ? (Double.isNaN(toCoord.z) ? from.getZ() : toCoord.z) : null;

		Point to = new Point(toCoord.x, toCoord.y, emm, alt);

		return to;
	}

	/**
	 * Transform the projected point
	 * 
	 * @param from
	 * @return projected from
	 */
	public Point transform(Point from) {

		return new Point(transform((Position)from));
	}

	/**
	 * Transform the projected line string
	 * 
	 * @param curve
	 * @return projected line string
	 */
	public Curve transform(Curve curve) {

		Curve to = new LineString(curve.hasZ(), curve.hasM());

		for (Point point : curve.getPoints()) {
			Point toPoint = transform(point);
			to.addPoint(toPoint);
		}

		return to;
	}

	/**
	 * Transform the projected line string
	 * 
	 * @param lineString
	 * @return projected line string
	 */
	public LineString transform(LineString lineString) {

		LineString to = (LineString)transform((Curve)lineString);

		return to;
	}

	/**
	 * Transform the projected linear ring
	 * 
	 * @param linearRing
	 * @return projected linear ring
	 */
	public LinearRing transform(LinearRing linearRing) {

		return new LinearRing(transform((Curve)linearRing));
	}

	/**
	 * Transform the projected polygon
	 * 
	 * @param polygon
	 * @return projected polygon
	 */
	public Polygon transform(Polygon polygon) {

		Polygon to = new Polygon(polygon.hasZ(), polygon.hasM());

		for (LinearRing ring : polygon.getRings()) {
			LinearRing toRing = new LinearRing(transform(ring));
			to.addRing(toRing);
		}

		return to;
	}

	/**
	 * Transform the projected multi point
	 * 
	 * @param multiPoint
	 * @return projected multi point
	 */
	public MultiPoint transform(MultiPoint multiPoint) {

		MultiPoint to = new MultiPoint(multiPoint.hasZ(), multiPoint.hasM());

		for (Point point : multiPoint.getGeometries()) {
			Point toPoint = transform(point);
			to.addGeometry(toPoint);
		}

		return to;
	}

	/**
	 * Transform the projected multi line string
	 * 
	 * @param multiLineString
	 * @return projected multi line string
	 */
	public MultiLineString transform(MultiLineString multiLineString) {

		MultiLineString to = new MultiLineString(multiLineString.hasZ(),
				multiLineString.hasM());

		for (LineString lineString : multiLineString.getGeometries()) {
			LineString toLineString = transform(lineString);
			to.addGeometry(toLineString);
		}

		return to;
	}

	/**
	 * Transform the projected multi polygon
	 * 
	 * @param multiPolygon
	 * @return projected multi polygon
	 */
	public MultiPolygon transform(MultiPolygon multiPolygon) {

		MultiPolygon to = new MultiPolygon(multiPolygon.hasZ(),
				multiPolygon.hasM());

		for (Polygon polygon : multiPolygon.getGeometries()) {
			Polygon toPolygon = transform(polygon);
			to.addGeometry(toPolygon);
		}

		return to;
	}

	/**
	 * Transform the projected circular string
	 * 
	 * @param circularString
	 * @return projected circular string
	 */
	public CircularString transform(CircularString circularString) {

		CircularString to = new CircularString(circularString.hasZ(),
				circularString.hasM());

		for (Point point : circularString.getPoints()) {
			Point toPoint = transform(point);
			to.addPoint(toPoint);
		}

		return to;
	}

	/**
	 * Transform the projected polyhedral surface
	 * 
	 * @param polyhedralSurface
	 * @return projected polyhedral surface
	 */
	public PolyhedralSurface transform(PolyhedralSurface polyhedralSurface) {

		PolyhedralSurface to = new PolyhedralSurface(polyhedralSurface.hasZ(),
				polyhedralSurface.hasM());

		for (Polygon polygon : polyhedralSurface.getPolygons()) {
			Polygon toPolygon = transform(polygon);
			to.addPolygon(toPolygon);
		}

		return to;
	}

	/**
	 * Transform the projected TIN
	 * 
	 * @param tin
	 * @return projected tin
	 */
	public TIN transform(TIN tin) {

		TIN to = new TIN(tin.hasZ(), tin.hasM());

		for (Polygon polygon : tin.getPolygons()) {
			Polygon toPolygon = transform(polygon);
			to.addPolygon(toPolygon);
		}

		return to;
	}

	/**
	 * Transform the projected triangle
	 * 
	 * @param triangle
	 * @return projected triangle
	 */
	public Triangle transform(Triangle triangle) {

		Triangle to = new Triangle(triangle.hasZ(), triangle.hasM());

		for (LinearRing ring : triangle.getRings()) {
			LinearRing toRing = new LinearRing(transform(ring));
			to.addRing(toRing);
		}

		return to;
	}

	/**
	 * Transform the projected geometry collection
	 * 
	 * @param geometryCollection
	 * @return projected geometry collection
	 */
	public SimpleGeometryCollection transform(
			SimpleGeometryCollection geometryCollection) {

		SimpleGeometryCollection to = new SimpleGeometryCollection(
				geometryCollection.hasZ(), geometryCollection.hasM());

		for (Geometry geometry : geometryCollection.getGeometries()) {
			Geometry toGeometry = transform(geometry);
			to.addGeometry(toGeometry);
		}

		return to;
	}

}
