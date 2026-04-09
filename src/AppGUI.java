import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AppGUI extends JFrame {

    private ArvoreBinaria arvore;
    private JTextField inputField;
    private JTextArea areaTextoArvore;
    private JLabel statusLabel;

    public AppGUI() {
        arvore = new ArvoreBinaria();

        setTitle("Gerenciador de Árvore Binária");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(780, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel manualInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        manualInputPanel.add(new JLabel("Valor:"));
        inputField = new JTextField(8);
        JButton btnInsert = new JButton("Inserir");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnRemover = new JButton("Remover");
        manualInputPanel.add(inputField);
        manualInputPanel.add(btnInsert);
        manualInputPanel.add(btnBuscar);
        manualInputPanel.add(btnRemover);

        JPanel stringInputPanel = new JPanel(new BorderLayout(5, 5));
        stringInputPanel.setBorder(BorderFactory.createTitledBorder("Carregar por String (Ex: (10 (5) (15)))"));
        areaTextoArvore = new JTextArea(4, 40);
        stringInputPanel.add(new JScrollPane(areaTextoArvore), BorderLayout.CENTER);
        JButton btnCarregarString = new JButton("Gerar Árvore da String");
        stringInputPanel.add(btnCarregarString, BorderLayout.SOUTH);

        topPanel.add(manualInputPanel, BorderLayout.NORTH);
        topPanel.add(stringInputPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel centroContainer = new JPanel(new GridLayout(3, 1, 5, 5));
        centroContainer.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnMostrar = new JButton("Visualizar Gráfico");
        JButton btnSalvar  = new JButton("Salvar em TXT");
        JButton btnLimpar  = new JButton("Limpar Árvore");
        JButton btnInverter = new JButton("Inverter Árvore");

        actionPanel.add(btnMostrar);
        actionPanel.add(btnInverter);
        actionPanel.add(btnSalvar);
        actionPanel.add(btnLimpar);

        JPanel painelPercursos = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        painelPercursos.setBorder(BorderFactory.createTitledBorder("Caminhos / Percursos"));
        JButton btnPre = new JButton("Pré-ordem");
        JButton btnEm  = new JButton("Em-ordem");
        JButton btnPos = new JButton("Pós-ordem");
        JButton btnTodosCaminhos = new JButton("Ver Todos os Caminhos");
        JButton btnCaminhoAte = new JButton("Caminho até Valor");

        painelPercursos.add(btnPre);
        painelPercursos.add(btnEm);
        painelPercursos.add(btnPos);
        painelPercursos.add(btnTodosCaminhos);
        painelPercursos.add(btnCaminhoAte);

        JPanel painelMetricas = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        painelMetricas.setBorder(BorderFactory.createTitledBorder("Métricas da Árvore e dos Nós"));
        JButton btnMetricasArvore = new JButton("Métricas da Árvore");
        JButton btnMetricasNo = new JButton("Métricas de um Nó (Use o Campo 'Valor')");
        JButton btnClassificar = new JButton("Classificar Árvore");

        painelMetricas.add(btnMetricasArvore);
        painelMetricas.add(btnMetricasNo);
        painelMetricas.add(btnClassificar);

        centroContainer.add(actionPanel);
        centroContainer.add(painelPercursos);
        centroContainer.add(painelMetricas);
        add(centroContainer, BorderLayout.CENTER);

        statusLabel = new JLabel("Aguardando ação...", SwingConstants.CENTER);
        statusLabel.setForeground(new Color(30, 136, 229));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        add(statusLabel, BorderLayout.SOUTH);

        btnMetricasArvore.addActionListener(e -> {
            if (arvore.getRoot() == null) {
                statusLabel.setText("A árvore está vazia!");
                return;
            }
            String mensagem = String.format("Métricas da Árvore:\n- Altura: %d\n- Profundidade: %d\n- Nível Máximo: %d",
                    arvore.getAlturaArvore(), arvore.getProfundidadeArvore(), arvore.getNivelArvore());
            JOptionPane.showMessageDialog(this, mensagem, "Métricas da Árvore", JOptionPane.INFORMATION_MESSAGE);
        });

        btnMetricasNo.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(inputField.getText().trim());
                if (!arvore.contains(valor)) {
                    statusLabel.setText("Valor não existe na árvore.");
                    return;
                }
                String mensagem = String.format("Métricas do Nó [%d]:\n- Altura: %d\n- Profundidade: %d\n- Nível: %d",
                        valor, arvore.getAlturaNo(valor), arvore.getProfundidadeNo(valor), arvore.getNivelNo(valor));
                JOptionPane.showMessageDialog(this, mensagem, "Métricas do Nó", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                statusLabel.setText("Por favor, digite um número no campo 'Valor' primeiro.");
            }
        });

        btnClassificar.addActionListener(e -> {
            if (arvore.getRoot() == null) {
                statusLabel.setText("A árvore está vazia!");
                return;
            }
            String classificacao = arvore.classificarArvore();
            String mensagem = "Classificação da Árvore Atual:\n\n" + classificacao;
            JOptionPane.showMessageDialog(this, mensagem, "Classificação", JOptionPane.INFORMATION_MESSAGE);
        });

        btnInsert.addActionListener(e -> {
            try {
                int val = Integer.parseInt(inputField.getText().trim());
                statusLabel.setText(arvore.insert(val) ? val + " inserido." : val + " já existe.");
                inputField.setText("");
            } catch (Exception ex) { statusLabel.setText("Erro: Valor inválido."); }
        });

        btnBuscar.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(inputField.getText().trim());
                statusLabel.setText(arvore.contains(valor) ? "O valor " + valor + " está na árvore." : "Valor não encontrado.");
            } catch (NumberFormatException ex) { statusLabel.setText("Digite um número."); }
        });

        btnRemover.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(inputField.getText().trim());
                if (arvore.contains(valor)) {
                    arvore.delete(valor);
                    statusLabel.setText(valor + " removido.");
                } else { statusLabel.setText("Valor não existe."); }
                inputField.setText("");
            } catch (NumberFormatException ex) { statusLabel.setText("Digite um número."); }
        });

        btnCarregarString.addActionListener(e -> {
            try {
                arvore.buildFromParentheses(areaTextoArvore.getText().trim());
                arvore.mostrarGUI();
                statusLabel.setText("Árvore carregada!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erro no formato."); }
        });

        btnMostrar.addActionListener(e -> arvore.mostrarGUI());

        btnInverter.addActionListener(e -> {
            if (arvore.getRoot() == null) {
                statusLabel.setText("A árvore está vazia!");
            } else {
                arvore.inverter();
                arvore.mostrarGUI();
                statusLabel.setText("Árvore invertida com sucesso.");
            }
        });

        btnSalvar.addActionListener(e -> {
            String nomeArquivo = "arvore_salva.txt";
            if(arvore.salvarEmArquivo(nomeArquivo)) statusLabel.setText("Salvo em: " + nomeArquivo);
        });

        btnLimpar.addActionListener(e -> { arvore.clear(); statusLabel.setText("Árvore limpa."); });

        btnPre.addActionListener(e -> mostrarPercurso("Pré-ordem", arvore.getPreOrdem()));
        btnEm.addActionListener(e -> mostrarPercurso("Em-ordem (Crescente)", arvore.getEmOrdem()));
        btnPos.addActionListener(e -> mostrarPercurso("Pós-ordem", arvore.getPosOrdem()));

        btnTodosCaminhos.addActionListener(e -> {
            List<String> caminhos = arvore.getTodosCaminhos();
            if (caminhos == null || caminhos.isEmpty()) {
                statusLabel.setText("Árvore vazia!");
            } else {
                String msg = String.join("\n", caminhos);
                JOptionPane.showMessageDialog(this, msg, "Caminhos", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnCaminhoAte.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(inputField.getText().trim());
                String caminho = arvore.getCaminhoPara(valor);
                if (caminho != null) {
                    JOptionPane.showMessageDialog(this, "Caminho: " + caminho, "Rota", JOptionPane.INFORMATION_MESSAGE);
                } else { statusLabel.setText("Valor não encontrado."); }
            } catch (Exception ex) { statusLabel.setText("Digite um valor."); }
        });
    }

    private void mostrarPercurso(String titulo, String resultado) {
        if (resultado == null || resultado.isEmpty()) { statusLabel.setText("Árvore vazia!"); return; }
        JOptionPane.showMessageDialog(this, resultado, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppGUI().setVisible(true));
    }
}