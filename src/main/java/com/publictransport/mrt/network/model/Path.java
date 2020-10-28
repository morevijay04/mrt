package com.publictransport.mrt.network.model;

import java.util.ArrayList;
import java.util.List;

import com.publictransport.mrt.model.Station;

public class Path<T> {
	
	private List<T> pathElements = new ArrayList<>();
	
	private double cost;

	public List<T> getPathElements() {
		return pathElements;
	}
	
	public void setPathElements(List<T> pathElements) {
		this.pathElements = pathElements;
	}
	
	public void addElement(T element) {
		pathElements.add(element);
	}
	
	public double getCost() {
		return cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	private int size() {
		return pathElements.size();
	}
	
	@Override
	public String toString() {
		return "Path Cost:"+cost+ " Elements:"+pathElements;
	}

	public static Path<Station> getUnreachablePath() {
		Path<Station> route = new Path<Station>();
		route.setCost(-1);
		return route;
	}
}
