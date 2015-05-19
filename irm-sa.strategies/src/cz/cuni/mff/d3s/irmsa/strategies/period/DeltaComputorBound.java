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
package cz.cuni.mff.d3s.irmsa.strategies.period;

import cz.cuni.mff.d3s.irm.model.design.ExchangeInvariant;
import cz.cuni.mff.d3s.irm.model.design.ProcessInvariant;
import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.DeltaComputor;

/**
 * Delta computor returning the middle to the bound.
 */
public class DeltaComputorBound implements DeltaComputor {

	@Override
	public void computeDelta(final InvariantInfo<?> info) {
		long min = 1;
		long max = -1L;
		long per = 0;
		if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
			final ProcessInvariantInstance pii = info.getInvariant();
			final ProcessInvariant pi = (ProcessInvariant) pii.getInvariant();
			min = pi.getProcessMinPeriod();
			max = pi.getProcessMaxPeriod();
			per = DeltaComputor.getCurrentPeriod(pii);
		} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
			final ExchangeInvariantInstance xii = info.getInvariant();
			final ExchangeInvariant xi = (ExchangeInvariant) xii.getInvariant();
			min = xi.getEnsembleMinPeriod();
			max = xi.getEnsembleMaxPeriod();
			per = DeltaComputor.getCurrentPeriod(xii);
		}
		if (max == -1L) {
			max = 3 * per / 2; //TODO tweak the constant -> delta = 1/4 per, maybe 1/2
		}
		if (min < 1) {
			min = 0;
		}
		switch (info.direction) {
		case UP:
			info.delta = (max - per) / 2;
			break;
		case DOWN:
			info.delta = (per - min) / 2;
		case NO:
			info.delta = 0;
			break;
		}
	}
}
