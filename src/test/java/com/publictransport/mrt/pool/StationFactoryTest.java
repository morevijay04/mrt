package com.publictransport.mrt.pool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;

import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.pool.exception.IncorrectInputDataException;

class StationFactoryTest {

	StationFactory factory = new StationFactory();
	
	@Test
	void testCreateStation() throws IncorrectInputDataException {
		Station station = factory.createStation("NS1,Jurong East,10 March 1990");
		assertEquals("Jurong East", station.getName());
		assertEquals("NS1", station.getCode());
		assertEquals("NS", station.getLine());
		assertEquals(new GregorianCalendar(1990, Calendar.MARCH, 10).getTime(), station.getOpeningDate());
	}
	
	@Test
	void testCreateStationIncorrectDate() {
		assertThrows(IncorrectInputDataException.class, () -> factory.createStation("NS2,Jurong East, 75 March 1990"));
	}
	
	@Test
	void testCreateStationEmptyData1() {
		assertThrows(IncorrectInputDataException.class, () -> factory.createStation(",Jurong East, 10 March 1990"));
	}
	
	@Test
	void testCreateStationEmptyData2() {
		assertThrows(IncorrectInputDataException.class, () -> factory.createStation(",, 10 March 1990"));
	}
	
	@Test
	void testCreateStationEmptyData3() {
		assertThrows(IncorrectInputDataException.class, () -> factory.createStation(",,"));
	}
	
	@Test
	void testCreateStationIncorrectFormat() {
		assertThrows(IncorrectInputDataException.class, () -> factory.createStation("NS1,Jurong East 10 March 1990"));
	}

}
