package com.publictransport.mrt.network.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.network.model.Network;

class MrtNetworkBuilderImplTest {

	MrtNetworkBuilderImpl builder = new MrtNetworkBuilderImpl();
	
	@Test
	void testConstructNetwork() {
		Set<Station> stations = new HashSet<>();
		Station station1 = new Station("name 1", "ST1", new GregorianCalendar(2020, Calendar.OCTOBER, 28).getTime(), "ST");
		stations.add(station1);
		Station station2 = new Station("name 2", "ST2", new GregorianCalendar(2020, Calendar.OCTOBER, 25).getTime(), "ST");
		stations.add(station2);
		Station station3 = new Station("name 3", "ST3", new GregorianCalendar(2020, Calendar.OCTOBER, 24).getTime(), "ST");
		stations.add(station3);
		Station station4 = new Station("name 4", "ST4", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "ST");
		stations.add(station4);
		Network<Station> network = builder.constructNetwork(stations);
		
		assertTrue(network.size() == 4);
		assertTrue(network.getVertex("ST1").connectedTo(network.getVertex("ST2")));
		assertTrue(!network.getVertex("ST1").connectedTo(network.getVertex("ST3")));
		assertTrue(!network.getVertex("ST1").connectedTo(network.getVertex("ST4")));
		
		assertTrue(network.getVertex("ST2").connectedTo(network.getVertex("ST1")));
		assertTrue(network.getVertex("ST2").connectedTo(network.getVertex("ST3")));
		assertTrue(!network.getVertex("ST2").connectedTo(network.getVertex("ST4")));
		
		assertTrue(network.getVertex("ST3").connectedTo(network.getVertex("ST2")));
		assertTrue(network.getVertex("ST3").connectedTo(network.getVertex("ST4")));
		assertTrue(!network.getVertex("ST3").connectedTo(network.getVertex("ST1")));
		
		assertTrue(network.getVertex("ST4").connectedTo(network.getVertex("ST3")));
		assertTrue(!network.getVertex("ST4").connectedTo(network.getVertex("ST2")));
		assertTrue(!network.getVertex("ST4").connectedTo(network.getVertex("ST1")));
	}
	
	@Test
	void testConstructEmptyNetwork() {
		Network<Station> network = builder.constructNetwork(new HashSet<Station>());
		assertTrue(network.size() == 0);
	}
	
	@Test
	void testConstructEmptyNetworkWithNullParam() {
		Network<Station> network = builder.constructNetwork(null);
		assertTrue(network.size() == 0);
	}

	
	@Test
	void testAddLineIntersectionToMap() {
		Set<Station> stations = new HashSet<>();
		Station station1 = new Station("Bay Front", "DT1", new GregorianCalendar(2020, Calendar.OCTOBER, 28).getTime(), "DT");
		stations.add(station1);
		Station station2 = new Station("Downtown", "DT2", new GregorianCalendar(2020, Calendar.OCTOBER, 25).getTime(), "DT");
		stations.add(station2);
		Station station3 = new Station("Chinatown", "DT3", new GregorianCalendar(2020, Calendar.OCTOBER, 24).getTime(), "DT");
		stations.add(station3);
		Station station4 = new Station("Fort Cannin", "DT4", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "DT");
		
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
		Station station12 = new Station("Marina Bay", "NS3", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "NS");
		stations.add(station12);
		Network<Station> network = builder.buildNetwork(stations);
		//builder.addLineIntersectionToMap(stations, network);
		
		//china town intersection
		assertTrue(network.getVertex(station3.getCode()).connectedTo(network.getVertex(station7.getCode())));
		assertTrue(network.getVertex(station7.getCode()).connectedTo(network.getVertex(station3.getCode())));

		//dhobi ghat intersection
		assertTrue(network.getVertex(station5.getCode()).connectedTo(network.getVertex(station9.getCode())));
		assertTrue(network.getVertex(station9.getCode()).connectedTo(network.getVertex(station5.getCode())));
		
		//Bay front intersection
		assertTrue(network.getVertex(station1.getCode()).connectedTo(network.getVertex(station11.getCode())));
		assertTrue(network.getVertex(station11.getCode()).connectedTo(network.getVertex(station1.getCode())));
		
		//china town not connected to 
		assertTrue(!network.getVertex(station3.getCode()).connectedTo(network.getVertex(station11.getCode())));
		assertTrue(!network.getVertex(station3.getCode()).connectedTo(network.getVertex(station12.getCode())));
		assertTrue(!network.getVertex(station3.getCode()).connectedTo(network.getVertex(station5.getCode())));
		assertTrue(!network.getVertex(station3.getCode()).connectedTo(network.getVertex(station10.getCode())));
	}
}
