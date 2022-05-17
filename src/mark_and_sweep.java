import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mark_and_sweep {

    static public int launch(String heapPath , String pointersPath , String rootsPath , String outputPath){

        file file = new file();
        // read the files
        List<List<String>> heap = file.Read(heapPath);
        List<List<String>> pointers = file.Read(pointersPath);
        List<List<String>> roots = file.Read(rootsPath);
        if(heap == null || pointers == null || roots == null) return 0;

        Vertex vertex;
        DFS depth_first_search = new DFS();
        sortByFirst sortHeap = new sortByFirst();
        checkOverLap overlap = new checkOverLap();


        // sort the heap
        heap = sortHeap.sort(heap);

        // check for overlap
        if(overlap.check(heap)){
            System.out.println("ERROR! There is overlap in the heap");
            return 0;
        }

        // make the graph
        Map<String , Vertex> vertices = make_graph(heap , pointers);
        if (vertices == null) return 0;

        //mark the visited nodes from file roots
        for(int i=0;i< roots.size();i++){
            vertex = vertices.get(roots.get(i).get(0));
            depth_first_search.dfs(vertex);
        }

        // write the output in the file "new-heap.csv"
        write(heap, vertices , outputPath);
        return 1;
    }

    static public Map<String , Vertex> make_graph(List<List<String>> heap, List<List<String>> pointers){
        Map<String , Vertex> vertices = new HashMap<>();
        Vertex vertex;

        // make the graph contains all the heap
        for(int i=0;i<heap.size();i++){

            // check for repeated id's in the heap file
            if(vertices.containsKey(heap.get(i).get(0))){
                System.out.println("ERROR! Repeated ID.");
                return null;
            }
            vertex = new Vertex(heap.get(i).get(0));
            vertices.put(vertex.getName() , vertex);
        }

        // put the pointers to vertices
        for(int i=0;i<pointers.size();i++){
            vertex = vertices.get(pointers.get(i).get(0));
            Vertex child = vertices.get(pointers.get(i).get(1));
            vertex.addNeighbour(child);
        }
        return vertices;
    }

    // this function is used to write the result to the file "new-heap.csv"
    static public void write(List<List<String>> heap , Map<String , Vertex> vertices , String outputPath){
        try {
            FileOutputStream fos = new FileOutputStream(outputPath , false);
            PrintWriter pw = new PrintWriter(fos);
            for(int i=0;i<heap.size();i++){
                StringBuilder sb = new StringBuilder();
                Vertex vertex = vertices.get(heap.get(i).get(0));
                if (vertex.isVisited()) {
                    sb.append(heap.get(i).get(0) + "," + heap.get(i).get(1) + "," + heap.get(i).get(2) + "\n");
                }
                else {
                    // do nothing
                }
                pw.print(sb);
            }
            pw.close();
        }
        catch (Exception e){
            System.out.println("An error occurred in writing to file.");
        }
    }

    public static void main(String[] args) {
        String heapPath = args[0];
        String pointersPath = args[1];
        String rootsPath = args[2];
        String outputPath = args[3];
        int check = launch(heapPath , pointersPath , rootsPath , outputPath);
        if(check == 1) System.out.println("All done , 0 errors");
    }
}
