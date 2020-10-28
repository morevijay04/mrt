package com.publictransport.mrt;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.publictransport.mrt.pool.StationFactory;
import com.publictransport.mrt.pool.StationPool;
import com.publictransport.mrt.pool.exception.UnsupportedStationFileException;

@SpringBootApplication
public class MrtApplication {

	private static String stationInputFile = "";
	
	public static void main(String[] args) {
		// START: remove this if block if running from IDE
		if (args.length == 1) {
			System.out.println("Staion input file is needed: please provide station input file as args to java command");
			System.exit(0);
		}
		stationInputFile = args[1];
		// END: remove this if block if running from IDE
		SpringApplication.run(MrtApplication.class, args);
	}
	
	@Bean
	public StationPool stationPool(@Autowired StationFactory factory) throws UnsupportedStationFileException, IOException, URISyntaxException {
		return new StationPool(factory, stationInputFile);
	}

}
