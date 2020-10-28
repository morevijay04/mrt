package com.publictransport.mrt.service;

import org.springframework.stereotype.Service;

import com.publictransport.mrt.model.RouteReqest;

@Service
public interface MainService {

	/**
	 * This method should be responsible to get routes for route request
	 * 
	 * @param routeRequest
	 * @return
	 */
	String getRoutes(RouteReqest routeRequest);

}
