package fherkin.io.impl;

import fherkin.model.ObjectHelper;

/**
 * Class representing a pair of objects.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class Pair<T1, T2> {
	
	private T1 first;
	private T2 second;
	
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null || !(o instanceof Pair))
			return false;
		
		Pair that = (Pair) o;
		return ObjectHelper.equals(first, that.first)
			&& ObjectHelper.equals(second, that.second);
	}
	
	@Override
	public int hashCode() {
		return ObjectHelper.hashCode(first, second);
	}

	public T1 getFirst() {
		return first;
	}

	public T2 getSecond() {
		return second;
	}

}