package cz.cuni.mff.d3s.deeco.irm;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;

public class Invariant implements IRMPrimitive {

	protected String id;
	protected String description;
	protected Operation operation;
	protected List<IRMPrimitive> parents;
	protected MonitorProvider provider;
	protected List<String> roles; // component role ids;

	public Invariant(String id, String description, Operation operation,
			List<String> roles, MonitorProvider provider) {
		this.description = description;
		this.operation = operation;
		this.roles = roles;
		this.provider = provider;
		this.id = id;
	}

	public List<IRMPrimitive> getParents() {
		return parents;
	}

	public void setParents(List<IRMPrimitive> parents) {
		this.parents = parents;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getDescription() {
		return description;
	}

	public boolean isLeaf() {
		return operation == null;
	}

	@Override
	public boolean isRoot() {
		return parents == null || parents.isEmpty();
	}

	public Operation getOperation() {
		return operation;
	}

	@Override
	public List<IRMPrimitive> getIRMPrimitives() {
		List<IRMPrimitive> elements = new LinkedList<IRMPrimitive>();
		if (!isLeaf())
			elements.addAll(operation.getIRMPrimitives());
		elements.add(this);
		return elements;
	}

	public String getId() {
		return id;
	}
	
	public String toString() {
		return id;
	}
}
