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
import java.util.Set;

import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;

/**
 * Interface for selecting processes for period adaptation.
 */
@FunctionalInterface
public interface AdapteeSelector {

	/**
	 * Returns filtered set of invariant instances to adapt.
	 * @param infos invariant instances to filter
	 * @return filtered set of invariant instances to adapt
	 */
	Set<InvariantInfo<?>> selectAdaptees(Collection<InvariantInfo<?>> infos);
}
