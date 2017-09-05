# Shortest-Path-Problem
## Algorithms ##
 * Breadth-first search
 * Uniform cost search (Dijkstra's algorithm)
 * A* search

## Prerequisite ##
Make sure run this program on Linux.
Make sure ANT is installed in your computer.

## Error Handler 01 ##
If your terminal shows the error message like 'cant find ant command'
please install ant.

## Error Handler 02 ##
If you use and IDE such as Eclipse, you can direcly copy all files and folder
into your target project folder, and refreash project view in Eclipse.


## Instructions ##

1. Open terminal

2. cd to Shortest-Path-Problem/

3. Type "ant" in terminal to compile all files

4. Type "ant SPPDriver" in terminal to Run the program

5. Results will be shown in terminal. 

6. Algorithms and parameters can be changed in config file (e.g. "~/Shortest-Path-Problem/config") 

7. map data, start point and end point can be changed in map.txt.

 * Given map.txt:
 * 1st Line = Size of map
 * 2nd Line = Start point
 * 3rd Line = End point
 * X presents no go zone
 * Rest Lines:
 * 2D array of the map
 * 
 * Example:
 * 4 4
 * 1 1
 * 3 3
 * 1 1 X 2
 * 2 1 X 2
 * 1 1 1 1
 * 2 2 2 1
 * 
 * Result of ucs:
 * 	* * X 2
 * 	2 * X 2
 * 	1 * * *
 * 	2 2 2 *

## Instruction END ##
