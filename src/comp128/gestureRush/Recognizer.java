package comp128.gestureRush;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/**
 * Recognizer to recognize 2D gestures. Uses the $1 gesture recognition algorithm.
 */
public class Recognizer {

    private final List<Template> templateArray = new ArrayList<>();
    private static final int TEMPLATENUM = 64;
    private static final double TEMPLATE_SIZE = 250.0;
    private static final double TEMPLATE_DIAGONAL = 0.5 * Math.sqrt(2) * TEMPLATE_SIZE;



    /**
     * Constructs a recognizer object
     */
    public Recognizer(){
    }


    //public double RecognizeGesture(Object x){}
   public Deque<Point> gestureNormalise(Deque<Point> points){
    Deque<Point> a = resample(points, TEMPLATENUM);        
    Deque<Point> b = rotateBy(a, -indicativeAngle(a));      
    Deque<Point> c = scaleTo(b, TEMPLATE_SIZE);            
    Deque<Point> d = translateTo(c, new Point(0, 0));        
    return d;
}


    /**
     * Uses a golden section search to calculate rotation that minimizes the distance between the gesture and the template points.
     * @param points
     * @param templatePoints
     * @return best distance
     */
    private double distanceAtBestAngle(Deque<Point> points, Deque<Point> templatePoints){
        double thetaA = -Math.toRadians(45);
        double thetaB = Math.toRadians(45);
        final double deltaTheta = Math.toRadians(2);
        double phi = 0.5*(-1.0 + Math.sqrt(5.0));// golden ratio
        double x1 = phi*thetaA + (1-phi)*thetaB;
        double f1 = distanceAtAngle(points, templatePoints, x1);
        double x2 = (1 - phi)*thetaA + phi*thetaB;
        double f2 = distanceAtAngle(points, templatePoints, x2);
        while(Math.abs(thetaB-thetaA) > deltaTheta){
            if (f1 < f2){
                thetaB = x2;
                x2 = x1;
                f2 = f1;
                x1 = phi*thetaA + (1-phi)*thetaB;
                f1 = distanceAtAngle(points, templatePoints, x1);
            }
            else{
                thetaA = x1;
                x1 = x2;
                f1 = f2;
                x2 = (1-phi)*thetaA + phi*thetaB;
                f2 = distanceAtAngle(points, templatePoints, x2);
            }
        }
        return Math.min(f1, f2);
    }

    public double indicativeAngle(Deque<Point >point){
        Point centroid = getCentroid(point);
        Point firstVal = point.peekFirst();
        double angle = Math.atan2(firstVal.getY()-centroid.getY(), firstVal.getX()-centroid.getX());
        return angle;
    }

    public Deque<Point> translateTo(Deque<Point> points, Point i){
        Point centroid = getCentroid(points);
        double x = i.getX() - centroid.getX();
        double y = i.getY() - centroid.getY();
        ArrayDeque<Point> tempArray = new ArrayDeque<>(points.size());
        for (Point p : points){
        tempArray.addLast(new Point(p.getX() + x, p.getY() + y));
    }
        return tempArray;
    }

    public Deque<Point> scaleTo(Deque<Point> points, double size){
        double maxValx = Double.NEGATIVE_INFINITY;
        double maxValy = Double.NEGATIVE_INFINITY;
        double minValx = Double.MAX_VALUE;
        double minValy = Double.MAX_VALUE; 
        for (Point p:points){
            if (p.getX() > maxValx){
                maxValx = p.getX();
            }
            if (p.getY() > maxValy){
                maxValy = p.getY();
            }
            if (p.getX() < minValx){
                minValx = p.getX();
            }
            if (p.getY() < minValy){
                minValy = p.getY();
            }
        }
        double width = maxValx - minValx;
        double height = maxValy - minValx;
        ArrayDeque<Point> tempArray = new ArrayDeque<>(points.size());
        for (Point p : points){
        tempArray.addLast(new Point(p.getX() * (size/width), p.getY() * (size/height)));}
        return tempArray;
    }
    
    private double distanceAtAngle(Deque<Point> points, Deque<Point> templatePoints, double theta){
        //TODO: Uncomment after rotate method is implemented
        Deque<Point> rotatedPoints = null;
        rotatedPoints = rotateBy(points, theta);
        return pathDistance(rotatedPoints, templatePoints);
    }

    public Deque<Point> rotateBy(Deque<Point> points, double angle){
       Point centroid = getCentroid(points);
       ArrayDeque<Point> tempArray = new ArrayDeque<>(points.size());
       for (Point pTwo : points){
        tempArray.addLast(pTwo.rotate(angle, centroid));
     }
       return tempArray;
}

    private Point getCentroid(Deque<Point> point){
        double x = 0;
        double y = 0;
        int n = 0;
        for (Point i:point){
            x += i.getX();
            y += i.getY();
            n ++;
        }
        return new Point(x/n,y/n);
    }

    public double pathLength(Deque<Point> points){
    if (points == null){
        return 0;
    }
    if (points.size() < 2) {
        return 0;
    }
    Iterator<Point> iteratedPoint = points.iterator();
    Point previosPoint = iteratedPoint.next();
    double totalLength = 0.0;
    while (iteratedPoint.hasNext()){
        Point x = iteratedPoint.next();
        totalLength += previosPoint.distance(x);
        previosPoint = x;
    }
    return totalLength;
}

    public double pathDistance(Deque<Point> a, Deque<Point> b){
        Iterator<Point> aTwo = a.iterator();
        Iterator<Point> bTwo = b.iterator();
        double sum = 0.0;
        double n = 0;
        while (aTwo.hasNext() && bTwo.hasNext()){
            sum += aTwo.next().distance(bTwo.next());
            n++;
            

        }
        return sum/n;
    }

    public Deque<Point> resample(Deque<Point> points, int n){
        ArrayList<Point> originalArray = new ArrayList<>(points);
        ArrayList<Point> newArray = new ArrayList<>(n);

        if (originalArray.isEmpty()){
            return new ArrayDeque<>();
        }

        newArray.add(originalArray.get(0));
        double total = 0;
        for (int x = 1; x < originalArray.size(); x++){
           total += originalArray.get(x - 1).distance(originalArray.get(x));
        }
        
        if (total == 0){
            while (originalArray.size() < n){
                 newArray.add(originalArray.get(0));
                 return new ArrayDeque<>(originalArray);
        }
        double interval = total / (n - 1.0);
        double D = 0.0;
        int i = 1;
        while (i < originalArray.size()){
        Point pointOne = originalArray.get(i - 1);
        Point pointTwo = originalArray.get(i);
        double pointThree = pointOne.distance(pointTwo);
        if (pointThree == 0.0){
            i++;
        }
        if (D + pointThree >= interval){
            double alpha = (interval - D) / pointThree;
            Point q = Point.interpolate(pointOne, pointTwo, alpha);
            newArray.add(q);
            originalArray.add(i, q);
            D = 0.0;
            i++;
        } else 
        {
            D += pointThree;
            i++;
        }
    }
        if (newArray.size() < n){
             newArray.add(originalArray.get(originalArray.size() - 1));
            return new ArrayDeque<>(originalArray);
        }
       
        }
     return new ArrayDeque<>(originalArray);
    }
}