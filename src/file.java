import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class file {

    // this function is used to read from the files and return the result as list of list of string
    public List<List<String>> Read(String name){
        List<List<String>> res = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(name));
            String data;
            while ((data = reader.readLine()) != null){
                List<String> words = List.of(data.split(","));
                res.add(words);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("An error occurred in reading the files.");
            return null;
        }
        return res;
    }
}
