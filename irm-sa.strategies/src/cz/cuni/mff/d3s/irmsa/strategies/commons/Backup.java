package cz.cuni.mff.d3s.irmsa.strategies.commons;

import cz.cuni.mff.d3s.irm.model.design.impl.KnowledgeImpl;

/**
 * Class holding the information needed to restore the previous state of the system.
 */
public abstract class Backup extends KnowledgeImpl {

	/**
	 * Only constructor.
	 */
	public Backup() {
		this.name = createBackupName();
		this.type = createBackupType();
	}

	/**
	 * Returns name of the backup. Called in constructor!
	 * @return name of the backup
	 */
	protected abstract String createBackupName();

	/**
	 * Returns name of the backup. Called in constructor!
	 * @return name of the backup
	 */
	protected abstract String createBackupType();
}