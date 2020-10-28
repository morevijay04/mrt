package com.publictransport.mrt.network.strategy;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.network.model.Network;
import com.publictransport.mrt.network.model.Vertex;

@Component
public class NightTravelTimeEdgeCosting extends AbstractMrtTravelTimeStrategy{
	
	@Override
	public void apply(final Network<Station> network) {
		
		// remove those which are on close lines
		removeClosedLine(network);
		
		network.allVerticesCodes().forEach(code->{
			Vertex<Station> stationVertex = network.getVertex(code);
			String fromLine = stationVertex.getEntity().getLine();
			if (fromLine.equalsIgnoreCase("TE")) {
				setTravelTime(stationVertex, 8d,10d);
			} else {
				setTravelTime(stationVertex, 10d,10d);
			}
		});
	}

	private void removeClosedLine(final Network<Station> network) {
		network.removeVertices(network.allVerticesCodes()
				.stream()
				.filter(code->network.getVertex(code).getEntity().getLine().equalsIgnoreCase("DT") 
						|| network.getVertex(code).getEntity().getLine().equalsIgnoreCase("CG")
						|| network.getVertex(code).getEntity().getLine().equalsIgnoreCase("CE"))
				.map(network::getVertex)
				.collect(Collectors.toSet()));
	}

}
