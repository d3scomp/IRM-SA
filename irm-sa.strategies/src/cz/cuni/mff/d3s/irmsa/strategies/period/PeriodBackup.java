package cz.cuni.mff.d3s.irmsa.strategies.period;

import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.irm.model.design.impl.KnowledgeImpl;
import cz.cuni.mff.d3s.irmsa.strategies.commons.Backup;
import cz.cuni.mff.d3s.irmsa.strategies.commons.Direction;

public class PeriodBackup extends Backup {

	/** Changes of process invariants. */
	public Map<String, Change> processes = new HashMap<>();

	/** Changes of exchange invariants. */
	public Map<String, Change> exchanges = new HashMap<>();

	@Override
	protected String createBackupName() {
		return "PeriodBackup";
	}

	@Override
	protected String createBackupType() {
		return "PeriodBackupType";
	}

	/**
	 * Simple wrapper around period change of an invariant.
	 */
	static public class Change extends KnowledgeImpl {

		/** Period delta. */
		public long delta;

		/** Direction of change to revert the change. */
		public Direction direction;

		/**
		 * Only constructor.
		 * @param delta period delta
		 * @param direction direction of change
		 */
		public Change(final long delta, final Direction direction) {
			this.name = "PeriodAdaptationManagerBackupItem";
			this.type = "PeriodAdaptationManagerBackupItemType";
			this.delta = delta;
			this.direction = direction;
		}
	}
}
