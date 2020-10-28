package com.publictransport.mrt.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.publictransport.mrt.model.RouteReqest;
import com.publictransport.mrt.service.MainService;

@RestController
public class MrtController {

	@Autowired
	private MainService service;
	
	@PostMapping("/getRoutes")
	public String getRoutes(@RequestBody RouteReqest routeRequest) {
		return  service.getRoutes(routeRequest);
	}
}
