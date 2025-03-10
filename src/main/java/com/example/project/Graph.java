package com.example.project;

import java.util.ArrayList;
import java.util.List;

import static com.example.project.NonTouchingLoops.findNonTouchingLoops;

public class Graph {

    private int[][] graph;
    private int start;
    private int end;
    private int numVertices;
    private List<List<Integer>> paths;
    private List<List<Integer>> cycles;
    private List<Integer> cyclesgains;
    private List<Integer> pathsgains;

    public Graph(int[][] adjancymatrix, int start, int end) {
        this.graph = adjancymatrix;
        this.start = start;
        this.end = end;
        this.numVertices = adjancymatrix.length;
        this.paths = new ArrayList<>();
        this.cycles = new ArrayList<>();
        this.cyclesgains = new ArrayList<>();
        this.pathsgains = new ArrayList<>();
    }

    public void findPaths() {
        boolean[] visited = new boolean[numVertices];
        List<Integer> path = new ArrayList<>();

        dfs(start, start, visited, path, false);
        visited[start] = true; // Mark the starting node as visited to avoid redundant cycles

    }

    public void findCycles() {
        boolean[] visited = new boolean[numVertices];
        List<Integer> path = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            dfs(i, i, visited, path, true);
            visited[i] = true; // Mark the starting node as visited to avoid redundant cycles
        }
    }

    private void dfs(int start, int current, boolean[] visited, List<Integer> path, boolean findcycle) {
        visited[current] = true;
        path.add(current);

        for (int neighbor = 0; neighbor < numVertices; neighbor++) {
            if (graph[current][neighbor] != 0) {
                if (neighbor == this.end && !findcycle) {  //search for path

                    List<Integer> currentpath = new ArrayList<>(path);
                    currentpath.add(neighbor);
                    paths.add(currentpath);

                } else if (neighbor == start && findcycle) {  //serch for cycles

                    List<Integer> currentcycle = new ArrayList<>(path);
                    currentcycle.add(neighbor);
                    cycles.add(currentcycle);

                } else if (!visited[neighbor]) {
                    dfs(start, neighbor, visited, path, findcycle);
                }
            }

        }

        path.removeLast();
        visited[current] = false;
    }

    void calculateCyclesgain() {
        for (List<Integer> loop : cycles) {
            int gain = 1;
            for (int i = 0; i < loop.size() - 1; i++) {
                gain *= graph[loop.get(i)][loop.get(i + 1)];
            }
//            gain *= graph[loop.getLast()][loop.getFirst()];
            cyclesgains.add(gain);

        }

    }

    void calculatePathsgain() {
        for (List<Integer> loop : paths) {
            int gain = 1;
            for (int i = 0; i < loop.size() - 2; i++) {
                gain *= graph[loop.get(i)][loop.get(i + 1)];
            }
            pathsgains.add(gain);

        }
    }


    public List<List<Integer>> nontouchinwithPath(List<List<Integer>> loops, List<Integer> ForwardPath) {
        List<List<Integer>> nontouching = new ArrayList<>();
        for (List<Integer> i : loops) {
            int found = 0;
            for (int j : i) {
                if (ForwardPath.contains(j)) {
                    found = 1;
                    break;
                }
            }
            if (found == 1) {
                continue;
            }
            nontouching.add(i);
        }
        return nontouching;
    }



    public static void main(String[] args) {
        // Example adjacency matrix
        int[][] adjacencyMatrix = {
                {0, 1, 0, 0, 0, 0, 0,0},
                {0, 0, 1, 0, 0, 0, 0,0},
                {0, 0, 0, 1, 0, 0, 0,0},
                {0, 0, 0, 0, 1, 0, 1,0},
                {0, 0, 0, 0, 0, 1, 0,0},
                {0, 0, 0, 0, 1, 0, 1,1},
                {0, 0, 1, 0, 0, 0, 0,1},
                {0,1,0,0,0,1,0,0}
        };

        Graph graph=new Graph(adjacencyMatrix,0,7);
        graph.findCycles();
        graph.findPaths();
        graph.calculatePathsgain();
        graph.calculateCyclesgain();
        List<List<Integer>> forwardpaths=graph.paths;
        List<List<Integer>> closedcloops=graph.cycles;

        System.out.println("Forward paths");
        for(List<Integer> path: forwardpaths ){
            System.out.println(path);

        }
        System.out.println("paths gain :"+graph.pathsgains);
        System.out.println("Closed loops");
        for(List<Integer> cycle: closedcloops ){
            System.out.println(cycle);
        }
//        for(int i=0 ; i<closedcloops.size() ; i++){
//            closedcloops.get(i).remove(closedcloops.get(i).size() - 1);
//        }
//
//        System.out.println("Closed loops");
//        for(List<Integer> cycle: closedcloops ){
//            System.out.println(cycle);
//        }

        List<List<Integer>> nontloops = findNonTouchingLoops(closedcloops);
        System.out.println("NON TOUCHING LOOOOOOOPS");
        for(List<Integer> cycle: nontloops ){

            System.out.println(cycle);
        }

        //System.out.println("cycles gain :"+graph.cyclesgains);
        //List<List<Integer>> nontouching1=graph.nontouchinwithPath(graph.cycles, graph.paths.get(0));
        // List<List<Integer>> nontouching2=graph.nontouchinwithPath(graph.cycles, graph.paths.get(1));
        //
        // System.out.println( "withfirstpath"+nontouching1);
        // System.out.println( "withsecondpath"+nontouching2);


    }
}