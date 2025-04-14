package com.example.project;

import java.util.*;

public class NonTouchingLoops {
    public List<List<Integer>> findNonTouchingLoops(List<List<Integer>> loops) {
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

        // Find all combinations of non-touching loops
        for (int k = 2; k <= n; k++) {  // Start from combinations of 2 loops
            findIndependentSets(graph, 0, current, result, n, k);
        }

        return result;
    }

    private static boolean shareNode(List<Integer> loop1, List<Integer> loop2) {
        Set<Integer> set = new HashSet<>(loop1);
        for (int node : loop2) {
            if (set.contains(node)) return true;
        }
        return false;
    }

    private static void findIndependentSets(boolean[][] graph, int index, List<Integer> current,
                                            List<List<Integer>> result, int n, int targetSize) {
        // If we've found a combination of the target size
        if (current.size() == targetSize) {
            result.add(new ArrayList<>(current));
            return;
        }

        // If we can't reach the target size
        if (index == n || current.size() + (n - index) < targetSize) {
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
            findIndependentSets(graph, index + 1, current, result, n, targetSize);
            current.remove(current.size() - 1);
        }

        // Skip this loop
        findIndependentSets(graph, index + 1, current, result, n, targetSize);
    }
}