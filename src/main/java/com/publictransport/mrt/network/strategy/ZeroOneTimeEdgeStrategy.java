package com.publictransport.mrt.network.strategy;

import org.springframework.stereotype.Component;

import com.publictransport.mrt.model.Station;
import com.publictransport.mrt.network.model.Network;

/**
 * This strategy will make line change expensive compare to using same line
 * 
 * @author morev
 *
 */
@Component
public class ZeroOneTimeEdgeStrategy extends AbstractMrtTravelTimeStrategy{

	@Override
	public void apply(Network<Station> network) {
		network.allVerticesCodes().forEach(code->{
			network.getVertex(code).setCost(Double.MAX_VALUE);
			setTravelTime(network.getVertex(code), 1d,100d);
		});
	}
}
