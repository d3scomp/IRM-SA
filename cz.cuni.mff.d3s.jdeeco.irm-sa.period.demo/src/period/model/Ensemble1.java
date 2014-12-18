package period.model;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Ensemble
@Invariant("I03")
@PeriodicScheduling(period=2000)
public class Ensemble1 {

	@Membership
	public static boolean membership(
		@In("member.Knowledge1") String Knowledge1,
		@In("coord.Knowledge2") String Knowledge2
	) {
			/*
				replace next line with actual condition
			*/
			return true;
	}

	@KnowledgeExchange
	public static void map(
		@In("member.Knowledge1") String Knowledge1,
		@InOut("coord.Knowledge2") ParamHolder<String> Knowledge2
	) {
		/*
		    Add knowledge exchange function here.
		 */
		Knowledge2.value = Knowledge2.value + Knowledge1 + ",";
		System.out.println("Ensemble1.map: " + Knowledge1 + " -> " + Knowledge2.value);
	}
}