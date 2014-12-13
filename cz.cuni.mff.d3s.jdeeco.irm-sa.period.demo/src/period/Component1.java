package period;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
@IRMComponent("Component1")
public class Component1 {

	public String Knowledge1;
	
	private static int counter = 0;
 
	public Component1() {
		/* 
			Just a skeleton,
			business logic to be provided here.
		*/
		Knowledge1 = "Component1Knowledge1_" + counter;
	}
 
	@Process
	@Invariant("I02")
	@PeriodicScheduling(period=1000) 
	public static void Process1(
		@Out("Knowledge1") ParamHolder<String> Knowledge1 
	) {
		/*
			Just a skeleton,
			business logic to be provided here.
		*/
		Knowledge1.value = "Component1Knowledge1_" + ++counter;
		System.out.println("Component1.Process1: " + Knowledge1.value);
	}
}
