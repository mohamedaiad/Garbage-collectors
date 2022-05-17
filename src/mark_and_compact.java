import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class mark_and_compact {

    static public void launch(String heapPath , String pointersPath , String rootsPath , String outputPath){

        // first do mark and sweep
        mark_and_sweep mark_and_sweep = new mark_and_sweep();
        int check = mark_and_sweep.launch(heapPath ,pointersPath , rootsPath , outputPath); // check if there is any error
        if(check == 0) return;

        // read the file that result from the mark and sweep to modify it
        file file = new file();
        List<List<String>> new_heap = file.Read(outputPath);

        // write to new heap again after some modifications
        try {
            FileOutputStream fos = new FileOutputStream(outputPath ,false);
            PrintWriter pw = new PrintWriter(fos);
            int diff,start,end = 0;
            for(int i=0;i<new_heap.size();i++){
                if(i==0){

                    // start from the first object in use
//                    start = Integer.parseInt(new_heap.get(i).get(1));
//                    end = Integer.parseInt(new_heap.get(i).get(2));

                    // start from 0
                    start = 0;
                    end = Integer.parseInt(new_heap.get(i).get(2)) - Integer.parseInt(new_heap.get(i).get(1));
                }
                else {

                    // get the difference between the start of the current object and the updated end of the last object
                    // -1 to go to the next place
                    diff = Integer.parseInt(new_heap.get(i).get(1)) - end - 1;

                    // compact them
                    start = Integer.parseInt(new_heap.get(i).get(1)) - diff;
                    end = Integer.parseInt(new_heap.get(i).get(2)) - diff;
                }
                pw.print(new_heap.get(i).get(0) + "," + start + "," + end + "\n");
            }
            pw.close();
        }
        catch (Exception e){
            System.out.println("An error occurred in writing to file.");
            return;
        }

        System.out.println("All done , 0 errors");
    }

    public static void main(String[] args) {
        String heapPath = args[0];
        String pointersPath = args[1];
        String rootsPath = args[2];
        String outputPath = args[3];
        launch(heapPath , pointersPath , rootsPath , outputPath);
    }
}
