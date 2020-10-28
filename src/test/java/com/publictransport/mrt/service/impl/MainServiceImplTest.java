package com.publictransport.mrt.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.publictransport.mrt.algorithm.PathFinder;
import com.publictransport.mrt.algorithm.impl.DjikstraShortestPathFinder;
import com.publictransport.mrt.model.RouteReqest;
import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.network.impl.MrtNetworkBuilderImpl;
import com.publictransport.mrt.network.strategy.TravelTimeStrategyController;
import com.publictransport.mrt.pool.StationFactory;
import com.publictransport.mrt.pool.StationPool;
import com.publictransport.mrt.pool.exception.UnsupportedStationFileException;

class MainServiceImplTest {
	
	MainServiceImpl service;
	@BeforeEach
	void init() throws UnsupportedStationFileException, IOException, URISyntaxException {
		service = new MainServiceImpl();
		service.stationPool = new StationPool(new StationFactory(), "");
		service.networkBuilder = new MrtNetworkBuilderImpl();
		service.pathFinder = new ArrayList<PathFinder<Station>>();
		service.pathFinder.add(new DjikstraShortestPathFinder());
		//service.pathFinder.add(new MinimumLineChangePathFinder());
		service.travelTimeStrategyController = new TravelTimeStrategyController();
		
	}
	
	@Test
	void testGetRoutesShortestPath() {
		RouteReqest routeRequest = new RouteReqest();
		routeRequest.setFrom("Boon Lay");
		routeRequest.setTo("Little India");
		routeRequest.setTravelTime(LocalDateTime.parse("2019-01-31T08:00", DateTimeFormatter.ISO_DATE_TIME));
		
		String respose = service.getRoutes(routeRequest);
		
		String[] responseArray =  respose.split("\n");
		assertEquals(Double.parseDouble(responseArray[3].split("\\s")[2]), 150d);
	}

	@Test
	void testGetRoutesShortestPathSameSourceDest() {
		RouteReqest routeRequest = new RouteReqest();
		routeRequest.setFrom("Boon Lay");
		routeRequest.setTo("Boon Lay");
		routeRequest.setTravelTime(LocalDateTime.parse("2019-01-31T08:00", DateTimeFormatter.ISO_DATE_TIME));
		
		String respose = service.getRoutes(routeRequest);
		
		String[] responseArray =  respose.split("\n");
		assertEquals(Double.parseDouble(responseArray[3].split("\\s")[2]), 0d);
	}
	
	@Test
	void testGetRoutesShortestPathNotReachable() {
		RouteReqest routeRequest = new RouteReqest();
		routeRequest.setFrom("Boon Lay");
		routeRequest.setTo("Downtown");
		// Not reachable in night as DT is unavaialble
		routeRequest.setTravelTime(LocalDateTime.parse("2019-01-31T23:00", DateTimeFormatter.ISO_DATE_TIME));
		
		String respose = service.getRoutes(routeRequest);
		assertTrue(respose.equals("No route found"));
	}
	
	@Test
	void testGetRoutesUnknownStartLocation() {
		RouteReqest routeRequest = new RouteReqest();
		routeRequest.setFrom("A");
		routeRequest.setTo("Downtown");
		routeRequest.setTravelTime(LocalDateTime.parse("2019-01-31T23:00", DateTimeFormatter.ISO_DATE_TIME));
		
		String respose = service.getRoutes(routeRequest);
		assertTrue(respose.equals("No route found"));
	}
	
	@Test
	void testGetRoutesUnknownEndLocation() {
		RouteReqest routeRequest = new RouteReqest();
		routeRequest.setFrom("Boon Lay");
		routeRequest.setTo("A");
		routeRequest.setTravelTime(LocalDateTime.parse("2019-01-31T23:00", DateTimeFormatter.ISO_DATE_TIME));
		
		String respose = service.getRoutes(routeRequest);
		assertTrue(respose.equals("No route found"));
	}
}
