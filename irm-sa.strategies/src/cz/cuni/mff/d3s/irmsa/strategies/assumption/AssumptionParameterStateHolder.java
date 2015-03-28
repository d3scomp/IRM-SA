package cz.cuni.mff.d3s.irmsa.strategies.assumption;

import cz.cuni.mff.d3s.irmsa.strategies.commons.StateHolder;

public class AssumptionParameterStateHolder extends StateHolder<AssumptionParameterBackup> {

	@Override
	protected String createStateHolderName() {
		return "AssumptionParameterStateHolder";
	}

	@Override
	protected String createStateHolderType() {
		return "AssumptionParameterStateHolderType";
	}

	@Override
	protected AssumptionParameterBackup resetBackup(final AssumptionParameterBackup backup) {
		if (backup == null) {
			return new AssumptionParameterBackup();
		}
		backup.parameters.clear();
		return backup;
	}
}
