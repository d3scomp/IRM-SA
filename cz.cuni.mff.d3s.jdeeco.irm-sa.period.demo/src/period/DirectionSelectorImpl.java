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

import cz.cuni.mff.d3s.irm.model.design.impl.KnowledgeImpl;

/**
 * Temporary direction selector that does nothing.
 */
public class DirectionSelectorImpl extends KnowledgeImpl implements DirectionSelector {

	/**
	 * Only constructor.
	 */
	public DirectionSelectorImpl() {
		this.name = "DirectionSelectorImpl";
		this.type = "DirectionSelectorImplType";
	}

	@Override
	public void selectDirection(InvariantInfo<?> info) {
		//TODO implement
		info.direction = Direction.NO;
	}
}
