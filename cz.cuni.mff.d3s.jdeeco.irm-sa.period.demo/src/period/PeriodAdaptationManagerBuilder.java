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

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Builder for PeriodAdaptationManager.
 * Do NOT pass nulls to its methods.
 */
public class PeriodAdaptationManagerBuilder {

	/** Counter for generating ids. */
	static protected AtomicLong counter = new AtomicLong(-1);

	/** After building a PeriodAdaptationManager, its builder is mapped here under its id. */
	final protected Map<String, PeriodAdaptationManagerBuilder> map;

	InvariantFitnessCombiner invariantFitnessCombiner =
			new InvariantFitnessCombinerAverage();

	AdapteeSelector adapteeSelector = new AdapteeSelectorTree();

	DirectionSelector directionSelector = new DirectionSelectorImpl();

	DeltaComputor deltaComputor = new DeltaComputorFixed();

	/**
	 * Only constructor.
	 * @param map where to store built managers' data, not null!
	 */
	protected PeriodAdaptationManagerBuilder(
			final Map<String, PeriodAdaptationManagerBuilder> map) {
		this.map = map;
	}

	/**
	 * Sets invariant fitness combiner.
	 * @param invariantFitnessCombiner new fitness combiner
	 * @return this
	 */
	public PeriodAdaptationManagerBuilder withInvariantFitnessCombiner(
			final InvariantFitnessCombiner invariantFitnessCombiner) {
		this.invariantFitnessCombiner = invariantFitnessCombiner;
		return this;
	}

	/**
	 * Sets adaptee selector.
	 * @param adapteeSelector new adaptee selector
	 * @return this
	 */
	public PeriodAdaptationManagerBuilder withAdapteeSelector(
			final AdapteeSelector adapteeSelector) {
		this.adapteeSelector = adapteeSelector;
		return this;
	}

	/**
	 * Sets direction selector.
	 * @param directionSelector new direction selector
	 * @return this
	 */
	public PeriodAdaptationManagerBuilder withDirectionSelector(
			final DirectionSelector directionSelector) {
		this.directionSelector = directionSelector;
		return this;
	}

	/**
	 * Sets delta computor.
	 * @param deltaComputor new delta computor
	 * @return this
	 */
	public PeriodAdaptationManagerBuilder withDeltaComputor(
			final DeltaComputor deltaComputor) {
		this.deltaComputor = deltaComputor;
		return this;
	}

	/**
	 * Build new PeriodAdaptationManager.
	 * The builder should not be modified after calling this!
	 * @return new PeriodAdaptationManager
	 */
	public PeriodAdaptationManager build() {
		final long id = counter.incrementAndGet();
		final PeriodAdaptationManager result = new PeriodAdaptationManager(id);
		map.put(result.id, this);
		return result;
	}
}

