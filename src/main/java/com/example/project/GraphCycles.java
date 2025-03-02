package com.example.project;

import java.util.ArrayList;
import java.util.List;

public class GraphCycles {
    private int[][] adjacencyMatrix;
    private ArrayList<Integer> gains;
    private int numVertices;
    private List<List<Integer>> cycles;

    public GraphCycles(int[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.numVertices = adjacencyMatrix.length;
        this.cycles = new ArrayList<>();
        this.gains = new ArrayList<>();
    }

    public void findCycles() {
        boolean[] visited = new boolean[numVertices];
        List<Integer> path = new ArrayList<>();

        for (int i = 0; i < numVertices; i++) {
            dfs(i, i, visited, path);
            visited[i] = true; // Mark the starting node as visited to avoid redundant cycles
        }
    }

    private void dfs(int start, int current, boolean[] visited, List<Integer> path) {
        visited[current] = true;
        path.add(current);

        for (int neighbor = 0; neighbor < numVertices; neighbor++) {
            if (adjacencyMatrix[current][neighbor] != 0) {
                if (neighbor == start) {
                    // A cycle is found
                    List<Integer> cycle = new ArrayList<>(path);

                    cycles.add(cycle);
                } else if (!visited[neighbor]) {
                    // Continue DFS
                    dfs(start, neighbor, visited, path);
                }
            }
        }

        // Backtrack
        path.removeLast();
        visited[current] = false;
    }

    void calculateCycle() {
        for (List<Integer> loop:cycles){
            int gain = 1;
            for (int i = 0; i < loop.size()-1; i++) {
                System.out.print(loop.get(i) + " ");
                gain *= adjacencyMatrix[loop.get(i)][loop.get(i+1)];
            }
            gain *= adjacencyMatrix[loop.getLast()][loop.getFirst()];
            gains.add(gain);
            System.out.println();

        }
    }

    public void printCycles() {
        if (cycles.isEmpty()) {
            System.out.println("No cycles found.");
        } else {
            System.out.println("Cycles found:");
            for (List<Integer> cycle : cycles) {
                System.out.println(cycle);
            }
        }
    }

    public static void main(String[] args) {
        // Example adjacency matrix
        int[][] adjacencyMatrix = {
                {0,2,0,0,2},
                {0,0,1,0,0},
                {0,1,0,1,0},
                {1,0,1,0,0},
                {0,0,0,1,1}
        };

        GraphCycles graph = new GraphCycles(adjacencyMatrix);
        graph.findCycles();
        graph.calculateCycle();
        for (int gain : graph.gains) {
            System.out.println(gain+" ");
        }
        graph.printCycles();
    }
}
