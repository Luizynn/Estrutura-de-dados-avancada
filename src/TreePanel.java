import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TreePanel extends JPanel {

    private static final int NO_LARGURA    = 36;
    private static final int NO_ALTURA     = 36;
    private static final int ESPACO_H      = 20;
    private static final int ESPACO_V      = 60;

    private static final String STYLE_BASE =
            "shape=ellipse;"        +
                    "strokeColor=#2E86C1;"  +
                    "fontColor=#1A5276;"    +
                    "fontStyle=1;"          +
                    "fontSize=12;"          +
                    "verticalAlign=middle;" +
                    "align=center;";

    private static final String STYLE_ROOT     = STYLE_BASE + "fillColor=#F1C40F;";
    private static final String STYLE_INTERNAL = STYLE_BASE + "fillColor=#AED6F1;";
    private static final String STYLE_LEAF     = STYLE_BASE + "fillColor=#ABEBC6;";

    private static final String STYLE_ARESTA =
            "strokeColor=#555555;"  +
                    "strokeWidth=1.5;"      +
                    "endArrow=none;";

    private mxGraphComponent graphComponent;
    private final Map<ArvoreBinaria.Node, int[]> coordenadas = new HashMap<>();

    public TreePanel(ArvoreBinaria.Node rootNode) {
        setLayout(new BorderLayout());
        construirGrafo(rootNode);
    }

    public void setRootNode(ArvoreBinaria.Node rootNode) {
        if (graphComponent != null) remove(graphComponent);
        construirGrafo(rootNode);
        revalidate();
        repaint();
    }

    private void construirGrafo(ArvoreBinaria.Node rootNode) {
        mxGraph graph = new mxGraph();
        graph.setEnabled(false);

        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            if (rootNode == null) {
                graph.insertVertex(parent, null, "Árvore vazia", 10, 10, 120, NO_ALTURA, STYLE_INTERNAL);
            } else {
                coordenadas.clear();
                int[] xCounter = {0};
                calcularPosicoes(rootNode, xCounter);
                Map<ArvoreBinaria.Node, Object> mapaVertices = new HashMap<>();
                inserirVertices(graph, parent, rootNode, rootNode, 0, mapaVertices);
                inserirArestas(graph, parent, rootNode, mapaVertices);
            }
        } finally {
            graph.getModel().endUpdate();
        }

        graphComponent = new mxGraphComponent(graph);
        graphComponent.setBackground(Color.WHITE);
        graphComponent.getViewport().setBackground(Color.WHITE);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(criarLegenda());
        southPanel.add(criarBarraZoom());

        add(graphComponent, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void calcularPosicoes(ArvoreBinaria.Node node, int[] xCounter) {
        if (node == null) return;
        calcularPosicoes(node.left,  xCounter);
        coordenadas.put(node, new int[]{xCounter[0]});
        xCounter[0]++;
        calcularPosicoes(node.right, xCounter);
    }

    private void inserirVertices(mxGraph graph, Object parent,
                                 ArvoreBinaria.Node node, ArvoreBinaria.Node root, int nivel,
                                 Map<ArvoreBinaria.Node, Object> mapaVertices) {
        if (node == null) return;

        String style;
        if (node == root) style = STYLE_ROOT;
        else if (node.left == null && node.right == null) style = STYLE_LEAF;
        else style = STYLE_INTERNAL;

        int[] coord = coordenadas.get(node);
        int px = coord[0] * (NO_LARGURA + ESPACO_H);
        int py = nivel    * (NO_ALTURA  + ESPACO_V);

        Object vertice = graph.insertVertex(parent, null, String.valueOf(node.value), px, py, NO_LARGURA, NO_ALTURA, style);
        mapaVertices.put(node, vertice);

        inserirVertices(graph, parent, node.left,  root, nivel + 1, mapaVertices);
        inserirVertices(graph, parent, node.right, root, nivel + 1, mapaVertices);
    }

    private void inserirArestas(mxGraph graph, Object parent, ArvoreBinaria.Node node, Map<ArvoreBinaria.Node, Object> mapaVertices) {
        if (node == null) return;
        if (node.left != null) {
            graph.insertEdge(parent, null, "", mapaVertices.get(node), mapaVertices.get(node.left), STYLE_ARESTA);
            inserirArestas(graph, parent, node.left, mapaVertices);
        }
        if (node.right != null) {
            graph.insertEdge(parent, null, "", mapaVertices.get(node), mapaVertices.get(node.right), STYLE_ARESTA);
            inserirArestas(graph, parent, node.right, mapaVertices);
        }
    }

    private JPanel criarLegenda() {
        JPanel legenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        legenda.setBackground(Color.WHITE);
        legenda.add(criarItemLegenda("Raiz", new Color(0xF1, 0xC4, 0x0F)));
        legenda.add(criarItemLegenda("Interno", new Color(0xAE, 0xD6, 0xF1)));
        legenda.add(criarItemLegenda("Folha", new Color(0xAB, 0xEB, 0xC6)));
        return legenda;
    }

    private JPanel criarItemLegenda(String texto, Color cor) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setBackground(Color.WHITE);
        JPanel box = new JPanel();
        box.setPreferredSize(new Dimension(12, 12));
        box.setBackground(cor);
        box.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        item.add(box);
        item.add(new JLabel(texto));
        return item;
    }

    private JPanel criarBarraZoom() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnIn = new JButton("+");
        JButton btnOut = new JButton("-");
        btnIn.addActionListener(e -> graphComponent.zoomIn());
        btnOut.addActionListener(e -> graphComponent.zoomOut());
        barra.add(new JLabel("Zoom: "));
        barra.add(btnOut);
        barra.add(btnIn);
        return barra;
    }
}