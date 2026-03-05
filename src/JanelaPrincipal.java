import javax.swing.*;

public class JanelaPrincipal extends JFrame {
    public JanelaPrincipal(ArvoreBinaria.Node root) {
        setTitle("Visualizador de Árvore Binária");
        setSize(800, 600); // Tamanho da JANELA física
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TreePanel painelArvore = new TreePanel(root);

        // ADICIONE AQUI: O JScrollPane vai criar as barras de rolagem
        // caso o TreePanel fique maior que a janela.
        JScrollPane scrollPane = new JScrollPane(painelArvore);

        add(scrollPane);
        setVisible(true);
    }
}