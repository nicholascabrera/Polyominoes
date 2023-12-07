import java.awt.Point;
import java.util.ArrayList;

public class Polyomino {
    private ArrayList<ArrayList<Point>> elements;
    private int[][] magnitudeMap;
    //private int maxOrder;

    public Polyomino(int[][] magnitudeMap, int maxOrder){
        this.elements = new ArrayList<ArrayList<Point>>();
        this.magnitudeMap = magnitudeMap;
    }

    public ArrayList<ArrayList<Point>> getElements() {
        return elements;
    }

    public int[][] getMagnitudeMap() {
        return magnitudeMap;
    }

    public void addToPolyomino(Point currentPoint, Point adjacentPoint){
        // System.out.println("Current Point: (" + (int)currentPoint.getX() + "," + (int)currentPoint.getY()+")");
        // System.out.println("Adjacent Point: (" + (int)adjacentPoint.getX() + "," + (int)adjacentPoint.getY()+")");
        if(!this.isElement(currentPoint) && !this.isElement(adjacentPoint)){
            // case 1: neither current or adjacent are contained in a list
            // add them both to a new list
            // update map
            this.addElements(currentPoint, adjacentPoint);  //this adds both and updates the map
        } else if(this.isElement(currentPoint) && this.isElement(adjacentPoint)){
            // case 2: current and adjacent point are both in the same list
            // do nothing.
            // case 3: current and adjacent point are in a different list
            // add all the elements of adjacent points list to the current points list
            // delete the adjacent points list
            // update map
            ArrayList<Point> currentPointList = new ArrayList<Point>(0);
            ArrayList<Point> adjacentPointList = new ArrayList<Point>(0);
            ArrayList<ArrayList<Point>> temporaryList = this.elements;
            
            for(ArrayList<Point> points : temporaryList){
                // case 2: current and adjacent point are both in the same list
                if(points.contains(currentPoint) && points.contains(adjacentPoint)){
                    // do nothing
                    return;
                } else if (points.contains(currentPoint) && !points.contains(adjacentPoint)){ // case 3: current and adjacent point are in a different list
                    currentPointList = points;
                } else if(!points.contains(currentPoint) && points.contains(adjacentPoint)){
                    adjacentPointList = points;
                }
            }
            
            if(currentPointList.size() > 0 && adjacentPointList.size() > 0){         //both lists have been found and populated
                this.elements.remove(currentPointList);
                this.elements.remove(adjacentPointList);

                // add all the elements of adjacent points list to the current points list
                currentPointList.addAll(adjacentPointList);

                // delete the adjacent points list
                this.elements.add(currentPointList);
            }
            // update map
            for(Point point : currentPointList){
                this.magnitudeMap[(int) point.getX()][(int) point.getY()] = currentPointList.size();
            }
        } else if(this.isElement(currentPoint) && !this.isElement(adjacentPoint)){
            // case 4: current point is in a list, adjacent point is not
            // add adjacent point to current point's list
            // update map
            for(ArrayList<Point> points : this.elements){
                if(points.contains(currentPoint)){
                    points.add(adjacentPoint);  // add adjacent point to current point's list
                    for(Point point : points){  // update map
                        this.magnitudeMap[(int)point.getX()][(int)point.getY()] = points.size();
                    }
                    return;
                }
            }
        } else if(!this.isElement(currentPoint) && this.isElement(adjacentPoint)){
            // case 5: current point is not in a list, and adjacent point is 
            // add current point to adjacent point's list
            // update map
            for(ArrayList<Point> points : this.elements){
                if(points.contains(adjacentPoint)){
                    points.add(currentPoint);  // add adjacent point to current point's list
                    for(Point point : points){  // update map
                        this.magnitudeMap[(int)point.getX()][(int)point.getY()] = points.size();
                    }
                    return;
                }
            }
        }
    }

    public void addElements(Point point1, Point point2){
        ArrayList<Point> points = new ArrayList<>();
        points.add(point1);
        points.add(point2);
        this.elements.add(points);
        for(Point point : points){
            this.magnitudeMap[(int)point.getX()][(int)point.getY()] = points.size();
        }
    }

    public void addElement(Point currentPoint){
        ArrayList<Point> points = new ArrayList<>();
        points.add(currentPoint);
        this.elements.add(points);
        this.magnitudeMap[(int)currentPoint.getX()][(int)currentPoint.getY()] = points.size();
    }

    public boolean isElement(Point currentPoint){
        for(ArrayList<Point> points : this.elements){
            if(points.contains(currentPoint)){
                return true;
            }
        }

        return false;
    }
}
