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

/**
 * Builder for PeriodAdaptationManager.
 */
public class PeriodAdaptationManagerBuilder {

	InvariantFitnessCombiner invariantFitnessCombiner =
			new InvariantFitnessCombinerAverage();

	AdapteeSelector adapteeSelector = new AdapteeSelectorTree();

	DirectionSelector directionSelector = new DirectionSelectorImpl();

	DeltaComputor deltaComputor = new DeltaComputorFixed();

	/**
	 * Only constructor.
	 */
	protected PeriodAdaptationManagerBuilder() {
		//nothing
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
	 * @return new PeriodAdaptationManager
	 */
	public PeriodAdaptationManager build() {
		final PeriodAdaptationManager result = new PeriodAdaptationManager();
		result.invariantFitnessCombiner = invariantFitnessCombiner;
		result.adapteeSelector = adapteeSelector;
		result.directionSelector = directionSelector;
		result.deltaComputor = deltaComputor;
		return result;
	}
}

