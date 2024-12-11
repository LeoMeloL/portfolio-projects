public class edge implements Comparable<edge> { 
    int src, dest, weight; 

    // Comparator function used for sorting edges  
    // based on their weight 
    public int compareTo(edge compareEdge) 
    { 
        return this.weight-compareEdge.weight; 
    } 
} 