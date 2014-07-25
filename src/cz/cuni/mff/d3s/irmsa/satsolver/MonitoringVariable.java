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
package cz.cuni.mff.d3s.irmsa.satsolver;

import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;

/**
 * Variable that holds the value of one invariant monitor that specifies whether
 * the corresponding invariant instance is (currently) satisfied or not.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
public class MonitoringVariable extends SATVariable {

	public MonitoringVariable(InvariantInstance wrappedInstance) {
		super(wrappedInstance);
	}

}
