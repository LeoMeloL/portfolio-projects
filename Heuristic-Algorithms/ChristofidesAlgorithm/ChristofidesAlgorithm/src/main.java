import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		List<int[]> cityList = new ArrayList<int[]>();
		int distanceMatrix[][];

		int V = 0;  // Number of vertices in graph 
	    int E = 0;  // Number of edges in graph 
	    
		// Read input file line by line by using scanner
		try {
			Scanner scanner = new Scanner(new File("C:\\Users\\Leonardo\\Desktop\\tsp1_253.txt"));
			while (scanner.hasNextLine()) {
				// Clean white spaces
				String cleanResult = cleanInput(scanner.nextLine());
				String[] arr = cleanResult.split("\\s+");
				// Adding to linked list
				cityList.add(new int[] {Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2])});
				V++; // Increment the vertices 
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		E = (V*V)-V;

		distanceMatrix = new int[V][V];

	    int distance = 0;

	    //creating distance matrix(weighted-adjacency)
	    for(int i=0; i<V; i++) {
		    for(int k=0; k<V; k++) {
		    	// Create and edge if the vertices are different
		    	if(i!=k) {
			    	// Distance between i and k
			    	distance = (int)Math.round(Math.sqrt(Math.pow(cityList.get(i)[1]-cityList.get(k)[1], 2)+Math.pow(cityList.get(i)[2]-cityList.get(k)[2], 2)));
				    // Add to the 2d distance matrix later used for prim algorithm
			    	distanceMatrix[i][k] = distance;
		    	}
		    }
		}

	    //creating graph for given inputs
		Graph g=new Graph(V,E);

	    //getting Minumum spanning tree with prim algorithm
		edge [] primsResult = g.primMST(distanceMatrix) ;

		//finding odd degree vertices and creating match between them to create euler cycle
	    edge mst[] = g.findAndAddPerfectMatches(primsResult,cityList);;

	    //creating new graph for our cyclic mst
	    Graph g1 = new Graph(V);

	    for(int i=1; i<mst.length; i++) {
	    	g1.addEdge(mst[i].src, mst[i].dest);
	    }

	    //creating euler cycle from cyclic mst
        g1.createEulerCircuit();

        //deleting repeated vertex for final form
	    ArrayList<Integer> resultCircuit = g1.clearRepeatedCities(g1.eulerianCircuit);

        //calculating path distance
        int totalDistance = calculateTotalDistance(resultCircuit, distanceMatrix);

        //writing through output
        FileWriter fw = null;
		BufferedWriter bw=null;
		try {
			fw = new FileWriter(new File("./outputs/"+filename.replace("input","output")));
			bw = new BufferedWriter(fw);
			bw.write(totalDistance+"\n");
			for(int k=0; k<resultCircuit.size()-1; k++) {
				bw.write(resultCircuit.get(k)+"\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//clearing input for spaces
	public static String cleanInput(String input) {
		int counter = 0;
		while(input.charAt(counter) == ' ') {
			counter++;
		}
		return input.substring(counter);
	}
	//function to calculate distance for given path
	public static int calculateTotalDistance(ArrayList<Integer> resultCircuit, int distanceMatrix[][]) {
		int sum = 0;
		int counter = 0;

		while(counter < resultCircuit.size()-1) {
			sum += distanceMatrix[resultCircuit.get(counter)][resultCircuit.get(counter+1)];
			counter++;
		}
		return sum;
	}

}