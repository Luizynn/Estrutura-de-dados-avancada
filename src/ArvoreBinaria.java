import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ArvoreBinaria {

    Node root;

    public class Node {
        int value;
        Node left;
        Node right;

        Node(int value){
            this.value = value;
        }
    }

    public void clear(){
        root = null;
    }

    public boolean insert(int value){
        Node newNode = new Node(value);
        if(root == null) {
            root = newNode;
            return true;
        }
        Node temp = root;
        while (true) {
            if (newNode.value == temp.value) {
                return false;
            }
            if (newNode.value < temp.value) {
                if(temp.left == null){
                    temp.left = newNode;
                    return true;
                }
                temp = temp.left;
            } else {
                if(temp.right == null) {
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
            if (value < temp.value){
                temp = temp.left;
            } else if (value > temp.value){
                temp = temp.right;
            } else {
                return true;
            }
        }
        return false;
    }

    public void delete(int value) {
        root = deleteNode(root, value);
    }

    private Node deleteNode(Node root, int value) {
        if (root == null) {
            return root;
        }

        if (value < root.value) {
            root.left = deleteNode(root.left, value);
        } else if (value > root.value) {
            root.right = deleteNode(root.right, value);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            root.value = minValue(root.right);
            root.right = deleteNode(root.right, root.value);
        }
        return root;
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

    private void salvarPreOrdem(Node node, PrintWriter writer) {
        if (node != null) {
            writer.println(node.value);
            salvarPreOrdem(node.left, writer);
            salvarPreOrdem(node.right, writer);
        }
    }

    private void salvarParentesesAninhados(Node node, PrintWriter writer) {
        if (node == null) {
            return;
        }

        writer.print("(" + node.value);

        if (node.left != null) {
            writer.print(" ");
            salvarParentesesAninhados(node.left, writer);
        }

        if (node.right != null) {
            writer.print(" ");
            salvarParentesesAninhados(node.right, writer);
        }

        writer.print(")");
    }

    private int minValue(Node root) {
        int minv = root.value;
        while (root.left != null) {
            minv = root.left.value;
            root = root.left;
        }
        return minv;
    }

    public void mostrarGUI() {
        JFrame frame = new JFrame("Visualização da Árvore Binária");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        TreePanel painel = new TreePanel(root);
        frame.add(painel);

        frame.setVisible(true);
    }


}