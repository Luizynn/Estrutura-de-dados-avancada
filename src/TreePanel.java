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

    private static final String STYLE_NO =
            "shape=ellipse;"        +
                    "fillColor=#AED6F1;"    +
                    "strokeColor=#2E86C1;"  +
                    "fontColor=#1A5276;"    +
                    "fontStyle=1;"          +
                    "fontSize=12;"          +
                    "verticalAlign=middle;" +
                    "align=center;";

    private static final String STYLE_ARESTA =
            "strokeColor=#555555;"  +
                    "strokeWidth=1.5;"      +
                    "endArrow=none;";

    private mxGraphComponent graphComponent;

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

    public void zoomIn() {
        if (graphComponent != null) {
            graphComponent.zoomIn();
            centralizarArvore();
        }
    }

    public void zoomOut() {
        if (graphComponent != null) {
            graphComponent.zoomOut();
            centralizarArvore();
        }
    }

    public void resetZoom() {
        if (graphComponent != null) {
            graphComponent.zoomTo(1.0, true);
            centralizarArvore();
        }
    }

    private void zoomSuave(double fator) {
        if (graphComponent == null) return;
        mxGraph g = graphComponent.getGraph();
        double novaEscala = g.getView().getScale() * fator;
        novaEscala = Math.max(0.2, Math.min(4.0, novaEscala));
        graphComponent.zoomTo(novaEscala, true);
        centralizarArvore();
    }

    private void construirGrafo(ArvoreBinaria.Node rootNode) {

        mxGraph graph = new mxGraph();
        graph.setAutoSizeCells(false);
        graph.setEnabled(false);
        configurarEstilos(graph);

        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            if (rootNode == null) {
                graph.insertVertex(parent, null, "Árvore vazia",
                        10, 10, 120, NO_ALTURA, STYLE_NO);
            } else {
                Map<ArvoreBinaria.Node, Object> mapaVertices = new HashMap<>();

                int[] xCounter = {0};
                calcularPosicoes(rootNode, xCounter);

                inserirVertices(graph, parent, rootNode, 0, mapaVertices);

                inserirArestas(graph, parent, rootNode, mapaVertices);
            }
        } finally {
            graph.getModel().endUpdate();
        }

        graphComponent = new mxGraphComponent(graph);
        graphComponent.setConnectable(false);
        graphComponent.setWheelScrollingEnabled(false);
        graphComponent.addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) zoomSuave(1.05);
            else                          zoomSuave(1.0 / 1.05);
        });

        graphComponent.setBackground(Color.WHITE);
        graphComponent.getViewport().setBackground(Color.WHITE);
        graphComponent.setBorder(null);

        final int[] dragInicio = {0, 0};
        final boolean[] arrastando = {false};

        graphComponent.getGraphControl().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                dragInicio[0] = e.getX();
                dragInicio[1] = e.getY();
                arrastando[0] = true;
                graphComponent.getGraphControl()
                        .setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                arrastando[0] = false;
                graphComponent.getGraphControl()
                        .setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });

        graphComponent.getGraphControl().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (!arrastando[0]) return;

                int dx = e.getX() - dragInicio[0];
                int dy = e.getY() - dragInicio[1];

                dragInicio[0] = e.getX();
                dragInicio[1] = e.getY();

                mxGraph g = graphComponent.getGraph();
                com.mxgraph.util.mxPoint t = g.getView().getTranslate();
                double escala = g.getView().getScale();

                g.getView().setTranslate(new com.mxgraph.util.mxPoint(
                        t.getX() + dx / escala,
                        t.getY() + dy / escala
                ));
                graphComponent.refresh();
            }
        });

        graphComponent.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                graphComponent.removeComponentListener(this);
                centralizarArvore();
            }
        });

        add(graphComponent, BorderLayout.CENTER);
        add(criarBarraZoom(), BorderLayout.SOUTH);
    }

    private final Map<ArvoreBinaria.Node, int[]> coordenadas = new HashMap<>();

    private void calcularPosicoes(ArvoreBinaria.Node node, int[] xCounter) {
        if (node == null) return;
        calcularPosicoes(node.left,  xCounter);
        coordenadas.put(node, new int[]{xCounter[0]});
        xCounter[0]++;
        calcularPosicoes(node.right, xCounter);
    }

    private void inserirVertices(mxGraph graph, Object parent,
                                 ArvoreBinaria.Node node, int nivel,
                                 Map<ArvoreBinaria.Node, Object> mapaVertices) {
        if (node == null) return;

        int[] coord = coordenadas.get(node);
        int px = coord[0] * (NO_LARGURA + ESPACO_H);
        int py = nivel    * (NO_ALTURA  + ESPACO_V);

        Object vertice = graph.insertVertex(
                parent, null,
                String.valueOf(node.value),
                px, py,
                NO_LARGURA, NO_ALTURA,
                STYLE_NO
        );
        mapaVertices.put(node, vertice);

        inserirVertices(graph, parent, node.left,  nivel + 1, mapaVertices);
        inserirVertices(graph, parent, node.right, nivel + 1, mapaVertices);
    }

    private void inserirArestas(mxGraph graph, Object parent,
                                ArvoreBinaria.Node node,
                                Map<ArvoreBinaria.Node, Object> mapaVertices) {
        if (node == null) return;

        if (node.left != null) {
            graph.insertEdge(parent, null, "",
                    mapaVertices.get(node), mapaVertices.get(node.left), STYLE_ARESTA);
            inserirArestas(graph, parent, node.left, mapaVertices);
        }
        if (node.right != null) {
            graph.insertEdge(parent, null, "",
                    mapaVertices.get(node), mapaVertices.get(node.right), STYLE_ARESTA);
            inserirArestas(graph, parent, node.right, mapaVertices);
        }
    }

    private void configurarEstilos(mxGraph graph) {
        mxStylesheet stylesheet = graph.getStylesheet();

        Map<String, Object> estiloNo = new HashMap<>(stylesheet.getDefaultVertexStyle());
        estiloNo.put(mxConstants.STYLE_SHAPE,          mxConstants.SHAPE_ELLIPSE);
        estiloNo.put(mxConstants.STYLE_FILLCOLOR,      "#AED6F1");
        estiloNo.put(mxConstants.STYLE_STROKECOLOR,    "#2E86C1");
        estiloNo.put(mxConstants.STYLE_FONTCOLOR,      "#1A5276");
        estiloNo.put(mxConstants.STYLE_FONTSTYLE,      1);
        estiloNo.put(mxConstants.STYLE_FONTSIZE,       12);
        estiloNo.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        stylesheet.setDefaultVertexStyle(estiloNo);

        Map<String, Object> estiloAresta = new HashMap<>(stylesheet.getDefaultEdgeStyle());
        estiloAresta.put(mxConstants.STYLE_STROKECOLOR, "#555555");
        estiloAresta.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        estiloAresta.put(mxConstants.STYLE_ENDARROW,    mxConstants.NONE);
        estiloAresta.put(mxConstants.STYLE_NOLABEL,     1);
        stylesheet.setDefaultEdgeStyle(estiloAresta);
    }

    private void centralizarArvore() {
        mxGraph g = graphComponent.getGraph();
        com.mxgraph.util.mxRectangle bounds = g.getGraphBounds();
        if (bounds == null) return;

        int panelW = graphComponent.getWidth();
        int panelH = graphComponent.getHeight();

        double offsetX = (panelW - bounds.getWidth())  / 2.0 - bounds.getX();
        double offsetY = (panelH - bounds.getHeight()) / 2.0 - bounds.getY();

        g.getView().setTranslate(new com.mxgraph.util.mxPoint(offsetX, offsetY));
        graphComponent.refresh();
    }


    private JPanel criarBarraZoom() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        barra.setBackground(new Color(240, 240, 240));
        barra.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        JButton btnMenos = new JButton("−");
        JButton btnReset = new JButton("Reset");
        JButton btnMais  = new JButton("+");

        for (JButton btn : new JButton[]{btnMenos, btnReset, btnMais}) {
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(65, 28));
        }

        btnMais.addActionListener (e -> zoomIn());
        btnMenos.addActionListener(e -> zoomOut());
        btnReset.addActionListener(e -> resetZoom());

        JLabel dica = new JLabel("  scroll do mouse também funciona");
        dica.setFont(new Font("SansSerif", Font.ITALIC, 11));
        dica.setForeground(Color.GRAY);

        barra.add(new JLabel("Zoom:"));
        barra.add(btnMenos);
        barra.add(btnReset);
        barra.add(btnMais);
        barra.add(dica);

        return barra;
    }
}