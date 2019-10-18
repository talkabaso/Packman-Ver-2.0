package GIS;

import java.io.IOException;
import java.util.ArrayList;
import GUI.guiGame;
import Geom.Point3D;
import Map.converts;

/**
 * This class represents game that contains Fruits, Packmans ghost and obstacles each of them has position and
 * meta data associated with them,
 * the game also include general score according to the Weight of the eaten fruits and the start time
 * @author Aric and Tal
 */

public class game {

	private static ArrayList<fruit> fruits;
	private static ArrayList<packman> packmans;
	private static ArrayList<ghost> ghosts;
	private static ArrayList<box> boxes;
	private static packman player;
	private static double score;

	// Constructor //
	public game(ArrayList<fruit>f,ArrayList<packman>p,ArrayList<ghost>g,ArrayList<box> b) {
	
		// static access:
		game.fruits=f;
		game.packmans=p;
		game.ghosts=g;
		game.boxes=b;
		game.score=0;
	}
	
	/**
	 * this function create deep copy to ArrayList of packmans in this game
	 * @return new ArrayList 
	 */
	public static ArrayList<packman> copyPack(){
		
		ArrayList<packman> pp=new ArrayList<packman>();

		for(int i=0;i<packmans.size();i++) {
			String id=packmans.get(i).getId();
			double speed=packmans.get(i).getSpeed();
			double radius=packmans.get(i).getRadius();
			metaDataPack tempData= new metaDataPack(id,speed, radius);
			double x=packmans.get(i).getX();
			double y=packmans.get(i).getY();
			double z= packmans.get(i).getZ();
			Point3D postionTemp = new Point3D(x, y,z);
			packman temp = new packman(tempData, postionTemp);
			pp.add(temp);
		}
		return pp;
	}
	
	/**
	 * this function create deep copy to ArrayList of fruits in this game
	 * @return new ArrayList 
	 */
	public static ArrayList<fruit> copyFruit(){
		
		ArrayList<fruit> ff=new ArrayList<fruit>();

		for(int i=0;i<fruits.size();i++) {
			String id=fruits.get(i).getId();
			double weight=fruits.get(i).getWeight();
			metaDataFruit tempData= new metaDataFruit(id, weight);
			double x=fruits.get(i).getX();
			double y=fruits.get(i).getY();
			double z=fruits.get(i).getZ();
			Point3D postionTemp = new Point3D(x,y,z);
			fruit temp = new fruit(tempData, postionTemp);
			ff.add(temp);
		}
		return ff;
	}

	/**
	 * This function initialize the game data, read from csv file and then create
	 * ArrayList of fruits and packmans that store all the data
	 * @param path the csv input file
	 * @throws IOException
	 */
	/*
	 * public static void createGameCollection(String path) throws IOException{
	 * 
	 * System.out.println("create game"); readCsv csv=new readCsv(path); // this
	 * path is the input csv file ArrayList<String> arr=csv.readCsvGame(); // create
	 * arrayList of all the lines as a string
	 * 
	 * for(int i=0;i<arr.size();i++) {
	 * 
	 * String [] parsed=arr.get(i).split(","); String id=parsed[1]; double
	 * x=Double.parseDouble(parsed[2]); double y=Double.parseDouble(parsed[3]);
	 * double z=Double.parseDouble(parsed[4]); double
	 * speedOrWeight=Double.parseDouble(parsed[5]);
	 * 
	 * if (parsed[0].charAt(0)=='P') {
	 * 
	 * double radius=Double.parseDouble(parsed[6]); metaDataPack data=new
	 * metaDataPack(id,speedOrWeight,radius); Point3D position=new Point3D(x,y,z);
	 * packman p=new packman(data,position); packmans.add(p); } else { metaDataFruit
	 * data=new metaDataFruit(id,speedOrWeight); Point3D position=new
	 * Point3D(x,y,z); fruit f=new fruit(data,position); fruits.add(f); } } }
	 */	
	// This function for receive data from getBoard function // 
	
	/**
	 * This function clear all the data that stored in the collections of the game
	 */
	public static void clear() {
		
		fruits.clear();
		packmans.clear();
		ghosts.clear();
		boxes.clear();
		score=0;
		
	}
	/**
	 * This function create new game from the data that stored in the ArrayList of strings
	 * exploit the data, create the elements and store them
	 * @param s the collection of the strings that represents the game
	 */
	public static void createNewGame(ArrayList<String> s) { 
		
		for(int i=0;i<s.size();i++) {
			
			String [] parsed=s.get(i).split(",");
			String id=parsed[1];
			double lat=Double.parseDouble(parsed[2]);
			double lon=Double.parseDouble(parsed[3]);
			double alt=Double.parseDouble(parsed[4]);
			Point3D position=new Point3D(lat,lon,alt);
			
			if (parsed[0].charAt(0)=='P') {

				double speed=Double.parseDouble(parsed[5]);
				double radius=Double.parseDouble(parsed[6]);
				metaDataPack data=new metaDataPack(id,speed,radius);
				packman p=new packman(data,position);
				packmans.add(p);
			}
			else {
				
				if (parsed[0].charAt(0)=='F') {
					
					double weight=Double.parseDouble(parsed[5]);
					metaDataFruit data=new metaDataFruit(id,weight);
					fruit f=new fruit(data,position);
					fruits.add(f);
				}
				else {
					if (parsed[0].charAt(0)=='G') {
						
						double speed=Double.parseDouble(parsed[5]);
						double radius=Double.parseDouble(parsed[6]);
						metaDataPack data=new metaDataPack(id,speed,radius);
						ghost g=new ghost(data,position);
						ghosts.add(g);
					
					}else {
						
						if (parsed[0].charAt(0)=='B') {
							
							 double lat2=Double.parseDouble(parsed[5]); // lat double y2
							 double lon2 =Double.parseDouble(parsed[6]); // lon double
							 double alt2=Double.parseDouble(parsed[7]) ; // alt Point3D p2=new Point3D(x2,y2,z2); box
							 Point3D position2=new Point3D(lat2,lon2,alt2);
							 box b=new box(position,position2,id);
							 boxes.add(b);
						}
						else { // this is for PlayerPackman ("M")
							
							double speed=Double.parseDouble(parsed[5]);
							double radius=Double.parseDouble(parsed[6]);
							metaDataPack data=new metaDataPack(id,speed,radius);
							player=new packman(data,position);
							
						}
					}
				}
			}
		}
	}

	/**
	 * this function call for open Gui window with the relevant collections of the game
	 * @throws IOException
	 */
	public static void paintGame(double h,double w) throws IOException {

		guiGame demo = new guiGame(); // create current Gui 

		// create fruit and packmans collection with Pixel coordinates
		// in order to send the collections to createGuiAndShow
		
		// converts the data of each element to pixels so that the Gui can show them appropriate
		ArrayList<fruit> fruitPix=converts.Coords2PixelArray(fruits,h,w);
		ArrayList<packman> packPix=converts.Coords2PixelArray(packmans,h,w);
		ArrayList<ghost> ghostPix=converts.Coords2PixelArray(ghosts, h, w);
		ArrayList<box> boxPix=converts.Coords2PixelArray(boxes, h, w);
		
		double lat=player.getX();
		double lon=player.getY();
		Point3D pixelPoint=converts.coords2Pixel(lat, lon, h, w);
		player.setPosition(pixelPoint);
		
		demo.openFileGUI(fruitPix,packPix,ghostPix,boxPix,player);
	}

	// Getters and Setters //

	public static double getScore() {
		return score;
	}

	public static void setScore(double score) {
		game.score = score;
	}
	
	public static void main(String[] args) throws IOException { 

//		ArrayList<packman> pack=new ArrayList<packman>();
//		ArrayList<fruit> fruits=new ArrayList<fruit>();
//
//		//game g=new game(fruits,pack);
//		String path = "D:\\מדעי המחשב\\שנה ב\\סמסטר א\\מונחה עצמים\\myMath Project\\Ex 3\\Ex3_data\\game_1543684662657.csv";
//
//	//	game.createGameCollection(path);
//		
//		algo.calcAll(fruits, pack);
//		
//		for(int i=0;i<pack.size();i++) {
//			
//			System.out.println("the score of packman number: "+pack.get(i).getId()+" is:"
//					+ " "+pack.get(i).getPoints()+" the time it takes is: "+pack.get(i).getTime());
//		}
//		
//		System.out.println("the Total time is: "+game.totalTime);
//		System.out.println("the game score is: "+game.score);
		
	}
}