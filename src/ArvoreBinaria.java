import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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

    public Node getRoot() { return root; }
    public void clear() { root = null; }

    public boolean insert(int value) {
        Node newNode = new Node(value);
        if (root == null) {
            root = newNode;
            return true;
        }

        Node temp = root;
        while (true) {
            if (value < temp.value) {
                if (temp.left == null) {
                    temp.left = newNode;
                    return true;
                }
                temp = temp.left;
            } else {
                if (temp.right == null) {
                    temp.right = newNode;
                    return true;
                }
                temp = temp.right;
            }
        }
    }

    public boolean contains(int value) {
        Node temp = root;
        while (temp != null) {
            if (value < temp.value) temp = temp.left;
            else if (value > temp.value) temp = temp.right;
            else return true;
        }
        return false;
    }

    public void delete(int value) { root = deleteNode(root, value); }

    private Node deleteNode(Node node, int value) {
        if (node == null) return null;
        if (value < node.value) node.left = deleteNode(node.left, value);
        else if (value > node.value) node.right = deleteNode(node.right, value);
        else {
            if (node.left == null) return node.right;
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

    public void inverter() {
        root = inverterRecursivo(root);
    }

    private Node inverterRecursivo(Node node) {
        if (node == null) return null;
        Node temp = node.left;
        node.left = inverterRecursivo(node.right);
        node.right = inverterRecursivo(temp);
        return node;
    }

    public List<String> getTodosCaminhos() {
        List<String> caminhos = new ArrayList<>();
        encontrarCaminhos(root, "", caminhos);
        return caminhos;
    }

    private void encontrarCaminhos(Node node, String caminhoAtual, List<String> caminhos) {
        if (node == null) return;
        caminhoAtual += (caminhoAtual.isEmpty() ? "" : " -> ") + node.value;
        if (node.left == null && node.right == null) {
            caminhos.add(caminhoAtual);
        } else {
            encontrarCaminhos(node.left, caminhoAtual, caminhos);
            encontrarCaminhos(node.right, caminhoAtual, caminhos);
        }
    }

    public String getCaminhoPara(int valor) {
        List<Integer> caminho = new ArrayList<>();
        if (buscarCaminho(root, valor, caminho)) {
            return caminho.toString().replace("[", "").replace("]", "").replace(", ", " -> ");
        }
        return null;
    }

    private boolean buscarCaminho(Node node, int valor, List<Integer> caminho) {
        if (node == null) return false;
        caminho.add(node.value);
        if (node.value == valor) return true;
        if (buscarCaminho(node.left, valor, caminho) || buscarCaminho(node.right, valor, caminho)) return true;
        caminho.remove(caminho.size() - 1);
        return false;
    }

    public boolean salvarEmArquivo(String nomeArquivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            salvarParentesesAninhados(root, writer);
            return true;
        } catch (IOException e) { return false; }
    }

    private void salvarParentesesAninhados(Node node, PrintWriter writer) {
        if (node == null) return;
        writer.print("(" + node.value);
        if (node.left != null) { writer.print(" "); salvarParentesesAninhados(node.left, writer); }
        if (node.right != null) { writer.print(" "); salvarParentesesAninhados(node.right, writer); }
        writer.print(")");
    }

    public void buildFromParentheses(String input) throws Exception {
        if (input == null || input.trim().isEmpty()) { this.root = null; return; }
        this.root = parseParentheses(input.trim());
    }

    private Node parseParentheses(String s) throws Exception {
        s = s.trim();
        if (s.isEmpty()) return null;
        if (s.startsWith("(")) s = s.substring(1, s.length() - 1).trim();
        if (s.isEmpty()) return null;
        int firstSpace = s.indexOf(' ');
        int firstParen = s.indexOf('(');
        int splitIdx = -1;
        if (firstSpace != -1 && firstParen != -1) splitIdx = Math.min(firstSpace, firstParen);
        else if (firstSpace != -1) splitIdx = firstSpace;
        else if (firstParen != -1) splitIdx = firstParen;

        int rootVal;
        String rest = "";
        if (splitIdx == -1) { rootVal = Integer.parseInt(s); }
        else {
            rootVal = Integer.parseInt(s.substring(0, splitIdx).trim());
            rest = s.substring(splitIdx).trim();
        }
        Node node = new Node(rootVal);
        if (!rest.isEmpty()) {
            int leftEnd = findMatchingParen(rest, 0);
            if (leftEnd != -1) {
                node.left = parseParentheses(rest.substring(0, leftEnd + 1));
                String rightPart = rest.substring(leftEnd + 1).trim();
                if (!rightPart.isEmpty()) node.right = parseParentheses(rightPart);
            }
        }
        return node;
    }

    private int findMatchingParen(String s, int start) {
        int count = 0;
        for (int i = start; i < s.length(); i++) {
            if (s.charAt(i) == '(') count++;
            else if (s.charAt(i) == ')') { count--; if (count == 0) return i; }
        }
        return -1;
    }

    private Node buscarNo(Node node, int valor) {
        if (node == null || node.value == valor) return node;
        if (valor < node.value) return buscarNo(node.left, valor);
        return buscarNo(node.right, valor);
    }

    public int getAlturaNo(int valor) {
        Node no = buscarNo(root, valor);
        return (no != null) ? calcularAltura(no) : -1;
    }

    public int getAlturaArvore() {
        return calcularAltura(root);
    }

    private int calcularAltura(Node node) {
        if (node == null) return -1;
        int alturaEsq = calcularAltura(node.left);
        int alturaDir = calcularAltura(node.right);
        return Math.max(alturaEsq, alturaDir) + 1;
    }

    public int getProfundidadeNo(int valor) {
        return calcularProfundidade(root, valor, 0);
    }

    public int getProfundidadeArvore() {
        return getAlturaArvore();
    }

    private int calcularProfundidade(Node node, int valor, int profAtual) {
        if (node == null) return -1;
        if (node.value == valor) return profAtual;
        if (valor < node.value) return calcularProfundidade(node.left, valor, profAtual + 1);
        return calcularProfundidade(node.right, valor, profAtual + 1);
    }

    public int getNivelNo(int valor) {
        return getProfundidadeNo(valor);
    }

    public int getNivelArvore() {
        return getAlturaArvore();
    }

    private int contarNos(Node node) {
        if (node == null) return 0;
        return 1 + contarNos(node.left) + contarNos(node.right);
    }

    private boolean isCheia() {
        if (root == null) return true;
        int altura = getAlturaArvore();
        int totalNosEsperados = (int) Math.pow(2, altura + 1) - 1;
        return contarNos(root) == totalNosEsperados;
    }

    private boolean isCompleta(Node node, int index, int totalNos) {
        if (node == null) return true;
        if (index >= totalNos) return false;
        return isCompleta(node.left, 2 * index + 1, totalNos) &&
                isCompleta(node.right, 2 * index + 2, totalNos);
    }

    private boolean isLinear(Node node) {
        if (node == null) return true;
        if (node.left != null && node.right != null) return false;
        if (node.left != null) return isLinear(node.left);
        if (node.right != null) return isLinear(node.right);
        return true;
    }

    public String classificarArvore() {
        if (root == null) return "Árvore Vazia";
        int totalNos = contarNos(root);
        if (isLinear(root)) {
            return "Degenerativa";
        } else if (isCheia()) {
            return "Cheia";
        } else if (isCompleta(root, 0, totalNos)) {
            return "Completa";
        }
        return "Incompleta";
    }

    public String getPreOrdem() { StringBuilder sb = new StringBuilder(); percorrerPreOrdem(root, sb); return sb.toString().trim(); }
    private void percorrerPreOrdem(Node node, StringBuilder sb) { if (node == null) return; sb.append(node.value).append(" "); percorrerPreOrdem(node.left, sb); percorrerPreOrdem(node.right, sb); }

    public String getEmOrdem() { StringBuilder sb = new StringBuilder(); percorrerEmOrdem(root, sb); return sb.toString().trim(); }
    private void percorrerEmOrdem(Node node, StringBuilder sb) { if (node == null) return; percorrerEmOrdem(node.left, sb); sb.append(node.value).append(" "); percorrerEmOrdem(node.right, sb); }

    public String getPosOrdem() { StringBuilder sb = new StringBuilder(); percorrerPosOrdem(root, sb); return sb.toString().trim(); }
    private void percorrerPosOrdem(Node node, StringBuilder sb) { if (node == null) return; percorrerPosOrdem(node.left, sb); percorrerPosOrdem(node.right, sb); sb.append(node.value).append(" "); }

    public void mostrarGUI() {
        JFrame frame = new JFrame("Visualização da Árvore Binária");
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);
        frame.add(new TreePanel(root), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}