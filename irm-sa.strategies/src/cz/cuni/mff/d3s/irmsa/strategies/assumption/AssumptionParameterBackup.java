package cz.cuni.mff.d3s.irmsa.strategies.assumption;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.irm.model.design.impl.KnowledgeImpl;
import cz.cuni.mff.d3s.irmsa.strategies.commons.Backup;
import cz.cuni.mff.d3s.irmsa.strategies.commons.Direction;

public class AssumptionParameterBackup extends Backup {

	private static final long serialVersionUID = -2097505002854547349L;

	/** Changes of assumption parameters. */
	public Map<String, Change> parameters = new HashMap<>();

	@Override
	protected String createBackupName() {
		return "AssumptionParameterBackup";
	}

	@Override
	protected String createBackupType() {
		return "AssumptionParameterBackupType";
	}

	/**
	 * Simple wrapper around adaptation change of an invariant.
	 */
	static public class Change extends KnowledgeImpl implements Serializable {

		private static final long serialVersionUID = 975693013506407761L;

		/** Adaptation delta. */
		public Number delta;

		/** Direction of change to revert the change. */
		public Direction direction;

		/** Parameter id. */
		public String paramId;

		/**
		 * Only constructor.
		 * @param parameter id
		 * @param delta period delta
		 * @param direction direction of change
		 */
		public Change(final String paramId, final Number delta, final Direction direction) {
			this.name = "AssumptionParameterAdaptationManagerBackupItem";
			this.type = "AssumptionParameterAdaptationManagerBackupItemType";
			this.paramId = paramId;
			this.delta = delta;
			this.direction = direction;
		}
	}
}
