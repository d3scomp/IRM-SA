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
package cz.cuni.mff.d3s.deeco.demo.vehicles.simple;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InvariantMonitor;
import cz.cuni.mff.d3s.deeco.annotations.InvariantMonitors;

/**
 * Holds the monitors for non-process and non-exchange invariants.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
@InvariantMonitors
public class Monitors {

	@InvariantMonitor("a1")
	public static boolean checkAssumption1(
		@In("Vehicle.position") Integer vehiclePosition,  
		@In("Parking.position") Integer parkingPosition 
	) {
		System.out.println("Invoked checkAssumption1 monitor");
		return (vehiclePosition - parkingPosition) < 5000; // always true
	}
	
	@InvariantMonitor("i4")
	public static boolean checkInvarianti4(
		@In("Vehicle.position") Integer vehiclePosition,  
		@In("Parking.position") Integer parkingPosition 
	) {
		System.out.println("Invoked checkInvarianti4 monitor");
		return false;
	}
	
}
