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
package cz.cuni.mff.d3s.irmsa.strategies.commons.variations;

import java.util.Collection;

import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;

/**
 * Interface for combining overall system fitness.
 */
@FunctionalInterface
public interface InvariantFitnessCombiner extends AdaptationResultListener {

	/**
	 * Returns combined overall system fitness.
	 * @param infos invariant instances with computed fitness
	 * @return combined overall system fitness
	 */
	double combineInvariantFitness(Collection<InvariantInfo<?>> infos);
}
