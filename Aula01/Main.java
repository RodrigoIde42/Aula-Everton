    import java.util.Arrays;
    import java.util.Scanner;

    public class Main {
        private static int[] estadoFinal;
        private static int newEstado;
        private static String newsEstado;
        private static int qtyEstado;
        private static boolean isDeterministic;
        private static String palavra;
        private static String abcw;
        private static int[][] Matrix;
        private static String[][] sMatrix;
        private static String[][] newMatrix; 

        public static void main(String[] args) {
            Scanner scan = new Scanner(System.in);
            
            System.out.println("Bem vindo ao meu programa!");
            breakWhile: while (true) {
                System.out.println("""
                Selecione uma das opções a seguir:
                    1. Criar matriz;
                    2. Verificar palavra;
                    3. Mostrar matriz;
                    4. Converter matriz não determinística em determinística;
                    5. Sair do programa.
                """);
                int op = scan.nextInt();
                scan.nextLine();

                clearScreen();

                switch (op) {
                    case 1:
                        System.out.println("Digite o alfabeto desejado: ");
                        abcw = scan.nextLine();
                        System.out.println("Quantos estados terá? ");
                        qtyEstado = scan.nextInt();
                        scan.nextLine();
                        System.out.println("Sua tabela é determinística(true/false)?");
                        isDeterministic = scan.nextBoolean();
                        scan.nextLine();
                
                        Matrix = new int[qtyEstado][abcw.length()];
                        sMatrix = new String[qtyEstado][abcw.length()];
                
                        if (isDeterministic) {
                            Matrix = createMatrix(Matrix, abcw, scan);
                            scan.nextLine();
                        } else {
                            sMatrix = createStringMatrix(sMatrix, abcw, scan);
                        }
                        
                        System.out.println("Digite o estado final (caso tenha mais, separe-os por virgula): ");
                        estadoFinal = Arrays.stream(scan.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();
                        System.out.println("O(s) estado(s) final(is) é(são): " + Arrays.toString(estadoFinal));

                        break;
                    case 2:
                        System.out.println("Digite uma palavra: ");
                        palavra = scan.nextLine();
                        char[] ch = palavra.toCharArray();
                
                        if (palavraInAbcw(ch, abcw)) {

                            if (verifyAbcw(ch, abcw, Matrix, 0, 0))
                                System.out.println("Palavra válida!");
                            else 
                                System.out.println("Palavra inválida!");
                
                        } else
                            System.out.println("Palavra contém caracteres que não estão no abcw");
                        break;
                    case 3:
                        if (isDeterministic) {
                            for (int[] strings : Matrix) {
                                for (int s : strings) {
                                    System.out.print(s);
                                }
                                System.out.println();
                            }
                        } else if(newMatrix.length > 0) {
                            for (String[] string : newMatrix) {
                                for (String c : string) {
                                    System.out.print(c);
                                }
                                System.out.println();
                            }
                        }
                        break;
                    case 4:
                        if (!isDeterministic) {
                            System.out.println("Só funciona com Não determinísticas");
                        } else {
                            newMatrix = new String[0][0];
                            newMatrix = convertNDtoD(sMatrix, abcw, "0");
                        }    

                        break;
                    case 5:
                        break breakWhile;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            }

            scan.close();
        }

        static boolean verifyAbcw(char[] ch, String abcw, int[][] matrix, int estado, int charIndex) {
            try {
                for (int i = 0; i < matrix[estado].length; i++) {
                    if(ch[charIndex] == abcw.charAt(i)) {
                        newEstado = matrix[estado][i];
                    }
                }
                verifyAbcw(ch, abcw, matrix, newEstado, charIndex + 1);
            } catch (Exception e) {
            }
            for (int ef : estadoFinal) {
                if (newEstado == ef) {
                    return true;
                }
            }
            return false;
        }
        
        static String[][] convertNDtoD(String[][] smatrix, String abcw, String estado) {
            for (String[] strings : newMatrix) {
                if (Arrays.stream(strings).anyMatch(estado::equals)) {
                    return newMatrix;
                }
            }
            for (int i = 0; i < abcw.length(); i++) {
                newsEstado = smatrix[i][Integer.parseInt(estado)];
                newMatrix = convertNDtoD(smatrix, abcw, newsEstado);
            }

            return newMatrix;
        }

        static int[][] createMatrix(int[][] matrix, String abcw, Scanner scan) {
            for (int i = 0; i < qtyEstado; i++) {
                System.out.println("Estado " + i);
                for (int j = 0; j < abcw.length(); j++) {
                    System.out.println("Digite o estado para " + abcw.charAt(j));
                    matrix[i][j] = scan.nextInt();
                }
            }

            return matrix;
        }

        static String[][] createStringMatrix(String[][] smatrix, String abcw, Scanner scan) {
            for (int i = 0; i < qtyEstado; i++) {
                System.out.println("Estado " + i);
                for (int j = 0; j < abcw.length(); j++) {
                    System.out.println("Digite o(s) estado(s) para: " + abcw.charAt(j));
                    smatrix[i][j] = scan.nextLine();
                }
            }

            return smatrix;
        }


        static boolean palavraInAbcw(char[] ch, String abcw) {
            for (char c : ch) {
                if (abcw.indexOf(c) == -1) {
                    return false;
                }
            }
            return true;
        }

        static void clearScreen() {  
            System.out.print("\033[H\033[2J");  
            System.out.flush();  
        } 
    }
