package GIS;

/**
 * this class contains all the data of Packman
 * @author Tal and Aric
 */

public class metaDataPack { // for not creating objects of 

	private String id;
	private double speed; // Speed of Packman
	private double radius; // radius of Packman
	private double score; // Score of all eaten fruits
	
	
	// Constructor //
	public metaDataPack(String id,double speed,double radius) {
		this.id=id;
		this.speed=speed;
		this.radius=radius;
		this.score=0;
	}
	
	// Getters and Setters //

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "metaDataPack [id=" + id + ", speed=" + speed + ", radius=" + radius + ", score=" + score + "]";
	}
}