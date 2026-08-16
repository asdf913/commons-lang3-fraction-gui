package org.apache.commons.lang3.math;

import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.ComboBoxModel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.function.TriFunction;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.reflect.Reflection;

import io.github.toolfactory.narcissus.Narcissus;

class FractionJPanelTest {

	private static Method METHOD_TO_FRACTION, METHOD_INVOKE, METHOD_CAST = null;

	@BeforeClass
	static void beforeClass() throws NoSuchMethodException {
		//
		final Class<?> clz = FractionJPanel.class;
		//
		(METHOD_TO_FRACTION = clz.getDeclaredMethod("toFraction", String.class, String.class, String.class))
				.setAccessible(true);
		//
		(METHOD_INVOKE = clz.getDeclaredMethod("invoke", Method.class, Object.class, Object[].class))
				.setAccessible(true);
		//
		(METHOD_CAST = clz.getDeclaredMethod("cast", Class.class, Object.class)).setAccessible(true);
		//
	}

	private static class IH implements InvocationHandler {

		private Boolean test = null;

		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			//
			if (Objects.equals(getReturnType(method), Void.TYPE)) {
				//
				return null;
				//
			} // if
				//
			final String name = getName(method);
			//
			if (Boolean.logicalAnd(Objects.equals(name, "toString"),
					method != null && method.getParameterCount() == 0)) {
				//
				return toString();
				//
			} // if
				//
			if (proxy instanceof Predicate) {
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
			} else if (proxy instanceof ComboBoxModel && Objects.equals(name, "getSelectedItem")) {
				//
				return null;
				//
			} else if (proxy instanceof Stream) {
				//
				if (Objects.equals(name, "map")) {
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
		Class<?>[] parameterTypes = null;
		//
		Collection<Object> collection = null;
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
				if (Objects.equals(ArrayUtils.get(parameterTypes, j), Boolean.TYPE)) {
					//
					add(collection, Boolean.TRUE);
					//
				} else {
					//
					add(collection, null);
					//
				} // if
					//
			} // for
				//
			os = toArray(collection);
			//
			toString = Objects.toString(m);
			//
			final Method m1 = m;
			//
			final Object[] os1 = os;
			//
			if (Modifier.isStatic(m.getModifiers())) {
				//
				if (Boolean.logicalAnd(Objects.equals(m.getName(), "toFraction"), Boolean.logicalOr(
						Arrays.equals(parameterTypes = m.getParameterTypes(),
								new Class<?>[] { String.class, String.class, String.class }),
						Arrays.equals(parameterTypes, new Class<?>[] { Class
								.forName("org.apache.commons.lang3.math.FractionJPanel$FractionJTextComponent") })))) {
					//
					Assert.assertThrows(IllegalStateException.class, () -> {
						Narcissus.invokeStaticMethod(m1, os1);
					});
					//
					continue;
					//
				} else {
					//
					result = Narcissus.invokeStaticMethod(m, os);
					//
				} // if
					//
			} else {
				//
				if (Boolean.logicalAnd(Objects.equals(m.getName(), "actionPerformed"),
						Arrays.equals(m.getParameterTypes(), new Class<?>[] { ActionEvent.class }))) {
					//
					final FractionJPanel instance1 = instance = ObjectUtils.getIfNull(instance,
							() -> (FractionJPanel) Narcissus.allocateInstance(FractionJPanel.class));
					//
					Assert.assertThrows(IllegalStateException.class, () -> {
						Narcissus.invokeMethod(instance1, m1, os1);
					});
					//
					continue;
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
				} else if (Objects.equals(parameterType, Boolean.TYPE)) {
					//
					add(collection, Boolean.TRUE);
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
			final Method m1 = m;
			//
			final Object[] os1 = os;
			//
			if (Modifier.isStatic(m.getModifiers())) {
				//
				if (Boolean.logicalAnd(Objects.equals(m.getName(), "toFraction"), Boolean.logicalOr(
						Arrays.equals(parameterTypes = m.getParameterTypes(),
								new Class<?>[] { String.class, String.class, String.class }),
						Arrays.equals(parameterTypes, new Class<?>[] { Class
								.forName("org.apache.commons.lang3.math.FractionJPanel$FractionJTextComponent") })))) {
					//
					Assert.assertThrows(IllegalStateException.class, () -> {
						Narcissus.invokeStaticMethod(m1, os1);
					});
					//
					continue;
					//
				} else {
					//
					result = Narcissus.invokeStaticMethod(m, os);
					//
				} // if
					//
			} else {
				//
				if (Boolean.logicalAnd(Objects.equals(m.getName(), "actionPerformed"),
						Arrays.equals(m.getParameterTypes(), new Class<?>[] { ActionEvent.class }))) {
					//
					final FractionJPanel instance1 = instance = ObjectUtils.getIfNull(instance,
							() -> (FractionJPanel) Narcissus.allocateInstance(FractionJPanel.class));
					//
					Assert.assertThrows(IllegalStateException.class, () -> {
						Narcissus.invokeMethod(instance1, m1, os1);
					});
					//
					continue;
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
			} // if
				//
			if (Boolean.logicalOr(Objects.equals(getReturnType(m), Boolean.TYPE),
					Boolean.logicalAnd(Objects.equals(name = getName(m), "getClass"),
							Arrays.equals(parameterTypes, new Object[] { Object.class })))
					|| Boolean.logicalAnd(Objects.equals(name, "toPlainString"),
							Arrays.equals(parameterTypes, new Object[] { BigDecimal.class }))) {
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

	@Test
	public void testToFraction() throws Throwable {
		//
		final String string = "1.2";
		//
		Assert.assertEquals(Fraction.getFraction(string), toFraction(string, null, null));
		//
		final String space = " ";
		//
		Assert.assertThrows(InvocationTargetException.class, () -> toFraction(string, space, null));
		//
		Assert.assertThrows(InvocationTargetException.class, () -> toFraction(string, null, space));
		//
		final int two = 2;
		//
		final int three = 3;
		//
		Assert.assertEquals(Fraction.getFraction(2, 3),
				toFraction(null, Integer.toString(two), Integer.toString(three)));
		//
		Assert.assertThrows(InvocationTargetException.class,
				() -> toFraction(null, String.join(".", Integer.toString(two), "1"), Integer.toString(three)));
		//
		Assert.assertThrows(InvocationTargetException.class, () -> toFraction(null, Integer.toString(two), space));
		//
		Assert.assertThrows(InvocationTargetException.class,
				() -> toFraction(null, Integer.toString(two), String.join(".", Integer.toString(three), "1")));
		//
		final int four = 4;
		//
		Assert.assertEquals(Fraction.getFraction(2, 3, 4),
				toFraction(Integer.toString(two), Integer.toString(three), Integer.toString(four)));
		//
	}

	private static Fraction toFraction(final String whole, final String numerator, final String denominator)
			throws Throwable {
		try {
			final Object obj = invoke(METHOD_TO_FRACTION, null, whole, numerator, denominator);
			if (obj instanceof Fraction) {
				return cast(Fraction.class, obj);
			} else if (obj == null) {
				return null;
			}
			throw new Throwable(Objects.toString(obj.getClass()));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static Object invoke(final Method method, final Object instance, final Object... args)
			throws IllegalAccessException, InvocationTargetException {
		return METHOD_INVOKE != null ? METHOD_INVOKE.invoke(null, method, instance, args) : null;
	}

	private static <T> T cast(final Class<T> clz, final Object instance) throws Throwable {
		try {
			return (T) invoke(METHOD_CAST, null, clz, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

}