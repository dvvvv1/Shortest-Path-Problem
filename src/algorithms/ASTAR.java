package algorithms;
import java.util.Collections;
import java.util.Vector;
import utils.MapNode;
import utils.MapNode.Status;

/**
 * A* search
 * @author puzhiyao
 * Email: Puzhi.Yao@gmail.com
 * Coding Date: 22 Mar 2015
 */
public class ASTAR {
	/*
	 * Predefined path cost
	 */
	private static int pathValue = -1;
	/*
	 * Predefined no go zone cost
	 */
	private static int noGoZoneValue = Integer.MAX_VALUE;
	/*
	 * The node in open set which has the lowest cost.
	 */
	private static MapNode lowestCostNode;
	/*
	 * Node local position on the Map
	 */
	private static enum Position {Top, Bot, Left, Right};
	/*
	 * The set of nodes already evaluated
	 */
	private static Vector<MapNode> closeSet;
	/*
	 * The set of nodes to be evaluated
	 */
	private static Vector<MapNode> openSet;
	/**
	 * This is the main function of A* search algorithm.
	 * This function using A* manhattan or A* euclidean method to find
	 * shortest path between two given node.
	 * @param startNode
	 * @param endNode
	 * @param nodes
	 * @param method
	 * @return
	 */
	public static Vector<int[]> aStar(int[] startNode, int[] endNode, MapNode[][] nodes, String method) {
		// initial all nodes' status to UN-visited
		for (int colOfNodeMap = 0; colOfNodeMap < nodes.length; colOfNodeMap++) {
			for (int rowOfNodeMap = 0; rowOfNodeMap < nodes[colOfNodeMap].length; rowOfNodeMap++) {
				nodes[colOfNodeMap][rowOfNodeMap].setTotCost(noGoZoneValue);// set the initial distance to INF
				nodes[colOfNodeMap][rowOfNodeMap].gridPosition[0] = colOfNodeMap;// save current node x position
				nodes[colOfNodeMap][rowOfNodeMap].gridPosition[1] = rowOfNodeMap;// save current node y position
				nodes[colOfNodeMap][rowOfNodeMap].setNodeVistied(false);// set all nodes are not visited yet
			}
		}
		
		// initialization of node set
		Vector<int[]> shortestPath;
		closeSet = new Vector<MapNode>();
		openSet = new Vector<MapNode>();
		// Add the start node into open set
		MapNode start = new MapNode();
		nodes[startNode[0]][startNode[1]].setHeuristics(0);
		nodes[startNode[0]][startNode[1]].setTotCost(0);
		start = nodes[startNode[0]][startNode[1]];
		openSet.add(start);

		while (openSet.size() != 0) {
			// sort openSet from lowest cost to highest
			int pivot;
			for (int indexOfOpenSet = 1; indexOfOpenSet < openSet.size(); indexOfOpenSet++) {
				pivot = indexOfOpenSet;
				while (pivot > 0 && openSet.get(pivot - 1).getTotCost() > openSet.get(pivot).getTotCost()) {
					Collections.swap(openSet, pivot, pivot - 1);
					pivot = pivot - 1;
				}
			}

			// the node in openset having the lowest cost
			lowestCostNode = new MapNode();
			lowestCostNode = openSet.get(0);

			// End case if the condition have approached
			if (lowestCostNode.gridPosition[0] == endNode[0] && lowestCostNode.gridPosition[1] == endNode[1]) {
				shortestPath = nodes[endNode[0]][endNode[1]].getTotPath();
				if (shortestPath.size() == 1) {
					return null;
				}
				shortestPath.add(endNode);
				// No result was found -- only the end node
				return shortestPath;
			}

			MapNode tempTopNode = new MapNode();
			MapNode tempBottomNode = new MapNode();
			MapNode tempLeftNode = new MapNode();
			MapNode tempRightNode = new MapNode();

			// remove current from open set
			openSet.remove(0);
			// add current to close set
			closeSet.add(lowestCostNode);

			// update Top node information from original nodes matrix
			if (lowestCostNode.topNode != null) {
				tempTopNode = nodes[lowestCostNode.topNode.gridPosition[0]][lowestCostNode.topNode.gridPosition[1]];
			} else {
				tempTopNode = null;
			}

			// update Bottom node information from original nodes matrix
			if (lowestCostNode.bottomNode != null) {
				tempBottomNode = nodes[lowestCostNode.bottomNode.gridPosition[0]][lowestCostNode.bottomNode.gridPosition[1]];
			} else {
				tempBottomNode = null;
			}

			// update Left node information from original nodes matrix
			if (lowestCostNode.leftNode != null) {
				tempLeftNode = nodes[lowestCostNode.leftNode.gridPosition[0]][lowestCostNode.leftNode.gridPosition[1]];
			} else {
				tempLeftNode = null;
			}

			// update Right node information from original nodes matrix
			if (lowestCostNode.rightNode != null) {
				tempRightNode = nodes[lowestCostNode.rightNode.gridPosition[0]][lowestCostNode.rightNode.gridPosition[1]];
			} else {
				tempRightNode = null;
			}

			// Manhattan, Euclidean method
			if (method.equals("manhattan")) {
				// update neighbor nodes
				// update top Node
				if (tempTopNode != null) {
					updateNodeInfoManhattan(tempTopNode, endNode, nodes,Position.Top);
				}
				// update bottom node
				if (tempBottomNode != null) {
					updateNodeInfoManhattan(tempBottomNode, endNode, nodes,Position.Bot);
				}
				// update Left node
				if (tempLeftNode != null) {
					updateNodeInfoManhattan(tempLeftNode, endNode, nodes,Position.Left);
				}
				// update Right node
				if (tempRightNode != null) {
					updateNodeInfoManhattan(tempRightNode, endNode, nodes,Position.Right);
				}
			}

			// Manhattan, Euclidean method
			else if (method.equals("euclidean")) {
				// update neighbor nodes
				// update top Node
				if (tempTopNode != null) {
					updateNodeInfoEuclidean(tempTopNode, endNode, nodes,Position.Top);
				}
				// update bottom node
				if (tempBottomNode != null) {
					updateNodeInfoEuclidean(tempBottomNode, endNode, nodes,Position.Bot);
				}
				// update Left node
				if (tempLeftNode != null) {
					updateNodeInfoEuclidean(tempLeftNode, endNode, nodes,Position.Left);
				}
				// update Right node
				if (tempRightNode != null) {
					updateNodeInfoEuclidean(tempRightNode, endNode, nodes,Position.Right);
				}
			}
		}
		shortestPath = nodes[endNode[0]][endNode[1]].getTotPath();
		if (shortestPath.size() == 1) {
			return null;
		}
		shortestPath.add(endNode);
		// No result was found -- only the end node
		return shortestPath;
	}
	
	/**
	 * This function checks that if given node is in open set.
	 * @param openSet
	 * @param lowestCostNode
	 * @return
	 */
	public static boolean checkOpenSet(Vector<MapNode> openSet, MapNode lowestCostNode) {
		// This function will check given node to see if it is in the openSet
		boolean check = false;
		for (int i = 0; i < openSet.size(); i++) {
			if (openSet.get(i).gridPosition[0] == lowestCostNode.gridPosition[0]
					&& openSet.get(i).gridPosition[1] == lowestCostNode.gridPosition[1]) {
				check = true;
			}
		}
		return check;
	}
	
	/**
	 * This function checks that if given node is in close set.
	 * @param closeSet
	 * @param lowestCostNode
	 * @return
	 */
	public static boolean checkCloseSet(Vector<MapNode> closeSet, MapNode lowestCostNode) {
		// This function will check given node to see if it is in the closeSet
		boolean check = false;
		for (int i = 0; i < closeSet.size(); i++) {
			if (closeSet.get(i).gridPosition[0] == lowestCostNode.gridPosition[0]
					&& closeSet.get(i).gridPosition[1] == lowestCostNode.gridPosition[1]) {
				check = true;
			}
		}
		return check;
	}
	
	/**
	 * This function calculates Manhattan distance
	 * @param start
	 * @param end
	 * @param nodes
	 * @return
	 */
	public static float manhattan(int[] start, int[] end, MapNode[][] nodes) {
		// A* manhattan distance
		float distance = 0;
		distance = (float) (Math.abs(start[0] - end[0]) + Math.abs(start[1] - end[1]));
		return distance;
	}
	
	/**
	 * This function calculates Euclidean distance.
	 * @param start
	 * @param end
	 * @param nodes
	 * @return
	 */
	public static float Euclidean(int[] start, int[] end, MapNode[][] nodes) {
		// A* Euclidean distance
		float distance = 0.0f;
		float a = (start[0] - end[0]) * (start[0] - end[0]);
		float b = (start[1] - end[1]) * (start[1] - end[1]);
		distance = (float) Math.sqrt(a + b);
		return distance;
	}
	
	/**
	 * This function updates neighbor nodes info using Manhattan distance
	 * @param currentNode
	 * @param endNode
	 * @param nodes
	 * @param localPos
	 */
	public static void updateNodeInfoManhattan(MapNode currentNode, int[] endNode, MapNode[][] nodes,Position localPos) {
		float hValue = lowestCostNode.getHeuristics() + 1;
		float nextCost = hValue + manhattan(currentNode.gridPosition, endNode, nodes);
		if (lowestCostNode.getOwnElevation() < currentNode.getOwnElevation()) {
			if (currentNode.getOwnElevation() == noGoZoneValue) {
				nextCost = noGoZoneValue;
			} else {
				hValue = lowestCostNode.getHeuristics() + 1
						+ (currentNode.getOwnElevation() - lowestCostNode.getOwnElevation());
				nextCost = hValue + manhattan(currentNode.gridPosition, endNode, nodes);
			}
		}
		if (checkCloseSet(closeSet, currentNode) == false && currentNode.getTotCost() > nextCost) {
			updateFirstArrivedNodeInfo(currentNode,nodes,hValue,nextCost);
			// update data back to original matrix
			if (localPos == Position.Top) {
				nodes[lowestCostNode.topNode.gridPosition[0]][lowestCostNode.topNode.gridPosition[1]] = currentNode;
			} else if (localPos == Position.Bot) {
				nodes[lowestCostNode.bottomNode.gridPosition[0]][lowestCostNode.bottomNode.gridPosition[1]] = currentNode;
			} else if (localPos == Position.Left) {
				nodes[lowestCostNode.leftNode.gridPosition[0]][lowestCostNode.leftNode.gridPosition[1]] = currentNode;
			} else if (localPos == Position.Right) {
				nodes[lowestCostNode.rightNode.gridPosition[0]][lowestCostNode.rightNode.gridPosition[1]] = currentNode;
			}
		}
		if (checkCloseSet(closeSet, currentNode) == false && checkOpenSet(openSet, currentNode) == false) {
			openSet.add(currentNode);
		}
	}
	/**
	 * This function updates neighbor nodes info using Euclidean distance
	 * @param currentNode
	 * @param endNode
	 * @param nodes
	 * @param localPos
	 */
	public static void updateNodeInfoEuclidean (MapNode currentNode, int[] endNode, MapNode[][] nodes,Position localPos) {
		float hValue = lowestCostNode.getHeuristics() + 1;
		float nextCost = hValue + Euclidean(currentNode.gridPosition, endNode, nodes);
		if (lowestCostNode.getOwnElevation() < currentNode.getOwnElevation()) {
			if (currentNode.getOwnElevation() == noGoZoneValue) {
				nextCost = noGoZoneValue;
			} else {
				hValue = lowestCostNode.getHeuristics() + 1
						+ (currentNode.getOwnElevation() - lowestCostNode.getOwnElevation());
				nextCost = hValue + Euclidean(currentNode.gridPosition, endNode, nodes);
			}
		}
		if (checkCloseSet(closeSet, currentNode) == false && currentNode.getTotCost() > nextCost) {
			updateFirstArrivedNodeInfo(currentNode,nodes,hValue,nextCost);
			// update data back to original matrix
			if (localPos == Position.Top) {
				nodes[lowestCostNode.topNode.gridPosition[0]][lowestCostNode.topNode.gridPosition[1]] = currentNode;
			} else if (localPos == Position.Bot) {
				nodes[lowestCostNode.bottomNode.gridPosition[0]][lowestCostNode.bottomNode.gridPosition[1]] = currentNode;
			} else if (localPos == Position.Left) {
				nodes[lowestCostNode.leftNode.gridPosition[0]][lowestCostNode.leftNode.gridPosition[1]] = currentNode;
			} else if (localPos == Position.Right) {
				nodes[lowestCostNode.rightNode.gridPosition[0]][lowestCostNode.rightNode.gridPosition[1]] = currentNode;
			}
		}
		if (checkCloseSet(closeSet, currentNode) == false && checkOpenSet(openSet, currentNode) == false) {
			openSet.add(currentNode);
		}
	}
	
	/**
	 * This function will update node if this node is not in closeSet or openSet
	 * it is the first time that program has arrived this node
	 * @param inputNode
	 * @param nodes
	 * @param hValue
	 * @param nextCost
	 */
	public static void updateFirstArrivedNodeInfo(MapNode inputNode, MapNode[][] nodes, float hValue, float nextCost) {
		// 
		// update cost
		inputNode.setHeuristics(hValue);
		inputNode.setTotCost(nextCost);
		Vector<int[]> tempPath = new Vector<int[]>();
		// record the path
		for (int m = 0; m < lowestCostNode.getTotPath().size(); m++) {
			int[] tempXY = new int[2];
			tempXY = lowestCostNode.getTotPath().get(m);
			tempPath.add(tempXY);
		}
		// check new node to see if it exists in path
		if (MapNode.checkPositionInPath(tempPath, lowestCostNode.gridPosition[0],
				lowestCostNode.gridPosition[1]) == false) {
			int[] tempXY2 = new int[2];
			tempXY2 = lowestCostNode.gridPosition;
			tempPath.add(tempXY2);
		}
		// assign new path to the node
		inputNode.setTotPath(tempPath);
		inputNode.setStatus(Status.EXPLORED);
	}

}
