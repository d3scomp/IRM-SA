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

import cz.cuni.mff.d3s.deeco.model.architecture.api.RemoteComponentInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.AssumptionInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;
import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;

/**
 * Interface for selecting processes for period adaptation.
 */
@FunctionalInterface
public interface AdapteeSelector extends AdaptationResultListener {

	static boolean isRemoteComponent(final InvariantInfo<?> info) {
		if (ProcessInvariantInstance.class.isAssignableFrom(info.clazz)) {
			final ProcessInvariantInstance pii = info.getInvariant();
			return pii.getComponentProcess().getComponentInstance() instanceof RemoteComponentInstance;
		} else if (ExchangeInvariantInstance.class.isAssignableFrom(info.clazz)) {
			return false; //TODO we assume ensembles are always local, right?
		} else if (AssumptionInstance.class.isAssignableFrom(info.clazz)) {
			final AssumptionInstance aii = info.getInvariant();
			return aii.getComponentInstance() instanceof RemoteComponentInstance;
		}
		return false;
	}

	/**
	 * Returns filtered set of invariant instances to adapt.
	 * @param infos invariant instances to filter
	 * @return filtered set of invariant instances to adapt
	 */
	Set<InvariantInfo<?>> selectAdaptees(Collection<InvariantInfo<?>> infos);
}
