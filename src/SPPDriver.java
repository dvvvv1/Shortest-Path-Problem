
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import algorithms.ASTAR;
import algorithms.BFS;
import algorithms.UCS;
import utils.Configuration;
import utils.MapNode;
import utils.MapNode.Status;

/**
 * Arguments: map.txt method modeForAStarMethod 
 * method = {bfs, ucs, astar}
 * modeForAStarMethod = {Manhattan, Euclidean}
 * @author puzhiyao
 * Email: puzhi.yao@gmail.com
 * Coding Date: 22 Mar 2015
 * 
 * Given map.txt:
 * 1st Line = Size of map
 * 2nd Line = Start point
 * 3rd Line = End point
 * Rest Lines:
 * 2D array of the map
 * 
 * Example:
 * 4 4
 * 1 1
 * 3 3
 * 1 1 2 2
 * 2 1 2 2
 * 1 1 1 1
 * 2 2 2 1
 * 
 * Result of ucs:
 * * * 2 2
 * 2 * 2 2
 * 1 * * *
 * 2 2 2 *
 */
public class SPPDriver {
	/*
	 * Size of the input map
	 */
	private static int[] sizeOfMap = new int[2];
	/*
	 * Search start point
	 */
	private static int[] startPoint = new int[2];
	/*
	 * Search target point
	 */
	private static int[] endPoint = new int[2];
	/*
	 * Map of nodes contains input data
	 */
	private static MapNode[][] nodes;
	/*
	 * method = {bfs, ucs, astar}
	 */
	private static String method;
	/*
	 * modeForAStarMethod = {Manhattan, Euclidean}
	 */
	private static String modeForAStarMethod;
	/*
	 * Program Error Exit Signal
	 */
	private static int ExitSignal = 0;
	/*
	 * Program Error Exit String list
	 */
	private static String ExitString01 = "Arguments Format Error.";
	private static String ExitString02 = "Map size does not match map data.";
	private static String ExitString03 = "The length of data line does not match node map.";
	private static String ValidationFailed01 = "User defined parameter does not match input data.";
	private static String ValidationFailedO2 = "User defined start point or end point is not in map range.";
	private static String NoValidResult  = "No Valid Result Found";
	/*
	 * Predefined node values
	 */
	private static int pathValue = -1;
	private static int noGoZoneValue = Integer.MAX_VALUE;
	private static int inputPointLength = 2;
	
	/**
	 * This is the driver function of shortest path problem
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// run configuration
		Configuration config = new Configuration();
		String[] parameters = config.getConfig();
		
		// read map information from text file
		readAllInputsFromMapFile(parameters);
		
		// connect each node in node map
		connectNodeMap();

		// validate start point and end point
		// ensure they are in the range of the input map data.
		validateStartPointAndEndPoint();

		// setup result container
		Vector<int[]> result;

		// perform method based on inputs
		if (method.equals("bfs")) {
			result = BFS.bfs(startPoint, endPoint, nodes);
			if (result == null) {
				System.out.println(NoValidResult);
			} else {
				updateResultOnMap(result, nodes);
			}
		} else if (method.equals("ucs")) {
			// System.out.println("UCS Shortest Path: ");
			result = UCS.ucs(startPoint, endPoint, nodes);
			if (result == null) {
				System.out.println(NoValidResult);
			} else {
				updateResultOnMap(result, nodes);
			}
		} else if (method.equals("astar")) {
			// System.out.println("A* Path:");
			result = ASTAR.aStar(startPoint, endPoint, nodes, modeForAStarMethod);
			if (result == null) {
				System.out.println(NoValidResult);
			} else {
				updateResultOnMap(result, nodes);
			}
		}
	}
	
	/**
	 * This function connects each node (Using Linked List to connect the
	 * the top, bottom, left and right nodes of current node
	 */
	private static void connectNodeMap() {
		// loop node map
		for (int colOfNodeMap = 0; colOfNodeMap < sizeOfMap[1]; colOfNodeMap++) {
			for (int rowOfNodeMap = 0; rowOfNodeMap < sizeOfMap[0]; rowOfNodeMap++) {
				// connect the top left node
				if (colOfNodeMap == 0 && rowOfNodeMap == 0) {
					nodes[colOfNodeMap][rowOfNodeMap].topNode = null;// top node does not exist
					nodes[colOfNodeMap][rowOfNodeMap].bottomNode = nodes[colOfNodeMap][rowOfNodeMap + 1];// connect bottom
					// node to
					// current node
					nodes[colOfNodeMap][rowOfNodeMap].leftNode = null;// left node does not exist
					nodes[colOfNodeMap][rowOfNodeMap].rightNode = nodes[colOfNodeMap + 1][rowOfNodeMap];// connect right
					// node to current
					// node
				}
				// connect the bottom left node
				else if (colOfNodeMap == 0 && rowOfNodeMap == sizeOfMap[0] - 1) {
					nodes[colOfNodeMap][rowOfNodeMap].topNode = nodes[colOfNodeMap][rowOfNodeMap - 1];
					nodes[colOfNodeMap][rowOfNodeMap].bottomNode = null;
					nodes[colOfNodeMap][rowOfNodeMap].leftNode = null;
					nodes[colOfNodeMap][rowOfNodeMap].rightNode = nodes[colOfNodeMap + 1][rowOfNodeMap];
				}
				// connect the top right node
				else if (colOfNodeMap == sizeOfMap[1] - 1 && rowOfNodeMap == 0) {
					nodes[colOfNodeMap][rowOfNodeMap].topNode = null;
					nodes[colOfNodeMap][rowOfNodeMap].bottomNode = nodes[colOfNodeMap][rowOfNodeMap + 1];
					nodes[colOfNodeMap][rowOfNodeMap].leftNode = nodes[colOfNodeMap - 1][rowOfNodeMap];
					nodes[colOfNodeMap][rowOfNodeMap].rightNode = null;
				}
				// connect the bottom right node
				else if (colOfNodeMap == sizeOfMap[1] - 1 && rowOfNodeMap == sizeOfMap[0] - 1) {
					nodes[colOfNodeMap][rowOfNodeMap].topNode = nodes[colOfNodeMap][rowOfNodeMap - 1];
					nodes[colOfNodeMap][rowOfNodeMap].bottomNode = null;
					nodes[colOfNodeMap][rowOfNodeMap].leftNode = nodes[colOfNodeMap - 1][rowOfNodeMap];
					nodes[colOfNodeMap][rowOfNodeMap].rightNode = null;
				}
				// connect the left edge nodes
				else if (colOfNodeMap == 0 && rowOfNodeMap != 0 && rowOfNodeMap != sizeOfMap[0] - 1) {
					nodes[colOfNodeMap][rowOfNodeMap].topNode = nodes[colOfNodeMap][rowOfNodeMap - 1];
					nodes[colOfNodeMap][rowOfNodeMap].bottomNode = nodes[colOfNodeMap][rowOfNodeMap + 1];
					nodes[colOfNodeMap][rowOfNodeMap].leftNode = null;
					nodes[colOfNodeMap][rowOfNodeMap].rightNode = nodes[colOfNodeMap + 1][rowOfNodeMap];
				}
				// connect the right edge nodes
				else if (colOfNodeMap == sizeOfMap[1] - 1 && rowOfNodeMap != 0 && rowOfNodeMap != sizeOfMap[0] - 1) {
					nodes[colOfNodeMap][rowOfNodeMap].topNode = nodes[colOfNodeMap][rowOfNodeMap - 1];
					nodes[colOfNodeMap][rowOfNodeMap].bottomNode = nodes[colOfNodeMap][rowOfNodeMap + 1];
					nodes[colOfNodeMap][rowOfNodeMap].leftNode = nodes[colOfNodeMap - 1][rowOfNodeMap];
					nodes[colOfNodeMap][rowOfNodeMap].rightNode = null;
				}
				// connect the top edge nodes
				else if (colOfNodeMap != 0 && colOfNodeMap != sizeOfMap[1] - 1 && rowOfNodeMap == 0) {
					nodes[colOfNodeMap][rowOfNodeMap].topNode = null;
					nodes[colOfNodeMap][rowOfNodeMap].bottomNode = nodes[colOfNodeMap][rowOfNodeMap + 1];
					nodes[colOfNodeMap][rowOfNodeMap].leftNode = nodes[colOfNodeMap - 1][rowOfNodeMap];
					nodes[colOfNodeMap][rowOfNodeMap].rightNode = nodes[colOfNodeMap + 1][rowOfNodeMap];
				}
				// connect the bottom edge nodes
				else if (colOfNodeMap != 0 && colOfNodeMap != sizeOfMap[1] - 1 && rowOfNodeMap == sizeOfMap[0] - 1) {
					nodes[colOfNodeMap][rowOfNodeMap].topNode = nodes[colOfNodeMap][rowOfNodeMap - 1];
					nodes[colOfNodeMap][rowOfNodeMap].bottomNode = null;
					nodes[colOfNodeMap][rowOfNodeMap].leftNode = nodes[colOfNodeMap - 1][rowOfNodeMap];
					nodes[colOfNodeMap][rowOfNodeMap].rightNode = nodes[colOfNodeMap + 1][rowOfNodeMap];
				}
				// connect all rest nodes
				else {
					nodes[colOfNodeMap][rowOfNodeMap].topNode = nodes[colOfNodeMap][rowOfNodeMap - 1];
					nodes[colOfNodeMap][rowOfNodeMap].bottomNode = nodes[colOfNodeMap][rowOfNodeMap + 1];
					nodes[colOfNodeMap][rowOfNodeMap].leftNode = nodes[colOfNodeMap - 1][rowOfNodeMap];
					nodes[colOfNodeMap][rowOfNodeMap].rightNode = nodes[colOfNodeMap + 1][rowOfNodeMap];
				}
				// update the weight on the edge between each node
				nodes[colOfNodeMap][rowOfNodeMap].updateTopWeight();// update the top edge weight
				nodes[colOfNodeMap][rowOfNodeMap].updateBottomWeight();// update the bottom edge
				// weight
				nodes[colOfNodeMap][rowOfNodeMap].updateLeftWeight();// update the Left edge weight
				nodes[colOfNodeMap][rowOfNodeMap].updateRightWeight();// update the Right edge weight
			}
		}
	}

	/**
	 * This function takes input arguments to fetch data map from
	 * local *.txt file.
	 * @param args
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private static void readAllInputsFromMapFile(String[] args) throws NumberFormatException, IOException {
		// initialization of file reading
		java.io.FileReader fr = new FileReader(args[0]);
		BufferedReader reader = new BufferedReader(fr);

		// read method
		if (args.length > 2) {
			method = args[1];
			modeForAStarMethod = args[2];
		} else if (args.length == 2) {
			method = args[1];
		} else {
			System.out.println(ExitString01);
			System.exit(ExitSignal);
		}

		// validate arguments
		if (!validateInputArguments()) {
			System.out.println(ExitString01);
			System.exit(ExitSignal);
		}

		// read first and second line
		// store the map size and start/end point
		String nextLine;

		// read rest data and store in matrix
		for (int indexOfLine = 0; indexOfLine < 3; indexOfLine++) {
			if ((nextLine = reader.readLine()) != null) {
				String[] elementOftheLine = nextLine.split("\\s");
				// validate the length of input line
				validateLengthOfItems(elementOftheLine.length, inputPointLength);

				// copy map data from input file
				for (int indesOfElements = 0; indesOfElements < 2; indesOfElements++) {
					Integer intReader = Integer.valueOf(elementOftheLine[indesOfElements]);
					if (indexOfLine == 0) {
						sizeOfMap[indesOfElements] = intReader;// store integers into list
					} else if (indexOfLine == 1) {
						startPoint[indesOfElements] = intReader;// store integers into list
					} else if (indexOfLine == 2) {
						endPoint[indesOfElements] = intReader;// store integers into list
					}
				}
			}
		}
		// initialization of node map
		nodes = new MapNode[sizeOfMap[1]][sizeOfMap[0]];

		// read map information
		int rowPosition = 0;
		while ((nextLine = reader.readLine()) != null) {
			// validate map size and data size
			if (rowPosition >= sizeOfMap[0]) {
				System.out.println(ExitString02);
				System.exit(ExitSignal);
			}

			String[] elementsOfEachLine = nextLine.split("\\s");
			// validate data length
			if (elementsOfEachLine.length != sizeOfMap[1]) {
				System.out.println(ExitString03);
				System.exit(ExitSignal);
			}
			// read data from file and store in node structure
			for (int colPosition = 0; colPosition < sizeOfMap[1]; colPosition++) {
				MapNode tempNode = new MapNode();
				if (elementsOfEachLine[colPosition].equals("X")) {
					tempNode.setNoGoZone(true);
					tempNode.setElevation(noGoZoneValue);// set elevation cost to Max
				} else {
					Integer intReader = Integer.valueOf(
							elementsOfEachLine[colPosition]);// reader all nteger
					tempNode.setRoad(true);
					tempNode.setElevation(intReader);
				}

				// record shortest path of each node
				Vector<int[]> initialPath = new Vector<int[]>();
				int[] initialPosition = new int[2];
				// setup initial position of each node
				initialPosition[0] = colPosition;
				initialPosition[1] = rowPosition;
				initialPath.add(initialPosition);
				tempNode.setTotPath(initialPath);
				// store the position of each node
				nodes[colPosition][rowPosition] = tempNode;
			}
			rowPosition++;
		}

		// validate map size and data size
		validateInputMap(rowPosition);
		reader.close();
		fr.close();
	}
	
	/**
	 * This function will check the position of node to see
	 * if it is in the result vector.
	 * @param result
	 * @param x
	 * @param y
	 * @return
	 */
	private static boolean checkResult(Vector<int[]> result, int x, int y) {
		boolean checkFlag = false;
		for (int indexOfResult = 0; indexOfResult < result.size(); indexOfResult++) {
			if (result.get(indexOfResult)[0] == x && result.get(indexOfResult)[1] == y) {
				checkFlag = true;
			}
		}
		return checkFlag;
	}

	/**
	 * This function will update result node on map and print new map.
	 * @param result
	 * @param nodes
	 */
	private static void updateResultOnMap(Vector<int[]> result, MapNode[][] nodes) {
		//
		int sizeOfMap1 = nodes.length;
		int sizeOfMap2 = nodes[0].length;
		for (int rowOfNodeMap = 0; rowOfNodeMap < sizeOfMap2; rowOfNodeMap++) {
			for (int colOfNodeMap = 0; colOfNodeMap < sizeOfMap1; colOfNodeMap++) {
				if (colOfNodeMap == sizeOfMap1 - 1) {
					if (nodes[colOfNodeMap][rowOfNodeMap].getOwnElevation() == noGoZoneValue) {
						System.out.println("X");
					} else if (checkResult(result, colOfNodeMap, rowOfNodeMap)) {
						System.out.println("*");
					} else {
						System.out.println(nodes[colOfNodeMap][rowOfNodeMap].getOwnElevation());
					}
				} else {
					if (nodes[colOfNodeMap][rowOfNodeMap].getOwnElevation() == noGoZoneValue) {
						System.out.print("X ");
					} else if (checkResult(result, colOfNodeMap, rowOfNodeMap)) {
						System.out.print("* ");
					} else {
						System.out.print(nodes[colOfNodeMap][rowOfNodeMap].getOwnElevation() + " ");
					}
				}
			}
		}
	}

	/**
	 * Debug Function
	 * This function prints out the input map data.
	 * @param nodes
	 */
	private void printMap(MapNode[][] nodes) {
		// This function will print out the map
		int sizeOfMap = nodes.length;
		for (int rowOfNodeMap = 0; rowOfNodeMap < sizeOfMap; rowOfNodeMap++) {
			for (int colOfNodeMap = 0; colOfNodeMap < sizeOfMap; colOfNodeMap++) {
				if (colOfNodeMap == sizeOfMap - 1) {
					if (nodes[colOfNodeMap][rowOfNodeMap].getOwnElevation() == noGoZoneValue) {
						System.out.println("X");
					} else if (nodes[colOfNodeMap][rowOfNodeMap].getOwnElevation() == pathValue) {
						System.out.println("*");
					} else {
						System.out.println(nodes[colOfNodeMap][rowOfNodeMap].getOwnElevation());
					}
				} else {
					if (nodes[colOfNodeMap][rowOfNodeMap].getOwnElevation() == noGoZoneValue) {
						System.out.print("X ");
					} else if (nodes[colOfNodeMap][rowOfNodeMap].getOwnElevation() == pathValue) {
						System.out.print("* ");
					} else {
						System.out.print(nodes[colOfNodeMap][rowOfNodeMap].getOwnElevation() + " ");
					}
				}
			}
		}
	}

	/**
	 * This function validates the format of input arguments.
	 * @return boolean
	 */
	private static boolean validateInputArguments() {
		// validate if method is bfs, ucs or astar
		if (method.equals("bfs") && modeForAStarMethod == "") {
			return true;
		} else if (method.equals("ucs") && modeForAStarMethod == "") {
			return true;
		} else if (method.equals("astar")) {
			if (modeForAStarMethod.equals("manhattan") || modeForAStarMethod.equals("euclidean")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * This function validates the format of input map file.
	 * @param rowPosition
	 */
	private static void validateInputMap(int rowPosition) {
		if (rowPosition != sizeOfMap[0]) {
			System.out.println(ExitString02);
			System.exit(ExitSignal);
		}
	}

	/**
	 * This function validates two input value is the same or not.
	 * @param length
	 * @param requirement
	 */
	private static void validateLengthOfItems(int length, int requirement) {
		if (length != requirement) {
			System.out.println(ValidationFailed01);
			System.exit(ExitSignal);
		}
	}

	/**
	 * This function validates start and end points within
	 * map range.
	 */
	private static void validateStartPointAndEndPoint() {
		// convert start and end position
		// range from 0 to size - 1
		startPoint[0] = startPoint[0] - 1;
		startPoint[1] = startPoint[1] - 1;
		endPoint[0] = endPoint[0] - 1;
		endPoint[1] = endPoint[1] - 1;

		// swap row and col
		int temp;
		temp = startPoint[0];
		startPoint[0] = startPoint[1];
		startPoint[1] = temp;

		temp = endPoint[0];
		endPoint[0] = endPoint[1];
		endPoint[1] = temp;

		// validation
		if (startPoint[0] >= sizeOfMap[1] || startPoint[0] < 0 ||
				startPoint[1] >= sizeOfMap[0] || startPoint[1] < 0 ||
				endPoint[0] >= sizeOfMap[1] || endPoint[0] < 0 ||
				endPoint[1] >= sizeOfMap[0] || endPoint[1] < 0) {
			System.out.println(ValidationFailedO2);
			System.exit(ExitSignal);
		}
	}
}
