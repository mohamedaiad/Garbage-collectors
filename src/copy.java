import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class copy {

    // this method is used to launch the program and calls the other methods
    static public void launch(String heapPath , String pointersPath , String rootsPath , String outputPath) {

        file file = new file();
        // read the files
        List<List<String>> heap = file.Read(heapPath);
        List<List<String>> pointers = file.Read(pointersPath);
        List<List<String>> roots = file.Read(rootsPath);
        if(heap == null || pointers == null || roots == null) return;

        // sort the heap
        sortByFirst sortHeap = new sortByFirst();
        heap = sortHeap.sort(heap);

        // check for overlap
        checkOverLap overlap = new checkOverLap();
        if(overlap.check(heap)){
            System.out.println("ERROR! There is overlap in the heap");
            return;
        }

        // make the graph from the mark and sweep class
        mark_and_sweep mark_and_sweep = new mark_and_sweep();
        Map<String , Vertex> vertices = mark_and_sweep.make_graph(heap , pointers);
        if(vertices == null) return;

        // to store the objects in the new order
        List<Vertex> new_heap = new ArrayList<>();

        // first put the roots in the list
        for(int i=0;i<roots.size();i++){
            Vertex vertex = vertices.get(roots.get(i).get(0));
            new_heap.add(vertex);
            vertex.setVisited(true);
        }

        // for each object add its children
        for(int i=0;i<new_heap.size();i++){
            List<Vertex> adjacent = new_heap.get(i).getAdjacent();
            // insert the vertices to the new heap
            for(int j=0;j<adjacent.size();j++){
                if(!adjacent.get(j).isVisited()) {
                    new_heap.add(adjacent.get(j));
                    adjacent.get(j).setVisited(true);
                }
            }
        }

        // write to the file new heap
        try {
            FileOutputStream fos = new FileOutputStream(outputPath ,false);
            PrintWriter pw = new PrintWriter(fos);

            int start,end =0, diff;
            for(int i=0;i<new_heap.size();i++){
                start = end + 1;
                if(i==0) start = 0;  // first time to write the first element is 0
                List<String> element = search_in_heap(heap , new_heap.get(i).getName());
                diff = Integer.parseInt(element.get(2)) - Integer.parseInt(element.get(1));
                end = start + diff;
                pw.print(element.get(0) + "," + start + "," + end + "\n");
            }
            pw.close();
        }
        catch (Exception e){
            System.out.println("An error occurred in writing to file.");
            return;
        }

        System.out.println("All done , 0 errors");

    }

    // this function is used to search for an object in the heap
    static public List<String> search_in_heap(List<List<String>> heap, String element){
        List<String> res = null;
        for(int i=0;i<heap.size();i++){
            if(heap.get(i).get(0).equals(element)) {
                res = heap.get(i);
                break;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String heapPath = args[0];
        String pointersPath = args[1];
        String rootsPath = args[2];
        String outputPath = args[3];
        launch(heapPath , pointersPath , rootsPath , outputPath);
    }
}
