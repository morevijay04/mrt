package com.publictransport.mrt.algorithm.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.network.NetworkBuilder;
import com.publictransport.mrt.network.impl.MrtNetworkBuilderImpl;
import com.publictransport.mrt.network.model.Network;
import com.publictransport.mrt.network.model.Path;
import com.publictransport.mrt.network.strategy.NightTravelTimeEdgeCosting;
import com.publictransport.mrt.network.strategy.NonPeakTravelTimeEdgeCosting;
import com.publictransport.mrt.network.strategy.PeakTravelTimeEdgeCosting;
import com.publictransport.mrt.pool.StationFactory;
import com.publictransport.mrt.pool.StationPool;
import com.publictransport.mrt.pool.exception.UnsupportedStationFileException;

class DijkstraShortestPathFinderTest {

	private NetworkBuilder<Station> builder = new MrtNetworkBuilderImpl();
	
	private DjikstraShortestPathFinder finder = new DjikstraShortestPathFinder();
	
	private StationPool pool; 
	
	@BeforeEach
	void setup() throws UnsupportedStationFileException, IOException, URISyntaxException {
		pool = new StationPool(new StationFactory(), ""); 
	}
	
	@Test
	void testFindShortestPathPeakTime() {
		Set<Station> openStations = pool.getOpenStations(new GregorianCalendar(2019, Calendar.JANUARY, 01).getTime());
		Network<Station> network = builder.buildNetwork(openStations);
		new PeakTravelTimeEdgeCosting().apply(network);
		Path<Station> path = finder.find(network, network.getVertex("EW27"), network.getVertex("DT12"));
		assertEquals(150d, path.getCost());
	}
	
	@Test
	void testFindShortestPathNonPeak() {
		Set<Station> openStations = pool.getOpenStations(new GregorianCalendar(2019, Calendar.JANUARY, 01).getTime());
		Network<Station> network = builder.buildNetwork(openStations);
		new NonPeakTravelTimeEdgeCosting().apply(network);
		Path<Station> path = finder.find(network, network.getVertex("EW27"), network.getVertex("DT12"));
		assertEquals(134d, path.getCost());
	}
	
	
	@Test
	void testFindShortestPathNightTimeUnreachable() {
		Set<Station> openStations = pool.getOpenStations(new GregorianCalendar(2019, Calendar.JANUARY, 01).getTime());
		Network<Station> network = builder.buildNetwork(openStations);
		new NightTravelTimeEdgeCosting().apply(network);
		Path<Station> path = finder.find(network, network.getVertex("EW27"), network.getVertex("DT12"));
		// as DT is unreachable in the night
		assertEquals(-1d, path.getCost());
	}
	
	@Test
	void testFindShortestPathNightTime() {
		Set<Station> openStations = pool.getOpenStations(new GregorianCalendar(2019, Calendar.JANUARY, 01).getTime());
		Network<Station> network = builder.buildNetwork(openStations);
		new NightTravelTimeEdgeCosting().apply(network);
		Path<Station> path = finder.find(network, network.getVertex("EW27"), network.getVertex("CC22"));
		// as DT is unreachable in the night
		assertEquals(60d, path.getCost());
	}
	
	
	@Test
	void testFindShortestPathWithNoTimeConsideration() {
		Set<Station> openStations = pool.getOpenStations(new GregorianCalendar(2019, Calendar.JANUARY, 01).getTime());
		Network<Station> network = builder.buildNetwork(openStations);
		Path<Station> path = finder.find(network, network.getVertex("CC21"), network.getVertex("DT14"));
		assertEquals(8, path.getPathElements().stream().map(s -> s.getName()).distinct().count());
	}
	
	@Test
	void testFind() {
		
		Set<Station> stations = new HashSet<>();
		Station station1 = new Station("Bay Front", "DT1", new GregorianCalendar(2020, Calendar.OCTOBER, 28).getTime(), "DT");
		stations.add(station1);
		Station station2 = new Station("Downtown", "DT2", new GregorianCalendar(2020, Calendar.OCTOBER, 25).getTime(), "DT");
		stations.add(station2);
		Station station2x = new Station("Downtown x", "DT3", new GregorianCalendar(2020, Calendar.OCTOBER, 25).getTime(), "DT");
		stations.add(station2x);
		Station station3 = new Station("Chinatown", "DT4", new GregorianCalendar(2020, Calendar.OCTOBER, 24).getTime(), "DT");
		stations.add(station3);
		Station station4 = new Station("Fort Cannin", "DT5", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "DT");
		
		stations.add(station4);
		Station station5 = new Station("Dhobi Ghat", "EW1", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "EW");
		stations.add(station5);
		Station station6 = new Station("Clarke Quay", "EW2", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "EW");
		stations.add(station6);
		Station station7 = new Station("Chinatown", "EW3", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "EW");
		stations.add(station7);
		Station station8 = new Station("Outram", "EW4", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "EW");
		stations.add(station8);
		
		Station station9 = new Station("Dhobi Ghat", "NS1", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NS");
		stations.add(station9);
		Station station10 = new Station("Promenade", "NS2", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NS");
		stations.add(station10);
		Station station11 = new Station("Bay Front", "NS3", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NS");
		stations.add(station11);
		Station station12 = new Station("Marina Bay", "NS4", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NS");
		stations.add(station12);
		Network<Station> network = builder.buildNetwork(stations);
		new PeakTravelTimeEdgeCosting().apply(network);
		Path<Station> path = finder.find(network, network.getVertex("NS2"), network.getVertex("DT4"));
		assertEquals(47d, path.getCost());
	}
	
	@Test
	void testFindNoPath() {
		
		Set<Station> stations = new HashSet<>();
		Station station1 = new Station("Bay Front", "DT1", new GregorianCalendar(2020, Calendar.OCTOBER, 28).getTime(), "DT");
		stations.add(station1);
		Station station2 = new Station("Downtown", "DT2", new GregorianCalendar(2020, Calendar.OCTOBER, 25).getTime(), "DT");
		stations.add(station2);
		Station station2x = new Station("Downtown x", "DT3", new GregorianCalendar(2020, Calendar.OCTOBER, 25).getTime(), "DT");
		stations.add(station2x);
		Station station3 = new Station("Chinatown", "DT4", new GregorianCalendar(2020, Calendar.OCTOBER, 24).getTime(), "DT");
		stations.add(station3);
		Station station4 = new Station("Fort Cannin", "DT5", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "DT");
		
		stations.add(station4);
		Station station5 = new Station("Dhobi Ghat", "EW1", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "EW");
		stations.add(station5);
		Station station6 = new Station("Clarke Quay", "EW2", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "EW");
		stations.add(station6);
		Station station7 = new Station("Chinatown", "EW3", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "EW");
		stations.add(station7);
		Station station8 = new Station("Outram", "EW4", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "EW");
		stations.add(station8);
		
		Station station9 = new Station("Dhobi Ghat", "NS1", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NS");
		stations.add(station9);
		Station station10 = new Station("Promenade", "NS2", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NS");
		stations.add(station10);
		Station station11 = new Station("Bay Front", "NS3", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NS");
		stations.add(station11);
		Station station12 = new Station("Marina Bay", "NS4", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NS");
		stations.add(station12);
		
		Station station13 = new Station("Not Avaialable", "NA1", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NA");
		stations.add(station13);
		
		Network<Station> network = builder.buildNetwork(stations);
		new PeakTravelTimeEdgeCosting().apply(network);
		Path<Station> path = finder.find(network, network.getVertex("NS2"), network.getVertex("NA1"));
		assertEquals(-1d, path.getCost());
		assertTrue(path.getPathElements().size() == 0);
	}
	
	@Test
	void testFindSourceIsDest() {
		
		Set<Station> stations = new HashSet<>();
		Station station1 = new Station("Bay Front", "DT1", new GregorianCalendar(2020, Calendar.OCTOBER, 28).getTime(), "DT");
		stations.add(station1);
		Station station2 = new Station("Downtown", "DT2", new GregorianCalendar(2020, Calendar.OCTOBER, 25).getTime(), "DT");
		stations.add(station2);
		Station station2x = new Station("Downtown x", "DT3", new GregorianCalendar(2020, Calendar.OCTOBER, 25).getTime(), "DT");
		stations.add(station2x);
		Station station3 = new Station("Chinatown", "DT4", new GregorianCalendar(2020, Calendar.OCTOBER, 24).getTime(), "DT");
		stations.add(station3);
		Station station4 = new Station("Fort Cannin", "DT5", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "DT");
		
		stations.add(station4);
		Station station5 = new Station("Dhobi Ghat", "EW1", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "EW");
		stations.add(station5);
		Station station6 = new Station("Clarke Quay", "EW2", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "EW");
		stations.add(station6);
		Station station7 = new Station("Chinatown", "EW3", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "EW");
		stations.add(station7);
		Station station8 = new Station("Outram", "EW4", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "EW");
		stations.add(station8);
		
		Station station9 = new Station("Dhobi Ghat", "NS1", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NS");
		stations.add(station9);
		Station station10 = new Station("Promenade", "NS2", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NS");
		stations.add(station10);
		Station station11 = new Station("Bay Front", "NS3", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NS");
		stations.add(station11);
		Station station12 = new Station("Marina Bay", "NS4", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NS");
		stations.add(station12);
		
		Station station13 = new Station("Not Avaialable", "NA1", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NA");
		stations.add(station13);
		
		Network<Station> network = builder.buildNetwork(stations);
		new PeakTravelTimeEdgeCosting().apply(network);
		Path<Station> path = finder.find(network, network.getVertex("NS2"), network.getVertex("NS2"));
		assertEquals(0d, path.getCost());
		assertTrue(path.getPathElements().size() == 1);
	}

}
