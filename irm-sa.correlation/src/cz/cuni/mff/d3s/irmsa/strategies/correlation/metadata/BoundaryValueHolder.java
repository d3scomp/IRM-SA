package cz.cuni.mff.d3s.irmsa.strategies.correlation.metadata;

public class BoundaryValueHolder {
	
	protected static final double EPSILON = 0.01;
	
	private double boundary;
	private boolean changed;
	
	public BoundaryValueHolder(double boundary) {
		this.boundary = boundary;
		this.changed = true;
	}
	
	public void setBoundary(double boundary) {
		if(Double.isNaN(boundary) && Double.isNaN(this.boundary)) {
			return;
		}
		if(Double.isNaN(boundary) || Double.isNaN(this.boundary)
				|| Math.abs(this.boundary - boundary) > EPSILON) {
			this.boundary = boundary;
			changed = true;
		}
	}
	
	public double getBoundary() {
		return boundary;
	}
	
	public boolean isValid() {
		return !Double.isNaN(boundary);
	}
	
	public void boundaryUsed() {
		changed = false;
	}
	
	public boolean hasChanged() {
		return changed;
	}

}
