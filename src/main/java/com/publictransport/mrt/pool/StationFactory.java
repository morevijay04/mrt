package com.publictransport.mrt.pool;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.pool.exception.IncorrectInputDataException;

@Component
public class StationFactory {
	
	private SimpleDateFormat dateFormat;
	
	public StationFactory() {
		dateFormat = (new SimpleDateFormat("dd MMMMM yyyyy"));
		dateFormat.setLenient(false);
	}

	public Station createStation(String line) throws IncorrectInputDataException {
		validateData(line);
		String token[] = line.split(",");
		try {
			return new Station(token[1].trim(), token[0].trim(), dateFormat.parse(token[2].trim()), token[0].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0].trim());
		} catch (ParseException e) {
			throw new IncorrectInputDataException("Incorrect input date to process | input:"+line);
		}
}

	private void validateData(String line) throws IncorrectInputDataException {
		if (Strings.isBlank(line) 
				|| line.split(",").length != 3
				|| Strings.isBlank(line.split(",")[0])
				|| Strings.isBlank(line.split(",")[1])
				|| Strings.isBlank(line.split(",")[2])) {
			throw new IncorrectInputDataException("Incorrect input string to process | input:"+line);
		}
	}
}
