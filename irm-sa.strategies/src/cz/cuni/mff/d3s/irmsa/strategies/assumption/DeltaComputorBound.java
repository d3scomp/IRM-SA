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

import org.eclipse.emf.common.util.EMap;

import cz.cuni.mff.d3s.deeco.annotations.AssumptionParameter;
import cz.cuni.mff.d3s.irm.model.runtime.api.AssumptionInstance;
import cz.cuni.mff.d3s.irmsa.strategies.commons.InvariantInfo;
import cz.cuni.mff.d3s.irmsa.strategies.commons.variations.DeltaComputor;

/**
 * Delta computor returning the middle to the bound.
 */
public class DeltaComputorBound implements DeltaComputor {

	@Override
	public void computeDelta(final InvariantInfo<?> info) {
		if (!(info instanceof AssumptionInfo)) {
			return;
		}
		final AssumptionInfo assumption = (AssumptionInfo) info;
		final AssumptionParameter parameter = assumption.parameter.getAnnotation(AssumptionParameter.class);
		final double min = parameter.minValue();
		final double max = parameter.maxValue();
		final AssumptionInstance instance = assumption.getInvariant();
		final EMap<String, Object> data = instance.getComponentInstance().getKnowledgeManager().getComponent().getInternalData();
		final Object object = data.get(assumption.getParameterId());
		double value;
		if (object == null || !(object instanceof Number)) {
			value = parameter.defaultValue();
		} else {
			value = ((Number) object).doubleValue();
		}
		switch (info.direction) {
		case UP:
			info.delta = (max - value) / 2;
			break;
		case DOWN:
			info.delta = (value - min) / 2;
		case NO:
			info.delta = 0;
			break;
		}
	}
}
