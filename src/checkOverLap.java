import java.util.List;

public class checkOverLap {

    // this function takes sorted heap and check if there is overlap between the indexes or not
    public boolean check(List<List<String>> heap){
        for(int i=1;i<heap.size();i++){
            if(Integer.parseInt(heap.get(i).get(1)) < Integer.parseInt(heap.get(i-1).get(2))){
                return true;
            }
        }
        return false;
    }
}
