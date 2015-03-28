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

import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;

/**
 * Interface for selecting delta of adaptation.
 */
@FunctionalInterface
public interface DeltaComputor {

	/**
	 * Returns current period of the process or -1.
	 * @param pii process invariant instance
	 * @return current period of the process or -1
	 */
	static public long getCurrentPeriod(final ProcessInvariantInstance pii) {
		for (Trigger trigger : pii.getComponentProcess().getTriggers()) {
			if (trigger instanceof TimeTrigger) {
				return ((TimeTrigger) trigger).getPeriod();
			}
		}
		return -1L;
	}

	/**
	 * Returns current period of the exchange or -1.
	 * @param xii exchange invariant instance
	 * @return current period of the exchange or -1
	 */
	static public long getCurrentPeriod(final ExchangeInvariantInstance xii) {
		for (Trigger trigger : xii.getEnsembleDefinition().getTriggers()) {
			if (trigger instanceof TimeTrigger) {
				return ((TimeTrigger) trigger).getPeriod();
			}
		}
		return -1L;
	}

	/**
	 * Computes delta of period adaptation.
	 * @param infos invariant instance to compute delta for
	 */
	void computeDelta(InvariantInfo<?> info);
}
