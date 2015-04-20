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
package cz.cuni.mff.d3s.irmsa.strategies.assumption;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.AdapteeSelector;

public class AdapteeSelectorTree implements AdapteeSelector {

	@Override
	public Set<InvariantInfo<?>> selectAdaptees(
			final Collection<InvariantInfo<?>> infos) {
		final Set<InvariantInfo<?>> result = new HashSet<>();
		int level = Integer.MAX_VALUE; //level of invariants in result
		for (InvariantInfo<?> invariant: infos) {
			if (AdapteeSelector.isRemoteComponent(invariant)) {
				continue;
			}
			if (invariant.level < 0) {
				invariant.computeInvariantLevel();
			}
			if (invariant.level < level) {
				result.clear();
				level = invariant.level;
				result.add(invariant);
			} else if (invariant.level == level) {
				result.add(invariant);
			}
		}
		return result;
	}
}
