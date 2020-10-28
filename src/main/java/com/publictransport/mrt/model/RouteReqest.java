package com.publictransport.mrt.model;

import java.time.LocalDateTime;

public class RouteReqest {

	private String from;

	private String to;

	private LocalDateTime travelTime;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public LocalDateTime getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(LocalDateTime travelTime) {
		this.travelTime = travelTime;
	}
}
