package cz.cuni.mff.d3s.irmsa.strategies.period;

import cz.cuni.mff.d3s.irmsa.strategies.commons.StateHolder;

public class PeriodStateHolder extends StateHolder<PeriodBackup> {

	private static final long serialVersionUID = 8934463139475027143L;

	@Override
	protected String createStateHolderName() {
		return "PeriodStateHolder";
	}

	@Override
	protected String createStateHolderType() {
		return "PeriodStateHolderType";
	}

	@Override
	protected PeriodBackup resetBackup(final PeriodBackup backup) {
		if (backup == null) {
			return new PeriodBackup();
		}
		backup.exchanges.clear();
		backup.processes.clear();
		return backup;
	}
}
