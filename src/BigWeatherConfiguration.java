import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BigWeatherConfiguration {
    private int dynoNumber;
    private int numOfPossibleBonds;
    private int bucketCost;
    private int bondCost;
    private final ArrayList<ArrayList<Integer>> adjacencyList = new ArrayList<>();
    private boolean[] visited;
    private int ccNum = 1;
    private int nodesPerComponent = 1;
    private Set<List<Integer>> edgesPerComponent = new HashSet<>();
    private int[] prev;

    public void readFile(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            int numLines = 0;
            while ((line = reader.readLine()) != null) {
                numLines++;
                if (numLines == 1) {
                    String[] data = line.split(" ");
                    dynoNumber = Integer.parseInt(data[0]);
                    numOfPossibleBonds = Integer.parseInt(data[1]);
                    bucketCost = Integer.parseInt(data[2]);
                    bondCost = Integer.parseInt(data[3]);
                    for (int i = 0; i < dynoNumber + 1; i++) {
                    adjacencyList.add(new ArrayList<>());
                }

                    continue;
                }
                String[] pair = line.split(" ");
                int dyno1 = Integer.parseInt(pair[0]);
                int dyno2 = Integer.parseInt(pair[1]);
                adjacencyList.get(dyno1).add(dyno2);
                adjacencyList.get(dyno2).add(dyno1);
                visited = new boolean[dynoNumber + 1];
                prev = new int[dynoNumber + 1];
            }
        } catch (IOException e) {
            System.out.println("Error processing the file! ");
        }

    }

    private long[] DFS() {
        long[] connectedAndCombinations = new long[2];
        long numberOfCombinations = 1;
        for (int i = 1; i < visited.length; i++) {
            nodesPerComponent = 1;
            if (!visited[i]) {
                prev[i] = 0;
                explore(adjacencyList, i);

                if (bucketCost == bondCost) {
                    numberOfCombinations *= nodesPerComponent * countSpanningTrees(nodesPerComponent, edgesPerComponent) * Math.pow(2, (nodesPerComponent - 1));
                } else if (bucketCost < bondCost) {
                    numberOfCombinations = 1;

                } else {
                    numberOfCombinations *= nodesPerComponent * countSpanningTrees(nodesPerComponent, edgesPerComponent);
                }
                ccNum++;
                edgesPerComponent = new HashSet<>();

            }
        }


        connectedAndCombinations[0] = ccNum - 1;
        connectedAndCombinations[1] = numberOfCombinations;
        return connectedAndCombinations;
    }


    private void explore(ArrayList<ArrayList<Integer>> adjacencyList, Integer node) {
        visited[node] = true;
        for (int adjNode : adjacencyList.get(node)) {
            List<Integer> pair;
            if (node > adjNode) {
                pair = List.of(node, adjNode);
            } else {
                pair = List.of(adjNode, node);
            }
            edgesPerComponent.add(pair);
            if (!visited[adjNode]) {
                prev[adjNode] = node;
                nodesPerComponent++;
                explore(adjacencyList, adjNode);

            }
        }
    }


    private static long countSpanningTrees(int size, Set<List<Integer>> edges) {
        long[][] laplacian = new long[size][size];

        for (List<Integer> edge : edges) {
            int u = (edge.get(0) - 1) % size;
            int v = (edge.get(1) - 1) % size;
            laplacian[u][u]++;
            laplacian[v][v]++;
            laplacian[u][v]--;
            laplacian[v][u]--;
        }

        return determinant(removeRowCol(laplacian, size - 1), size - 1);
    }



    private static long[][] removeRowCol(long[][] matrix, int n) {
        long[][] result = new long[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                result[i][j] = matrix[i][j];
        return result;
    }




    private static long determinant(long[][] mat, int n) {
        long mod = 1_000_000_007;
        long det = 1;
        for (int i = 0; i < n; i++) {
            int pivot = i;
            while (pivot < n && mat[pivot][i] == 0) pivot++;
            if (pivot == n) return 0;
            if (i != pivot) {
                long[] tmp = mat[i];
                mat[i] = mat[pivot];
                mat[pivot] = tmp;
                det = -det;
            }
            det = (det * mat[i][i]) % mod;
            long inv = modInverse(mat[i][i], mod);
            for (int j = i + 1; j < n; j++) {
                long factor = (mat[j][i] * inv) % mod;
                for (int k = i; k < n; k++) {
                    mat[j][k] = (mat[j][k] - factor * mat[i][k]) % mod;
                    if (mat[j][k] < 0) mat[j][k] += mod;
                }
            }
        }
        return (det + mod) % mod;
    }



    private static long modInverse(long a, long mod) {
        return BigInteger.valueOf(a).modInverse(BigInteger.valueOf(mod)).longValue();
    }




    public void computeStatistics() {
        long totalCost;
        long numCheapestSolutions;
        if (bucketCost < bondCost) {
            totalCost = bucketCost * dynoNumber;
            numCheapestSolutions = 1;
        } else {
            long[] componentsCombinations;
            componentsCombinations = DFS();
            numCheapestSolutions = componentsCombinations[1];
            long components = componentsCombinations[0];
            totalCost = components * bucketCost + (dynoNumber - components) * bondCost;
        }

            System.out.println("Total cost: "+totalCost);
            System.out.println("Number of cheapest solutions: "+ numCheapestSolutions);
            System.out.println("Visualising one cheapest solution... ");
            Visualizer visualizer = new Visualizer();
            if(bucketCost > bondCost){
                visualizer. drawTreeStructure(prev);
            }else{
                visualizer.drawNodes(dynoNumber);
            }



    }
}
