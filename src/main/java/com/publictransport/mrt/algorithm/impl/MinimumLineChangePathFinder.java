package com.publictransport.mrt.algorithm.impl;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publictransport.mrt.algorithm.PathFinder;
import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.network.NetworkBuilder;
import com.publictransport.mrt.network.model.Network;
import com.publictransport.mrt.network.model.Path;
import com.publictransport.mrt.network.model.Vertex;
import com.publictransport.mrt.network.strategy.TravelTimeStrategyController;


@Component
public class MinimumLineChangePathFinder implements PathFinder<Station>{

	@Autowired
	NetworkBuilder<Station> networkBuilder;
	
	@Autowired
	DjikstraShortestPathFinder shortestPathFinder; 
	
	@Autowired
	TravelTimeStrategyController strategyController;
	
	@Override
	public Path<Station> find(Network<Station> network, Vertex<Station> source, Vertex<Station> destination) {
		if (source == null || destination == null ) {
			return Path.getUnreachablePath();
		}
		Network<Station> freshNetwork = networkBuilder.buildNetwork(network.allVerticesCodes().stream().map(code->network.getVertex(code).getEntity()).collect(Collectors.toSet()));
		strategyController.getZeroOneTimeEdgeStrategy().apply(freshNetwork);
		Path<Station> route = shortestPathFinder.find(freshNetwork, freshNetwork.getVertex(source.getId()), freshNetwork.getVertex(destination.getId()));
		updateRouteCost(route, network);
		return route;
	}

	private void updateRouteCost(Path<Station> route, Network<Station> network) {
		double travelTime = 0d;
		if (route.getPathElements().size()>1) {
			int i = 0;
			Vertex<Station> startVertex  = network.getVertex(route.getPathElements().get(i++).getCode());
			
			while (i<route.getPathElements().size()) {
				Vertex<Station> toVertex = network.getVertex(route.getPathElements().get(i++).getCode());
				travelTime += startVertex.getEdgeCost(toVertex);
				startVertex  = toVertex;
			}
		} else if (route.getPathElements().isEmpty()){
			travelTime = Path.getUnreachablePath().getCost();
		}
		route.setCost(travelTime);
	}

	@Override
	public String getFindAction() {
		return "Minimum Line change path";
	}

}
