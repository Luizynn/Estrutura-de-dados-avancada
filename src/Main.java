import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        ArvoreBinaria arvore = new ArvoreBinaria();
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        System.out.println("Bem-vindo ao testador da Árvore Binária!");

        while (opcao != 5) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Inserir valor");
            System.out.println("2. Deletar valor");
            System.out.println("3. Buscar valor (Contains)");
            System.out.println("4. Mostrar Árvore (Interface Gráfica)");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.next();
                continue;
            }

            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o valor para inserir: ");
                    int valorInserir = scanner.nextInt();
                    if (arvore.insert(valorInserir)) {
                        System.out.println("-> Valor " + valorInserir + " inserido com sucesso!");
                    } else {
                        System.out.println("-> O valor " + valorInserir + " já existe na árvore.");
                    }
                    break;

                case 2:
                    System.out.print("Digite o valor para deletar: ");
                    int valorDeletar = scanner.nextInt();
                    arvore.delete(valorDeletar);
                    System.out.println("-> Comando de deleção executado (se o nó existia, foi removido).");
                    break;

                case 3:
                    System.out.print("Digite o valor para buscar: ");
                    int valorBuscar = scanner.nextInt();
                    if (arvore.contains(valorBuscar)) {
                        System.out.println("-> O valor " + valorBuscar + " ESTÁ presente na árvore.");
                    } else {
                        System.out.println("-> O valor " + valorBuscar + " NÃO FOI ENCONTRADO na árvore.");
                    }
                    break;

                case 4:
                    System.out.println("-> Abrindo janela de visualização...");
                    arvore.mostrarGUI();
                    break;

                case 5:
                    System.out.println("-> Encerrando o programa. Até logo!");
                    break;

                default:
                    System.out.println("-> Opção inválida. Escolha um número entre 1 e 5.");
            }
        }

        scanner.close();
    }
}
