import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ConcurrentNavigableMap;

public class FieldView extends JPanel {

    public static Controller controller;

    private static final int RECT_X = 0;
    private static final int RECT_Y = RECT_X;
    private static final int RECT_WIDTH = 100;
    private static final int RECT_HEIGHT = RECT_WIDTH;
    private Color currColor = Color.CYAN;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw the rectangle here
        g.setColor(currColor);
        g.fillRect(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);
    }

    @Override
    public Dimension getPreferredSize() {
        // so that our GUI is big enough
        return new Dimension(RECT_WIDTH + 2 * RECT_X, RECT_HEIGHT + 2 * RECT_Y);
    }

    public FieldView(){
        super();
        this.setBackground(Color.blue);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me){
                clickedOn();
                System.out.println(me.getPoint());
            }
        });
    }

    private void clickedOn(){
        controller.clickedOnField(this);
        currColor = Color.RED;
        repaint();
    }

    public void removeGlow(){
        currColor=Color.CYAN;
        repaint();
    }
}
