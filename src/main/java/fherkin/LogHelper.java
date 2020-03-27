package fherkin;

import org.apache.commons.logging.Log;

/**
 * Helper class for logging trace statements.
 * 
 * @author Mike Clemens
 * @since 1.0.0
 */
public class LogHelper {
	
	public static void trace(Log log, Class<?> cls, String methodName, Object... args) {
		if(log.isTraceEnabled()) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(cls.getSimpleName());
			buffer.append('.');
			buffer.append(methodName);
			buffer.append('(');
			
			for(int i = 0; i < args.length; i++) {
				if(i > 0)
					buffer.append(", ");
				
				if(args[i] instanceof String) {
					buffer.append('"');
					buffer.append(((String) args[i]).replaceAll("\"", "\\\\\""));
					buffer.append('"');
				}
				else
					buffer.append(args[i]);
			}
			
			buffer.append(')');
			log.trace(buffer.toString());
		}
	}

}