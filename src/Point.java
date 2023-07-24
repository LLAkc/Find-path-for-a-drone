import java.util.ArrayList;
import java.util.List;

public class Point implements Comparable<Point> {
    public int x;
    public int y;
    public int totalchange = 0;
    public double cost = 0.0;
    public Point parent;
    static int[][] loc = new int[3][3];
    

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Point(int x, int y,double cost,Point parent) {
    	this.x = x;
        this.y = y;
        this.cost = cost;
        this.parent = parent;
	}

    public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

	@Override
	public int compareTo(Point o) {
		if (this.cost>o.cost) {
			return 1;
		}
		else if (this.cost<o.cost) {
			return -1;
		}
		else {
			return compareLoc(this, o);
		}
	}

    public int compareLoc(Point p1,Point p2) {
    	Point add1= new Point(p1.x-p1.parent.x, p1.y-p1.parent.y);
    	Point add2= new Point(p2.x-p2.parent.x, p2.y-p2.parent.y);
    			
    	
    	return Integer.compare(loc[1+add2.x][1+add2.y], loc[1+add1.x][1+add1.y]);
    }
    
    public static void filloc() {
    	loc[0][0] = 4;
    	loc[0][1] = 7;
    	loc[0][2] = 2;
    	loc[1][0] = 9;
    	loc[1][1] = 1;
    	loc[1][2] = 8;
    	loc[2][0] = 5;
    	loc[2][1] = 6;
    	loc[2][2] = 3;
	}
}
