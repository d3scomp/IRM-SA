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
 * Used to mark a class that contains invariant monitors. These monitors are not
 * only for leaf invariants, but for every invariant in the IRM graph.
 * </p>
 * <p>
 * Although it is possible to contain monitors for Process and Exchange
 * invariants, these types of monitors can also be specified in the respective
 * Component and Ensemble classes (in which case they take precedence over the
 * monitors specified in a specialized Monitors class).
 * </p>
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InvariantMonitors {
	
}

