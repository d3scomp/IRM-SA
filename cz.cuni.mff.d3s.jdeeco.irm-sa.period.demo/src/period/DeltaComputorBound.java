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

import cz.cuni.mff.d3s.irm.model.design.ExchangeInvariant;
import cz.cuni.mff.d3s.irm.model.design.ProcessInvariant;
import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;

/**
 * Delta computor returning always the same value if possible.
 */
public class DeltaComputorBound implements DeltaComputor {

	@Override
	public void computeDelta(final InvariantInfo<?> info) {
		long min = 0;
		long max = 0;
		long per = 0;
		if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
			final ProcessInvariantInstance pii = info.getInvariant();
			final ProcessInvariant pi = (ProcessInvariant) pii.getInvariant();
//			min = pi.getProcessMinPeriod(); //TODO add getProcessMinPeriod to ProcessInvariant
			min = 0;
//			max = pi.getProcessMaxPeriod(); //TODO add getProcessMaxPeriod to ProcessInvariant
			max = 0;
			per = pi.getProcessPeriod();
		} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
			final ExchangeInvariantInstance xii = info.getInvariant();
			final ExchangeInvariant xi = (ExchangeInvariant) xii.getInvariant();
//			min = xi.getEnsebleMinPeriod(); //TODO add getEnsembleMinPeriod to ExchangeInvariant
			min = 0;
//			max = xi.getEnsebleMaxPeriod(); //TODO add getEnsembleMaxPeriod to ExchangeInvariant
			max = 0;
			per = xi.getEnsemblePeriod();
		}
		switch (info.direction) {
		case UP:
			info.delta = (max - per) / 2;
			break;
		case DOWN:
			info.delta = (per - min) / 2;
		}
	}
}
