import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ArvoreBinaria {

    Node root;

    public class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    public Node getRoot() {
        return root;
    }

    public void clear() {
        root = null;
    }

    public boolean insert(int value) {
        Node newNode = new Node(value);
        if (root == null) {
            root = newNode;
            return true;
        }
        Node temp = root;
        while (true) {
            if (newNode.value == temp.value) return false;
            if (newNode.value < temp.value) {
                if (temp.left == null)  { temp.left  = newNode; return true; }
                temp = temp.left;
            } else {
                if (temp.right == null) { temp.right = newNode; return true; }
                temp = temp.right;
            }
        }
    }

    public boolean contains(int value) {
        Node temp = root;
        while (temp != null) {
            if      (value < temp.value) temp = temp.left;
            else if (value > temp.value) temp = temp.right;
            else                         return true;
        }
        return false;
    }

    public void delete(int value) {
        root = deleteNode(root, value);
    }

    private Node deleteNode(Node node, int value) {
        if (node == null) return null;
        if      (value < node.value) node.left  = deleteNode(node.left,  value);
        else if (value > node.value) node.right = deleteNode(node.right, value);
        else {
            if (node.left  == null) return node.right;
            if (node.right == null) return node.left;
            node.value = minValue(node.right);
            node.right = deleteNode(node.right, node.value);
        }
        return node;
    }

    private int minValue(Node node) {
        int min = node.value;
        while (node.left != null) { node = node.left; min = node.value; }
        return min;
    }

    public boolean salvarEmArquivo(String nomeArquivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            salvarParentesesAninhados(root, writer);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
            return false;
        }
    }

    private void salvarParentesesAninhados(Node node, PrintWriter writer) {
        if (node == null) return;
        writer.print("(" + node.value);
        if (node.left  != null) { writer.print(" "); salvarParentesesAninhados(node.left,  writer); }
        if (node.right != null) { writer.print(" "); salvarParentesesAninhados(node.right, writer); }
        writer.print(")");
    }

    public void mostrarGUI() {
        JFrame frame = new JFrame("Visualização da Árvore Binária");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Painel JGraphX
        TreePanel treePanel = new TreePanel(root);
        frame.add(treePanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }


    public void buildFromParentheses(String input) throws Exception {
        if (input == null || input.trim().isEmpty()) {
            this.root = null;
            return;
        }
        this.root = parseParentheses(input.trim());
    }

    private Node parseParentheses(String s) throws Exception {
        s = s.trim();
        if (s.isEmpty()) return null;

        if (s.startsWith("(")) {
            s = s.substring(1, s.length() - 1).trim();
        }

        if (s.isEmpty()) return null;

        int firstSpace = s.indexOf(' ');
        int firstParen = s.indexOf('(');
        int splitIdx = -1;

        if (firstSpace != -1 && firstParen != -1) splitIdx = Math.min(firstSpace, firstParen);
        else if (firstSpace != -1) splitIdx = firstSpace;
        else if (firstParen != -1) splitIdx = firstParen;

        int rootVal;
        String rest = "";

        if (splitIdx == -1) {
            rootVal = Integer.parseInt(s);
        } else {
            rootVal = Integer.parseInt(s.substring(0, splitIdx).trim());
            rest = s.substring(splitIdx).trim();
        }

        Node node = new Node(rootVal);

        if (!rest.isEmpty()) {
            int leftEnd = findMatchingParen(rest, 0);
            if (leftEnd != -1) {
                String leftSub = rest.substring(0, leftEnd + 1);
                node.left = parseParentheses(leftSub);

                String rightPart = rest.substring(leftEnd + 1).trim();
                if (!rightPart.isEmpty()) {
                    node.right = parseParentheses(rightPart);
                }
            }
        }
        return node;
    }

    private int findMatchingParen(String s, int start) {
        int count = 0;
        for (int i = start; i < s.length(); i++) {
            if (s.charAt(i) == '(') count++;
            else if (s.charAt(i) == ')') {
                count--;
                if (count == 0) return i;
            }
        }
        return -1;
    }
}