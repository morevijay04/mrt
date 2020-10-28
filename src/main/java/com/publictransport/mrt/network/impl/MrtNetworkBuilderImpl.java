package com.publictransport.mrt.network.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.network.NetworkBuilder;
import com.publictransport.mrt.network.model.Network;
import com.publictransport.mrt.network.model.Vertex;

@Component
public class MrtNetworkBuilderImpl implements NetworkBuilder<Station> {

	@Override
	public Network<Station> buildNetwork(Set<Station> stations) {
		
		// step1: constrct network from line information
		Network<Station> mrtNetwork = constructNetwork(stations);
		
		// step2: find intersections and update network
		addLineIntersectionToMap(stations, mrtNetwork);

		return mrtNetwork;
	}

	void addLineIntersectionToMap(Set<Station> stations, Network<Station> mrtNetwork) {
		Map<String, List<Station>> intersectingStationMap = new HashMap<>();
		stations.forEach(s -> {
			if (intersectingStationMap.get(s.getName().toLowerCase()) == null) {
				intersectingStationMap.put(s.getName().toLowerCase(), new ArrayList<Station>());
			}
			intersectingStationMap.get(s.getName().toLowerCase()).add(s);
		});
		
		intersectingStationMap.keySet().forEach(location -> {
			if (intersectingStationMap.get(location).size() > 1) {
				intersectingStationMap.get(location)
				.forEach(currentStation -> {
					intersectingStationMap.get(location)
					.forEach(otherStation -> {
						mrtNetwork.getVertex(currentStation.getCode())
						.addOrUpdateEdge(mrtNetwork.getVertex(otherStation.getCode()), 0d);
					});	
				});
			}
		});
	}

	Network<Station> constructNetwork(Set<Station> stations) {
		Network<Station> mrtNetwork = new Network<Station>();
		
		if (stations == null || stations.isEmpty()) {
			return mrtNetwork;
		}
		// Step 1: sort stations by code, assumption is sorted code stations are connected by order
		List<Station> stationsList = new ArrayList<Station>(stations); 
		sortStationsByCode(stationsList);

		String currentLine = "";
		Station prevStation = null;
		
		for (Station station : stationsList) {
			//Step 2:  construct new vertext
			Vertex<Station> vertex = new Vertex<Station>();
			vertex.setEntity(station);
			vertex.setId(station.getCode());
			
			//Step 3: assign edge
			if (currentLine.equals(station.getLine())) {
				double cost = 1d; 
				// on same line add as an edge to previous station
				Vertex<Station> prevVertex = mrtNetwork.getVertex(prevStation.getCode());
				prevVertex.addOrUpdateEdge(vertex, cost);
				vertex.addOrUpdateEdge(prevVertex, cost);
			} else {
				currentLine = station.getLine();
			}
			mrtNetwork.addVertex(vertex);
			prevStation = station;
		}
		
		return mrtNetwork;
	}

	private void sortStationsByCode(List<Station> stationsList) {
		Collections.sort(stationsList, (s1,s2) -> { 
			if (s1.getLine().equalsIgnoreCase(s2.getLine())) {
				return new Integer(Integer.parseInt(s1.getCode().replace(s1.getLine(), "")))
						.compareTo(Integer.parseInt(s2.getCode().replace(s2.getLine(), "")));
			} else {
				return s1.getCode().compareTo(s2.getCode());	
			}
		});
	}

}
