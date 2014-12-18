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
package period;

import java.util.Collection;

import cz.cuni.mff.d3s.irm.model.design.Knowledge;

/**
 * Interface for combining overall system fitness.
 */
public interface InvariantFitnessCombiner extends Knowledge {

	/**
	 * Returns combined overall system fitness.
	 * @param infos invariant instances with computed fitness
	 * @return combined overall system fitness
	 */
	double combineInvariantFitness(Collection<InvariantInfo<?>> infos);
}
