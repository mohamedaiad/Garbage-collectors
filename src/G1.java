import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class G1 {
    //    private file file = new file();
    static private List<List<String>> new_heap;
    static private int[][] notfree = new int[16][3]; // to store the non-free block in memory and the space it takes

    static public void launch(int size , String heapPath , String pointersPath , String rootsPath , String outputPath){
        int bytes_per_block = size / 16;

        // first do mark and sweep
        mark_and_sweep mark_and_sweep = new mark_and_sweep();
        int check = mark_and_sweep.launch(heapPath ,pointersPath , rootsPath , outputPath); // check if there is any error
        if(check == 0) return;

        // read the file that result from mark and sweep to edit it
        file file = new file();
        new_heap = file.Read(outputPath);

        int [][] free = makefree(size, bytes_per_block); // to store the free block in memory
        // check for size error
        if(free == null) return;
        int firstfree = 0; // to know from where to start
        while (free[firstfree][2] == 0) firstfree++;

        // write to new heap again after some modifications
        try {
            FileOutputStream fos = new FileOutputStream(outputPath ,false);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < new_heap.size(); i++) {

                // this is used to know the size of the object in the memory and move it to the suitable place
                int start = Integer.parseInt(new_heap.get(i).get(1));
                int end = Integer.parseInt(new_heap.get(i).get(2));
                int diff = end - start + 1;  // size of the object
                int j = firstfree;

                // look for suitable place
                while (j< 16 && free[j][2] < diff) {
                    j++;
                }

                // if the block gets empty , make it free to use it later
                int index = start / bytes_per_block;
                notfree[index][2] -= diff;
                if(notfree[index][2] == 0) {
                    free[index][2] = bytes_per_block;
                }

                // move the object to the new place in the heap
                start = free[j][0];
                end = start + diff - 1;

                // update array free
                free[j][0] += diff;             // the start of the next element in this block
                free[j][2] -= diff;             // free size in this block

                // write the object with the new place to the file
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

    static public int[][] makefree(int size,int bytes_per_block){

        int[][] free = new int[16][3];
        int counter =0,endBlock = bytes_per_block -1;

        // make the free and non-free arrays(initialize all the blocks are empty)
        for(int i=0;i<size;i+=bytes_per_block){
            free[counter][0] = i;
            free[counter][1] = i+endBlock;
            free[counter][2] = bytes_per_block;  // free bytes
            notfree[counter][0] = i;
            notfree[counter][1] = i+endBlock;
            notfree[counter][2] = 0;
            counter++;
        }

        // remove the used blocks from the free array (make the free space = 0)
        // save the actual used memory in the non-free array
        for(int i=0;i<new_heap.size();i++){
            int start = Integer.parseInt(new_heap.get(i).get(1));
            int end = Integer.parseInt(new_heap.get(i).get(2));
            int diff = end - start;
            int index = start / bytes_per_block;
            if(index >= 16){
                System.out.println("ERROR! there are some elements out of bound!");
                return null;
            }
            free[index][2] = 0;
            notfree[index][2] += diff +1 ;
        }
        return free;
    }

    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        String heapPath = args[1];
        String pointersPath = args[2];
        String rootsPath = args[3];
        String outputPath = args[4];
        launch(size , heapPath , pointersPath , rootsPath , outputPath);
    }
}
