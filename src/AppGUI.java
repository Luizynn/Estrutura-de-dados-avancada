import javax.swing.*;
import java.awt.*;


public class AppGUI extends JFrame {

    private ArvoreBinaria arvore;
    private JTextField inputField;
    private JLabel statusLabel;

    public AppGUI() {
        arvore = new ArvoreBinaria();


        setTitle("Controle da Árvore Binária");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Valor (Inteiro):"));
        inputField = new JTextField(10);
        inputPanel.add(inputField);

        statusLabel = new JLabel("Aguardando ação...", SwingConstants.CENTER);
        statusLabel.setForeground(Color.BLUE);

        topPanel.add(inputPanel);
        topPanel.add(statusLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton btnInsert = new JButton("Inserir");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnRemover = new JButton("Remover");
        JButton btnMostrar = new JButton("Mostrar Árvore");

        buttonPanel.add(btnInsert);
        buttonPanel.add(btnBuscar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnMostrar);

        add(buttonPanel, BorderLayout.CENTER);

        btnInsert.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(inputField.getText());
                boolean inseriu = arvore.insert(valor);
                if (inseriu) {
                    statusLabel.setText(valor + " inserido com sucesso!");
                } else {
                    statusLabel.setText(valor + " já existe na árvore.");
                }
                inputField.setText("");
            } catch (NumberFormatException ex) {
                statusLabel.setText("Status: Por favor, digite um número válido.");
            }
        });

        btnBuscar.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(inputField.getText());
                boolean encontrou = arvore.contains(valor);
                if (encontrou) {
                    statusLabel.setText("O valor " + valor + " está na árvore.");
                } else {
                    statusLabel.setText("O valor " + valor + " NÃO foi encontrado.");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Por favor, digite um número válido.");
            }
        });

        btnRemover.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(inputField.getText());
                if (arvore.contains(valor)) {
                    arvore.delete(valor);
                    statusLabel.setText("Status: " + valor + " removido da árvore.");
                } else {
                    statusLabel.setText("Status: Erro - " + valor + " não existe na árvore.");
                }
                inputField.setText("");
            } catch (NumberFormatException ex) {
                statusLabel.setText("Status: Por favor, digite um número válido.");
            }
        });

        btnMostrar.addActionListener(e -> {
            arvore.mostrarGUI();
            statusLabel.setText("Status: Árvore exibida em nova janela.");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AppGUI().setVisible(true);
        });
    }
}