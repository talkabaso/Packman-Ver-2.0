package GIS;

import Geom.Point3D;

/**
 * this class represents the ghost element in the game
 * @author Tal and Aric
 */
public class ghost {
	
	private Point3D position;
	private metaDataPack data;

	// Constructor //
	public ghost(metaDataPack data,Point3D position) {

		this.data=data;
		this.position=position;
	}

	//Getters and Setters
	
	public Point3D getPosition() {

		return position;
	}

	public metaDataPack getData() {
		return data;
	}

	public void setData(metaDataPack data) {
		this.data = data;
	}

	public double getSpeed() {

		return data.getSpeed();
	}
	
	public double getRadius() {

		return data.getRadius();
	}

	public void setPosition(Point3D p) {

		position.set_x(p.x());
		position.set_y(p.y());
		position.set_z(p.z());
	}

	public String getId() {
		return data.getId();
	}

	public void setId(String id) {
		data.setId(id);
	}

	public double getX() {
		return position.get_x();
	}

	public void setX(double x) {
		position.set_x(x);
	}

	public double getY() {
		return position.get_y();
	}

	public void setY(double y) {
		position.set_y(y);
	}

	public double getZ() {
		return position.get_z();
	}

	public void setZ(double z) {
		position.set_z(z);
	}

	public void setSpeed(double speed) {
		data.setSpeed(speed);	
	}

}