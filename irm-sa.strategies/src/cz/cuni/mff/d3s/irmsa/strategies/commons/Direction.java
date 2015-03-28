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
package cz.cuni.mff.d3s.irmsa.strategies.commons;

/**
 * Direction of adjustment.
 */
public enum Direction {
	UP {
		@Override
		public byte getCoef() {
			return 1;
		}

		@Override
		public Direction opposite() {
			return DOWN;
		}
	},
	DOWN {
		@Override
		public byte getCoef() {
			return  -1;
		}

		@Override
		public Direction opposite() {
			return UP;
		}
	},
	NO {
		@Override
		public byte getCoef() {
			return 0;
		}

		@Override
		public Direction opposite() {
			return NO;
		}
	};

	/**
	 * Returns coefficient of this direction. Intended to multiply the delta.
	 * @return coefficient of this direction
	 */
	public abstract byte getCoef();

	/**
	 * Returns opposite direction.
	 * @return opposite direction
	 */
	public abstract Direction opposite();
}
