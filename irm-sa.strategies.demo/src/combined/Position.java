package combined;

import java.io.Serializable;

import cz.cuni.mff.d3s.irm.model.design.impl.KnowledgeImpl;

public class Position extends KnowledgeImpl  implements Serializable, Cloneable {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -5980471557237426848L;
	
	public final double x;
	public final double y;
	
	public Position(double x, double y) {
		this.name = "PositionKnowledge";
		this.type = "PositionKnowledgeType";
		
		this.x = x;
		this.y = y;
	}
	

	@Override
	public String toString() {
		return String.format("[%.4f,%.4f]", x, y);
	}

	@Override
	public Position clone() {
		try {
			return (Position) super.clone();
		} catch (CloneNotSupportedException e) {
			return null; //should never happen
		}
	}
}
