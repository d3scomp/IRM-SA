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
public class DeltaComputorFixed implements DeltaComputor {

	/** Default delta in ms. */
	static final long DEFAULT_DELTA = 1000;

	/** Fixed delta. */
	public final long delta;

	/**
	 * Default constructor.
	 */
	public DeltaComputorFixed() {
		this(DEFAULT_DELTA);
	}

	/**
	 * Sets fixed delta.
	 * @param delta fixed delta
	 */
	public DeltaComputorFixed(final long delta) {
		this.delta = delta;
	}

	@Override
	public void computeDelta(final InvariantInfo<?> info) {
		long per = 0;
		long min = 250;
		long max = 2000;
		if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
			final ProcessInvariantInstance pii = info.getInvariant();
			final ProcessInvariant pi = (ProcessInvariant) pii.getInvariant();
//			min = pi.getProcessMinPeriod(); //TODO add getProcessMinPeriod to ProcessInvariant
//			max = pi.getProcessMaxPeriod(); //TODO add getProcessMaxPeriod to ProcessInvariant
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
			info.delta = per + delta > max ? max - per : delta;
			break;
		case DOWN:
			info.delta = per - delta < max ? per - min : delta;
			break;
		case NO:
			info.delta = delta;
		}
	}
}
