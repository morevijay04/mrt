package com.publictransport.mrt.network.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Network<T> implements Cloneable{

	private Map<String, Vertex<T>> vertices;

	public Network() {
		vertices = new HashMap<>();
	}

	public int size() {
		return vertices.size();
	}

	public Vertex<T> getVertex(String code) {
		return vertices.get(code);
	}

	public void addVertex(Vertex<T> vertex) {
		vertices.put(vertex.getId(), vertex);
	}

	public Set<String> allVerticesCodes() {
		return vertices.keySet();
	}

	public void removeVertices(Set<Vertex<T>> verticesToRemove) {
		verticesToRemove.forEach(vertex -> vertices.remove(vertex.getId()));
		// remove edges
		vertices.values().forEach(v -> {
			v.getAllEdgeVertices().removeAll(verticesToRemove);
		});
	}
	
	@Override
	public Network<T> clone() {
	    try {
	        return (Network<T>) super.clone();
	    } catch (CloneNotSupportedException e) {
	        return null;
	    }
	}
}
