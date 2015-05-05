package filter;

/**
 * A filter for applying noise and inaccuracy to a data.
 * Filter is bound to a certain data type is it meant to
 * modify. Filters can be chained to combine e.g. noise
 * and inaccuracy, or different types of noises. 
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 * @param <T> The data type that can be filtered by the filter.
 */
public abstract class Filter<T> {

	/**
	 * Inner filter is dedicated for filter chaining.
	 */
	protected Filter<T> innerFilter;
	
	/**
	 * Construct new filter without a chain inside it.
	 */
	public Filter() {
		innerFilter = null;
	}
	
	/**
	 * Construct new filter with a chain of filters inside it. 
	 * @param innerFilter The chain of inner filters.
	 */
	public Filter(Filter<T> innerFilter) {
		this.innerFilter = innerFilter;
	}
	
	/**
	 * Add inner filter/chain of filters.
	 * @param filter the filter/chain of filters to add.
	 */
	public void addFilter(Filter<T> filter){
		if(innerFilter == null){
			innerFilter = filter;
		} else {
			// Add the given filter to the end of the chain
			innerFilter.addFilter(filter);
		}
	}
	
	/**
	 * Filter the given data. Returns data with all the filters
	 * in the chain applied.
	 * @param data The data to be filtered.
	 * @return Data with all the filters in the chain applied.
	 */
	public T apply(final T data) {
		T filteredData = innerFilter != null
				? innerFilter.apply(data)
				: data;
		return applyNoise(filteredData);
	}
	
	/**
	 * Apply the noise defined by the filter instance to the
	 * given data.
	 * @param data The data to be filtered.
	 * @return Data with applied noise.
	 */
	abstract protected T applyNoise(final T data);
	
}
