import javax.swing.*;
import java.awt.*;

public class AppGUI extends JFrame {

    private ArvoreBinaria arvore;
    private JTextField inputField;
    private JLabel statusLabel;

    public AppGUI() {
        arvore = new ArvoreBinaria();

        setTitle("Árvore Binária");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Valor:"));
        inputField = new JTextField(10);
        inputPanel.add(inputField);

        statusLabel = new JLabel("Aguardando ação", SwingConstants.CENTER);
        statusLabel.setForeground(Color.BLUE);

        topPanel.add(inputPanel);
        topPanel.add(statusLabel);
        add(topPanel, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnInsert  = new JButton("Inserir");
        JButton btnBuscar  = new JButton("Buscar");
        JButton btnRemover = new JButton("Remover");
        JButton btnMostrar = new JButton("Mostrar Árvore");
        JButton btnSalvar  = new JButton("Salvar TXT");
        JButton btnLimpar  = new JButton("Limpar Árvore");

        buttonPanel.add(btnInsert);
        buttonPanel.add(btnBuscar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnMostrar);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnLimpar);

        add(buttonPanel, BorderLayout.CENTER);

        btnInsert.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(inputField.getText().trim());
                boolean inseriu = arvore.insert(valor);
                statusLabel.setText(inseriu
                        ? valor + " inserido com sucesso!"
                        : valor + " já existe na árvore.");
                inputField.setText("");
            } catch (NumberFormatException ex) {
                statusLabel.setText("Por favor, digite um número válido.");
            }
        });

        btnBuscar.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(inputField.getText().trim());
                statusLabel.setText(arvore.contains(valor)
                        ? "O valor " + valor + " está na árvore."
                        : "O valor " + valor + " NÃO foi encontrado.");
            } catch (NumberFormatException ex) {
                statusLabel.setText("Por favor, digite um número válido.");
            }
        });

        btnRemover.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(inputField.getText().trim());
                if (arvore.contains(valor)) {
                    arvore.delete(valor);
                    statusLabel.setText(valor + " removido da árvore.");
                } else {
                    statusLabel.setText(valor + " não existe na árvore.");
                }
                inputField.setText("");
            } catch (NumberFormatException ex) {
                statusLabel.setText("Por favor, digite um número válido.");
            }
        });


        btnMostrar.addActionListener(e -> {
            arvore.mostrarGUI();
            statusLabel.setText("Árvore exibida em nova janela.");
        });

        btnSalvar.addActionListener(e -> {
            String nomeArquivo = "arvore_salva.txt";
            statusLabel.setText(arvore.salvarEmArquivo(nomeArquivo)
                    ? "Árvore salva em " + nomeArquivo
                    : "Erro ao salvar o arquivo.");
        });

        btnLimpar.addActionListener(e -> {
            arvore.clear();
            repaint();
            statusLabel.setText("Árvore limpa.");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppGUI().setVisible(true));
    }
}