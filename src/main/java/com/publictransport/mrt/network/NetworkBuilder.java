package com.publictransport.mrt.network;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.publictransport.mrt.network.model.Network;

@Service
public interface NetworkBuilder<T> {
	
	public Network<T> buildNetwork(Set<T> entities);

}
