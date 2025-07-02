package algorithms;
import java.util.Vector;
import utils.MapNode;
import utils.MapNode.Status;

/**
 * Breadth-first search
 * @author pz.yao
 * Coding Date: 22 Mar 2015
 */
public class BFS {
	/*
	 *  Predefined evaluation cost 
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
		
	public static Vector<int[]> bfs(int[] startPosition, int[] unexploreNode, MapNode[][] nodes) {
		// This function is using BFS method to obtain the path
		// between any two given nodes.

		Vector<int[]> shortestPath;

		// initial all nodes' status to UN-visited
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				nodes[i][j].setTotDistance(noGoZoneValue);// set the initial
				// distance to
				// INF
				nodes[i][j].gridPosition[0] = i;// save current node x position
				nodes[i][j].gridPosition[1] = j;// save current node y position
				nodes[i][j].setNodeVistied(false);// set all nodes are not
				// visited yet
			}
		}

		// create start node and add it into the execution queue
		MapNode tempNode = new MapNode();
		tempNode = nodes[startPosition[0]][startPosition[1]];
		Vector<MapNode> tempQueue = new Vector<MapNode>();
		tempQueue.add(tempNode);
		// main loop: check all nodes on the map
		while (tempQueue.size() != 0) {
			// create four nearby nodes
			MapNode tempTopNode = new MapNode();
			MapNode tempBottomNode = new MapNode();
			MapNode tempLeftNode = new MapNode();
			MapNode tempRightNode = new MapNode();

			// update Top node information from original nodes matrix
			if (tempQueue.get(0).topNode != null) {
				tempTopNode = nodes[tempQueue.get(0).topNode.gridPosition[0]][tempQueue.get(0).topNode.gridPosition[1]];
			} else {
				tempTopNode = null;
			}

			// update Bottom node information from original nodes matrix
			if (tempQueue.get(0).bottomNode != null) {
				tempBottomNode = nodes[tempQueue.get(0).bottomNode.gridPosition[0]][tempQueue
				                                                                    .get(0).bottomNode.gridPosition[1]];
			} else {
				tempBottomNode = null;
			}

			// update Left node information from original nodes matrix
			if (tempQueue.get(0).leftNode != null) {
				tempLeftNode = nodes[tempQueue.get(0).leftNode.gridPosition[0]][tempQueue
				                                                                .get(0).leftNode.gridPosition[1]];
			} else {
				tempLeftNode = null;
			}

			// update Right node information from original nodes matrix
			if (tempQueue.get(0).rightNode != null) {
				tempRightNode = nodes[tempQueue.get(0).rightNode.gridPosition[0]][tempQueue
				                                                                  .get(0).rightNode.gridPosition[1]];
			} else {
				tempRightNode = null;
			}

			// start re-calculate distance of each node
			// check the top node and update new distance
			if (tempTopNode != null) {
				if (tempTopNode.getNodeVisited() == false && tempTopNode.getOwnElevation() != noGoZoneValue
						&& tempTopNode.getStatus() != Status.EXPLORED) {
					Vector<int[]> tempPath = new Vector<int[]>();
					// record the path
					for (int m = 0; m < tempQueue.get(0).getTotPath().size(); m++) {
						int[] tempXY = new int[2];
						tempXY = tempQueue.get(0).getTotPath().get(m);
						tempPath.add(tempXY);
					}
					// check new node to see if exists in path
					if (MapNode.checkPositionInPath(tempPath, tempQueue.get(0).gridPosition[0],
							tempQueue.get(0).gridPosition[1]) == false) {
						int[] tempXY2 = new int[2];
						tempXY2 = tempQueue.get(0).gridPosition;
						tempPath.add(tempXY2);
					}
					// assign new path to the node
					tempTopNode.setTotPath(tempPath);
					// update data back to original matrix
					tempTopNode.setStatus(Status.EXPLORED);
					nodes[tempTopNode.gridPosition[0]][tempTopNode.gridPosition[1]] = tempTopNode;
				}

				if (tempTopNode.gridPosition[0] == unexploreNode[0]
						&& tempTopNode.gridPosition[1] == unexploreNode[1]) {
					shortestPath = nodes[unexploreNode[0]][unexploreNode[1]].getTotPath();
					// check if result is valid
					if (shortestPath.size() == 1 && UCS.validatePositionArray(shortestPath.get(firstNode),unexploreNode)) {
						return null;
					}
					shortestPath.add(unexploreNode);
					return shortestPath;
				}

				if (nodes[tempTopNode.gridPosition[0]][tempTopNode.gridPosition[1]].getNodeVisited() == false
						&& MapNode.checkNodeInQueue(tempQueue, tempTopNode.gridPosition[0],
								tempTopNode.gridPosition[1]) == false
								&& tempTopNode.getOwnElevation() != noGoZoneValue) {
					// check un-visited nodes and add new node into execution
					// queue
					tempQueue.add(tempTopNode);
				}
			}

			// check the bottom node and update new distance
			if (tempBottomNode != null) {
				if (tempBottomNode.getNodeVisited() == false && tempBottomNode.getOwnElevation() != noGoZoneValue
						&& tempBottomNode.getStatus() != Status.EXPLORED) {
					Vector<int[]> tempPath = new Vector<int[]>();
					// record the path
					for (int m = 0; m < tempQueue.get(0).getTotPath().size(); m++) {
						int[] tempXY = new int[2];
						tempXY = tempQueue.get(0).getTotPath().get(m);
						tempPath.add(tempXY);
					}
					// check new node to see if it exists in path
					if (MapNode.checkPositionInPath(tempPath, tempQueue.get(0).gridPosition[0],
							tempQueue.get(0).gridPosition[1]) == false) {
						int[] tempXY2 = new int[2];
						tempXY2 = tempQueue.get(0).gridPosition;
						tempPath.add(tempXY2);
					}
					// assign new path to the node
					tempBottomNode.setTotPath(tempPath);
					tempBottomNode.setStatus(Status.EXPLORED);
					// update data back to original matrix
					nodes[tempQueue.get(0).bottomNode.gridPosition[0]][tempQueue
					                                                   .get(0).bottomNode.gridPosition[1]] = tempBottomNode;
				}
				if (tempBottomNode.gridPosition[0] == unexploreNode[0]
						&& tempBottomNode.gridPosition[1] == unexploreNode[1]) {
					shortestPath = nodes[unexploreNode[0]][unexploreNode[1]].getTotPath();
					// check if result is valid
					if (shortestPath.size() == 1 && UCS.validatePositionArray(shortestPath.get(firstNode),unexploreNode)) {
						return null;
					}
					shortestPath.add(unexploreNode);
					return shortestPath;
				}

				if (nodes[tempBottomNode.gridPosition[0]][tempBottomNode.gridPosition[1]].getNodeVisited() == false
						&& MapNode.checkNodeInQueue(tempQueue, tempBottomNode.gridPosition[0],
								tempBottomNode.gridPosition[1]) == false
								&& tempBottomNode.getOwnElevation() != noGoZoneValue) {
					// check un-visited nodes and add new node into execution
					// queue
					tempQueue.add(tempBottomNode);
				}
			}

			// check the left node and update new distance
			if (tempLeftNode != null) {
				if (tempLeftNode.getNodeVisited() == false && tempLeftNode.getOwnElevation() != noGoZoneValue
						&& tempLeftNode.getStatus() != Status.EXPLORED) {
					Vector<int[]> tempPath = new Vector<int[]>();
					// record the path
					for (int m = 0; m < tempQueue.get(0).getTotPath().size(); m++) {
						int[] tempXY = new int[2];
						tempXY = tempQueue.get(0).getTotPath().get(m);
						tempPath.add(tempXY);
					}
					// check new node to see if it exists in path
					if (MapNode.checkPositionInPath(tempPath, tempQueue.get(0).gridPosition[0],
							tempQueue.get(0).gridPosition[1]) == false) {
						int[] tempXY2 = new int[2];
						tempXY2 = tempQueue.get(0).gridPosition;
						tempPath.add(tempXY2);
					}
					// assign new path to the node
					tempLeftNode.setTotPath(tempPath);
					tempLeftNode.setStatus(Status.EXPLORED);
					// update data back to original matrix
					nodes[tempQueue.get(0).leftNode.gridPosition[0]][tempQueue
					                                                 .get(0).leftNode.gridPosition[1]] = tempLeftNode;
				}
				if (tempLeftNode.gridPosition[0] == unexploreNode[0]
						&& tempLeftNode.gridPosition[1] == unexploreNode[1]) {
					shortestPath = nodes[unexploreNode[0]][unexploreNode[1]].getTotPath();
					// check if result is valid
					if (shortestPath.size() == 1 && UCS.validatePositionArray(shortestPath.get(firstNode),unexploreNode)) {
						return null;
					}
					shortestPath.add(unexploreNode);
					return shortestPath;
				}

				if (nodes[tempLeftNode.gridPosition[0]][tempLeftNode.gridPosition[1]].getNodeVisited() == false
						&& MapNode.checkNodeInQueue(tempQueue, tempLeftNode.gridPosition[0],
								tempLeftNode.gridPosition[1]) == false
								&& tempLeftNode.getOwnElevation() != noGoZoneValue) {
					// check un-visited nodes and add new node into execution
					// queue
					tempQueue.add(tempLeftNode);
				}
			}

			// check the right node and update new distance
			if (tempRightNode != null) {
				if (tempRightNode.getNodeVisited() == false && tempRightNode.getOwnElevation() != noGoZoneValue
						&& tempRightNode.getStatus() != Status.EXPLORED) {
					Vector<int[]> tempPath = new Vector<int[]>();
					// record the path
					for (int m = 0; m < tempQueue.get(0).getTotPath().size(); m++) {
						int[] tempXY = new int[2];
						tempXY = tempQueue.get(0).getTotPath().get(m);
						tempPath.add(tempXY);
					}
					// check to see if new node existed in path
					if (MapNode.checkPositionInPath(tempPath, tempQueue.get(0).gridPosition[0],
							tempQueue.get(0).gridPosition[1]) == false) {
						int[] tempXY2 = new int[2];
						tempXY2 = tempQueue.get(0).gridPosition;
						tempPath.add(tempXY2);
					}
					// assign new path to the node
					tempRightNode.setTotPath(tempPath);
					tempRightNode.setStatus(Status.EXPLORED);
					// update data back to original matrix
					nodes[tempQueue.get(0).rightNode.gridPosition[0]][tempQueue
					                                                  .get(0).rightNode.gridPosition[1]] = tempRightNode;
				}
				if (tempRightNode.gridPosition[0] == unexploreNode[0]
						&& tempRightNode.gridPosition[1] == unexploreNode[1]) {
					shortestPath = nodes[unexploreNode[0]][unexploreNode[1]].getTotPath();
					// check if result is valid
					if (shortestPath.size() == 1 && UCS.validatePositionArray(shortestPath.get(firstNode),unexploreNode)) {
						return null;
					}
					shortestPath.add(unexploreNode);
					return shortestPath;
				}

				if (nodes[tempRightNode.gridPosition[0]][tempRightNode.gridPosition[1]].getNodeVisited() == false
						&& MapNode.checkNodeInQueue(tempQueue, tempRightNode.gridPosition[0],
								tempRightNode.gridPosition[1]) == false
								&& tempRightNode.getOwnElevation() != noGoZoneValue) {
					// check un-visited nodes and add new node into execution
					// queue
					tempQueue.add(tempRightNode);
				}
			}
			// set current node to visited node and
			// remove current node from execution queue
			tempQueue.get(0).setNodeVistied(true);
			nodes[tempQueue.get(0).gridPosition[0]][tempQueue.get(0).gridPosition[1]].setNodeVistied(true);
			tempQueue.remove(0);
		}
		
		shortestPath = nodes[unexploreNode[0]][unexploreNode[1]].getTotPath();
		// check if result is valid
		if (shortestPath.size() == 1 && UCS.validatePositionArray(shortestPath.get(firstNode),unexploreNode)) {
			return null;
		}
		shortestPath.add(unexploreNode);
		return shortestPath;
	}

}
