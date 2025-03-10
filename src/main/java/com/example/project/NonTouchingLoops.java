package com.example.project;

import java.util.*;

public class NonTouchingLoops {

    public static List<List<Integer>> findNonTouchingLoops(List<List<Integer>> loops) {
        int n = loops.size();
        boolean[][] graph = new boolean[n][n];

        // Construct adjacency matrix
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (shareNode(loops.get(i), loops.get(j))) {
                    graph[i][j] = graph[j][i] = true; // Mark as touching
                }
            }
        }

        List<List<Integer>> result = new ArrayList<>();
        List<Integer> current = new ArrayList<>();
        findMaxIndependentSets(graph, 0, current, result, n);
        return result;
    }

    private static boolean shareNode(List<Integer> loop1, List<Integer> loop2) {
        Set<Integer> set = new HashSet<>(loop1);
        for (int node : loop2) {
            if (set.contains(node)) return true;
        }
        return false;
    }

    private static void findMaxIndependentSets(boolean[][] graph, int index, List<Integer> current,
                                               List<List<Integer>> result, int n) {
        if (index == n) {
            if (current.size() > 1) { // Skip individual loops
                result.add(new ArrayList<>(current));
            }
            return;
        }

        // Check if the current loop is independent of selected loops
        boolean canSelect = true;
        for (int i : current) {
            if (graph[i][index]) {
                canSelect = false;
                break;
            }
        }

        // Include this loop if it is independent
        if (canSelect) {
            current.add(index);
            findMaxIndependentSets(graph, index + 1, current, result, n);
            current.remove(current.size() - 1);
        }

        // Skip this loop
        findMaxIndependentSets(graph, index + 1, current, result, n);
    }

}
