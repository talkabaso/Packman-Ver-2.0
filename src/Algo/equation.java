package Algo;
import java.io.IOException;
import java.util.ArrayList;
import GIS.box;
import GIS.fruit;
import GIS.metaDataFruit;
import Geom.Point3D;
import Map.converts;
import Map.pix;
import graph.Graph;
import graph.Graph_Algo;
import graph.Node;

public class equation {

	static ArrayList<Node> allNodes = new ArrayList<Node>();

	/**
	 * This function calculate the value of equation like t=ax+b in specific x value
	 * @param p1 point 
	 * @param p2 point
	 * @param edgeX the x value we want to calculate in the equation
	 * @return
	 */
	private static double eq(Point3D p1,Point3D p2,double edgeX) {

		double x1=p1.x();
		double x2=p2.x();
		double y2=p2.y();
		double y1=p1.y();
		double m=(y2-y1)/(x2-x1);
		double yValue=m*edgeX-m*x1+y1;
		return yValue;

	}

	/**
	 * This function checks if some box in the collection cut the vertical line between two points
	 * with the same x value
	 * @param boxes collection of boxes
	 * @param edge the first point 
	 * @param edge2 the second point
	 * @return
	 */
	//check if some box cut the vertical line between edge to edge 2 
	private static boolean verticalLine(ArrayList<box> boxes, Point3D edge,Point3D edge2) {

		for(int j=0;j<boxes.size();j++) {

			Point3D start=boxes.get(j).getStart();
			Point3D end=boxes.get(j).getEnd();
			double maxY=Math.max(edge.y(), edge2.y());
			double minY=Math.min(edge.y(), edge2.y());

			if(start.x()<edge.x() && end.x()>edge.x() && start.y()<maxY && start.y()>minY) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This function checks if specific point is not inside any box in the collection
	 * @param boxes collection of boxes
	 * @param pos the position of the point
	 * @return
	 */
	// check if pos is outside from all boxes
	private static boolean outOfBoxes(ArrayList<box> boxes,Point3D pos) { 

		for(int j=0;j<boxes.size();j++) {

			Point3D start=boxes.get(j).getStart();
			Point3D end=boxes.get(j).getEnd();

			if (pos.x()>start.x() && pos.x()<end.x()) { 

				if (pos.y()<start.y() && pos.y()>end.y()) {

					return false;
				}
			}
		}
		return true;
	}

	/**
	 * This function checks if the specific box that represented by two edges cut the line between two points
	 * @param p1 the first point
	 * @param p2 the second point
	 * @param edge the first edge of the box 
	 * @param edge2 the second edge of the box (the opposite to the first edge) 
	 * @return
	 */

	//check if the specific box (edge,edge2) cut the line
	private static boolean validLine(Point3D p1,Point3D p2,Point3D edge,Point3D edge2) {

		// check if the x isnt in the range there is no way to block the line
		double maxX=Math.max(p1.x(), p2.x());
		double minX=Math.min(p1.x(), p2.x());
		if(edge.x()>maxX && edge2.x()>maxX || edge.x()<minX && edge2.x()<minX) {
			return true;
		}
		// check if the y isnt in the range there is no way to block the line
		double maxY=Math.max(p1.y(), p2.y());
		double minY=Math.min(p1.y(), p2.y());
		if(edge.y()>maxY && edge2.y()>maxY || edge.y()<minY && edge2.y()<minY) {
			return true;
		}

		double edgeVal=eq(p1,p2,edge.x());
		double edgeVal2=eq(p1,p2,edge2.x());
		boolean validLine=false;
		if (edgeVal>edge.y() && edgeVal2>edge2.y()) { // they both under

			validLine=true;
		}
		if (edgeVal<edge.y() && edgeVal2<edge2.y()) { //they both above

			validLine=true;
		}
		return validLine; // there is one value above and one value under so the box cut the line
	}

	/**
	 * This function checks if any box cut the line between two specific points
	 * @param boxes collection of boxes
	 * @param player the location of our player(some point)
	 * @param positionToCheckDirectLine the second point
	 * @return
	 */

	//check if any block cut the line
	private static boolean check(ArrayList<box> boxes,Point3D player,Point3D positionToCheckDirectLine) {

		boolean direct=true; 
		boolean direct2=true;

		// check if the source and destination inside some box
		boolean playerInside = outOfBoxes(boxes, player);
		boolean destInside = outOfBoxes(boxes, positionToCheckDirectLine);
		if(playerInside==false || destInside==false) {
			return false;
		}

		for(int j=0;j<boxes.size();j++) { 

			Point3D start=boxes.get(j).getStart();
			Point3D end=boxes.get(j).getEnd();
			Point3D right=boxes.get(j).getRightDown();
			Point3D left=boxes.get(j).getLeftUp();

			//avoid from valid lines inside the blocks 
			//cant line end to start and right to left
			double endCheck=end.y()-15;
			double startCheck=start.y()+5;
			double rightCheck=right.y()+5;
			double leftCheck=left.y()-15;

			Point3D pStart=new Point3D(start.x(),startCheck);
			Point3D pEnd=new Point3D(end.x(),endCheck);
			Point3D pRight=new Point3D(right.x(),rightCheck);
			Point3D pLeft=new Point3D(left.x(),leftCheck);

			// to avoid the checking of start to end and right to left from the same box

			if (player.isEquals(pStart) && positionToCheckDirectLine.isEquals(pEnd) ||
					player.isEquals(pEnd)&& positionToCheckDirectLine.isEquals(pStart) ) {

				return false;
			}
			if (player.isEquals(pRight) && positionToCheckDirectLine.isEquals(pLeft) ||
					player.isEquals(pLeft) && positionToCheckDirectLine.isEquals(pRight)) {

				return false;
			}

			// these checks for equation like x=num

			if(player.x()==positionToCheckDirectLine.x() && player.y()!=positionToCheckDirectLine.y()) {

				return verticalLine(boxes,player,positionToCheckDirectLine);
			}

			// if the points are just a regular points on the screen  
			direct=validLine(player,positionToCheckDirectLine,start,end);
			direct2=validLine(player,positionToCheckDirectLine,left,right);
			if (direct2== false || direct==false) {

				return false;
			}

		}	
		return direct2==true && direct==true ;//if true add this point

	}

	/**
	 * This function check the fruits that has direct line to packman according to the boxes in the graph
	 * @param boxes collection of boxes
	 * @param fruits collection of fruit
	 * @param player our packman position 
	 * @param sourceId the id of our play as a string
	 * @param h the height of the current map
	 * @param w the width of the current map
	 * @throws IOException
	 */

	//check the fruits that has direct line to packman according to the boxes in the graph
	public static void calcFruits(Graph G,ArrayList<box>boxes,ArrayList<fruit>fruits,
			Point3D player,String sourceId,double h,double w) throws IOException {

		for(int i=0;i<fruits.size();i++) { // pass all the fruits

			boolean addLine;
			Point3D fruitPos= fruits.get(i).getPosition();
			addLine=check(boxes, player,fruitPos); //check if there is direct line bet player and fruit
			if(addLine && !(isContains(allNodes,sourceId))) {

				Node source=new Node(sourceId);
				G.add(source);
				allNodes.add(source);

			}

			if (addLine) {

				addToGraph(G,player,fruitPos,sourceId,"fruit "+i,h,w);
			}
		}
	}

	/**
	 * This function check the direct lines between the player and any edge of the boxes 
	 * @param boxes collection of boxes
	 * @param player the player position
	 * @param sourceId the id of the player as a string
	 * @param h the height of the current screen
	 * @param w the width of the current screen
	 * @throws IOException
	 */
	public static void calcBoxes(Graph G,ArrayList<box>boxes,Point3D player,
			String sourceId,double h,double w) throws IOException {

		for(int i=0;i<boxes.size();i++) {

			// tell us if we need to add the line 
			boolean addStart;
			boolean addEnd;
			boolean addRight;
			boolean addLeft;

			// add 1 in order not to check inside the box 
			Point3D start = new Point3D(boxes.get(i).getStart());
			start.set_y(start.get_y()+5);

			Point3D end = new Point3D(boxes.get(i).getEnd());
			end.set_y(end.get_y()-15);

			Point3D right = new Point3D(boxes.get(i).getRightDown());
			right.set_y(right.get_y()+5);

			Point3D left = new Point3D(boxes.get(i).getLeftUp());
			left.set_y(left.get_y()-15);

			addStart=check(boxes,player,start);
			addEnd=check(boxes,player,end);

			addRight=check(boxes,player,right);
			addLeft=check(boxes,player,left);

			// if the node is exist we dont need to create another one
			if((addStart || addEnd || addLeft || addRight) && !(isContains(allNodes,sourceId))) {

				Node source=new Node(sourceId);
				G.add(source);
				allNodes.add(source);

			}

			//if its true so there is no box that blocks the line so
			//add the position to the graph

			if (addStart) {

				addToGraph(G,player,start,sourceId,"box "+i+" start",h,w);
			}
			if (addEnd) {

				addToGraph(G,end,player,sourceId,"box "+i+" end",h,w);
			}
			if (addRight) {

				addToGraph(G,right,player,sourceId,"box "+i+" right",h,w);
			}
			if (addLeft) {

				addToGraph(G,left,player,sourceId,"box "+i+" left",h,w);
			}			
		}
	}

	/**
	 * This function check if the node with the specific name exist in the collection
	 * @param allNodes collection of nodes
	 * @param name the name of the node that we are looking for
	 * @return  if name is inside the collection
	 */
	public static boolean isContains(ArrayList<Node> allNodes,String name) {

		int size=allNodes.size();
		int i=0;

		while(i<size) {

			if (allNodes.get(i).get_name().compareTo(name) == 0) {
				return true;
			}

			i++;
		}
		return false;

	}

	/**
	 * This function add to the graph the nodes that connected between each other
	 * @param validPointToPlayer the point that has direct line to the player
	 * @param player the point of the player
	 * @param sourceId the id of the player
	 * @param destId the id of the other point
	 * @param h the current height of the map 
	 * @param w the current width of the map
	 * @throws IOException
	 */
	public static void addToGraph(Graph G,Point3D validPointToPlayer ,Point3D player,
			String sourceId,String destId,double h, double w) throws IOException {

		if(!(isContains(allNodes,destId))) {

			Node dest=new Node(destId);
			G.add(dest);
			allNodes.add(dest);
		}

		pix a=new pix(validPointToPlayer.x(),validPointToPlayer.y());
		pix b=new pix(player.x(),player.y());
		double distance=converts.distanceBet2Pixels(a, b,h, w);
		//System.out.println("from: "+sourceId+" to "+destId);
		//System.out.println(distance);

		G.addEdge(sourceId, destId, distance);
	}

	/**
	 * This function check all the connection that we have in the graph from any edge to another 
	 * @param boxes collection of boxes
	 * @param fruits collection of fruits
	 * @param playerPos the position of the player
	 * @param h the current height of the screen
	 * @param w the current width of the screen
	 * @return return new Graph with all accessible paths
	 * @throws IOException
	 */
	public static Graph buildGraph(ArrayList<box> boxes,ArrayList<fruit>fruits,Point3D 
			playerPos,double h,double w) throws IOException {

		Graph G = new Graph(); // create new graph because there are some fruits that eaten
		allNodes.clear(); 

		int size =boxes.size();

		for(int i=0;i<size;i++) {

			// not allow the packman to touch the boxes
			Point3D start = new Point3D(boxes.get(i).getStart());
			start.set_y(start.get_y()+5);

			Point3D end = new Point3D(boxes.get(i).getEnd());
			end.set_y(end.get_y()-15);

			Point3D right = new Point3D(boxes.get(i).getRightDown());
			right.set_y(right.get_y()+5);

			Point3D left = new Point3D(boxes.get(i).getLeftUp());
			left.set_y(left.get_y()-15);

			calcBoxes(G,boxes, start,"box "+i+" start",h,w);
			calcBoxes(G,boxes, end,"box "+i+" end",h,w);
			calcBoxes(G,boxes, right,"box "+i+" right",h,w);
			calcBoxes(G,boxes, left,"box "+i+" left",h,w);

		}

		int sizeFruits=fruits.size();
		for(int i=0; i<sizeFruits;i++) {

			Point3D position=fruits.get(i).getPosition();
			calcBoxes(G,boxes, position,"fruit "+i,h,w);
		}

		Point3D packPosition=playerPos;
		String source="packman";
		calcBoxes(G,boxes, packPosition, source,h,w);
		calcFruits(G,boxes,fruits, packPosition, source,h,w);

		return G;
	}

	/**
	 * This function check the fruit the we should eat (that takes us the the shortest time)
	 * @param boxes collection of boxes
	 * @param fruits collection of fruits
	 * @param player the position of the player
	 * @param h the current height of the screen
	 * @param w the current width of the screen
	 * @return the index of the fruits according to the collection of the fruits
	 * @throws IOException
	 */
	public static int minToEat(ArrayList<box>boxes,ArrayList<fruit> fruits,Point3D player
			,double h,double w) throws IOException {

		Graph G=buildGraph(boxes, fruits, player,h,w);
		Graph_Algo.dijkstra(G, "packman");
		double min=Double.MAX_VALUE;
		int index=-1;

		for(int i=0;i<fruits.size();i++) {

			Node b = G.getNodeByName("fruit "+i);
			double playerFruitDist=b.getDist();

			if (playerFruitDist<min) {

				min=playerFruitDist;
				index=i;
			}
		}

		return index;	
	}

	/**
	 * This function create collection of the points in the screen that we should go in order to arrive the target
	 * @param boxes collection of boxes
	 * @param fruits collection of fruits
	 * @param player the position of the player
	 * @param fruitIndex the point that we want to arrive (the target)
	 * @param h the current height of the screen
	 * @param w the current width of the screen
	 * @return ArrayList of Point3D that represents the position we need to pass in order to arrive our target
	 * @throws IOException
	 */
	public static ArrayList<Point3D> getPath(ArrayList<box> boxes,ArrayList<fruit> fruits,Point3D player,
			int fruitIndex,double h,double w) throws IOException{

		ArrayList<Point3D> pathPos = new ArrayList<Point3D>();
		Graph G=buildGraph(boxes,fruits,player,h,w); // build the graph according to the current positions
		Graph_Algo.dijkstra(G, "packman"); // calculate the shortest paths from packman(our player)
		Node b = G.getNodeByName("fruit "+fruitIndex);// calculate the path to the target fruit

		ArrayList<String> path = b.getPath();
		System.out.println(b.getPath());

		int sizePath=path.size();
		for(int i=0;i<sizePath;i++) {

			String [] parsed=path.get(i).split(","); // the elements of the path
			String [] spaces=parsed[0].split(" "); // inside the specific path point check who is its
			if(spaces[0].compareTo("fruit")==0) {

				int index=Integer.parseInt(spaces[1]);
				if (index<fruits.size()) {

					pathPos.add(fruits.get(index).getPosition());

				}
			}
			else {
				if (boxes.size()>0) {

					if (spaces[0].compareTo("box")==0) {

						int index=Integer.parseInt(spaces[1]);

						switch(spaces[2]) {

						case "right":
							Point3D right = new Point3D(boxes.get(index).getRightDown());
							right.set_y(right.get_y()+3);
							right.set_x(right.get_x()+3);
							pathPos.add(right);
							break;
						case "left":
							Point3D left = new Point3D(boxes.get(index).getLeftUp());
							left.set_y(left.get_y()-3);
							left.set_x(left.get_x()-3);
							pathPos.add(left);
							break;
						case "start":
							Point3D start = new Point3D(boxes.get(index).getStart());
							start.set_y(start.get_y()+3);
							start.set_x(start.get_x()-3);
							pathPos.add(start);
							break;
						case "end":
							Point3D end = new Point3D(boxes.get(index).getEnd());
							end.set_y(end.get_y()-3);
							end.set_x(end.get_x()+3);
							pathPos.add(end);
						}

					}
				}
			}
		}
		pathPos.add(fruits.get(fruitIndex).getPosition());// add the fruit point to the path

		System.out.println(pathPos);

		return pathPos;
	}
}