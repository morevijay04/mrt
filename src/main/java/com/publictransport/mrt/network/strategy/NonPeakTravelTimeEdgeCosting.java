package com.publictransport.mrt.network.strategy;

import org.springframework.stereotype.Component;

import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.network.model.Network;
import com.publictransport.mrt.network.model.Vertex;

@Component
public class NonPeakTravelTimeEdgeCosting extends AbstractMrtTravelTimeStrategy {

	@Override
	public void apply(Network<Station> network) {
		network.allVerticesCodes().forEach(code -> {
			Vertex<Station> stationVertex = network.getVertex(code);
			String fromLine = stationVertex.getEntity().getLine();
			if (fromLine.equalsIgnoreCase("DT") || fromLine.equalsIgnoreCase("TE")) {
				setTravelTime(stationVertex, 8d, 10d);
			} else {
				setTravelTime(stationVertex, 10d, 10d);
			}
		});
	}
}
