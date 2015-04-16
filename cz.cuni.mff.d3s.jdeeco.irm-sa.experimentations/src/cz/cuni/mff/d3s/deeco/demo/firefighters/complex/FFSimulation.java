package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.io.File;

public abstract class FFSimulation {
	static public final String MODELS_BASE_PATH = "designModels" + File.separator;
	static public final String XMIFILE_PREFIX = "firefighters_complex";
	static final String DESIGN_MODEL_PATH = MODELS_BASE_PATH + "firefighters_complex.irmdesign";
	
	static final int NETWORK_DELAY = 200; // in milliseconds
	static final int SIMULATION_DURATION = 60000; // in milliseconds
}
