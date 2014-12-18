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
import java.util.HashSet;
import java.util.Set;

import cz.cuni.mff.d3s.irm.model.design.impl.KnowledgeImpl;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;

public class AdapteeSelectorFitness extends KnowledgeImpl implements AdapteeSelector {

	/**
	 * Only constructor.
	 */
	public AdapteeSelectorFitness() {
		this.name = "FitnessAdapteeSelector";
		this.type = "FitnessAdapteeSelectorType";
	}

	@Override
	public Set<InvariantInfo<?>> selectAdaptees(
			final Collection<InvariantInfo<?>> infos) {
		final Set<InvariantInfo<?>> result = new HashSet<>();
		double fitness = Double.MAX_VALUE; //fitness of invariants in result
		for (InvariantInfo<?> invariant: infos) {
			final InvariantInstance instance = invariant.getInvariant(); //TODO not only unsatisfied?
			if (!instance.isSatisfied()) {
				if (invariant.fitness < fitness) {
					result.clear();
					fitness = invariant.fitness;
					result.add(invariant);
				} else if (invariant.fitness == fitness) { //TODO not compare double on equality?
					result.add(invariant);
				}
			}
		}
		return result;
	}
}
