import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;


public class Drawing extends JPanel implements MouseListener, MouseMotionListener {
    private Point muis = new Point();
    private static int HANDLE_SIZE = 8;
    Shape theCircle = new Ellipse2D.Double(10, 10, 10, 10);

    // True if the user pressed, dragged or released the mouse outside of the rectangle; false otherwise.
    boolean pressOut = false;

    private List<Bezier> segments = new ArrayList<>();

    // Holds the coordinates of the user's last mousePressed event.
    private int last_x, last_y;
    private Handle selectedHandle;
    private Bezier selectedBezier;

    public Drawing() {
        System.out.println("Constructor");
        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        Point2D.Double p1 = new Point2D.Double(10,10);
        Point2D.Double cp1 = new Point2D.Double(80,10);
        Point2D.Double cp2 = new Point2D.Double(50,100);
        Point2D.Double p2 = new Point2D.Double(20,50);

        segments.add(new Bezier(p1, cp1, cp2, p2));

        p1 = new Point2D.Double(110,110);
        cp1 = new Point2D.Double(180,110);
        cp2 = new Point2D.Double(150,200);
        p2 = new Point2D.Double(120,150);
        segments.add(new Bezier(p1, cp1, cp2, p2));
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        for (Bezier b : segments) {
            g.setColor(Color.red);

            for (Handle h : b.getHandles()) {
                if (h.contains(muis)) {
                    g2.fill(h);
                } else {
                    g2.draw(h);
                }
            }
            float dash[] = {2.0f};

            Stroke previous = g2.getStroke();
            g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));

            for (Shape s : b.getHelpLines()) {
                g2.draw(s);
            }
            g2.setStroke(previous);

            if (b.isOnCurve(muis)) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.GREEN);
            }
            g2.draw(b);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        System.out.println("Pressed");
        
        for(Bezier b : segments) {

            if (b.isOnCurve(mouseEvent.getPoint())) {
                System.out.println("Move whole curve");
                last_x = (int) (b.getBounds().x - mouseEvent.getX());
                last_y = (int) (b.getBounds().y - mouseEvent.getY());
                pressOut = false;
                selectedBezier = b;
                updateLocation(mouseEvent, b);
                return;
            } else {
                pressOut = true;
            }

            for (Handle h : b.getHandles()) {
                if (h.contains(mouseEvent.getX(), mouseEvent.getY())) {
                    last_x = (int) (h.x - mouseEvent.getX());
                    last_y = (int) (h.y - mouseEvent.getY());
                    pressOut = false;
                    selectedHandle = h;
                    updateLocation(mouseEvent, h);
                    return;
                } else {
                    pressOut = true;
                }
            }
        }
    }

    private void updateLocation(MouseEvent mouseEvent, Handle h) {
        h.x = last_x + mouseEvent.getX();
        h.y = last_y + mouseEvent.getY();
        h.getBezier().update();

        repaint();
    }

    private void updateLocation(MouseEvent mouseEvent, Bezier b) {
        b.move(last_x + mouseEvent.getX(), last_y + mouseEvent.getY());

        b.update();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (selectedHandle != null) {
            if (selectedHandle.contains(mouseEvent.getX(), mouseEvent.getY())) {
                if (!pressOut) {
                    updateLocation(mouseEvent, selectedHandle);
                }
            }
        }
        selectedHandle = null;
        selectedBezier = null;
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (!pressOut) {
            if (selectedBezier != null) {
                updateLocation(mouseEvent, selectedBezier);
            } else if (selectedHandle != null) {
                updateLocation(mouseEvent, selectedHandle);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        muis = mouseEvent.getPoint();
        repaint();
    }
}
