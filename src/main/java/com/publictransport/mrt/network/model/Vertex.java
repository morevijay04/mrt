package com.publictransport.mrt.network.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Vertex<T> implements Cloneable{

	private String id;

	private T entity;
	
	private Double cost = Double.MAX_VALUE;

	private Map<Vertex<T>, Double> edges = new HashMap<>();

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void addOrUpdateEdge(Vertex<T> toVertex, Double cost) {
		if (!this.equals(toVertex)) {
			edges.put(toVertex, cost);
		}
	}
	
	public void removeEdge(Vertex<T> toVertex) {
		edges.remove(toVertex);
	}
	
	public Double getEdgeCost(Vertex<T> toVertex) {
		return edges.get(toVertex);
	}
	
	public Set<Vertex<T>> getAllEdgeVertices() {
		return this.edges.keySet();
	}

	public int numberOfEdges() {
		return this.edges.size();
	}

	public boolean connectedTo(Vertex<T> toVertex) {
		return edges.containsKey(toVertex);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	public Double getCost() {
		return cost;
	}
	
	public void setCost(Double cost) {
		this.cost = cost;
	}
	
	
	@Override
	public Vertex<T> clone() {
	    try {
	        return (Vertex<T>) super.clone();
	    } catch (CloneNotSupportedException e) {
	        return null;
	    }
	}
}
