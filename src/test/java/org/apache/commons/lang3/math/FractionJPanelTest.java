package org.apache.commons.lang3.math;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.swing.ComboBoxModel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.function.TriFunction;
import org.meeuw.functional.TriPredicate;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.reflect.Reflection;

import io.github.toolfactory.narcissus.Narcissus;

class FractionJPanelTest {

	private static class IH implements InvocationHandler {

		private Boolean test = null;

		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			//
			final String name = getName(method);
			//
			if (Boolean.logicalOr(proxy instanceof Predicate, proxy instanceof TriPredicate)) {
				//
				if (Objects.equals(name, "test")) {
					//
					return test;
					//
				} // if
					//
			} else if (Boolean.logicalOr(proxy instanceof Function, proxy instanceof TriFunction)) {
				//
				if (Objects.equals(name, "apply")) {
					//
					return null;
					//
				} // if
					//
			} else if (proxy instanceof ComboBoxModel) {
				//
				if (Objects.equals(name, "getSelectedItem")) {
					//
					return null;
					//
				} // if
					//
			} // if
				//
			throw new Throwable(name);
			//
		}

	}

	private static String getName(final Member instance) {
		return instance != null ? instance.getName() : null;
	}

	@Test
	void testNull() throws Throwable {
		//
		final Method[] ms = FractionJPanel.class.getDeclaredMethods();
		//
		Method m = null;
		//
		Object result = null;
		//
		String toString = null;
		//
		Object[] os = null;
		//
		FractionJPanel instance = null;
		//
		for (int i = 0; ms != null && i < ms.length; i++) {
			//
			if ((m = ArrayUtils.get(ms, i)) == null || m.isSynthetic()) {
				//
				continue;
				//
			} // if
				//
			os = toArray(Collections.nCopies(m.getParameterCount(), null));
			//
			toString = Objects.toString(m);
			//
			if (Modifier.isStatic(m.getModifiers())) {
				//
				result = Narcissus.invokeStaticMethod(m, os);
				//
			} else {
				//
				result = Narcissus
						.invokeMethod(
								instance = ObjectUtils.getIfNull(instance,
										() -> (FractionJPanel) Narcissus.allocateInstance(FractionJPanel.class)),
								m, os);
				//
			} // if
				//
			if (Objects.equals(getReturnType(m), Boolean.TYPE)) {
				//
				Assert.assertNotNull(result, toString);
				//
			} else {
				//
				Assert.assertNull(result, toString);
				//
			} // if
				//
		} // for
			//
	}

	private static Class<?> getReturnType(final Method instance) {
		return instance != null ? instance.getReturnType() : null;
	}

	private static Object[] toArray(final Collection<?> instance) {
		return instance != null ? instance.toArray() : null;
	}

	@Test
	void testNotNull() throws Throwable {
		//
		final Method[] ms = FractionJPanel.class.getDeclaredMethods();
		//
		Method m = null;
		//
		Object result = null;
		//
		String toString, name = null;
		//
		Object[] os = null;
		//
		FractionJPanel instance = null;
		//
		Class<?>[] parameterTypes = null;
		//
		Class<?> parameterType = null;
		//
		Collection<Object> collection = null;
		//
		IH ih = null;
		//
		Field[] fs = null;
		//
		Field f = null;
		//
		for (int i = 0; ms != null && i < ms.length; i++) {
			//
			if ((m = ArrayUtils.get(ms, i)) == null || m.isSynthetic()
					|| (parameterTypes = m.getParameterTypes()) == null) {
				//
				continue;
				//
			} // if
				//
			clear(collection = ObjectUtils.getIfNull(collection, ArrayList::new));
			//
			for (int j = 0; j < parameterTypes.length; j++) {
				//
				if ((parameterType = ArrayUtils.get(parameterTypes, j)) != null && parameterType.isArray()) {
					//
					add(collection, Array.newInstance(parameterType.getComponentType(), 0));
					//
				} else if (Objects.equals(parameterType, Class.class)) {
					//
					add(collection, Class.class);
					//
				} else if (Objects.equals(parameterType, JTextComponent.class)) {
					//
					add(collection, Narcissus.allocateInstance(JTextField.class));
					//
				} else if (parameterType != null && parameterType.isInterface()) {
					//
					if ((ih = ObjectUtils.getIfNull(ih, IH::new)) != null
							&& (fs = IH.class.getDeclaredFields()) != null) {
						//
						for (int k = 0; k < fs.length; k++) {
							//
							if ((f = ArrayUtils.get(fs, k)) == null) {
								//
								continue;
								//
							} // if
								//
							if (Objects.equals(f.getType(), Boolean.class)) {
								//
								Narcissus.setField(ih, f, Boolean.TRUE);
								//
							} // if
								//
						} // for
							//
					} // if
						//
					add(collection, Reflection.newProxy(parameterType, ih = ObjectUtils.getIfNull(ih, IH::new)));
					//
				} else {
					//
					System.out.println(parameterType);
					//
					add(collection, Narcissus.allocateInstance(parameterType));
					//
				} // if
					//
			} // for
				//
			os = toArray(collection);
			//
			toString = Objects.toString(m);
			//
			if (Modifier.isStatic(m.getModifiers())) {
				//
				result = Narcissus.invokeStaticMethod(m, os);
				//
			} else {
				//
				result = Narcissus
						.invokeMethod(
								instance = ObjectUtils.getIfNull(instance,
										() -> (FractionJPanel) Narcissus.allocateInstance(FractionJPanel.class)),
								m, os);
				//
			} // if
				//
			if (Objects.equals(getReturnType(m), Boolean.TYPE)
					|| Boolean.logicalAnd(Objects.equals(getName(m), "getClass"),
							Arrays.equals(parameterTypes, new Object[] { Object.class }))) {
				//
				Assert.assertNotNull(result, toString);
				//
			} else {
				//
				Assert.assertNull(result, toString);
				//
			} // if
				//
		} // for
			//
	}

	private static <E> void add(final Collection<E> instance, final E item) {
		if (instance != null) {
			instance.add(item);
		}
	}

	private static void clear(final Collection<?> instance) {
		if (instance != null) {
			instance.clear();
		}
	}

}