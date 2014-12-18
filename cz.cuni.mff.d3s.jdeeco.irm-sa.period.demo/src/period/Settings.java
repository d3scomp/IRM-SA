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

public interface Settings {

//  The configuration used for the JSS paper.
//	public static final int PROCESS_PERIOD = 500; // in milliseconds
//	public static final int ENSEMBLE_PERIOD = 500;
//	public static final int ADAPTATION_PERIOD = 500;
//	public static final int BROADCAST_PERIOD = 400;
//
//	public static final int NETWORK_DELAY = 500; // in milliseconds
//	public static final long BASE_INACCURACY = 1000;
//	public static final long INACCURACY_INTERVAL = 1000;

	public static final int PROCESS_PERIOD = 500; // in milliseconds
	public static final int ENSEMBLE_PERIOD = 500;
	public static final int ADAPTATION_PERIOD = 500;
	public static final int BROADCAST_PERIOD = 400;

	public static final int NETWORK_DELAY = 500; // in milliseconds
	public static final long BASE_INACCURACY = 1000;
	public static final long INACCURACY_INTERVAL = 1000;
}
