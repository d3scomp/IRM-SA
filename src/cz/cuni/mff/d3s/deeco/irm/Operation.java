package cz.cuni.mff.d3s.deeco.irm;

import java.util.LinkedList;
import java.util.List;


public enum Operation implements IRMPrimitive {
	OR, AND;
	
	private List<IRMPrimitive> children;
	private IRMPrimitive parent;
	
	public List<IRMPrimitive> getChildren() {
		return children;
	}

	public void setChildren(List<IRMPrimitive> children) {
		this.children = children;
	}
	
	@Override
	public boolean isRoot() {
		return false;
	}
	
	@Override
	public IRMPrimitive getParent() {
		return parent;
	}

	@Override
	public List<IRMPrimitive> getIRMPrimitives() {
		List<IRMPrimitive> result = new LinkedList<>();
		for (IRMPrimitive primitive : children)
			result.addAll(primitive.getIRMPrimitives());
		result.add(this);
		return result;
	}

}
