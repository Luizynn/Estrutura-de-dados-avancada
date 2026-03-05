import javax.swing.*;
import java.awt.*;

public class TreePanel extends JPanel {
    private ArvoreBinaria.Node rootNode;

    public TreePanel(ArvoreBinaria.Node rootNode) {
        this.rootNode = rootNode;
        setBackground(Color.WHITE);
    }

    public void setRootNode(ArvoreBinaria.Node rootNode) {
        this.rootNode = rootNode;
        repaint();
    }

    // Método auxiliar para calcular a altura (profundidade) da árvore
    private int calcularAltura(ArvoreBinaria.Node node) {
        if (node == null) return 0;
        return 1 + Math.max(calcularAltura(node.left), calcularAltura(node.right));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (rootNode == null) {
            g.drawString("A árvore está vazia.", 20, 30);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        // Anti-aliasing para manter a qualidade dos círculos, linhas e textos perfeitos
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int altura = calcularAltura(rootNode);

        // --- MATEMÁTICA DO ESPAÇAMENTO DINÂMICO (ARESTAS) ---
        // Em vez de usar escala global, manipulamos o espaço disponível para as linhas.

        // O espaço horizontal inicial (para os filhos da raiz) é exatamente 1/4 da largura total do painel.
        // A cada nível recursivo, esse espaço é dividido por 2. Matematicamente, isso garante
        // que os nós nunca ultrapassem as bordas laterais da janela.
        int xOffset = getWidth() / 4;

        // O espaço vertical divide a altura total da tela pelo número de níveis (altura) da árvore.
        // Subtraímos 80 pixels do total para garantir uma margem no topo e na base.
        int yOffset = 80; // Valor padrão inicial
        if (altura > 1) {
            yOffset = (getHeight() - 80) / (altura - 1);
            // Garantimos um espaçamento mínimo de 35 pixels para que um nível não engula o outro.
            yOffset = Math.max(35, yOffset);
        }

        // Ponto inicial da raiz (Centro no eixo X, Margem no eixo Y)
        int startX = getWidth() / 2;
        int startY = 40;

        // Chama o método recursivo. Note que removemos o affine transform (escala).
        drawTree(g2d, startX, startY, rootNode, xOffset, yOffset);
    }

    private void drawTree(Graphics2D g, int x, int y, ArvoreBinaria.Node node, int xOffset, int yOffset) {
        if (node == null) return;

        g.setColor(Color.BLACK);

        // Desenhamos as linhas (arestas) PRIMEIRO para que fiquem "atrás" dos nós
        if (node.left != null) {
            g.drawLine(x, y, x - xOffset, y + yOffset);
            drawTree(g, x - xOffset, y + yOffset, node.left, xOffset / 2, yOffset);
        }
        if (node.right != null) {
            g.drawLine(x, y, x + xOffset, y + yOffset);
            drawTree(g, x + xOffset, y + yOffset, node.right, xOffset / 2, yOffset);
        }

        // O raio agora é intocável! Os nós nunca diminuirão de tamanho.
        int raio = 15;

        // Fundo do nó
        g.setColor(new Color(173, 216, 230));
        g.fillOval(x - raio, y - raio, 2 * raio, 2 * raio);

        // Borda do nó
        g.setColor(Color.BLACK);
        g.drawOval(x - raio, y - raio, 2 * raio, 2 * raio);

        // Texto do nó centralizado
        FontMetrics fm = g.getFontMetrics();
        String valorString = String.valueOf(node.value);
        int textWidth = fm.stringWidth(valorString);
        int textHeight = fm.getAscent();

        g.setColor(Color.BLACK);
        g.drawString(valorString, x - (textWidth / 2), y + (textHeight / 4));
    }
}