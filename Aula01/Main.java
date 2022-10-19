    import java.util.ArrayList;
import java.util.Arrays;
    import java.util.Scanner;

    public class Main {
        private static int[] estadoFinal;
        private static int newEstado;
        private static String[] newsEstado;
        private static int qtyEstado;
        private static boolean isDeterministic;
        private static String palavra;
        private static String abcw;
        private static int[][] Matrix;
        private static String[][] sMatrix;
        private static ArrayList<String[]> newMatrix; 
        private static ArrayList<String> pastEstado;
        private static ArrayList<Integer> pastIndex;

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
                        } else {
                            try {
                                for (int i = 0; i < newMatrix.size(); i++) {
                                    for (int j = 0; j < newMatrix.get(i).length; j++) {
                                        System.out.print(newMatrix.get(i)[j]);
                                    }
                                    System.out.println();
                                }
                            } catch (Exception e) {
                                for (String[] s : sMatrix) {
                                    for (String c : s) {
                                        System.out.print(c);
                                    }
                                    System.out.println();
                                }
                            }
                        }
                        try { Thread.sleep(2000); } catch (InterruptedException e) {}
                        break;
                    case 4:
                        if (isDeterministic) {
                            System.out.println("Só funciona com Não determinísticas");
                        } else {
                            pastIndex = new ArrayList<Integer>(0);
                            pastEstado = new ArrayList<String>(0);
                            newsEstado = new String[abcw.length()];
                            newMatrix = new ArrayList<>(0);
                            newMatrix.add(new String[abcw.length()]);
                            newMatrix = convertNDtoD(sMatrix, abcw, "0", 0);
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
        
        static ArrayList<String[]> convertNDtoD(String[][] smatrix, String abcw, String estado, int index) {
            try {
                StringBuilder otherState = new StringBuilder();
                estado.chars().distinct().forEach(c -> otherState.append((char) c));
                if (!estado.equals(otherState.toString())) {
                    return newMatrix;
                } else {
                    estado = String.valueOf(otherState);
                }
                pastEstado.add(estado);
                try {
                    newMatrix.get(index);
                } catch (Exception e) {
                    newMatrix.add(new String[abcw.length()]);
                }
                try {
                    for (int i = 0; i < pastEstado.size() - 1; i++) {
                        if (pastEstado.get(i).contains(estado)) {
                            return newMatrix;
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                char[] ch = estado.toCharArray();
                for (int i = 0; i < abcw.length(); i++) {
                    newsEstado[i] = " ";
                    for (char c : ch) {
                        newsEstado[i] += " " + smatrix[Character.getNumericValue(c)][i];
                    }
                    
                    newsEstado[i] = newsEstado[i].replaceAll("\\s+","");
                    newMatrix.get(index)[i] = newsEstado[i];
                    
                    newMatrix = convertNDtoD(smatrix, abcw, newsEstado[i], index + 1);
                }
            } catch (Exception e) {
                System.out.println(e);
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
