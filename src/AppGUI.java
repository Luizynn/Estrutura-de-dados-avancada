import javax.swing.*;
import java.awt.*;

public class AppGUI extends JFrame {

    private ArvoreBinaria arvore;
    private JTextField inputField;
    private JTextArea areaTextoArvore;
    private JLabel statusLabel;

    public AppGUI() {
        arvore = new ArvoreBinaria();

        setTitle("Gerenciador de Árvore Binária");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 550); // Ajustado para caber tudo confortavelmente
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel manualInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        manualInputPanel.add(new JLabel("Valor individual:"));
        inputField = new JTextField(10);
        JButton btnInsert  = new JButton("Inserir");
        JButton btnBuscar  = new JButton("Buscar");
        JButton btnRemover = new JButton("Remover");

        manualInputPanel.add(inputField);
        manualInputPanel.add(btnInsert);
        manualInputPanel.add(btnBuscar);
        manualInputPanel.add(btnRemover);

        JPanel stringInputPanel = new JPanel(new BorderLayout(5, 5));
        stringInputPanel.setBorder(BorderFactory.createTitledBorder("Carregar Árvore por Texto (Ex: (10 (5) (15)))"));
        areaTextoArvore = new JTextArea(5, 40);
        areaTextoArvore.setLineWrap(true);
        stringInputPanel.add(new JScrollPane(areaTextoArvore), BorderLayout.CENTER);

        JButton btnCarregarString = new JButton("Gerar Árvore da String");
        stringInputPanel.add(btnCarregarString, BorderLayout.SOUTH);

        topPanel.add(manualInputPanel, BorderLayout.NORTH);
        topPanel.add(stringInputPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnMostrar = new JButton("Mostrar Árvore (Gráfico)");
        JButton btnSalvar  = new JButton("Salvar em TXT"); // Botão restaurado
        JButton btnLimpar  = new JButton("Limpar Árvore");

        actionPanel.add(btnMostrar);
        actionPanel.add(btnSalvar);
        actionPanel.add(btnLimpar);
        add(actionPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Sistema pronto", SwingConstants.CENTER);
        statusLabel.setForeground(new Color(30, 136, 229));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        add(statusLabel, BorderLayout.SOUTH);


        btnInsert.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(inputField.getText().trim());
                boolean inseriu = arvore.insert(valor);
                statusLabel.setText(inseriu ? valor + " inserido com sucesso!" : valor + " já existe.");
                inputField.setText("");
            } catch (NumberFormatException ex) {
                statusLabel.setText("Erro: Digite um número válido.");
            }
        });

        btnBuscar.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(inputField.getText().trim());
                statusLabel.setText(arvore.contains(valor) ? "O valor " + valor + " está na árvore." : "Valor não encontrado.");
            } catch (NumberFormatException ex) {
                statusLabel.setText("Digite um número para buscar.");
            }
        });

        btnRemover.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(inputField.getText().trim());
                if (arvore.contains(valor)) {
                    arvore.delete(valor);
                    statusLabel.setText(valor + " removido.");
                } else {
                    statusLabel.setText("Valor não existe na árvore.");
                }
                inputField.setText("");
            } catch (NumberFormatException ex) {
                statusLabel.setText("Digite um número para remover.");
            }
        });

        btnCarregarString.addActionListener(e -> {
            try {
                String input = areaTextoArvore.getText().trim();
                if (input.isEmpty()) throw new Exception("O campo está vazio.");
                arvore.buildFromParentheses(input);
                arvore.mostrarGUI();
                statusLabel.setText("Árvore carregada via string!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro no formato: " + ex.getMessage());
                statusLabel.setText("Falha ao processar string.");
            }
        });

        btnMostrar.addActionListener(e -> {
            arvore.mostrarGUI();
            statusLabel.setText("Visualização aberta.");
        });

        btnSalvar.addActionListener(e -> {
            String nomeArquivo = "arvore_salva.txt";
            boolean sucesso = arvore.salvarEmArquivo(nomeArquivo);
            statusLabel.setText(sucesso ? "Salvo em: " + nomeArquivo : "Erro ao salvar arquivo.");
        });

        btnLimpar.addActionListener(e -> {
            arvore.clear();
            statusLabel.setText("Árvore limpa.");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppGUI().setVisible(true));
    }
}