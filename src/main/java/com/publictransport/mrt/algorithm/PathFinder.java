package com.publictransport.mrt.algorithm;

import org.springframework.stereotype.Service;

import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.network.model.Network;
import com.publictransport.mrt.network.model.Path;
import com.publictransport.mrt.network.model.Vertex;
import com.publictransport.mrt.network.strategy.EdgeCostingStrategy;

@Service
public interface PathFinder<T> {

	public Path<T> find(Network<T> network, Vertex<Station> source, Vertex<Station> destination);
	
	public String getFindAction();
}
