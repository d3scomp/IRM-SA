package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AssumptionParameter {
	String name();
	double defaultValue();
	double minValue();
	double maxValue();
	Scope scope() default Scope.MONITOR;
	Direction initialDirection() default Direction.UP;

	enum Scope { MONITOR, COMPONENT }
	enum Direction { UP, DOWN }
}
