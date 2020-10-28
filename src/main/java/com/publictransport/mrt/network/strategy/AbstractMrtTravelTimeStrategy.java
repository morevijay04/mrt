package com.publictransport.mrt.network.strategy;

import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.network.model.Network;
import com.publictransport.mrt.network.model.Vertex;

public abstract class AbstractMrtTravelTimeStrategy implements EdgeCostingStrategy<Station>{

	@Override
	public abstract void apply(Network<Station> network);

	protected void setTravelTime(Vertex<Station> stationVertex, double travelTime, double changeLineTime) {
		stationVertex.getAllEdgeVertices().forEach(edgeVert -> {
			if (edgeVert.getEntity().getLine().equalsIgnoreCase(stationVertex.getEntity().getLine())) {
				stationVertex.addOrUpdateEdge(edgeVert, travelTime);
			} else {
				// if not same, then its line change
				stationVertex.addOrUpdateEdge(edgeVert, changeLineTime);
			}
		});
	}
}
