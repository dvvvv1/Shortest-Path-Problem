package algorithms;

import java.util.Vector;
import utils.MapNode;

/**
 * Uniform Cost Search/Dijkstra's algorithm
 * @author pz.yao
 * Coding Date: 22 Mar 2015
 */
public class UCS {
	/*
	 * Predefined evaluation cost
	 */
	private static int pathValue = -1;
	private static int noGoZoneValue = Integer.MAX_VALUE;
	/*
	 * First Node in queue
	 */
	private static int firstNode = 0;

	/*
	 * Node local position on the Map
	 */
	private static enum Position {
		Top, Bot, Left, Right
	};

	/**
	 * This function is using UCS method to update the distance and weight of
	 * each node and edge to obtain the shortest path between any two given
	 * nodes.
	 * 
	 * @param startPosition
	 * @param unexploreNode
	 * @param nodes
	 * @return
	 */
	public static Vector<int[]> ucs(int[] startPosition, int[] unexploreNode, MapNode[][] nodes) {
		// initialization of result path
		Vector<int[]> shortestPath;

		// initial all nodes' distance to INF
		for (int colOfNodes = 0; colOfNodes < nodes.length; colOfNodes++) {
			for (int rowOfNodes = 0; rowOfNodes < nodes[colOfNodes].length; rowOfNodes++) {
				// set the initial distance to INF
				nodes[colOfNodes][rowOfNodes].setTotDistance(noGoZoneValue);
				// save current node x position
				nodes[colOfNodes][rowOfNodes].gridPosition[0] = colOfNodes;
				// save current node y position
				nodes[colOfNodes][rowOfNodes].gridPosition[1] = rowOfNodes;
				// set all nodes are not visited yet
				nodes[colOfNodes][rowOfNodes].setNodeVistied(false);
			}
		}

		// set the start point total distance to 0
		nodes[startPosition[0]][startPosition[1]].setTotDistance(0);
		// create start node and add it into the execution queue
		MapNode tempNode = new MapNode();
		tempNode = nodes[startPosition[0]][startPosition[1]];
		Vector<MapNode> UCSExecutionQueue = new Vector<MapNode>();
		UCSExecutionQueue.add(tempNode);
		// main loop: check all nodes on the map
		while (UCSExecutionQueue.size() != 0) {
			// create four nearby nodes
			MapNode tempTopNode = new MapNode();
			MapNode tempBottomNode = new MapNode();
			MapNode tempLeftNode = new MapNode();
			MapNode tempRightNode = new MapNode();

			// update Top node information from original nodes matrix
			if (UCSExecutionQueue.get(firstNode).topNode != null) {
				tempTopNode = nodes[UCSExecutionQueue.get(firstNode).topNode.gridPosition[0]][UCSExecutionQueue
						.get(firstNode).topNode.gridPosition[1]];
			} else {
				tempTopNode = null;
			}

			// update Bottom node information from original nodes matrix
			if (UCSExecutionQueue.get(firstNode).bottomNode != null) {
				tempBottomNode = nodes[UCSExecutionQueue.get(firstNode).bottomNode.gridPosition[0]][UCSExecutionQueue
						.get(firstNode).bottomNode.gridPosition[1]];
			} else {
				tempBottomNode = null;
			}

			// update Left node information from original nodes matrix
			if (UCSExecutionQueue.get(firstNode).leftNode != null) {
				tempLeftNode = nodes[UCSExecutionQueue.get(firstNode).leftNode.gridPosition[0]][UCSExecutionQueue
						.get(firstNode).leftNode.gridPosition[1]];
			} else {
				tempLeftNode = null;
			}

			// update Right node information from original nodes matrix
			if (UCSExecutionQueue.get(firstNode).rightNode != null) {
				tempRightNode = nodes[UCSExecutionQueue.get(firstNode).rightNode.gridPosition[0]][UCSExecutionQueue
						.get(firstNode).rightNode.gridPosition[1]];
			} else {
				tempRightNode = null;
			}

			// start re-calculate distance of each node
			// check the top node and update new distance
			if (tempTopNode != null) {
				if (tempTopNode.getTotDistance() > UCSExecutionQueue.get(firstNode).getTotDistance()
						+ UCSExecutionQueue.get(firstNode).getTopWeight()
						&& UCSExecutionQueue.get(firstNode).getTotDistance()
								+ UCSExecutionQueue.get(firstNode).getTopWeight() > 0) {
					updateUCSNodeInfo(tempTopNode, UCSExecutionQueue, nodes, Position.Top);
				}

				if (nodes[tempTopNode.gridPosition[0]][tempTopNode.gridPosition[1]].getNodeVisited() == false
						&& MapNode.checkNodeInQueue(UCSExecutionQueue, tempTopNode.gridPosition[0],
								tempTopNode.gridPosition[1]) == false
						&& tempTopNode.getOwnElevation() != noGoZoneValue) {
					// check un-visited nodes and add new node into execution
					// queue
					UCSExecutionQueue.add(tempTopNode);
				}
			}
			// check the bottom node and update new distance
			if (tempBottomNode != null) {
				if (tempBottomNode.getTotDistance() > UCSExecutionQueue.get(firstNode).getTotDistance()
						+ UCSExecutionQueue.get(firstNode).getBottomWeight()
						&& UCSExecutionQueue.get(firstNode).getTotDistance()
								+ UCSExecutionQueue.get(firstNode).getBottomWeight() > 0) {
					updateUCSNodeInfo(tempBottomNode, UCSExecutionQueue, nodes, Position.Bot);
				}
				if (nodes[tempBottomNode.gridPosition[0]][tempBottomNode.gridPosition[1]].getNodeVisited() == false
						&& MapNode.checkNodeInQueue(UCSExecutionQueue, tempBottomNode.gridPosition[0],
								tempBottomNode.gridPosition[1]) == false
						&& tempBottomNode.getOwnElevation() != noGoZoneValue) {
					// check un-visited nodes and add new node into execution
					// queue
					UCSExecutionQueue.add(tempBottomNode);
				}
			}
			// check the left node and update new distance
			if (tempLeftNode != null) {
				if (tempLeftNode.getTotDistance() > UCSExecutionQueue.get(firstNode).getTotDistance()
						+ UCSExecutionQueue.get(firstNode).getLeftWeight()
						&& UCSExecutionQueue.get(firstNode).getTotDistance()
								+ UCSExecutionQueue.get(firstNode).getLeftWeight() > 0) {
					updateUCSNodeInfo(tempLeftNode, UCSExecutionQueue, nodes, Position.Left);
				}
				if (nodes[tempLeftNode.gridPosition[0]][tempLeftNode.gridPosition[1]].getNodeVisited() == false
						&& MapNode.checkNodeInQueue(UCSExecutionQueue, tempLeftNode.gridPosition[0],
								tempLeftNode.gridPosition[1]) == false
						&& tempLeftNode.getOwnElevation() != noGoZoneValue) {
					// check un-visited nodes and add new node into execution
					// queue
					UCSExecutionQueue.add(tempLeftNode);
				}
			}
			// check the right node and update new distance
			if (tempRightNode != null) {
				if (tempRightNode.getTotDistance() > UCSExecutionQueue.get(firstNode).getTotDistance()
						+ UCSExecutionQueue.get(firstNode).getRightWeight()
						&& UCSExecutionQueue.get(firstNode).getTotDistance()
								+ UCSExecutionQueue.get(firstNode).getRightWeight() > 0) {
					updateUCSNodeInfo(tempRightNode, UCSExecutionQueue, nodes, Position.Right);
				}
				if (nodes[tempRightNode.gridPosition[0]][tempRightNode.gridPosition[1]].getNodeVisited() == false
						&& MapNode.checkNodeInQueue(UCSExecutionQueue, tempRightNode.gridPosition[0],
								tempRightNode.gridPosition[1]) == false
						&& tempRightNode.getOwnElevation() != noGoZoneValue) {
					// check un-visited nodes and add new node into execution
					// queue
					UCSExecutionQueue.add(tempRightNode);
				}
			}
			// set current node to visited node and
			// remove current node from execution queue
			UCSExecutionQueue.get(firstNode).setNodeVistied(true);
			nodes[UCSExecutionQueue.get(firstNode).gridPosition[0]][UCSExecutionQueue.get(firstNode).gridPosition[1]]
					.setNodeVistied(true);
			UCSExecutionQueue.remove(firstNode);
		}

		// Debug Mode
		// print out the end node
		// print out the total distance between two points
		// print out the total number node of path
		// End point not Found
		// System.out.println("End Point:
		// "+(nodes[unexploreNode[0]][unexploreNode[1]].gridPosition[0]+1)+"
		// "+(nodes[unexploreNode[0]][unexploreNode[1]].gridPosition[1]+1));
		// System.out.println("Total number of nodes:
		// "+nodes[unexploreNode[0]][unexploreNode[1]].getTotPath().size());
		shortestPath = nodes[unexploreNode[0]][unexploreNode[1]].getTotPath();
		// check if result is valid
		if (shortestPath.size() == 1 && validatePositionArray(shortestPath.get(firstNode),unexploreNode)) {
			return null;
		}
		shortestPath.add(unexploreNode);
		return shortestPath;
	}

	/**
	 * This function updates new distance to the input node
	 * 
	 * @param inputNode
	 * @param UCSExecutionQueue
	 * @param nodes
	 */
	public static void updateUCSNodeInfo(MapNode inputNode, Vector<MapNode> UCSExecutionQueue, MapNode[][] nodes,
			Position pos) {
		// update new distance to the top node
		if (pos == Position.Top) {
			inputNode.setTotDistance(UCSExecutionQueue.get(firstNode).getTotDistance()
					+ UCSExecutionQueue.get(firstNode).getTopWeight());
		} else if (pos == Position.Bot) {
			inputNode.setTotDistance(UCSExecutionQueue.get(firstNode).getTotDistance()
					+ UCSExecutionQueue.get(firstNode).getBottomWeight());
		} else if (pos == Position.Left) {
			inputNode.setTotDistance(UCSExecutionQueue.get(firstNode).getTotDistance()
					+ UCSExecutionQueue.get(firstNode).getLeftWeight());
		} else if (pos == Position.Right) {
			inputNode.setTotDistance(UCSExecutionQueue.get(firstNode).getTotDistance()
					+ UCSExecutionQueue.get(firstNode).getRightWeight());
		}

		Vector<int[]> tempPath = new Vector<int[]>();
		// record the path
		for (int indexOfQueue = 0; indexOfQueue < UCSExecutionQueue.get(firstNode).getTotPath()
				.size(); indexOfQueue++) {
			int[] tempXY = new int[2];
			tempXY = UCSExecutionQueue.get(firstNode).getTotPath().get(indexOfQueue);
			tempPath.add(tempXY);
		}
		// check new node to see if exists in path
		if (MapNode.checkPositionInPath(tempPath, UCSExecutionQueue.get(firstNode).gridPosition[0],
				UCSExecutionQueue.get(firstNode).gridPosition[1]) == false) {
			int[] tempXY2 = new int[2];
			tempXY2 = UCSExecutionQueue.get(firstNode).gridPosition;
			tempPath.add(tempXY2);
		}
		// assign new path to the node
		inputNode.setTotPath(tempPath);
		// update data back to original matrix
		nodes[inputNode.gridPosition[0]][inputNode.gridPosition[1]] = inputNode;
	}
	/**
	 * This function checks if two given arrays are the same.
	 * @param position1
	 * @param position2
	 * @return
	 */
	public static boolean validatePositionArray(int[] position1, int[] position2) {
		if(position1[0] == position2[0] && position1[1] == position2[1]) {
			return true;
		} else {
			return false;
		}
	}

}
