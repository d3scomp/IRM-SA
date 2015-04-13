package cz.cuni.mff.d3s.irmsa.strategies.commons;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Stores last invariant infos from monitor to adapt methods of EvolutionaryAdaptationManager.
 */
class InvariantInfosStorage {

	/**
	 * Mapping of Manager ID -> Set of InvariantInfo.
	 * Synchronized just to be sure.
	 */
	static private Map<String, Set<InvariantInfo<?>>> storage =
			Collections.synchronizedMap(new HashMap<>());

	/**
	 * Stores invariant infos for given manager.
	 * @param managerId manager id
	 * @param infos invariant infos
	 */
	static void storeInvariantInfos(final String managerId,
			final Set<InvariantInfo<?>> infos) {
		storage.put(managerId, infos);
	}

	/**
	 * Retrieves invariant infos for given manager.
	 * @param managerId manager id
	 * @return last invariant infos
	 */
	static Set<InvariantInfo<?>> retrieveInvariantInfos(final String managerId) {
		return storage.get(managerId);
	}

	/**
	 * Utility classes need no constructor.
	 */
	private InvariantInfosStorage() {
		//nothing
	}
}
