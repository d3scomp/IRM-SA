package cz.cuni.mff.d3s.deeco.irm;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;


public class Invariant implements IRMPrimitive {

	protected String id;
	protected String description;
	protected Operation operation;
	protected IRMPrimitive parent;
	protected List<String> roles; //component role ids;

	public Invariant(String id, String description,
			Operation operation, List<String> roles) {
		this.description = description;
		this.operation = operation;
		this.roles = roles;
	}
	
	public IRMPrimitive getParent() {
		return parent;
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
		return parent == null;
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
	
	protected List<Parameter> replaceRolesByAssignment(
			List<String> assignedRoles, List<Parameter> parameters) {
		//TODO
		return null;
	}
}
