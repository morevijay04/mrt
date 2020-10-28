package com.publictransport.mrt.network.strategy;

import org.springframework.stereotype.Service;

import com.publictransport.mrt.network.model.Network;

@Service
public interface EdgeCostingStrategy<T> {

	public void apply(Network<T> network);
}
