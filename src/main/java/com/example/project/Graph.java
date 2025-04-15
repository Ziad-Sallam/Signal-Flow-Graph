package com.example.project;

import java.util.ArrayList;
import java.util.List;

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

    public List<List<Integer>> getCycles() {
        return cycles;
    }

    public List<List<Integer>> getPaths() {
        return paths;
    }
    public List<Integer> getPathsgains() {
        return pathsgains;
    }

    public List<Integer> getCyclesgains() {
        return cyclesgains;
    }


    public void findPaths() {
        paths.clear();
        boolean[] visited = new boolean[numVertices];
        List<Integer> path = new ArrayList<>();

        dfs(start, start, visited, path, false);
        visited[start] = true;
    }

    public void findCycles() {
        cycles.clear();
        boolean[] visited = new boolean[numVertices];
        List<Integer> path = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            dfs(i, i, visited, path, true);
            visited[i] = true;
        }
    }

    private void dfs(int start, int current, boolean[] visited, List<Integer> path, boolean findcycle) {
        visited[current] = true;
        path.add(current);

        for (int neighbor = 0; neighbor < numVertices; neighbor++) {
            if (graph[current][neighbor] != 0) {
                if (neighbor == this.end && !findcycle) {
                    List<Integer> currentpath = new ArrayList<>(path);
                    currentpath.add(neighbor);
                    paths.add(currentpath);
                } else if (neighbor == start && findcycle) {
                    List<Integer> currentcycle = new ArrayList<>(path);
                    currentcycle.add(neighbor);
                    cycles.add(currentcycle);
                } else if (!visited[neighbor]) {
                    dfs(start, neighbor, visited, path, findcycle);
                }
            }
        }

        path.remove(path.size() - 1);
        visited[current] = false;
    }

    public void calculateCyclesgain() {
        cyclesgains.clear();
        for (List<Integer> loop : cycles) {
            int gain = 1;
            for (int i = 0; i < loop.size() - 1; i++) {
                gain *= graph[loop.get(i)][loop.get(i + 1)];
            }
            cyclesgains.add(gain);
        }
    }

    public void calculatePathsgain() {
        pathsgains.clear();
        for (List<Integer> loop : paths) {
            int gain = 1;
            for (int i = 0; i < loop.size() - 1; i++) {
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
}
