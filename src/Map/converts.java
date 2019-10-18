package Map;

import java.io.IOException;
import java.util.ArrayList;
import Coords.MyCoords;
import GIS.box;
import GIS.fruit;
import GIS.ghost;
import GIS.metaDataFruit;
import GIS.metaDataPack;
import GIS.packman;
import Geom.Point3D;

/**
 * this class contains all converts from pixels to Coordinates and vice versa 
 * @author Tal and Aric
 */

public class converts {

	static map m ;

	// Constructor //
	public converts() throws IOException {

		m= new map();
	}

	/**
	 * this function convert Pixels to Coordinates
	 * @param x pixel on X-axis
	 * @param y pixel on Y-axis
	 * @param h the height of the current screen
	 * @param w the width of the current screen
	 * @return Point3D after convert to [lat,lon,alt]
	 */
	public static Point3D pixel2Coords(double x,double y,double h,double w) {

		//		double dx=(x+20)/w;
		//		double dy=(y+40)/h;

		double dx=(x)/w;
		double dy=(y)/h;

		double totalLon=m.end.y()-m.start.y();
		double totalLat=m.end.x()-m.start.x();

		double goLon=totalLon*dx;
		double goLat=totalLat*dy;

		double answer1=m.start.y()+goLon;
		double answer2=m.start.x()+goLat;

		return new Point3D(answer2,answer1,0);
	}

	/**
	 * this function convert Coordinates to Pixels
	 * @param x coordinate value
	 * @param y coordinate value
	 * @param h the height of the current screen
	 * @param w the width of the current screen
	 * @return Point3D after convert to [lat,lon,alt]
	 */
	public static Point3D coords2Pixel(double x,double y,double h,double w) {

		double distLat = m.start.x()-m.end.x();
		double distLon = m.end.y()-m.start.y();
		double dx=(m.start.x()-x)/(distLat);
		double dy=(y-m.start.y())/(distLon);

		double goY=(h*dx);
		double goX=(w*dy);

		int answer1=(int)(goY);
		int answer2=(int)(goX);

		return new Point3D(answer2,answer1,0);	
	}

	/**
	 * this function calculate the distance between two pixels
	 * @param x pixel on X-axis
	 * @param y pixel on Y-axis
	 * @param h the height of the current screen
	 * @param w the width of the current screen
	 * @return the distance
	 */
	public static double distanceBet2Pixels(pix a,pix b,double h,double w) {

		Point3D p1=pixel2Coords(a.x(),a.y(),h,w);
		Point3D p2=pixel2Coords(b.x(),b.y(),h,w);

		MyCoords m =new MyCoords();
		double distance=m.distance3d(p1, p2);

		return distance;
	}

	/**
	 * this function calculate the angle/azimuth between two pixels
	 * @param x pixel on X-axis
	 * @param y pixel on Y-axis
	 * @param h the height of the current screen
	 * @param w the width of the current screen
	 * @return the angle
	 */
	public static double angleBet2Pixels(pix a,pix b,double h,double w) {

		Point3D p1=pixel2Coords(a.x(),a.y(),h,w);
		Point3D p2=pixel2Coords(b.x(),b.y(),h,w);

		MyCoords m =new MyCoords();
		double angle=m.azimuth(p1, p2);

		return angle;
	}
	
	/**
	 * This generic function get collection of element in the game and converts it into pixels 
	 * @param pp the generic collection
	 * @param h the current height of the screen
	 * @param w the current width of the screen
	 * @return  ArrayList that contains the locations after convert
	 */

	public static<T> ArrayList<T> Coords2PixelArray(ArrayList<T> pp,double h,double w ) {

		int packSize=pp.size();
		int pIndex=0;
		// create fruit and packmans collection with PIXEL coords
		// in order to send the collections to createGuiAndShow		

		ArrayList arrayPix=new ArrayList();// type not configured because its generic arrayList
		
		while(pIndex<packSize) {

			if (pp.get(pIndex) instanceof packman) {
			
//				packman thisPack=(packman)pp.get(pIndex);
//				packman tempPack=thisPack.packmanToPix(thisPack, h, w);
//				arrayPix.add(tempPack);
				
				double x = ((packman) pp.get(pIndex)).getX();
				double y = ((packman) pp.get(pIndex)).getY();
				Point3D temp =coords2Pixel(x,y,h,w);
				Point3D position =new Point3D(temp.x(),temp.y(),temp.z());
				String id=Integer.toString(pIndex);
				double speed = ((packman)pp.get(pIndex)).getSpeed();
				double radius = ((packman)pp.get(pIndex)).getRadius();
				metaDataPack data=new metaDataPack(id,speed,radius);
				packman tempPack=new packman(data,position);
				arrayPix.add(tempPack);
				pIndex++;
				System.out.println(tempPack);

			}else {

				if (pp.get(pIndex) instanceof fruit) {

					fruit thisFruit=((fruit) pp.get(pIndex));
					double x = thisFruit.getX();
					double y = thisFruit.getY();
					Point3D temp =coords2Pixel(x,y,h,w);
					Point3D position =new Point3D(temp.x(),temp.y(),temp.z());
					String id=Integer.toString(pIndex);
					metaDataFruit data=new metaDataFruit(id,1);
					fruit tempF=new fruit(data,position);
					arrayPix.add(tempF);
					pIndex++;
					System.out.println(tempF);

				}else {
					
					if (pp.get(pIndex) instanceof ghost) {
						
						ghost thisGhost=((ghost) pp.get(pIndex));
						double x = thisGhost.getX();
						double y = thisGhost.getY();
						Point3D temp =coords2Pixel(x,y,h,w);
						Point3D position =new Point3D(temp.x(),temp.y(),temp.z());
						String id=Integer.toString(pIndex);
						double speed = thisGhost.getSpeed();
						double radius = thisGhost.getRadius();
						metaDataPack data=new metaDataPack(id,speed,radius);
						ghost tempGhost=new ghost(data,position);
						arrayPix.add(tempGhost);
						pIndex++;
						System.out.println(tempGhost);
					}else {
						
						if (pp.get(pIndex) instanceof box) {
							
							box current =(box) pp.get(pIndex);
							Point3D startCoords=current.getStart();
							double lat=startCoords.x();
							double lon=startCoords.y();

							Point3D startpix =coords2Pixel(lat,lon,h,w);

							Point3D endCoords=current.getEnd();
							lat=endCoords.x();
							lon=endCoords.y();

							Point3D endPix =coords2Pixel(lat,lon,h,w);

							box bPixel=new box(startpix,endPix,current.getId());
							System.out.println(bPixel);

							arrayPix.add(bPixel);

							pIndex++;	
						}	
					}					
				}
			}

		}
		return arrayPix;// just one type

	}

	/**
	 * This generic function get collection of element in the game and converts it into coordinates 
	 * @param ff the generic collection
	 * @param h the current height of the screen
	 * @param w the current width of the screen
	 * @return  ArrayList that contains the locations after convert
	 */
	public static<T> ArrayList<T> pixels2CoordsArray(ArrayList<T> ff,double h,double w ) throws IOException {

		int fruitSize=ff.size();
		int fIndex=0;

		ArrayList arrayCoords=new ArrayList(); // the new generic collection that will store tha data
		while(fIndex<fruitSize) {

			if (ff.get(fIndex) instanceof fruit) {
			
				double x=((fruit) ff.get(fIndex)).getX();
				double y=((fruit) ff.get(fIndex)).getY();
				Point3D temp =pixel2Coords(x,y,h,w);
				String id=Integer.toString(fIndex);
				metaDataFruit data=new metaDataFruit(id,1);
				Point3D position =new Point3D(temp.x(),temp.y(),temp.z());
				fruit tempF=new fruit(data,position);
				arrayCoords.add(tempF);
				fIndex++;
			}else {
				
				if (ff.get(fIndex) instanceof packman ) {
					
					double x = ((packman)ff.get(fIndex)).getX();
					double y = ((packman)ff.get(fIndex)).getY();
					Point3D temp =pixel2Coords(x,y,h,w);
					String id=Integer.toString(fIndex);
					double speed = ((packman)(ff.get(fIndex))).getSpeed();
					double radius = ((packman)ff.get(fIndex)).getRadius();
					metaDataPack data=new metaDataPack(id,speed,radius);
					Point3D position =new Point3D(temp.x(),temp.y(),temp.z());
					packman tempPack=new packman(data,position);
					arrayCoords.add(tempPack);
					fIndex++;
					System.out.println(tempPack);
					
				}else {
					
					if (ff.get(fIndex) instanceof ghost) {
						
						double x = ((ghost)ff.get(fIndex)).getX();
						double y = ((ghost)ff.get(fIndex)).getY();				
						Point3D temp =pixel2Coords(x,y,h,w);
						String id=Integer.toString(fIndex);
						double speed = ((ghost)ff.get(fIndex)).getSpeed();
						double radius = ((ghost)ff.get(fIndex)).getRadius();
						metaDataPack data=new metaDataPack(id,speed,radius);
						Point3D position =new Point3D(temp.x(),temp.y(),temp.z());
						ghost tempGhost=new ghost(data,position);
						arrayCoords.add(tempGhost);
						fIndex++;
						System.out.println(tempGhost);
					}
				}
			}
			
		}
		return arrayCoords;
	}
}