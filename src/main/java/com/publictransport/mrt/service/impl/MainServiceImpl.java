package com.publictransport.mrt.service.impl;

import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publictransport.mrt.algorithm.PathFinder;
import com.publictransport.mrt.model.RouteReqest;
import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.network.NetworkBuilder;
import com.publictransport.mrt.network.model.Network;
import com.publictransport.mrt.network.model.Path;
import com.publictransport.mrt.network.strategy.TravelTimeStrategyController;
import com.publictransport.mrt.pool.StationPool;
import com.publictransport.mrt.service.MainService;

@Component
public class MainServiceImpl implements MainService {

	@Autowired
	StationPool stationPool;

	@Autowired
	NetworkBuilder<Station> networkBuilder;

	@Autowired
	List<PathFinder<Station>> pathFinder;
	
	@Autowired
	TravelTimeStrategyController travelTimeStrategyController;

	@Override
	public String getRoutes(RouteReqest routeRequest) {

		// step 1: construct graph of stations available by filtering station pool by request date
		Network<Station> network = constructMrtNetwork(routeRequest);
		
		// step 2: Loop over strategies to get multiple routes
		Map<PathFinder<Station>, Path<Station>> resultRoutes = findRoute(routeRequest, network);

		// step 3: Build response to the use using result from step 2
		return constructRespose(resultRoutes);
	}

	private Map<PathFinder<Station>, Path<Station>> findRoute(RouteReqest routeRequest, Network<Station> network) {
		Map<PathFinder<Station>, Path<Station>> resultRoutes = new HashMap<>();
		
		Station sourceStation = stationPool.getStationByName(routeRequest.getFrom());
		Station destStation = stationPool.getStationByName(routeRequest.getTo());
		if (!((sourceStation==null || destStation==null 
				|| network.getVertex(sourceStation.getCode()) == null || network.getVertex(destStation.getCode()) == null))) {
			// get source and destination vertex and pass to finders
			pathFinder.forEach(pathFinder -> {
				resultRoutes.put(pathFinder, pathFinder.find(network, network.getVertex(sourceStation.getCode()), network.getVertex(destStation.getCode())));
			});
		}
		return resultRoutes;
	}

	private Network<Station> constructMrtNetwork(RouteReqest routeRequest) {
		// step1 : get open stations at travel date
		Set<Station> availableStations = stationPool
				.getOpenStations(Date.from(routeRequest.getTravelTime().atZone(ZoneId.systemDefault()).toInstant()));
		// step2 : build network
		Network<Station> network = networkBuilder.buildNetwork(availableStations);
		// step3 : update travel time
		travelTimeStrategyController.getTravelTimeStrategy(routeRequest.getTravelTime()).apply(network);
		return network;
	}

	private String constructRespose(Map<PathFinder<Station>, Path<Station>> resultRoutes) {
		final StringBuilder respose = new StringBuilder();
		resultRoutes.keySet().forEach(pathFinder -> {
			if (resultRoutes.get(pathFinder).getCost() >= 0) {
				List<Station> pathElements = resultRoutes.get(pathFinder).getPathElements();
				respose.append("\n##"+pathFinder.getFindAction());
				respose.append("\nRoute: (" + pathElements.stream().map(Station::getCode).collect(Collectors.toList()) + ")");
				respose.append("\nTime taken: " + resultRoutes.get(pathFinder).getCost()+ " Minutes");
				String line = pathElements.get(0).getLine();
				for (int i = 1; i<pathElements.size(); i++) {
					if (!line.equalsIgnoreCase(pathElements.get(i).getLine())) {
						respose.append("\n Change from " + line + " line to "+ pathElements.get(i).getLine() +" line");
						line = pathElements.get(i).getLine();
					} else {
						respose.append("\n Take "+line+" line from " +pathElements.get(i-1).getName()+ " to "+ pathElements.get(i).getName());
					}
				}
				
				respose.append("\n--------------------------------------");
			}
		});
		
		if (respose.length() == 0) {
			return "No route found";
		}
		
		return respose.toString();
	}
}
