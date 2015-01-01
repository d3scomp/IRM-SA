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

import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;

/**
 * Combines overall fitness by weighted average.
 */
public class InvariantFitnessCombinerAverage implements InvariantFitnessCombiner {

	@Override
	public double combineInvariantFitness(final Collection<InvariantInfo<?>> infos) {
		double result = 0.0;
		for (InvariantInfo<?> info : infos) {
			if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ProcessInvariantInstance pii = info.getInvariant();
				//a) weighted average
//				info.weight = info.fitness * pii.getWeight() /  infos.size(); //TODO add getWeigth to ProcessInvariantInstance/ProcessInvariant
				info.weight = 1.0;
			} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
				final ExchangeInvariantInstance xii = info.getInvariant();
				//a) weighted average
//				info.weight = info.fitness * xii.getWeight() /  infos.size(); //TODO add getWeigth to ExchangeInvariantInstance/ExchangeInvariant
				info.weight = 1.0;
			}
			result += info.weight;
		}
		return result;
	}

}
