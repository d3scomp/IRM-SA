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
package cz.cuni.mff.d3s.irmsa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.irm.model.runtime.api.ExchangeInvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.IRMInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.InvariantInstance;
import cz.cuni.mff.d3s.irm.model.runtime.api.ProcessInvariantInstance;

/**
 * Responsible for enacting the changes prescribed by the IRM runtime model
 * instances to the jDEEco runtime metadata model. These changes are then
 * translated (by EMF listeners) to starting/stopping tasks on the jDEECo
 * scheduler.
 *
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class ArchitectureReconfigurator {

	private final RuntimeMetadata runtime;
	private final List<IRMInstance> IRMInstances = new ArrayList<>();

	private final Set<ComponentProcess> processesToRun = new HashSet<>();
	private final Set<EnsembleDefinition> ensemblesToRun = new HashSet<>();

	private final Set<ComponentProcess> processesToStop = new HashSet<>();
	private final Set<EnsembleDefinition> ensemblesToStop = new HashSet<>();

	public ArchitectureReconfigurator(RuntimeMetadata runtime) {
		super();
		this.runtime = runtime;
	}

	public void addInstance(IRMInstance i) {
		IRMInstances.add(i);
	}

	/**
	 *
	 */
	public void toggleProcessesAndEnsembles() {
		// mark the processes and ensembles that should be scheduled (active) by traversing all models
		// if a process/ensemble is active in *at least one* IRM instance, then it's marked as active
		for (IRMInstance irmInstance : IRMInstances) {
			for (InvariantInstance ii: irmInstance.getInvariantInstances()) {
				if (ii instanceof ProcessInvariantInstance) {
					ComponentProcess p = ((ProcessInvariantInstance) ii).getComponentProcess();
					if (p != null) {
						if (ii.isSelected()) {
							processesToRun.add(p);
						} else {
							processesToStop.add(p);
						}
					}
				} else if (ii instanceof ExchangeInvariantInstance) {
					EnsembleDefinition e = ((ExchangeInvariantInstance) ii).getEnsembleDefinition();
					if (e != null) {
						if (ii.isSelected()) {
							ensemblesToRun.add(e);
						} else {
							ensemblesToStop.add(e);
						}
					}
				}
			}
		}
		// apply changes to the runtime model using ecore listeners
		// by changing ComponentProcess.isActive and EnsembleContoller.isActive
		for (ComponentInstance c : runtime.getComponentInstances()) {
			// system components are excepted from this process
			if (!c.isSystemComponent()) {
				// go through all processes and activate/deactivate them
				for (ComponentProcess p : c.getComponentProcesses()) {
					if (processesToRun.contains(p)) {
						p.setActive(true);
					} else if (processesToStop.contains(p)) {
						p.setActive(false);
					}
				}
				// go through all ensemble controllers and activate/deactivate them
				for (EnsembleController ec : c.getEnsembleControllers()) {
					if (ensemblesToRun.contains(ec.getEnsembleDefinition())) {
						ec.setActive(true);
					} else if (ensemblesToStop.contains(ec.getEnsembleDefinition())) {
						ec.setActive(false);
					}
				}
			}
		}
	}

}
