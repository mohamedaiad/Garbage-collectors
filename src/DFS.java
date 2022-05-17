import java.util.Stack;

public class DFS {
    Stack<Vertex> stack = new Stack<>();

    // this function is used to make depth first search from the roots file and make the vertices visited
    // the unvisited nodes are considered garbage
    public void dfs(Vertex vertex){
        stack.push(vertex);
        vertex.setVisited(true);
        while (!stack.empty()){
            Vertex actualVertex = stack.pop();
            for(Vertex v : actualVertex.getAdjacent()){
                if(!v.isVisited()){
                    v.setVisited(true);
                    stack.push(v);
                }
            }
        }
    }
}
