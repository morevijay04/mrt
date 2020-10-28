package com.publictransport.mrt.algorithm.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.publictransport.mrt.algorithm.PathFinder;
import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.network.model.Network;
import com.publictransport.mrt.network.model.Path;
import com.publictransport.mrt.network.model.Vertex;

@Component
public class DjikstraShortestPathFinder implements PathFinder<Station> {
	@Override
	public Path<Station> find(Network<Station> network, Vertex<Station> source, Vertex<Station> destination) {
		if (source == null || destination == null ) {
			return Path.getUnreachablePath();
		}
		
		Set<Vertex<Station>> visited = new HashSet<>();
		Map<Vertex<Station>, Vertex<Station>> pathToVertext = new HashMap<>();
		PriorityQueue<Vertex<Station>> heap = prepareDataStructures(network, source, destination, pathToVertext);
		Vertex<Station> vertex = heap.poll();
		while (vertex != null) {
			for (Vertex<Station> toVertex : vertex.getAllEdgeVertices()) {
				if (toVertex.getCost().compareTo(vertex.getCost() + vertex.getEdgeCost(toVertex)) > 0) {
					toVertex.setCost(vertex.getCost() + vertex.getEdgeCost(toVertex));
					pathToVertext.put(toVertex, vertex);
					heap.remove(toVertex);
					heap.add(toVertex);
				}
			}
			visited.add(vertex);
			while (visited.contains(vertex)) {
				vertex = heap.poll();	
			}
			
		}
		return constructPath(source, destination, pathToVertext);
	}

	private PriorityQueue<Vertex<Station>> prepareDataStructures(Network<Station> network, Vertex<Station> source,
			Vertex<Station> destination, Map<Vertex<Station>, Vertex<Station>> pathToVertext) {
		PriorityQueue<Vertex<Station>> heap = new PriorityQueue<>(new Comparator<Vertex<Station>>() {
			@Override
			public int compare(Vertex<Station> o1, Vertex<Station> o2) {
				return o1.getCost().compareTo(o2.getCost());
			}
		});		
		setZeroCostOnAllStationsOnLocation(network, source);
		setZeroCostOnAllStationsOnLocation(network, destination);
		source.setCost(0d);
		
		heap.add(source);
		pathToVertext.put(source, null);
		return heap;
	}

	private Path<Station> constructPath(Vertex<Station> source, Vertex<Station> destination, Map<Vertex<Station>, Vertex<Station>> pathToVertext) {
		if (pathToVertext.containsKey(destination)) {
			Path<Station> route = new Path<Station>();
			route.setCost(destination.getCost());
			Stack<Station> stationStack = new Stack<Station>();
			stationStack.push(destination.getEntity());
			for (Vertex<Station> fromVertex = pathToVertext
					.get(destination); fromVertex != null; fromVertex = pathToVertext.get(fromVertex)) {
				stationStack.push(fromVertex.getEntity());
			}
			while (!stationStack.isEmpty()) {
				Station station = stationStack.pop();
				route.addElement(station);
			}
			return trimRoute(route);
		} else {
			return Path.getUnreachablePath();
		}
	}

	private Path<Station> trimRoute(Path<Station> route) {
		lTrim(route);
		rTrim(route);
		return route;
	}

	private void rTrim(Path<Station> route) {
		if (route.getPathElements().size() > 1) {
			Station toStation = route.getPathElements().get(route.getPathElements().size() - 1);
			int trimIdx = route.getPathElements().size() - 1;
			for (int i = route.getPathElements().size() - 2; i > 0; i--) {
				Station prevStation = route.getPathElements().get(i);
				if (!prevStation.getName().equalsIgnoreCase(toStation.getName())) {
					break;
				} else {
					trimIdx--;
				}
			}
			route.setPathElements(route.getPathElements().subList(0, trimIdx+1));
		}
	}

	private void lTrim(Path<Station> route) {
		if (route.getPathElements().size() > 1) {
			Station fromStation = route.getPathElements().get(0);

			int trimIdx = 0;
			for (int i = 1; i < route.getPathElements().size(); i++) {
				Station nextStation = route.getPathElements().get(i);
				if (!nextStation.getName().equalsIgnoreCase(fromStation.getName())) {
					break;
				} else {
					trimIdx++;
				}
			}
			route.setPathElements(route.getPathElements().subList(trimIdx, route.getPathElements().size()));
		}
	}

	private void setZeroCostOnAllStationsOnLocation(Network<Station> network, Vertex<Station> source) {

		List<Vertex> stationsAtSourceLocation = source.getAllEdgeVertices().stream()
				.filter(toVert -> source.getEntity().getName().equalsIgnoreCase(toVert.getEntity().getName()))
				.collect(Collectors.toList());
		stationsAtSourceLocation.add(source);
		stationsAtSourceLocation.forEach(from -> {
			stationsAtSourceLocation.forEach(to -> {
				from.addOrUpdateEdge(to, 0d);
				to.addOrUpdateEdge(from, 0d);
			});
		});
	}

	@Override
	public String getFindAction() {
		return "Shortest path";
	}

}
