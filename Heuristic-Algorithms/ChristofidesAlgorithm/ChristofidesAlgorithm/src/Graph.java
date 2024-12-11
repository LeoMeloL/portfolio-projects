import java.util.*;

public class Graph {

	int V, E;    // V-> no. of vertices & E->no.of edges
	private LinkedList<Integer>[] adj; // adjacency list
	ArrayList<Integer> eulerianCircuit = new ArrayList<Integer>();
	
	Graph(int v, int e) {
        this.V = v;
        this.E = e;

    }
	Graph(int numOfVertices) {
        // initialise vertex count
        this.V = numOfVertices;

        // initialise adjacency list
        initGraph();
    }
	
	void initGraph() {
	        adj = new LinkedList[V];
	        for (int i = 0; i < V; i++)
	        { 
	            adj[i] = new LinkedList<Integer>();
	        } 
	    } 

	 // add edge u-v 
	void addEdge(Integer u, Integer v) {
	        adj[u].add(v); 
	        adj[v].add(u); 
	    } 
	  
	    // This function removes edge u-v from graph. 
	void removeEdge(Integer u, Integer v) {
	        adj[u].remove(v); 
	        adj[v].remove(u); 
	    }

			//***Eulerian Cycle******

	    /* The main function that print Eulerian Trail.
	       It first finds an odd degree vertex (if there  
	       is any) and then calls printEulerUtil() to 
	       print the path */
	void createEulerCircuit() {
	        // Find a vertex with odd degree 
	        Integer u = 0; 
	        for (int i = 0; i < V; i++)
	        { 
	            if (adj[i].size() % 2 == 1) 
	            { 
	                u = i; 
	                break; 
	            } 
	        }
	        // Print tour starting from odd v 
	        eulerUtil(u);
	    } 
	  
	    // Print Euler tour starting from vertex u 
	void eulerUtil(Integer u) {
	        // Recur for all the vertices adjacent to this vertex 
	        for (int i = 0; i < adj[u].size(); i++) 
	        { 
	            Integer v = adj[u].get(i); 
	            // If edge u-v is a valid next edge 
	            if (isValidNextEdge(u, v))  
	            { 
	                //System.out.print(u + "-" + v + " ");
	                eulerianCircuit.add(u);
	                eulerianCircuit.add(v);
	                // This edge is used so remove it now 
	                removeEdge(u, v);  
	                eulerUtil(v);
	            } 
	        } 
	    }
	    
	    // The function to check if edge u-v can be 
	    // considered as next edge in Euler Tout 
	boolean isValidNextEdge(Integer u, Integer v) {
	        // The edge u-v is valid in one of the 
	        // following two cases: 
	  
	        // 1) If v is the only adjacent vertex of u  
	        // ie size of adjacent vertex list is 1 
	        if (adj[u].size() == 1) { 
	            return true; 
	        } 
	  
	        // 2) If there are multiple adjacents, then 
	        // u-v is not a bridge Do following steps  
	        // to check if u-v is a bridge 
	        // 2.a) count of vertices reachable from u 
	        boolean[] isVisited = new boolean[this.V];
	        int count1 = dfsCount(u, isVisited); 
	  
	        // 2.b) Remove edge (u, v) and after removing 
	        //  the edge, count vertices reachable from u 
	        removeEdge(u, v); 
	        isVisited = new boolean[this.V];
	        int count2 = dfsCount(u, isVisited); 
	  
	        // 2.c) Add the edge back to the graph 
	        addEdge(u, v); 
	        return (count1 > count2) ? false : true; 
	    } 
	  
	    // A DFS based function to count reachable 
	    // vertices from v 
	int dfsCount(Integer s, boolean[] isVisited) {
	    	int count=0;
			// Initially mark all vertices as not visited

			// Create a stack for DFS
			Stack<Integer> stack = new Stack<>();

			// Push the current source node
			stack.push(s);

			while(stack.empty() == false)
			{
				// Pop a vertex from stack and print it
				s = stack.peek();
				stack.pop();

				// Stack may contain same vertex twice. So
				// we need to print the popped item only
				// if it is not visited.
				if(isVisited[s] == false)
				{
					//System.out.print(s + " ");
					isVisited[s]= true;
					count++;
				}

				// Get all adjacent vertices of the popped vertex s
				// If a adjacent has not been visited, then push it
				// to the stack.
				Iterator<Integer> itr = adj[s].iterator();

				while (itr.hasNext())
				{
					int v = itr.next();
					if(!isVisited[v]) {
						stack.push(v);
					}
				}
			}
			return count;
		}

				//******Hamiltonian Cycle*******
	ArrayList<Integer> clearRepeatedCities(ArrayList<Integer> cities) {
		// Find and remove duplicate cities
		int[] citiesArray = new int[V];
		ArrayList<Integer> resultCircuit = new ArrayList<Integer>();
		for(int i=0; i<cities.size(); i++) {
			citiesArray[cities.get(i)]++;
			if(citiesArray[cities.get(i)] == 1) {
				resultCircuit.add(cities.get(i));
			}
		}
		resultCircuit.add(resultCircuit.get(0));
		return resultCircuit;
	}

				//*****Perfect Matching******
	edge[] findAndAddPerfectMatches(edge[] mst,List<int[]> citylist){
    	int[] neighbourCounterOnMST = new int[V];
    	
    	for(int i = 1 ; i < mst.length ; i++) {
    		int src = mst[i].src;
            int dest = mst[i].dest;
            neighbourCounterOnMST[src]++;
            neighbourCounterOnMST[dest]++;
    	}
    	
    	ArrayList<edge> newEdgesForOddVertexs = new ArrayList<edge>();
    	List<int[]> oddDegreVertex = new ArrayList<int[]>();
    	
    	for(int i = 0 ; i < neighbourCounterOnMST.length ; i++) {
    		if(neighbourCounterOnMST[i] % 2 == 1) {
    			oddDegreVertex.add(citylist.get(i));
    		}
    	}
    	findMatchesWithNearestNeighbour(oddDegreVertex,newEdgesForOddVertexs);
    	
    	//merging new edges into mst so all nodes have even number edge now
    	edge[] newEdges = newEdgesForOddVertexs.toArray(new edge[0]);
    	int fal = mst.length-1;        //determines length of firstArray  
    	int sal = newEdges.length;   //determines length of secondArray  
    	edge[] result = new edge[fal + sal];  //resultant array of size first array and second array  
    	System.arraycopy(mst, 0, result, 0, fal);  
    	System.arraycopy(newEdges, 0, result, fal, sal);

    	int[] neighbourCounterOnMST2 = new int[V];
        
        for (int i = 1; i < fal+sal; ++i) {
        	int src = result[i].src;
            int dest = result[i].dest;
            neighbourCounterOnMST2[src]++;
            neighbourCounterOnMST2[dest]++;
             
        }
        return result;
    }

	void findMatchesWithNearestNeighbour(List<int[]> oddDegreVertex, ArrayList<edge> newEdgesForOddVertexs) {
		// TODO Auto-generated method stub
		int distance=0,min=Integer.MAX_VALUE,nextcityIndex=0,indexForRemove=0;
		edge tempEdge;
		
		int[] temp,temp2;
		for(int i=0 ;  i < oddDegreVertex.size() ;i=nextcityIndex) {
			
			temp=oddDegreVertex.get(i);
			
			oddDegreVertex.remove(i);
			
			for (int k = 0; k < oddDegreVertex.size(); k++) {
				temp2 = oddDegreVertex.get(k);
				
				distance = (int) Math.round(Math.sqrt(Math.pow(temp[1] - temp2[1], 2) + Math.pow(temp[2] - temp2[2], 2)));
				
				if(distance<min ) {
					min=distance;
					nextcityIndex=0;
					indexForRemove=k;
				}
			}
			
			temp2=oddDegreVertex.get(indexForRemove);
			tempEdge = new edge();
			tempEdge.src = temp[0];
			tempEdge.dest = temp2[0];
			tempEdge.weight = min;
			newEdgesForOddVertexs.add(tempEdge);
			
			min=Integer.MAX_VALUE;
			oddDegreVertex.remove(indexForRemove);
			
			if(oddDegreVertex.size()==2){
				tempEdge = new edge();
				tempEdge.src = oddDegreVertex.get(0)[0];
				tempEdge.dest = oddDegreVertex.get(1)[0];
				tempEdge.weight = (int) Math.round(Math.sqrt(Math.pow(oddDegreVertex.get(0)[1] - oddDegreVertex.get(1)[1], 2) + Math.pow(oddDegreVertex.get(0)[2] - oddDegreVertex.get(1)[2], 2)));
				newEdgesForOddVertexs.add(tempEdge);
				break;
			}

		}
		
	}

				//******Prim Algorithm****

	// A utility function to find the vertex with minimum key
	// value, from the set of vertices not yet included in MST
	int minKey(int key[], Boolean mstSet[]) {
		// Initialize min value
		int min = Integer.MAX_VALUE, min_index = -1;

		for (int v = 0; v < V; v++)
			if (mstSet[v] == false && key[v] < min) {
				min = key[v];
				min_index = v;
			}

		return min_index;
	}

	// A utility function to print the constructed MST stored in
	// parent[]
	edge[] getMST(int parent[], int graph[][]) {
		edge[] mst = new edge[V];
		for (int i = 1; i < V; i++) {
			mst[i]=new edge();
			mst[i].src=parent[i];
			mst[i].dest=i;
			mst[i].weight=graph[i][parent[i]];
		}
		return mst;
	}

	// Function to construct and print MST for a graph represented
	// using adjacency matrix representation
	edge[] primMST(int graph[][]) {
		// Array to store constructed MST
		int parent[] = new int[V];

		// Key values used to pick minimum weight edge in cut
		int key[] = new int[V];

		// To represent set of vertices included in MST
		Boolean mstSet[] = new Boolean[V];

		// Initialize all keys as INFINITE
		for (int i = 0; i < V; i++) {
			key[i] = Integer.MAX_VALUE;
			mstSet[i] = false;
		}

		// Always include first 1st vertex in MST.
		key[0] = 0; // Make key 0 so that this vertex is
		// picked as first vertex
		parent[0] = -1; // First node is always root of MST

		// The MST will have V vertices
		for (int count = 0; count < V - 1; count++) {
			// Pick thd minimum key vertex from the set of vertices
			// not yet included in MST
			int u = minKey(key, mstSet);

			// Add the picked vertex to the MST Set
			mstSet[u] = true;

			// Update key value and parent index of the adjacent
			// vertices of the picked vertex. Consider only those
			// vertices which are not yet included in MST
			for (int v = 0; v < V; v++)

				// graph[u][v] is non zero only for adjacent vertices of m
				// mstSet[v] is false for vertices not yet included in MST
				// Update the key only if graph[u][v] is smaller than key[v]
				if (graph[u][v] != 0 && mstSet[v] == false && graph[u][v] < key[v]) {
					parent[v] = u;
					key[v] = graph[u][v];
				}
		}

		// print the constructed MST
		return getMST(parent, graph);
	}
}