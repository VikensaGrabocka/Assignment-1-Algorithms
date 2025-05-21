import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Visualizer {


    public void drawNodes(int numNodes) {
        SwingUtilities.invokeLater(() -> createFrame("One possible cheapest solution", new GraphPanel(numNodes, null)));
    }


    public void drawTreeStructure(int[] parentArray) {

        SwingUtilities.invokeLater(() -> createFrame("One possible cheapest solution",
                new GraphPanel(parentArray.length - 1, parentArray)));
    }


    private void createFrame(String title, GraphPanel panel) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }


    static class GraphPanel extends JPanel {
        private final int numNodes;
        private final int[] parentArray;
        private final Map<Integer, Point> nodePositions = new HashMap<>();

        public GraphPanel(int numNodes, int[] parentArray) {
            this.numNodes = numNodes;
            this.parentArray = parentArray;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(getWidth(), getHeight()) / 3;
            double angleStep = 2 * Math.PI / numNodes;


            for (int i = 1; i <= numNodes; i++) {
                double angle = (i - 1) * angleStep;
                int x = centerX + (int) (radius * Math.cos(angle));
                int y = centerY + (int) (radius * Math.sin(angle));
                nodePositions.put(i, new Point(x, y));
            }


            if (parentArray != null) {
                g2d.setStroke(new BasicStroke(2));
                for (int i = 1; i <= numNodes; i++) {
                    int parent = parentArray[i];
                    if (parent != i && parent >= 1 && parent <= numNodes) {
                        Point p1 = nodePositions.get(i);
                        Point p2 = nodePositions.get(parent);
                        g2d.setColor(Color.BLUE);
                        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }


            for (int i = 1; i <= numNodes; i++) {
                Point p = nodePositions.get(i);
                boolean isRoot = parentArray == null || parentArray[i] == i || parentArray[i] == 0;

                g2d.setColor(parentArray == null ? Color.GREEN : (isRoot ? Color.GREEN : Color.RED));
                int size = 20;
                g2d.fillOval(p.x - size / 2, p.y - size / 2, size, size);

                g2d.setColor(Color.BLACK);
                g2d.drawOval(p.x - size / 2, p.y - size / 2, size, size);
                g2d.drawString(String.valueOf(i), p.x - 5, p.y + 25);
            }
        }
    }
}
