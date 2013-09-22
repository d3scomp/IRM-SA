package irm.test;

import cz.cuni.mff.d3s.deeco.annotations.Assumption;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeUtil;

public class Assumptions {

	@Assumption("6")
	public static boolean a6(@In("GL.gmInDanger") Boolean gmInDanger) {
		return gmInDanger;
	}

	@Assumption("7")
	public static boolean a7(@In("GL.gmInDanger") Boolean gmInDanger) {
		return !gmInDanger;
	}

	@Assumption("15")
	public static boolean a15(@In("GM.noMovementSince") Long noMovementSince) {
		return RuntimeUtil.getRuntime().getCurrentTime() - noMovementSince > 20000;
	}

	@Assumption("16")
	public static boolean a16(@In("GM.noMovementSince") Long noMovementSince) {
		return RuntimeUtil.getRuntime().getCurrentTime() - noMovementSince < 20000;
	}

	@Assumption("17")
	public static boolean a17(
			@In("GM.nearbyGMInDanger") Boolean nearbyGMInDanger) {
		return !nearbyGMInDanger;
	}

	@Assumption("18")
	public static boolean a18(
			@In("GM.nearbyGMInDanger") Boolean nearbyGMInDanger) {
		return nearbyGMInDanger;
	}

	@Assumption("25")
	public static boolean a25(
			@In("GM.breathingAparatusUsed") Boolean breathingAparatusUsed) {
		return breathingAparatusUsed;
	}

	@Assumption("24")
	public static boolean a24(
			@In("GM.breathingAparatusUsed") Boolean breathingAparatusUsed) {
		return !breathingAparatusUsed;
	}
	
	@Assumption("27")
	public static boolean a27() {
		return true;
	}
	
	@Assumption("28")
	public static boolean a28() {
		return true;
	}
}
