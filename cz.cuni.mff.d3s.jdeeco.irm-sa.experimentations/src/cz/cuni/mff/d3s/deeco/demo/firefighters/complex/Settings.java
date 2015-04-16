package cz.cuni.mff.d3s.deeco.demo.firefighters.complex;

import java.io.File;

public class Settings {
	
//  The configuration used for the JSS paper.
//	public static final int PROCESS_PERIOD = 500; // in milliseconds
//	public static final int ENSEMBLE_PERIOD = 500; 
//	public static final int ADAPTATION_PERIOD = 500;
//	public static final int BROADCAST_PERIOD = 400;
//	
//	public static final int NETWORK_DELAY = 500; // in milliseconds
//	public static final long BASE_INACCURACY = 1000;
//	public static final long INACCURACY_INTERVAL = 1000;
	
	public static final int PROCESS_PERIOD = 500; // in milliseconds
	public static final int ENSEMBLE_PERIOD = 500; 
	public static final int ADAPTATION_PERIOD = 500;
	public static final int BROADCAST_PERIOD = 400;
	
	public static int NETWORK_DELAY = 500; // in milliseconds
	public static final long BASE_INACCURACY = 1000;
	public static final long INACCURACY_INTERVAL = 1000;
	
	public static final String MODELS_BASE_PATH = "designModels" + File.separator;
	public static final String XMIFILE_PREFIX = "firefighters_complex";
	public static final String DESIGN_MODEL_PATH = MODELS_BASE_PATH + "firefighters_complex.irmdesign";

	public static final int SIMULATION_DURATION = 60000; // in milliseconds
}
