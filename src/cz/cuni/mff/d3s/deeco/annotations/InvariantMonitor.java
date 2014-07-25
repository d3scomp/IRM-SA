/*******************************************************************************
 * Copyright 2014 Charles University in Prague
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Used to mark a method as an invariant monitor. An invariant monitor is
 * declared as a <code>public static boolean</code> method of a DEECo component.
 * </p>
 * <p>
 * The value refers to the invariant ID of the design model - the invariant ID
 * acts then as traceability link from requirements to implementation.
 * </p>
 * <p>
 * These monitors are parsed just like DEECo processes, but are *not* used to
 * generate runtime tasks. They are used by the AdaptationManager during the
 * creation of the IRM runtime instances.
 * </p>
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InvariantMonitor {
	String value();
}

