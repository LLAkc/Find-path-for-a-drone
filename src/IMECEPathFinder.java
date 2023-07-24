import java.util.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class IMECEPathFinder{
	  public int[][] grid;
	  public int height, width;
	  public int maxFlyingHeight;
	  public double fuelCostPerUnit, climbingCostPerUnit;
	  
	  int maxValue = 0;
	  int minvalue = Integer.MAX_VALUE;

	  

	  public IMECEPathFinder(String filename, int rows, int cols, int maxFlyingHeight, double fuelCostPerUnit, double climbingCostPerUnit){

		  grid = new int[rows][cols];
		  this.height = rows;
		  this.width = cols;
		  this.maxFlyingHeight = maxFlyingHeight;
		  this.fuelCostPerUnit = fuelCostPerUnit;
		  this.climbingCostPerUnit = climbingCostPerUnit;
		  
		  
		  try {
		      File file = new File(filename);
		      Scanner reader = new Scanner(file);
		      for (int i = 0; i < rows; i++) {
		    	  for (int j = 0; j < cols; j++) {
		    		  grid[i][j] = Integer.parseInt(reader.next());
		    		  if (grid[i][j]>maxValue) {
						maxValue = grid[i][j];
					}
		    		  if (grid[i][j]<minvalue) {
						minvalue = grid[i][j];
					}
		    	  }
			}
		      reader.close();
		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    }
		  

	  }


	  /**
	   * Draws the grid using the given Graphics object.
	   * Colors should be grayscale values 0-255, scaled based on min/max elevation values in the grid
	   */
	  public void drawGrayscaleMap(Graphics g){

		  for (int i = 0; i < grid.length; i++)
		  {
			  for (int j = 0; j < grid[0].length; j++) {
				  int value = (grid[i][j] - minvalue)*256/(maxValue-minvalue+1);
				  g.setColor(new Color(value, value, value));
				  g.fillRect(j, i, 1, 1);
			  }
		  }
	  }

	/**
	 * Get the most cost-efficient path from the source Point start to the destination Point end
	 * using Dijkstra's algorithm on pixels.
	 * @return the List of Points on the most cost-efficient path from start to end
	 */
	public List<Point> getMostEfficientPath(Point start, Point end) {
		Point.filloc();
		getOutput();

		List<Point> path = new ArrayList<>();
		ArrayList<Point> queue = new ArrayList<>();
		Point pointArray[][] = new Point[height][width];
		Point start2 = new Point(start.y, start.x);
		Point end2 = new Point(end.y, end.x);
		
		int heightImpact = 0;
		queue.add(start2);
		pointArray[start2.x][start2.y] = start2;
				
		Point currPoint = start2;
		while(queue.size()>0) {
			Collections.sort(queue);
			currPoint = queue.get(0);
			queue.remove(0);
			
			
			
			//W
			if (currPoint.y>0) {
				if (grid[currPoint.x][currPoint.y-1] <= maxFlyingHeight) {
					
					Point newPoint = new Point(currPoint.x, currPoint.y-1);
					int tempInt = grid[currPoint.x][currPoint.y-1] - grid[currPoint.x][currPoint.y];
					heightImpact = (tempInt>0) ? tempInt : 0;
					newPoint.cost = getcost(currPoint, newPoint, heightImpact);
					newPoint.parent = currPoint;
					
					if (pointArray[currPoint.x][currPoint.y-1] == null) {
						pointArray[currPoint.x][currPoint.y-1] = newPoint;
						queue.add(newPoint);
					}
					else {
						if (pointArray[currPoint.x][currPoint.y-1].cost> newPoint.cost) {
							if(queue.contains(pointArray[currPoint.x][currPoint.y-1])) {
								queue.remove(pointArray[currPoint.x][currPoint.y-1]);
								queue.add(newPoint);
							}
							pointArray[currPoint.x][currPoint.y-1] = newPoint;
						}
					}
				}
			}
			
			
			//E
			if (currPoint.y<width-1) {
				if (grid[currPoint.x][currPoint.y+1] <= maxFlyingHeight) {
					
					Point newPoint = new Point(currPoint.x, currPoint.y+1);
					int tempInt = grid[currPoint.x][currPoint.y + 1] - grid[currPoint.x][currPoint.y];
					heightImpact = (tempInt>0) ? tempInt : 0;
					newPoint.cost = getcost(currPoint, newPoint, heightImpact);
					newPoint.parent = currPoint;
					
					if (pointArray[currPoint.x][currPoint.y+1] == null) {
						pointArray[currPoint.x][currPoint.y+1] = newPoint;
						queue.add(newPoint);
					}
					else {
						if (pointArray[currPoint.x][currPoint.y+1].cost> newPoint.cost) {
							if(queue.contains(pointArray[currPoint.x][currPoint.y+1])) {
								queue.remove(pointArray[currPoint.x][currPoint.y+1]);
								queue.add(newPoint);
							}
							pointArray[currPoint.x][currPoint.y+1] = newPoint;
							
						}
					}
				}
			}

			
			//N
			if (currPoint.x>0) {
				if (grid[currPoint.x-1][currPoint.y] <= maxFlyingHeight) {
					
					Point newPoint = new Point(currPoint.x-1, currPoint.y);
					int tempInt = grid[currPoint.x-1][currPoint.y] - grid[currPoint.x][currPoint.y];
					heightImpact = (tempInt>0) ? tempInt : 0;
					newPoint.cost = getcost(currPoint, newPoint, heightImpact);
					newPoint.parent = currPoint;

					if (pointArray[currPoint.x-1][currPoint.y] == null) {
						pointArray[currPoint.x-1][currPoint.y] = newPoint;
						queue.add(newPoint);
					}
					else {
						if (pointArray[currPoint.x-1][currPoint.y].cost> newPoint.cost) {
							if(queue.contains(pointArray[currPoint.x-1][currPoint.y])) {
								queue.remove(pointArray[currPoint.x-1][currPoint.y]);
								queue.add(newPoint);
							}
							pointArray[currPoint.x-1][currPoint.y] = newPoint;
							
						}
					}
				}
			}
			
			
			//S
			if (currPoint.x<height-1) {
				if (grid[currPoint.x+1][currPoint.y] <= maxFlyingHeight) {
					
					Point newPoint = new Point(currPoint.x+1, currPoint.y);
					int tempInt = grid[currPoint.x+1][currPoint.y] - grid[currPoint.x][currPoint.y];
					heightImpact = (tempInt>0) ? tempInt : 0;
					newPoint.cost = getcost(currPoint, newPoint, heightImpact);
					newPoint.parent = currPoint;
					
					if (pointArray[currPoint.x+1][currPoint.y] == null) {
						pointArray[currPoint.x+1][currPoint.y] = newPoint;
						queue.add(newPoint);
					}
					else {
						if (pointArray[currPoint.x+1][currPoint.y].cost> newPoint.cost) {
							if(queue.contains(pointArray[currPoint.x+1][currPoint.y])) {
								queue.remove(pointArray[currPoint.x+1][currPoint.y]);
								queue.add(newPoint);
							}
							pointArray[currPoint.x+1][currPoint.y] = newPoint;
							
						}
					}

				}
			}

			
			//SW
			if (currPoint.x<height-1 && currPoint.y>0) {
				if (grid[currPoint.x+1][currPoint.y-1] <= maxFlyingHeight) {
					
					Point newPoint = new Point(currPoint.x+1, currPoint.y-1);
					int tempInt = grid[currPoint.x+1][currPoint.y-1] - grid[currPoint.x][currPoint.y];
					heightImpact = (tempInt>0) ? tempInt : 0;
					newPoint.cost = getcost(currPoint, newPoint, heightImpact);
					newPoint.parent = currPoint;
					
					if (pointArray[currPoint.x+1][currPoint.y-1] == null) {
						pointArray[currPoint.x+1][currPoint.y-1] = newPoint;
						queue.add(newPoint);
					}
					else {
						if (pointArray[currPoint.x+1][currPoint.y-1].cost> newPoint.cost) {
							if(queue.contains(pointArray[currPoint.x+1][currPoint.y-1])) {
								queue.remove(pointArray[currPoint.x+1][currPoint.y-1]);
								queue.add(newPoint);
							}
							pointArray[currPoint.x+1][currPoint.y-1] = newPoint;
						}
					}
				}
			}
			
			
			//NW
			if (currPoint.x>0 && currPoint.y>0) {
				if (grid[currPoint.x-1][currPoint.y-1] <= maxFlyingHeight) {
					
					Point newPoint = new Point(currPoint.x-1, currPoint.y-1);
					int tempInt = grid[currPoint.x-1][currPoint.y-1] - grid[currPoint.x][currPoint.y];
					heightImpact = (tempInt>0) ? tempInt : 0;
					newPoint.cost = getcost(currPoint, newPoint, heightImpact);
					newPoint.parent = currPoint;
					
					if (pointArray[currPoint.x-1][currPoint.y-1] == null) {
						pointArray[currPoint.x-1][currPoint.y-1] = newPoint;
						queue.add(newPoint);
					}
					else {
						if (pointArray[currPoint.x-1][currPoint.y-1].cost> newPoint.cost) {
							if(queue.contains(pointArray[currPoint.x-1][currPoint.y-1])) {
								queue.remove(pointArray[currPoint.x-1][currPoint.y-1]);
								queue.add(newPoint);
							}
							pointArray[currPoint.x-1][currPoint.y-1] = newPoint;
						}
					}
				}
			}
			
			
			
			
			
			//SE
			if (currPoint.x<height-1 && currPoint.y<width-1) {
				if (grid[currPoint.x+1][currPoint.y+1] <= maxFlyingHeight) {
					Point newPoint = new Point(currPoint.x+1, currPoint.y+1);
					int tempInt = grid[currPoint.x+1][currPoint.y+1] - grid[currPoint.x][currPoint.y];
					heightImpact = (tempInt>0) ? tempInt : 0;
					newPoint.cost = getcost(currPoint, newPoint, heightImpact);
					newPoint.parent = currPoint;
					
					if (pointArray[currPoint.x+1][currPoint.y+1] == null) {
						pointArray[currPoint.x+1][currPoint.y+1] = newPoint;
						queue.add(newPoint);
					}
					else {
						if (pointArray[currPoint.x+1][currPoint.y+1].cost> newPoint.cost) {
							if(queue.contains(pointArray[currPoint.x+1][currPoint.y+1])) {
								queue.remove(pointArray[currPoint.x+1][currPoint.y+1]);
								queue.add(newPoint);
							}
							pointArray[currPoint.x+1][currPoint.y+1] = newPoint;
						}
					}
				}
			}
			
			
			
			//NE
			if (currPoint.x>0 && currPoint.y<width-1) {
				if (grid[currPoint.x-1][currPoint.y+1] <= maxFlyingHeight) {
					
					Point newPoint = new Point(currPoint.x-1, currPoint.y+1);
					int tempInt = grid[currPoint.x-1][currPoint.y+1] - grid[currPoint.x][currPoint.y];
					heightImpact = (tempInt>0) ? tempInt : 0;
					newPoint.cost = getcost(currPoint, newPoint, heightImpact);
					newPoint.parent = currPoint;
					
					if (pointArray[currPoint.x-1][currPoint.y+1] == null) {
						pointArray[currPoint.x-1][currPoint.y+1] = newPoint;
						queue.add(newPoint);
					}
					else {
						if (pointArray[currPoint.x-1][currPoint.y+1].cost> newPoint.cost) {
							if(queue.contains(pointArray[currPoint.x-1][currPoint.y+1])) {
								queue.remove(pointArray[currPoint.x-1][currPoint.y+1]);
								queue.add(newPoint);
							}
							pointArray[currPoint.x-1][currPoint.y+1] = newPoint;
							
						}
					}
				}
			}
		}
		
		
		
		
		currPoint = pointArray[end2.x][end2.y];
		
		
		path.add(new Point(currPoint.y, currPoint.x,currPoint.cost,currPoint.parent));
		int count = 0;
        
		while(true) {
			count++;
			if (currPoint.parent == pointArray[start2.x][start2.y]) {
				path.add(new Point(currPoint.parent.y, currPoint.parent.x,currPoint.cost,currPoint.parent));
				Collections.reverse(path);
				return path;
			}
			currPoint = currPoint.parent;
			path.add(new Point(currPoint.y, currPoint.x,currPoint.cost,currPoint.parent));
			if (count == width*height) {
				return new ArrayList<>();
				
			}
		}
		
	}
		
	

	/**
	 * Calculate the most cost-efficient path from source to destination.
	 * @return the total cost of this most cost-efficient path when traveling from source to destination
	 */
	public double getMostEfficientPathCost(List<Point> path){
		double totalCost = 0.0;
		totalCost = path.get(path.size()-1).cost;

		return totalCost;
	}


	/**
	 * Draw the most cost-efficient path on top of the grayscale map from source to destination.
	 */
	public void drawMostEfficientPath(Graphics g, List<Point> path){
		Point temPoint;
		for (int i = 0; i < path.size(); i++) {
			temPoint= path.get(i);
			g.setColor(new Color(0, 255, 0));
			g.fillRect(temPoint.x, temPoint.y, 1, 1);
		}
	}

	/**
	 * Find an escape path from source towards East such that it has the lowest elevation change.
	 * Choose a forward step out of 3 possible forward locations, using greedy method described in the assignment instructions.
	 * @return the list of Points on the path
	 */
	public List<Point> getLowestElevationEscapePath(Point start){
		List<Point> pathPointsList = new ArrayList<>();

		Point currPoint = start;
		Point nextPoint;
		pathPointsList.add(currPoint);
		int change;
		int northEastchange;
		int southEastchange;
		while (currPoint.x < width-1) {
			change =Math.abs(grid[currPoint.y][currPoint.x]-grid[currPoint.y][currPoint.x+1]) ;
			nextPoint = new Point(currPoint.x+1, currPoint.y);
			
			if (currPoint.y>0) {
				northEastchange = Math.abs(grid[currPoint.y-1][currPoint.x+1]-grid[currPoint.y][currPoint.x]) ;
				if (northEastchange<change) {
					change = northEastchange;
					nextPoint = new Point(currPoint.x+1, currPoint.y-1);
				}
			}
			if (currPoint.y < height-1) {
				southEastchange = Math.abs(grid[currPoint.y+1][currPoint.x+1]-grid[currPoint.y][currPoint.x]) ;
				if (southEastchange<change) {
					change = southEastchange;
					nextPoint = new Point(currPoint.x+1, currPoint.y+1);
				}
			}
			nextPoint.totalchange += currPoint.totalchange+change;
			pathPointsList.add(nextPoint);
			currPoint = nextPoint;
			
		}
		
		

		return pathPointsList;
	}


	/**
	 * Calculate the escape path from source towards East such that it has the lowest elevation change.
	 * @return the total change in elevation for the entire path
	 */
	public int getLowestElevationEscapePathCost(List<Point> pathPointsList){
		int totalChange = 0;

		totalChange = pathPointsList.get(pathPointsList.size()-1).totalchange;

		return totalChange;
	}


	/**
	 * Draw the escape path from source towards East on top of the grayscale map such that it has the lowest elevation change.
	 */
	public void drawLowestElevationEscapePath(Graphics g, List<Point> pathPointsList){
		Point temPoint;
		for (int i = 0; i < pathPointsList.size(); i++) {
			temPoint= pathPointsList.get(i);
			g.setColor(new Color(255, 255, 0));
			g.fillRect(temPoint.x, temPoint.y, 1, 1);
		}
	}


	public double getcost(Point p1,Point p2,int heightImpact) {
		double cost = 0.0;
		double diff = 0.0;
		diff = Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y-p2.y, 2));
		cost = p1.cost + (diff * fuelCostPerUnit) + (climbingCostPerUnit * heightImpact);
		return cost;
	}
	
	public void getOutput() {
		  
		
		try {
			FileWriter wr = new FileWriter("grayscaleMap.dat");

			for (int i = 0; i < grid.length; i++){
				for (int j = 0; j < grid[0].length; j++) {
					int value = (grid[i][j] - minvalue)*256/(maxValue-minvalue+1);
					wr.write(value+" ");
				  }
				wr.write("\n");
			}
			wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
}
