package utils;
import java.util.Vector;

/**
 * @author pz.yao
 * Coding Date: 22 Mar 2015
 */
public class MapNode {
	/*
	 * Predefined two status of the node
	 */
	public enum Status {
		UNEXPLORED, EXPLORED
	};
	
	/*
	 * Node status = Explored or unexplored
	 */
	private Status status;
	/*
	 * Node role
	 */
	private Boolean noGoZone;
	private Boolean road;
	/*
	 * Elevation of node (local cost)
	 */
	private int ownElevation;
	/*
	 * The elevation weight of current node to other nodes
	 */
	private int topWeight;
	private int bottomWeight;
	private int leftWeight;
	private int rightWeight;
	/*
	 * The total distance between start point to this point
	 */
	private int totDistance;
	/*
	 * Visited node property
	 */
	private boolean nodeVisited = false;
	/*
	 * Store the shortest path from start point to this point
	 */
	private Vector<int[]> path;
	/*
	 * Node role cost
	 */
	private int weightOfRoad = 1;
	private int weightOfMapEdge = Integer.MAX_VALUE;// wall or not node there
	private int weightOfNoGoZone = Integer.MAX_VALUE;// Obstacle
	/*
	 * Total cost
	 */
	private float f_cost;
	/*
	 * h value
	 */
	private float heuristics;
	/*
	 * Store the position of this point
	 */
	public int[] gridPosition = new int[2];

	/*
	 * Four nodes connect to top, bottom, left and right nearby nodes
	 */
	public MapNode topNode;
	public MapNode bottomNode;
	public MapNode leftNode;
	public MapNode rightNode;
	/**
	 * Constructor
	 */
	public MapNode() {
		// node initialization
		this.status = status.UNEXPLORED;
		this.noGoZone = false;
		this.road = false;
		this.f_cost = Integer.MAX_VALUE;
	}

	/**
	 * Read the no go zone status
	 * @return
	 */
	public Boolean isNoGoZone() {
		return this.noGoZone;
	}

	/**
	 * Set the no go zone status
	 * @param bool
	 */
	public void setNoGoZone(Boolean bool) {
		this.noGoZone = bool;
	}

	/**
	 * Read the road status
	 * @return
	 */
	public Boolean isRoad() {
		return this.road;
	}

	/**
	 * Set the road status
	 * @param bool
	 */
	public void setRoad(Boolean bool) {
		this.road = bool;
	}

	/**
	 * Read the node status
	 * @return
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Set the node status
	 * @param status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Update the top weight of current node
	 */
	public void updateTopWeight() {
		if (this.topNode != null) {
			if (this.topNode.isNoGoZone() == true) {
				// set edge weight to Integer.MAX_VALUE if the next node is NO
				// go zone or
				// boundary node
				this.topWeight = weightOfNoGoZone;
			} else if (this.topNode.road == true && this.topNode.isNoGoZone() == false) {
				// set edge weight to 1 if the next node is a road node
				this.topWeight = this.topNode.getOwnElevation();
			}
		} else {
			// set edge weight to INF if the next node is NULL
			this.topWeight = weightOfMapEdge;
		}
	}

	/**
	 * Update the bottom weight of current node
	 */
	public void updateBottomWeight() {
		if (this.bottomNode != null) {
			if (this.bottomNode.isNoGoZone() == true) {
				// set edge weight to 9999 if the next node is NO go zone or
				// boundary node
				this.bottomWeight = weightOfNoGoZone;
			} else if (this.bottomNode.isRoad() == true && this.bottomNode.isNoGoZone() == false) {
				// set edge weight to 1 if the next node is a road node
				this.bottomWeight = this.bottomNode.getOwnElevation();
			}
		} else {
			// set edge weight to INF if the next node is NULL
			this.bottomWeight = weightOfMapEdge;
		}
	}

	/**
	 * Update the left weight of current node
	 */
	public void updateLeftWeight() {
		if (this.leftNode != null) {
			if (this.leftNode.isNoGoZone() == true) {
				// set edge weight to 9999 if the next node is NO go zone or
				// boundary node
				this.leftWeight = weightOfNoGoZone;
			} else if (this.leftNode.isRoad() == true && this.leftNode.isNoGoZone() == false) {
				// set edge weight to 1 if the next node is a road node
				this.leftWeight = this.leftNode.getOwnElevation();
			}
		} else {
			// set edge weight to INF if the next node is NULL
			this.leftWeight = weightOfMapEdge;
		}
	}

	/**
	 * Update the right weight of current node
	 */
	public void updateRightWeight() {
		if (this.rightNode != null) {
			if (this.rightNode.isNoGoZone() == true) {
				// set edge weight to 9999 if the next node is NO go zone or
				// boundary node
				this.rightWeight = weightOfNoGoZone;
			} else if (this.rightNode.isRoad() == true && this.rightNode.isNoGoZone() == false) {
				// set edge weight to 1 if the next node is a road node
				this.rightWeight = this.rightNode.getOwnElevation();
			}
		} else {
			// set edge weight to INF if the next node is NULL
			this.rightWeight = weightOfMapEdge;
		}
	}
	
	/**
	 * Check if node is already in path.
	 * @param path
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean checkPositionInPath(Vector<int[]> path, int x, int y) {
		// This function is used to check that
		// the give node if it exist in path
		boolean positionChecker = false;
		for (int i = 0; i < path.size(); i++) {
			if (path.get(i)[0] == x && path.get(i)[1] == y) {
				positionChecker = true;
			}
		}
		return positionChecker;
	}
	
	/**
	 * Check if node is in queue.
	 * @param tempQueue
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean checkNodeInQueue(Vector<MapNode> tempQueue, int x, int y) {
		// This function is used to check that
		// the give node if it exist in execution queue
		boolean nodeChecker = false;
		for (int i = 0; i < tempQueue.size(); i++) {
			if (tempQueue.get(i).gridPosition[0] == x && tempQueue.get(i).gridPosition[1] == y) {
				nodeChecker = true;
			}
		}
		return nodeChecker;
	}

	/**
	 * Read the top weight of current node
	 * @return
	 */
	public int getTopWeight() {
		return this.topWeight;
	}

	/**
	 * Read the bottom weight of current node
	 * @return
	 */
	public int getBottomWeight() {
		return this.bottomWeight;
	}

	/**
	 * Read the left weight of current node
	 * @return
	 */
	public int getLeftWeight() {
		return this.leftWeight;
	}

	/**
	 * Read the right weigt of current node
	 * @return
	 */
	public int getRightWeight() {
		return this.rightWeight;
	}

	/**
	 * Read the total distance
	 * @return
	 */
	public int getTotDistance() {
		return this.totDistance;
	}

	/**
	 * Set total distance
	 * @param n
	 */
	public void setTotDistance(int n) {
		this.totDistance = n;
	}

	/**
	 * Read the path
	 * @return
	 */
	public Vector<int[]> getTotPath() {
		return this.path;
	}

	/**
	 * Set path
	 * @param path
	 */
	public void setTotPath(Vector<int[]> path) {
		this.path = path;
	}

	/**
	 * Check if this node has been visited
	 * @return
	 */
	public boolean getNodeVisited() {
		return this.nodeVisited;
	}

	/**
	 * Set the node visited property
	 * @param nodeVisit
	 */
	public void setNodeVistied(boolean nodeVisit) {
		this.nodeVisited = nodeVisit;
	}
	/**
	 * Set elevation value
	 * @param elevation
	 */
	public void setElevation(int elevation) {
		this.ownElevation = elevation;
	}
	/**
	 * Get node elevation value
	 * @return
	 */
	public int getOwnElevation() {
		return this.ownElevation;
	}
	/**
	 * Set total cost of the node
	 * @param cost
	 */
	public void setTotCost(float cost) {
		this.f_cost = cost;
	}
	/**
	 * Get total cost of the node
	 * @return
	 */
	public float getTotCost() {
		return this.f_cost;
	}
	/**
	 * Set h value
	 * @param h
	 */
	public void setHeuristics(float h) {
		this.heuristics = h;
	}
	/**
	 * get h value
	 * @return
	 */
	public float getHeuristics() {
		return this.heuristics;
	}
}
