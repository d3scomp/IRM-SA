package cz.cuni.mff.d3s.deeco.irm;

import java.util.List;


public interface IRMPrimitive {
	
	List<IRMPrimitive> getIRMPrimitives();
	IRMPrimitive getParent();
	boolean isRoot();
	
}
