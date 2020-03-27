package fherkin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.commons.logging.Log;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for:  LogHelper
 * 
 * @author Mike Clemens
 */
public class LogHelperTest extends LogHelper {
	
	private Log enabled;
	private Log disabled;
	private String enabledString;
	private String disabledString;
	
	public LogHelperTest() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader == null)
			classLoader = LogHelperTest.class.getClassLoader();
		
		enabled = (Log) Proxy.newProxyInstance(classLoader, new Class<?>[] {Log.class}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if(method.getName().equals("isTraceEnabled"))
					return true;
				else
				if(method.getName().equals("trace")) {
					enabledString = (String) args[0];
					return null;
				}
				else
					throw new NotImplementedException(method.getName());
			}
		});
		
		disabled = (Log) Proxy.newProxyInstance(classLoader, new Class<?>[] {Log.class}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if(method.getName().equals("isTraceEnabled"))
					return false;
				else
				if(method.getName().equals("trace")) {
					disabledString = (String) args[0];
					return null;
				}
				else
					throw new NotImplementedException(method.getName());
			}
		});
	}
	
	@Before
	public void before() {
		enabledString = null;
		disabledString = null;
	}
	
	@Test
	public void testTraceDisabled() {
		trace(disabled, getClass(), "testTraceDisabled", new Object[0]);
		Assert.assertNull(disabledString);
	}

	@Test
	public void testTraceEnabled() {
		trace(enabled, getClass(), "testTraceEnabled", new Object[0]);
		Assert.assertNotNull(enabledString);
		Assert.assertEquals("LogHelperTest.testTraceEnabled()", enabledString);
	}
	
	@Test
	public void testTraceArgs() {
		trace(enabled, getClass(), "testTraceArgs", new Object[] {
				12345,
				123.45f,
				"this is a string",
				"this is a \"so-called\" string",
				true
		});
		
		Assert.assertEquals("LogHelperTest.testTraceArgs(12345, 123.45, \"this is a string\", \"this is a \\\"so-called\\\" string\", true)", enabledString);
	}

}