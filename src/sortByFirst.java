import java.util.ArrayList;
import java.util.List;

public class sortByFirst {

    // this function is used to sort the heap by the start index
    public List<List<String>> sort(List<List<String>> heap){
        List<List<String>> res = new ArrayList<>();
        res.add(heap.get(0));
        for(int i=1;i<heap.size();i++){
            int j;
            for(j=0;j< res.size();j++){
                if(Integer.parseInt(heap.get(i).get(1)) < Integer.parseInt(res.get(j).get(1))){
                    break;
                }
            }
            res.add(j, heap.get(i));
        }
        return res;
    }
}
