package com.publictransport.mrt.network.strategy;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publictransport.mrt.model.Station;

@Component
public class TravelTimeStrategyController {
	
	/*@Autowired
	private NightTravelTimeEdgeCosting nightTravelTimeEdgeCosting;
	
	@Autowired
	private PeakTravelTimeEdgeCosting peakTravelTimeEdgeCosting;
	
	@Autowired
	private NonPeakTravelTimeEdgeCosting nonPeakTravelTimeEdgeCosting;
	
	@Autowired
	private ZeroOneTimeEdgeStrategy zeroOneTimeEdgeStrategy;*/
	
	
	public EdgeCostingStrategy<Station> getTravelTimeStrategy(LocalDateTime travelTime) {
		if (travelTime.getHour() >= 22 || travelTime.getHour() < 6) {
			return new NightTravelTimeEdgeCosting();	
		}
		//on week day
		if (!(DayOfWeek.SATURDAY.equals(travelTime.getDayOfWeek()) || DayOfWeek.SUNDAY.equals(travelTime.getDayOfWeek()))) {
			if ((travelTime.getHour() >= 6 && travelTime.getHour() < 9)
					|| (travelTime.getHour() >= 18 && travelTime.getHour() < 21)) {
				return new PeakTravelTimeEdgeCosting();	
			}	
		}
		return new NonPeakTravelTimeEdgeCosting();
	}
	
	public EdgeCostingStrategy<Station> getZeroOneTimeEdgeStrategy() {
		return new ZeroOneTimeEdgeStrategy();
	}

}
