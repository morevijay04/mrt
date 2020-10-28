package com.publictransport.mrt.pool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;

import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.pool.exception.IncorrectInputDataException;
import com.publictransport.mrt.pool.exception.UnsupportedStationFileException;


public class StationPool {

	private Set<Station> pool;

	private StationFactory factory;
	
	private String DEFAULT_INPUT_FILE = "StationMap.csv";
	
	public StationPool(StationFactory factory, String stationInputFile) throws UnsupportedStationFileException, IOException, URISyntaxException {
		pool = new HashSet<Station>();
		this.factory = factory;
		buildStationPool(Strings.isEmpty(stationInputFile)? DEFAULT_INPUT_FILE : stationInputFile);
	}

	public void buildStationPool(String inputFile)
			throws UnsupportedStationFileException, IOException, URISyntaxException {
		
		// provide lock over pool in case building or updating pool is requirement in future
		if (!isSupportedFileFormat(inputFile)) {
			throw new UnsupportedStationFileException("input file is not supported");
		}
		if (ClassLoader.getSystemResource(inputFile) == null && !new File(inputFile).exists()) {
			throw new FileNotFoundException("input file is not found | inputFile:" + inputFile);
		}
		
		URI fileUri = ClassLoader.getSystemResource(inputFile) == null ?new File(inputFile).toURI() : ClassLoader.getSystemResource(inputFile).toURI();

		// Step 1: read file
		Path path = Paths.get(fileUri);
		Files.lines(path).skip(1).forEach(line -> {
			// step 2: build station object from line
			Station station;
			try {
				station = factory.createStation(line);
				// Step 3: add it to the pool
				addStation(station);
			} catch (IncorrectInputDataException e) {
				// print and process rest of the file
				e.printStackTrace();
			}
		});
	}

	public int size() {
		return pool.size();
	}

	public boolean isSupportedFileFormat(String inputFile) {
		return inputFile != null && inputFile.lastIndexOf(".") > 0
				&& inputFile.substring(inputFile.lastIndexOf(".") + 1).equals("csv");
	}

	public Set<Station> getOpenStations(Date date) {
		if (date == null) {
			return new HashSet();
		}
		return pool.stream().filter(s -> s.isOpenedBy(date)).collect(Collectors.toSet());

	}

	public void addStation(Station station) {
		// provide lock over pool in case building or updating pool is requirement in future
		if (station != null) {
			pool.add(station);
		}
	}

	public void removeStation(Station station) {
		// provide lock over pool in case building or updating pool is requirement in future
		pool.remove(station);
	}
	
	public Station getStationByName(String name) {
		Optional<Station> staion =  pool.stream().filter(s -> s.getName().equalsIgnoreCase(name)).findFirst();
		if (staion.isPresent()) {
			return staion.get();
		} else {
			return null;
		}
	}

}
