import javax.swing.*;
import java.awt.*;

public class TreePanel extends JPanel {
    private ArvoreBinaria.Node rootNode;

    public TreePanel(ArvoreBinaria.Node rootNode) {
        this.rootNode = rootNode;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (rootNode != null) {
            drawTree(g, getWidth() / 2, 40, rootNode, getWidth() / 4, 60);
        } else {
            g.drawString("A árvore está vazia.", 20, 30);
        }
    }

    private void drawTree(Graphics g, int x, int y, ArvoreBinaria.Node node, int xOffset, int yOffset) {
        if (node == null) return;

        g.setColor(Color.BLACK);
        if (node.left != null) {
            g.drawLine(x, y, x - xOffset, y + yOffset);
            drawTree(g, x - xOffset, y + yOffset, node.left, xOffset / 2, yOffset);
        }
        if (node.right != null) {
            g.drawLine(x, y, x + xOffset, y + yOffset);
            drawTree(g, x + xOffset, y + yOffset, node.right, xOffset / 2, yOffset);
        }

        int raio = 15;
        g.setColor(new Color(173, 216, 230));
        g.fillOval(x - raio, y - raio, 2 * raio, 2 * raio);
        g.setColor(Color.BLACK);
        g.drawOval(x - raio, y - raio, 2 * raio, 2 * raio);

        FontMetrics fm = g.getFontMetrics();
        String valorString = String.valueOf(node.value);
        int textWidth = fm.stringWidth(valorString);
        int textHeight = fm.getAscent();

        g.setColor(Color.BLACK);
        g.drawString(valorString, x - (textWidth / 2), y + (textHeight / 4));
    }
}