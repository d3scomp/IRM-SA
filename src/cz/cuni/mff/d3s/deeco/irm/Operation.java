package cz.cuni.mff.d3s.deeco.irm;

import java.util.LinkedList;
import java.util.List;


public class Operation implements IRMPrimitive {
	
	private List<? extends IRMPrimitive> children;
	private IRMPrimitive parent;
	private OperationType type;
	
	public Operation(OperationType type) {
		this.type = type;
	}
	
	public List<? extends IRMPrimitive> getChildren() {
		return children;
	}
	
	public OperationType getType() {
		return type;
	}

	public void setChildren(List<? extends IRMPrimitive> children) {
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
