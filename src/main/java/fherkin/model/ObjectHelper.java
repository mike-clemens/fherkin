package fherkin.model;

/**
 * Class with object helper methods.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class ObjectHelper {
	
	public static boolean equals(Object o1, Object o2) {
		if(o1 == null && o2 == null)
			return true;
		if(o1 == null && o2 != null)
			return false;
		if(o1 != null && o2 == null)
			return false;
		return o1.equals(o2);
	}
	
	public static int hashCode(Object... objects) {
		int hashCode = 17;
		for(Object o : objects)
			hashCode *= 37 + (o == null ? 0 : o.hashCode());
		return hashCode;
	}

}