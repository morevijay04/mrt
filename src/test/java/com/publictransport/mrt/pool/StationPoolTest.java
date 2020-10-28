package com.publictransport.mrt.pool;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.pool.exception.IncorrectInputDataException;
import com.publictransport.mrt.pool.exception.UnsupportedStationFileException;

class StationPoolTest {
	
	@InjectMocks
	private StationPool stationPool;
	
	@Mock
	private StationFactory factory; 
	
	 @BeforeEach
	public void initMocks(){
	    MockitoAnnotations.initMocks(this);
	}

	@Test
	void buildStationsPooltest() throws UnsupportedStationFileException, IOException, URISyntaxException, IncorrectInputDataException {
		Mockito.when(factory.createStation(Mockito.anyString())).thenAnswer(new Answer<Station>() {
		    private int count = 0;

		    public Station answer(InvocationOnMock invocation) {
		    	return new Station("name_"+ count, "NE" +(count++), new Date(), "NE");
		    }
		});
		
		stationPool.buildStationPool("StationMap.csv");
		assertTrue(stationPool.size() == 166);
	}
	
	@Test
	void buildStationsPoolUnsupportedFiletest() {
		int initialSize = stationPool.size();
		try {
			stationPool.buildStationPool("stationMap.xls");
		} catch (Exception e) {
			assertTrue(e.getClass().equals(UnsupportedStationFileException.class));
		}
		assertTrue(stationPool.size() == initialSize);
	}
	
	@Test
	void buildStationsPoolFileNotFoundtest() {
		int initialSize = stationPool.size();
		try {
			stationPool.buildStationPool("unknown.csv");
		} catch (Exception e) {
			assertTrue(e.getClass().equals(FileNotFoundException.class));
		}
		assertTrue(stationPool.size() == initialSize);
	}
	
	@Test
	void supportedFileFormatTest() {
		assertTrue(stationPool.isSupportedFileFormat("StationMap.csv") == true);
		assertTrue(stationPool.isSupportedFileFormat("StationMap.xls") == false);
		assertTrue(stationPool.isSupportedFileFormat("StationMap.doc") == false);
		assertTrue(stationPool.isSupportedFileFormat(null) == false);
	}
	
	
	@Test
	void testGetOpenStations() throws UnsupportedStationFileException, IOException, URISyntaxException {
		stationPool = new StationPool(factory, "");
		Station station1 = new Station("name 1", "ST1", new GregorianCalendar(2020, Calendar.OCTOBER, 28).getTime(), "ST");
		stationPool.addStation(station1);
		Station station2 = new Station("name 2", "ST2", new GregorianCalendar(2020, Calendar.OCTOBER, 25).getTime(), "ST");
		stationPool.addStation(station2);
		Station station3 = new Station("name 3", "ST3", new GregorianCalendar(2020, Calendar.OCTOBER, 24).getTime(), "ST");
		stationPool.addStation(station3);
		Station station4 = new Station("name 4", "ST4", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "ST");
		stationPool.addStation(station4);
		Set<Station> openStations = stationPool.getOpenStations(new GregorianCalendar(2020, Calendar.OCTOBER, 26).getTime());
		assertTrue(openStations.size() == 2);
		assertTrue(openStations.contains(station3));
		assertTrue(openStations.contains(station2));
	}
	
	@Test
	void testGetOpenStationsException() {
		Station station1 = new Station("name 1", "ST1", new GregorianCalendar(2020, Calendar.OCTOBER, 28).getTime(), "ST");
		stationPool.addStation(station1);
		Station station2 = new Station("name 2", "ST2", new GregorianCalendar(2020, Calendar.OCTOBER, 25).getTime(), "ST");
		stationPool.addStation(station2);
		Station station3 = new Station("name 3", "ST3", new GregorianCalendar(2020, Calendar.OCTOBER, 24).getTime(), "ST");
		stationPool.addStation(station3);
		Station station4 = new Station("name 4", "ST4", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "ST");
		stationPool.addStation(station4);
		Set<Station> openStations = stationPool.getOpenStations(null);
		assertTrue(openStations.size() == 0);
	}
	
	@Test
	void testGetOpenStationsWithNoOpenStations() {
		Station station1 = new Station("name 1", "ST1", new GregorianCalendar(2020, Calendar.OCTOBER, 28).getTime(), "ST");
		stationPool.addStation(station1);
		Station station2 = new Station("name 2", "ST2", new GregorianCalendar(2020, Calendar.OCTOBER, 25).getTime(), "ST");
		stationPool.addStation(station2);
		Station station3 = new Station("name 3", "ST3", new GregorianCalendar(2020, Calendar.OCTOBER, 24).getTime(), "ST");
		stationPool.addStation(station3);
		Station station4 = new Station("name 4", "ST4", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime(), "ST");
		stationPool.addStation(station4);
		Set<Station> openStations = stationPool.getOpenStations(new GregorianCalendar(1800, Calendar.OCTOBER, 26).getTime());
		assertTrue(openStations.size() == 0);
	}

}
