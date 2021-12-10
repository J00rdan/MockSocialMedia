package com.example.socialnetwork.Utils;

import java.util.ArrayList;

public class Graph {
    int V;
    ArrayList<ArrayList<Integer>> adj;
    ArrayList<ArrayList<Integer>> components;

    public Graph(int v) {
        V = v;
        adj = new ArrayList<>();
        for(int i = 0; i < V; i++)
            adj.add(i, new ArrayList<>());
    }

    public void addEdge(int src, int dest){
        adj.get(src).add(dest);
        adj.get(dest).add(src);
    }

    void DFS(int v, boolean[] visited, ArrayList<Integer> component){
        visited[v] = true;
        component.add(v);
        for(int x:adj.get(v)){
            if(!visited[x])
                DFS(x, visited, component);
        }
    }

    public int connectedComponents(){
        int count = 0;

        boolean[] visited = new boolean[V];
        for(int v = 0; v < V; v++){
            if(!visited[v]){
                ArrayList<Integer> component = new ArrayList<>();
                DFS(v, visited, component);
                count ++;
            }
        }
        return count;
    }

    public ArrayList<Integer> maxConnectedComponents(){
        int maxSize = 0;
        ArrayList<Integer> maxComponent = new ArrayList<>();

        boolean[] visited = new boolean[V];
        for(int v = 0; v < V; v++){
            if(!visited[v]){
                ArrayList<Integer> component = new ArrayList<>();
                DFS(v, visited, component);
                if(component.size() > maxSize) {
                    maxComponent = component;
                    maxSize = component.size();
                }
            }
        }
        return maxComponent;
    }
}
