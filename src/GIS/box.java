package GIS;

import Geom.Point3D;

/** This class represents a box that used as an obstacle in the game 
 * each box has start point and end that represent it and unique id.
 * 
 * @author aric
 *
 */

public class box {
	
	private Point3D start;
	private Point3D end;
	private String id;
	
	public box(Point3D p1,Point3D p2,String id) {
		
		start=p1;
		end=p2;
		this.id=id;
	}

	public Point3D getStart() {
		return start;
	}

	public void setStart(Point3D start) {
		this.start = start;
	}

	public Point3D getEnd() {
		return end;
	}

	public void setEnd(Point3D end) {
		this.end = end;
	}
	
	public Point3D getRightDown() {
		return new Point3D(end.x(),start.y(),0);
	}
	public Point3D getLeftUp() {
		return new Point3D(start.x(),end.y(),0);
	}
	public String getId() {
		return id;
	}
}
