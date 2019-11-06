import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Handle extends Ellipse2D.Double {
    public static final int HANDLE_SIZE = 6;
    private Bezier bezier;

    public Handle(Bezier b, double x, double y) {
        bezier = b;
        this.x = x;
        this.y = y;
        this.height = HANDLE_SIZE;
        this.width = HANDLE_SIZE;
    }

    public Bezier getBezier() {
        return bezier;
    }

    public Point2D getCenter() {
        return new Point2D.Double(x+HANDLE_SIZE/2, y + HANDLE_SIZE/2);
    }
}
