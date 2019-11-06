import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Bezier extends CubicCurve2D.Double {
    private Bezier previous;
    private Bezier next;

    private Handle p1, cp1, cp2, p2;

    public void move(int x, int y) {
        int currentX = getBounds().x;
        int currentY = getBounds().y;

        int displacementX = x - currentX;
        int displacementY = y - currentY;

        p1.x = p1.x + displacementX;
        p1.y = p1.y + displacementY;
        cp1.x = cp1.x + displacementX;
        cp1.y = cp1.y + displacementY;
        cp2.x = cp2.x + displacementX;
        cp2.y = cp2.y + displacementY;
        p2.x = p2.x + displacementX;
        p2.y = p2.y + displacementY;
    }

    Line2D line1, line2;

    public Bezier(Point2D p1, Point2D cp1, Point2D cp2, Point2D p2) {
        super(p1.getX(), p1.getY(), cp1.getX(), cp1.getY(), cp2.getX(), cp2.getY(), p2.getX(), p2.getY());
        this.p1 = new Handle(this,(int)p1.getX()-Handle.HANDLE_SIZE/2, (int)p1.getY()-Handle.HANDLE_SIZE/2);
        this.cp1 = new Handle(this,(int)cp1.getX()-Handle.HANDLE_SIZE/2, (int)cp1.getY()-Handle.HANDLE_SIZE/2);
        this.cp2 = new Handle(this,(int)cp2.getX()-Handle.HANDLE_SIZE/2, (int)cp2.getY()-Handle.HANDLE_SIZE/2);
        this.p2 = new Handle(this,(int)p2.getX()-Handle.HANDLE_SIZE/2, (int)p2.getY()-Handle.HANDLE_SIZE/2);
        this.line1 = new Line2D.Double(p1, cp1);
        this.line2 = new Line2D.Double(cp2, p2);
    }

    public List<Handle> getHandles() {
        List<Handle> handles = new ArrayList<>();
        handles.add(p1);
        handles.add(p2);
        handles.add(cp1);
        handles.add(cp2);
        return handles;
    }

    public List<Line2D> getHelpLines() {
        List<Line2D> lines = new ArrayList<>();
        lines.add(line1);
        lines.add(line2);
        return lines;
    }

    public boolean isOnCurve(Point2D point) {
        double detail = 0.001;
        double TRESHOLD = 1f;

        for ( double t = 0; t < 1; t += detail) {
            double x = Math.pow(1-t, 3) * p1.getX() + 3* Math.pow(1-t, 2) * t * cp1.getX() + 3*(1-t) * Math.pow(t, 2) * cp2.getX() + Math.pow(t, 3) * p2.getX();
            double y = Math.pow(1-t, 3) * p1.getY() + 3* Math.pow(1-t, 2) * t * cp1.getY() + 3*(1-t) * Math.pow(t, 2) * cp2.getY() + Math.pow(t, 3) * p2.getY();

            if (Math.abs(x - point.getX())<TRESHOLD && Math.abs(y - point.getY())<TRESHOLD){
                return true;
            }
        }

        return false;
    }

    public void update() {
        setCurve(p1.getCenter(), cp1.getCenter(), cp2.getCenter(), p2.getCenter());
        line1.setLine(p1.getCenter(), cp1.getCenter());
        line2.setLine(cp2.getCenter(), p2.getCenter());
    }
}
