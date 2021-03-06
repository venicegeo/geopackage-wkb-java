package mil.nga.sf.wkb;

import java.io.IOException;
import java.nio.ByteOrder;

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
import mil.nga.sf.TIN;
import mil.nga.sf.Triangle;
import mil.nga.sf.util.ByteWriter;
import mil.nga.sf.util.SFException;

/**
 * Well Known Binary writer
 * 
 * @author osbornb
 */
public class GeometryWriter {

	/**
	 * Write a geometry to the byte writer
	 * 
	 * @param writer
	 * @param geometry
	 * @throws IOException
	 */
	public static void writeGeometry(ByteWriter writer, Geometry geometry)
			throws IOException {

		// Write the single byte order byte
		byte byteOrder = writer.getByteOrder() == ByteOrder.BIG_ENDIAN ? (byte) 0
				: (byte) 1;
		writer.writeByte(byteOrder);

		// Write the geometry type integer
		writer.writeInt(Utils.getWkbCode(geometry));

		GeometryType geometryType = geometry.getGeometryType();

		switch (geometryType) {

		case GEOMETRY:
			throw new SFException("Unexpected Geometry Type of "
					+ geometryType.name() + " which is abstract");
		case POINT:
			writePoint(writer, (Point) geometry);
			break;
		case LINESTRING:
			writeLineString(writer, (Curve) geometry);
			break;
		case POLYGON:
			writePolygon(writer, (Polygon) geometry);
			break;
		case MULTIPOINT:
			writeMultiPoint(writer, (MultiPoint) geometry);
			break;
		case MULTILINESTRING:
			writeMultiLineString(writer, (MultiLineString) geometry);
			break;
		case MULTIPOLYGON:
			writeMultiPolygon(writer, (MultiPolygon) geometry);
			break;
		case GEOMETRYCOLLECTION:
			writeGeometryCollection(writer, (SimpleGeometryCollection) geometry);
			break;
		case CIRCULARSTRING:
			writeCircularString(writer, (CircularString) geometry);
			break;
		case COMPOUNDCURVE:
			writeCompoundCurve(writer, (CompoundCurve) geometry);
			break;
		case CURVEPOLYGON:
			writeCurvePolygon(writer, (Polygon) geometry);
			break;
		case MULTICURVE:
			throw new SFException("Unexpected Geometry Type of "
					+ geometryType.name() + " which is abstract");
		case MULTISURFACE:
			throw new SFException("Unexpected Geometry Type of "
					+ geometryType.name() + " which is abstract");
		case CURVE:
			throw new SFException("Unexpected Geometry Type of "
					+ geometryType.name() + " which is abstract");
		case SURFACE:
			throw new SFException("Unexpected Geometry Type of "
					+ geometryType.name() + " which is abstract");
		case POLYHEDRALSURFACE:
			writePolyhedralSurface(writer, (PolyhedralSurface) geometry);
			break;
		case TIN:
			writeTIN(writer, (TIN) geometry);
			break;
		case TRIANGLE:
			writeTriangle(writer, (Triangle) geometry);
			break;
		default:
			throw new SFException("Geometry Type not supported: "
					+ geometryType);
		}

	}

	/**
	 * Write a Point
	 * 
	 * @param writer
	 * @param point
	 * @throws IOException
	 */
	public static void writePoint(ByteWriter writer, Point point)
			throws IOException {

		writer.writeDouble(point.getX());
		writer.writeDouble(point.getY());

		Double alt = point.getZ();
		if (alt != null){
			writer.writeDouble(alt);
		}

		Double emm = point.getM();
		if (emm != null) {
			writer.writeDouble(emm);
		}
	}

	/**
	 * Write a Line String
	 * 
	 * @param writer
	 * @param lineString
	 * @throws IOException
	 */
	public static void writeLineString(ByteWriter writer, Curve lineString)
			throws IOException {

		writer.writeInt(lineString.numPoints());

		for (Point point : lineString.getPoints()) {
			writePoint(writer, point);
		}
	}

	/**
	 * Write a Polygon
	 * 
	 * @param writer
	 * @param polygon
	 * @throws IOException
	 */
	public static void writePolygon(ByteWriter writer, Polygon polygon)
			throws IOException {

		writer.writeInt(polygon.numRings());

		for (LinearRing ring : polygon.getRings()) {
			writeLineString(writer, ring);
		}
	}

	/**
	 * Write a Multi Point
	 * 
	 * @param writer
	 * @param multiPoint
	 * @throws IOException
	 */
	public static void writeMultiPoint(ByteWriter writer, MultiPoint multiPoint)
			throws IOException {

		writer.writeInt(multiPoint.numGeometries());

		for (Point point : multiPoint.getGeometries()) {
			writeGeometry(writer, point);
		}
	}

	/**
	 * Write a Multi Line String
	 * 
	 * @param writer
	 * @param multiLineString
	 * @throws IOException
	 */
	public static void writeMultiLineString(ByteWriter writer,
			MultiLineString multiLineString) throws IOException {

		writer.writeInt(multiLineString.numGeometries());

		for (LineString lineString : multiLineString.getGeometries()) {
			writeGeometry(writer, lineString);
		}
	}

	/**
	 * Write a Multi Polygon
	 * 
	 * @param writer
	 * @param multiPolygon
	 * @throws IOException
	 */
	public static void writeMultiPolygon(ByteWriter writer,
			MultiPolygon multiPolygon) throws IOException {

		writer.writeInt(multiPolygon.numGeometries());

		for (Polygon polygon : multiPolygon.getGeometries()) {
			writeGeometry(writer, polygon);
		}
	}

	/**
	 * Write a Geometry Collection
	 * 
	 * @param writer
	 * @param geometryCollection
	 * @throws IOException
	 */
	public static void writeGeometryCollection(ByteWriter writer,
			SimpleGeometryCollection geometryCollection) throws IOException {

		writer.writeInt(geometryCollection.numGeometries());

		for (Geometry simpleGeometry : geometryCollection.getGeometries()) {
			writeGeometry(writer, simpleGeometry);
		}
	}

	/**
	 * Write a Circular String
	 * 
	 * @param writer
	 * @param circularString
	 * @throws IOException
	 */
	public static void writeCircularString(ByteWriter writer,
			CircularString circularString) throws IOException {

		writer.writeInt(circularString.numPoints());

		for (Point point : circularString.getPoints()) {
			writePoint(writer, point);
		}
	}

	/**
	 * Write a Compound Curve
	 * 
	 * @param writer
	 * @param compoundCurve
	 * @throws IOException
	 */
	public static void writeCompoundCurve(ByteWriter writer,
			CompoundCurve compoundCurve) throws IOException {

		writer.writeInt(compoundCurve.numCurves());

		for (Curve curve : compoundCurve.getCurves()) {
			writeGeometry(writer, curve);
		}
	}

	/**
	 * Write a Curve Polygon
	 * 
	 * @param writer
	 * @param curvePolygon
	 * @throws IOException
	 */
	public static void writeCurvePolygon(ByteWriter writer,
			Polygon curvePolygon) throws IOException {

		writer.writeInt(curvePolygon.numRings());

		for (LinearRing ring : curvePolygon.getRings()) {
			writeGeometry(writer, ring);
		}
	}

	/**
	 * Write a Polyhedral Surface
	 * 
	 * @param writer
	 * @param polyhedralSurface
	 * @throws IOException
	 */
	public static void writePolyhedralSurface(ByteWriter writer,
			PolyhedralSurface polyhedralSurface) throws IOException {

		writer.writeInt(polyhedralSurface.numPolygons());

		for (Polygon polygon : polyhedralSurface.getPolygons()) {
			writeGeometry(writer, polygon);
		}
	}

	/**
	 * Write a TIN
	 * 
	 * @param writer
	 * @param tin
	 * @throws IOException
	 */
	public static void writeTIN(ByteWriter writer, TIN tin) throws IOException {

		writer.writeInt(tin.numPolygons());

		for (Polygon polygon : tin.getPolygons()) {
			writeGeometry(writer, polygon);
		}
	}

	/**
	 * Write a Triangle
	 * 
	 * @param writer
	 * @param triangle
	 * @throws IOException
	 */
	public static void writeTriangle(ByteWriter writer, Triangle triangle)
			throws IOException {

		writer.writeInt(triangle.numRings());

		for (LinearRing ring : triangle.getRings()) {
			writeLineString(writer, ring);
		}
	}

}
