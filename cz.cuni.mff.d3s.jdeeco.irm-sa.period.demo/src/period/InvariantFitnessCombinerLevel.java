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

import cz.cuni.mff.d3s.irm.model.design.impl.KnowledgeImpl;
import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;

/**
 * Combines overall fitness by considering invariant level.
 */
public class InvariantFitnessCombinerLevel extends KnowledgeImpl implements InvariantFitnessCombiner {

	/**
	 * Only constructor.
	 */
	public InvariantFitnessCombinerLevel() {
		this.name = "LevelFitnessCombiner";
		this.type = "LevelFitnessCombinerType";
	}

	@Override
	public double combineInvariantFitness(final Collection<InvariantInfo<?>> infos) {
		double result = 0.0;
		for (InvariantInfo<?> info : infos) {
			//b) upper level monitors more important
			info.level = info.computeInvariantLevel();
			info.weight = info.fitness / info.level;
			result += info.weight;
		}
		return result;
	}

}
