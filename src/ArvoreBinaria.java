import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ArvoreBinaria {

    Node root;
    private boolean isAVL = false;
    private boolean isRedBlack = false;
    private List<String> auditoria = new ArrayList<>();
    private List<Integer> sequenciaInsercao = new ArrayList<>();

    public List<Integer> getSequenciaInsercao() {
        return sequenciaInsercao;
    }

    public void clearSequenciaInsercao() {
        sequenciaInsercao.clear();
    }

    public List<String> getAuditoria() {
        return auditoria;
    }

    public void clearAuditoria() {
        auditoria.clear();
    }

    public void setAVL(boolean isAVL) {
        this.isAVL = isAVL;
        if (isAVL) clearAuditoria();
    }

    public boolean isAVL() {
        return isAVL;
    }

    public void setRedBlack(boolean isRedBlack) {
        this.isRedBlack = isRedBlack;
        if (isRedBlack) clearAuditoria();
    }

    public boolean isRedBlack() {
        return isRedBlack;
    }

    public class Node {
        int value;
        Node left;
        Node right;
        Node parent;
        int altura;
        boolean isRed;

        Node(int value) {
            this.value = value;
            this.altura = 1;
            this.isRed = true;
        }
    }

    public Node getRoot() { return root; }
    public void clear() { root = null; auditoria.clear(); sequenciaInsercao.clear(); }

    private boolean isInserted = false;

    public boolean insert(int value) {
        if (isAVL) auditoria.add("-> Solicitada inserção do valor: " + value);
        if (isRedBlack) auditoria.add("-> Solicitada inserção na Árvore Rubro-Negra: " + value);
        isInserted = false;
        if (isRedBlack) {
            Node newNode = new Node(value);
            root = insertNodeRBT(root, newNode);
            if (isInserted) {
                fixInsertRBT(newNode);
                auditoria.add("   Inserção do valor " + value + " concluída na Árvore Rubro-Negra.");
                sequenciaInsercao.add(value);
            } else {
                auditoria.add("   Valor " + value + " já existe. Nenhuma alteração.");
            }
        } else {
            root = insertNode(root, value);
            if (isInserted) {
                sequenciaInsercao.add(value);
            }
            if (isAVL) {
                if (isInserted) auditoria.add("   Inserção do valor " + value + " concluída.");
                else auditoria.add("   Valor " + value + " já existe. Nenhuma alteração.");
            }
        }
        return isInserted;
    }

    private Node insertNodeRBT(Node root, Node newNode) {
        Node y = null;
        Node x = root;

        while (x != null) {
            y = x;
            if (newNode.value < x.value) {
                x = x.left;
            } else if (newNode.value > x.value) {
                x = x.right;
            } else {
                isInserted = false;
                return root;
            }
        }

        newNode.parent = y;
        if (y == null) {
            root = newNode;
        } else if (newNode.value < y.value) {
            y.left = newNode;
        } else {
            y.right = newNode;
        }

        isInserted = true;
        return root;
    }

    private void fixInsertRBT(Node z) {
        while (z.parent != null && z.parent.isRed) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (y != null && y.isRed) {
                    auditoria.add("   ! Conflito Vermelho-Vermelho no nó [" + z.value + "] e pai [" + z.parent.value + "]. Tio [" + y.value + "] é Vermelho.");
                    auditoria.add("     * Recoloração: Pai [" + z.parent.value + "] e Tio [" + y.value + "] -> Preto; Avô [" + z.parent.parent.value + "] -> Vermelho.");
                    z.parent.isRed = false;
                    y.isRed = false;
                    z.parent.parent.isRed = true;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        auditoria.add("   ! Conflito Vermelho-Vermelho (Caso Direita/Esquerda). Rotacionando esquerda no pai [" + z.parent.value + "].");
                        z = z.parent;
                        leftRotateRBT(z);
                    }
                    auditoria.add("   ! Conflito Vermelho-Vermelho (Caso Esquerda/Esquerda).");
                    auditoria.add("     * Recoloração: Pai [" + z.parent.value + "] -> Preto; Avô [" + z.parent.parent.value + "] -> Vermelho.");
                    z.parent.isRed = false;
                    z.parent.parent.isRed = true;
                    auditoria.add("     * Rotação à Direita no avô [" + z.parent.parent.value + "].");
                    rightRotateRBT(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y != null && y.isRed) {
                    auditoria.add("   ! Conflito Vermelho-Vermelho no nó [" + z.value + "] e pai [" + z.parent.value + "]. Tio [" + y.value + "] é Vermelho.");
                    auditoria.add("     * Recoloração: Pai [" + z.parent.value + "] e Tio [" + y.value + "] -> Preto; Avô [" + z.parent.parent.value + "] -> Vermelho.");
                    z.parent.isRed = false;
                    y.isRed = false;
                    z.parent.parent.isRed = true;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        auditoria.add("   ! Conflito Vermelho-Vermelho (Caso Esquerda/Direita). Rotacionando direita no pai [" + z.parent.value + "].");
                        z = z.parent;
                        rightRotateRBT(z);
                    }
                    auditoria.add("   ! Conflito Vermelho-Vermelho (Caso Direita/Direita).");
                    auditoria.add("     * Recoloração: Pai [" + z.parent.value + "] -> Preto; Avô [" + z.parent.parent.value + "] -> Vermelho.");
                    z.parent.isRed = false;
                    z.parent.parent.isRed = true;
                    auditoria.add("     * Rotação à Esquerda no avô [" + z.parent.parent.value + "].");
                    leftRotateRBT(z.parent.parent);
                }
            }
        }
        if (root.isRed) {
            auditoria.add("   * Forçando cor da Raiz [" + root.value + "] -> Preto.");
            root.isRed = false;
        }
    }

    private void leftRotateRBT(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotateRBT(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }
        x.parent = y.parent;
        if (y.parent == null) {
            this.root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }
        x.right = y;
        y.parent = x;
    }

    private Node insertNode(Node node, int value) {
        if (node == null) {
            isInserted = true;
            return new Node(value);
        }

        if (value < node.value) {
            node.left = insertNode(node.left, value);
        } else if (value > node.value) {
            node.right = insertNode(node.right, value);
        } else {
            return node;
        }

        if (isAVL) {
            return balancear(node);
        } else {
            atualizarAltura(node);
            return node;
        }
    }

    private int getAltura(Node node) {
        return (node == null) ? 0 : node.altura;
    }

    private int getFatorBalanceamento(Node node) {
        return (node == null) ? 0 : getAltura(node.left) - getAltura(node.right);
    }

    private void atualizarAltura(Node node) {
        if (node != null) {
            node.altura = 1 + Math.max(getAltura(node.left), getAltura(node.right));
        }
    }

    private Node rotacaoDireita(Node y) {
        if (isAVL) auditoria.add("   * Rotação Simples à Direita no nó [" + y.value + "]");
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        atualizarAltura(y);
        atualizarAltura(x);
        return x;
    }

    private Node rotacaoEsquerda(Node x) {
        if (isAVL) auditoria.add("   * Rotação Simples à Esquerda no nó [" + x.value + "]");
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        atualizarAltura(x);
        atualizarAltura(y);
        return y;
    }

    private Node rotacaoDuplaEsquerdaDireita(Node node) {
        if (isAVL) auditoria.add("   * Iniciando Rotação Dupla (Esquerda-Direita) no nó [" + node.value + "]");
        node.left = rotacaoEsquerda(node.left);
        return rotacaoDireita(node);
    }

    private Node rotacaoDuplaDireitaEsquerda(Node node) {
        if (isAVL) auditoria.add("   * Iniciando Rotação Dupla (Direita-Esquerda) no nó [" + node.value + "]");
        node.right = rotacaoDireita(node.right);
        return rotacaoEsquerda(node);
    }

    private Node balancear(Node node) {
        if (node == null) return null;
        atualizarAltura(node);
        int balance = getFatorBalanceamento(node);

        if (balance > 1 && getFatorBalanceamento(node.left) >= 0) {
            if (isAVL) auditoria.add("   ! Desbalanceamento detectado no nó [" + node.value + "] (Fator: " + balance + "). Aplicando rotação simples à direita.");
            return rotacaoDireita(node);
        }
        if (balance < -1 && getFatorBalanceamento(node.right) <= 0) {
            if (isAVL) auditoria.add("   ! Desbalanceamento detectado no nó [" + node.value + "] (Fator: " + balance + "). Aplicando rotação simples à esquerda.");
            return rotacaoEsquerda(node);
        }
        if (balance > 1 && getFatorBalanceamento(node.left) < 0) {
            if (isAVL) auditoria.add("   ! Desbalanceamento detectado no nó [" + node.value + "] (Fator: " + balance + "). Condição para rotação dupla identificada.");
            return rotacaoDuplaEsquerdaDireita(node);
        }
        if (balance < -1 && getFatorBalanceamento(node.right) > 0) {
            if (isAVL) auditoria.add("   ! Desbalanceamento detectado no nó [" + node.value + "] (Fator: " + balance + "). Condição para rotação dupla identificada.");
            return rotacaoDuplaDireitaEsquerda(node);
        }
        return node;
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

    public void delete(int value) { 
        if (isAVL) auditoria.add("-> Solicitada remoção do valor: " + value);
        if (isRedBlack) auditoria.add("-> Solicitada remoção na Árvore Rubro-Negra: " + value);
        if (isRedBlack) {
            deleteRBT(value);
        } else {
            root = deleteNode(root, value); 
        }
        if (isAVL) auditoria.add("   Remoção do valor " + value + " concluída (caso existisse).");
        if (isRedBlack) auditoria.add("   Remoção do valor " + value + " concluída na Árvore Rubro-Negra (caso existisse).");
    }

    private void deleteRBT(int value) {
        Node z = buscarNo(root, value);
        if (z == null) {
            auditoria.add("   Valor " + value + " não encontrado para remoção.");
            return;
        }
        deleteNodeRBT(z);
    }

    private void deleteNodeRBT(Node z) {
        Node y = z;
        boolean yOriginalColor = y.isRed;
        Node x;
        Node xParent;
        if (z.left == null) {
            x = z.right;
            xParent = z.parent;
            transplantRBT(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            xParent = z.parent;
            transplantRBT(z, z.left);
        } else {
            y = minValueNode(z.right);
            yOriginalColor = y.isRed;
            x = y.right;
            if (y.parent == z) {
                xParent = y;
            } else {
                xParent = y.parent;
                transplantRBT(y, y.right);
                y.right = z.right;
                if (y.right != null) {
                    y.right.parent = y;
                }
            }
            transplantRBT(z, y);
            y.left = z.left;
            if (y.left != null) {
                y.left.parent = y;
            }
            y.isRed = z.isRed;
        }
        if (!yOriginalColor) {
            auditoria.add("   ! Remoção de nó Preto [" + y.value + "] viola propriedade de altura preta. Iniciando correção de balanceamento.");
            fixDeleteRBT(x, xParent);
        }
    }

    private void fixDeleteRBT(Node x, Node xParent) {
        while (x != root && (x == null || !x.isRed)) {
            if (x == xParent.left) {
                Node w = xParent.right;
                if (w != null && w.isRed) {
                    auditoria.add("     * Caso 1 (Irmão Vermelho): Recolore irmão [" + w.value + "] -> Preto, Pai [" + xParent.value + "] -> Vermelho. Rotaciona esquerda no pai.");
                    w.isRed = false;
                    xParent.isRed = true;
                    leftRotateRBT(xParent);
                    w = xParent.right;
                }
                if (w == null || ((w.left == null || !w.left.isRed) && (w.right == null || !w.right.isRed))) {
                    auditoria.add("     * Caso 2 (Irmão Preto com filhos Pretos): Recolore irmão [" + (w != null ? w.value : "null") + "] -> Vermelho. Move foco para o pai.");
                    if (w != null) {
                        w.isRed = true;
                    }
                    x = xParent;
                    xParent = x.parent;
                } else {
                    if (w.right == null || !w.right.isRed) {
                        if (w.left != null) {
                            auditoria.add("     * Caso 3 (Irmão Preto, filho esquerdo Vermelho): Recolore filho esquerdo [" + w.left.value + "] -> Preto, Irmão [" + w.value + "] -> Vermelho. Rotaciona direita no irmão.");
                            w.left.isRed = false;
                        }
                        w.isRed = true;
                        rightRotateRBT(w);
                        w = xParent.right;
                    }
                    auditoria.add("     * Caso 4 (Irmão Preto, filho direito Vermelho): Transpõe cores do pai [" + xParent.value + "] para o irmão [" + (w != null ? w.value : "null") + "], colore pai e filho direito -> Preto. Rotaciona esquerda no pai.");
                    if (w != null) {
                        w.isRed = xParent.isRed;
                        if (w.right != null) {
                            w.right.isRed = false;
                        }
                    }
                    xParent.isRed = false;
                    leftRotateRBT(xParent);
                    x = root;
                    xParent = null;
                }
            } else {
                Node w = xParent.left;
                if (w != null && w.isRed) {
                    auditoria.add("     * Caso 1 (Irmão Vermelho): Recolore irmão [" + w.value + "] -> Preto, Pai [" + xParent.value + "] -> Vermelho. Rotaciona direita no pai.");
                    w.isRed = false;
                    xParent.isRed = true;
                    rightRotateRBT(xParent);
                    w = xParent.left;
                }
                if (w == null || ((w.right == null || !w.right.isRed) && (w.left == null || !w.left.isRed))) {
                    auditoria.add("     * Caso 2 (Irmão Preto com filhos Pretos): Recolore irmão [" + (w != null ? w.value : "null") + "] -> Vermelho. Move foco para o pai.");
                    if (w != null) {
                        w.isRed = true;
                    }
                    x = xParent;
                    xParent = x.parent;
                } else {
                    if (w.left == null || !w.left.isRed) {
                        if (w.right != null) {
                            auditoria.add("     * Caso 3 (Irmão Preto, filho direito Vermelho): Recolore filho direito [" + w.right.value + "] -> Preto, Irmão [" + w.value + "] -> Vermelho. Rotaciona esquerda no irmão.");
                            w.right.isRed = false;
                        }
                        w.isRed = true;
                        leftRotateRBT(w);
                        w = xParent.left;
                    }
                    auditoria.add("     * Caso 4 (Irmão Preto, filho esquerdo Vermelho): Transpõe cores do pai [" + xParent.value + "] para o irmão [" + (w != null ? w.value : "null") + "], colore pai e filho esquerdo -> Preto. Rotaciona direita no pai.");
                    if (w != null) {
                        w.isRed = xParent.isRed;
                        if (w.left != null) {
                            w.left.isRed = false;
                        }
                    }
                    xParent.isRed = false;
                    rightRotateRBT(xParent);
                    x = root;
                    xParent = null;
                }
            }
        }
        if (x != null) {
            if (!x.isRed) {
                auditoria.add("     * Colorindo nó de foco [" + x.value + "] -> Preto.");
            }
            x.isRed = false;
        }
    }

    private void transplantRBT(Node u, Node v) {
        if (u.parent == null) {
            this.root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    private Node minValueNode(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

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
        if (node == null) return null;
        if (isAVL) return balancear(node);
        atualizarAltura(node);
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
        atualizarAlturaDeTodosOsNos(this.root);
        if (isAVL) {
            auditoria.clear();
            auditoria.add("-> Árvore carregada via String (sem balanceamento automático de inserções).");
        }
        if (isRedBlack) {
            auditoria.clear();
            auditoria.add("-> Árvore carregada via String na Árvore Rubro-Negra (cores padrão podem não estar balanceadas).");
            arrumarPonteirosPaiDeTodosOsNos(this.root, null);
        }
        sequenciaInsercao.clear();
        preencherSequenciaDeInsercaoAPartirDoNo(this.root);
    }

    private void preencherSequenciaDeInsercaoAPartirDoNo(Node node) {
        if (node == null) return;
        sequenciaInsercao.add(node.value);
        preencherSequenciaDeInsercaoAPartirDoNo(node.left);
        preencherSequenciaDeInsercaoAPartirDoNo(node.right);
    }

    private void arrumarPonteirosPaiDeTodosOsNos(Node node, Node parent) {
        if (node == null) return;
        node.parent = parent;
        if (parent == null) {
            node.isRed = false;
        } else {
            node.isRed = true;
        }
        arrumarPonteirosPaiDeTodosOsNos(node.left, node);
        arrumarPonteirosPaiDeTodosOsNos(node.right, node);
    }

    private int atualizarAlturaDeTodosOsNos(Node node) {
        if (node == null) return 0;
        int altLeft = atualizarAlturaDeTodosOsNos(node.left);
        int altRight = atualizarAlturaDeTodosOsNos(node.right);
        node.altura = 1 + Math.max(altLeft, altRight);
        return node.altura;
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
        frame.add(new TreePanel(this), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}