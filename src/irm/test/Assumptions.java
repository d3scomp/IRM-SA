package irm.test;

import cz.cuni.mff.d3s.deeco.annotations.Condition;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeUtil;

public class Assumptions {

	@Condition("2")
	public static boolean a2(@In("GM.groupLeaderId") String gmGLId,
			@In("GL.id") String glId) {
		return glId.equals(gmGLId);
	}

	@Condition("6")
	public static boolean a6(@In("GL.gmInDanger") Boolean gmInDanger) {
		return gmInDanger;
	}

	@Condition("7")
	public static boolean a7(@In("GL.gmInDanger") Boolean gmInDanger) {
		return !gmInDanger;
	}

	@Condition("15")
	public static boolean a15(@In("GM.noMovementSince") Long noMovementSince) {
		return RuntimeUtil.getRuntime().getCurrentTime() - noMovementSince > 20000;
	}

	@Condition("16")
	public static boolean a16(@In("GM.noMovementSince") Long noMovementSince) {
		return RuntimeUtil.getRuntime().getCurrentTime() - noMovementSince < 20000;
	}

	@Condition("17")
	public static boolean a17(
			@In("GM.nearbyGMInDanger") Boolean nearbyGMInDanger) {
		return !nearbyGMInDanger;
	}

	@Condition("18")
	public static boolean a18(
			@In("GM.nearbyGMInDanger") Boolean nearbyGMInDanger) {
		return nearbyGMInDanger;
	}

	@Condition("25")
	public static boolean a25(
			@In("GM.breathingAparatusUsed") Boolean breathingAparatusUsed) {
		return breathingAparatusUsed;
	}

	@Condition("24")
	public static boolean a24(
			@In("GM.breathingAparatusUsed") Boolean breathingAparatusUsed) {
		return !breathingAparatusUsed;
	}

	@Condition("27")
	public static boolean a27() {
		return true;
	}

	@Condition("28")
	public static boolean a28() {
		return true;
	}
}
