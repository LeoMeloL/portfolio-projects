import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MSTTSP {
    private static int totalCost;
    private static List<Integer> path;

    private static void mstTSP(int[][] graph) {
        long inicio = System.nanoTime();
        int V = graph.length;
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];

        Arrays.fill(key, Integer.MAX_VALUE);
        key[0] = 0;
        parent[0] = -1;

        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet);
            mstSet[u] = true;

            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }

        totalCost = 0;
        path = new ArrayList<>();
        for (int i = 1; i < V; i++) {
            totalCost += graph[i][parent[i]];
            path.add(i);
        }

        totalCost += graph[1][parent[1]];
        path.add(1);

        // Inclui a última aresta de volta ao ponto inicial
        totalCost += graph[path.get(path.size() - 1)][0];

        path.add(0);

        System.out.println("Total Cost of TSP solution: " + totalCost);
        System.out.println("Path: " + path);
        long fim = System.nanoTime();
        long tempoDeExecucao = fim - inicio;
        System.out.println(tempoDeExecucao);
    }

    private static int minKey(int[] key, boolean[] mstSet) {
        int min = Integer.MAX_VALUE, minIndex = -1;

        for (int v = 0; v < key.length; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }

        return minIndex;
    }


    public static void main(String[] args) {
        //Uso: java MSTTSP inputs nomedoarquivo.txt
        String pastaInputs = args[0];
        String nomeArquivo = args[1];
        String caminhoArquivo = pastaInputs + System.getProperty("file.separator") + nomeArquivo;

        try {
            int[][] graph = lerMatrizDoArquivo(caminhoArquivo);
            mstTSP(graph);

            // Agora você pode acessar totalCost e path diretamente
            System.out.println("Total Cost: " + totalCost);
            System.out.println("Path: " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[][] lerMatrizDoArquivo(String nomeArquivo) throws IOException {
        List<int[]> linhas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.trim().isEmpty()) {  // Ignora linhas vazias
                    String[] valores = linha.split("\\s+");
                    int[] linhaMatriz = new int[valores.length];

                    for (int i = 0; i < valores.length; i++) {
                        try {
                            linhaMatriz[i] = Integer.parseInt(valores[i]);
                        } catch (NumberFormatException e) {
                            // Trata erros de conversão
                            System.err.println("Erro ao converter para inteiro: " + valores[i]);
                            e.printStackTrace();
                        }
                    }
                    linhas.add(linhaMatriz);
                }
            }
        }

        int[][] matriz = new int[linhas.size()][];
        for (int i = 0; i < linhas.size(); i++) {
            matriz[i] = linhas.get(i);
        }

        return matriz;
    }
}
