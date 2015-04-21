package combined;

/**
 * Utility class.
 */
public class Utils {
	/**
	 * Returns true if key is contained in the array
	 * @param array array to search in
	 * @param key key to search for
	 * @return true if key is contained in the array, false otherwise
	 */
	static public boolean contains(int[] array, int key) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == key) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Utility classes need no constructor.
	 */
	private Utils() {
		//nothing
	}
}
