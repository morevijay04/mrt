package com.publictransport.mrt.network.strategy;

import org.springframework.stereotype.Component;

import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.network.model.Network;
import com.publictransport.mrt.network.model.Vertex;

@Component
public class PeakTravelTimeEdgeCosting extends AbstractMrtTravelTimeStrategy {
	
	@Override
	public void apply(Network<Station> network) {
		network.allVerticesCodes().forEach(code->{
			Vertex<Station> stationVertex = network.getVertex(code);
			String fromLine = stationVertex.getEntity().getLine();
			if (fromLine.equalsIgnoreCase("NS") || fromLine.equalsIgnoreCase("NE")) {
				setTravelTime(stationVertex, 12d,15d);
			} else {
				setTravelTime(stationVertex, 10d,15d);
			}
		});
	}

}
