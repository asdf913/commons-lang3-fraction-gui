package org.apache.commons.lang3.math;

import java.awt.FocusTraversalPolicy;
import java.awt.Window;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.function.TriFunction;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.reflect.Reflection;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import io.github.toolfactory.narcissus.Narcissus;

class FractionJPanelTest {

	private static final String EMPTY = "";

	private static Method METHOD_TO_FRACTION, METHOD_INVOKE, METHOD_CAST, METHOD_CREATE_FOCUS_TRAVERSAL_POLICY,
			METHOD_TO_MATH_ML = null;

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
		(METHOD_CREATE_FOCUS_TRAVERSAL_POLICY = clz.getDeclaredMethod("createFocusTraversalPolicy", List.class))
				.setAccessible(true);
		//
		(METHOD_TO_MATH_ML = clz.getDeclaredMethod("toMathML", String.class, String.class, String.class))
				.setAccessible(true);
		//
	}

	private static class IH implements InvocationHandler {

		private Boolean test = null;

		private Integer size, length = null;

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
			} else if (Boolean.logicalAnd(Objects.equals(name, "equals"),
					method != null && Arrays.equals(method.getParameterTypes(), new Class<?>[] { Object.class }))) {
				//
				return equals(proxy);
				//
			} else if (Objects.equals(getReturnType(method), method != null ? method.getDeclaringClass() : null)) {
				//
				return proxy;
				//
			} else if (Boolean.logicalAnd(proxy instanceof Iterable, Objects.equals(name, "spliterator"))) {
				//
				return null;
				//
			} // if
				//
			if (Boolean.logicalAnd(proxy instanceof Collection, Objects.equals(name, "toArray"))) {
				//
				return null;
				//
			} // if
				//
			if (Boolean.logicalAnd(proxy instanceof Predicate, Objects.equals(name, "test"))) {
				//
				return test;
				//
			} else if (Boolean.logicalAnd(Boolean.logicalOr(proxy instanceof Function, proxy instanceof TriFunction),
					Objects.equals(name, "apply"))) {
				//
				return null;
				//
			} else if (proxy instanceof ComboBoxModel) {
				//
				if (contains(Arrays.asList("getSelectedItem", "getElementAt"), name)) {
					//
					return null;
					//
				} else if (Objects.equals(name, "getSize")) {
					//
					return size;
					//
				} // if
					//
			} else if (Boolean.logicalAnd(proxy instanceof Stream, contains(Arrays.asList("map", "toList"), name))) {
				//
				return null;
				//
			} else if (Boolean.logicalAnd(proxy instanceof Member, Objects.equals(name, "getName"))) {
				// ,
				return null;
				//
			} else if (Boolean.logicalAnd(proxy instanceof Entry,
					contains(Arrays.asList("getKey", "getValue"), name))) {
				//
				return null;
				//
			} else if (Boolean.logicalAnd(proxy instanceof DocumentEvent, Objects.equals(name, "getDocument"))) {
				//
				return null;
				//
			} else if (proxy instanceof Document) {
				//
				if (Objects.equals(name, "getLength")) {
					//
					return length;
					//
				} else if (Objects.equals(name, "getText")) {
					//
					return null;
					//
				} // if
					//
			} else if (Boolean.logicalAnd(proxy instanceof Map, contains(Arrays.asList("get", "entrySet"), name))) {
				//
				return null;
				//
			} else if (Boolean.logicalAnd(proxy instanceof Browser, Objects.equals(name, "newPage"))) {
				//
				return null;
				//
			} else if (Boolean.logicalAnd(proxy instanceof Locator, Objects.equals(name, "screenshot"))) {
				//
				return null;
				//
			} else if (Boolean.logicalAnd(proxy instanceof Page, Objects.equals(name, "locator"))) {
				//
				return null;
				//
			} else if (Boolean.logicalAnd(proxy instanceof Playwright, Objects.equals(name, "chromium"))) {
				//
				return null;
				//
			} else if (Boolean.logicalAnd(proxy instanceof BrowserType, Objects.equals(name, "launch"))) {
				//
				return null;
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

	private IH ih = null;

	@BeforeMethod
	void beforeMethod() {
		//
		ih = new IH();
		//
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
		final Class<?> clz = Class.forName("org.apache.commons.lang3.math.FractionJPanel$FractionJTextComponent");
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
				if (Objects.equals(parameterType = ArrayUtils.get(parameterTypes, j), Boolean.TYPE)) {
					//
					add(collection, Boolean.TRUE);
					//
				} else if (Objects.equals(parameterType, Character.TYPE)) {
					//
					add(collection, Character.valueOf(' '));
					//
				} else if (Objects.equals(parameterType, Integer.TYPE)) {
					//
					add(collection, Integer.valueOf(1));
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
			name = getName(m);
			//
			if (Modifier.isStatic(m.getModifiers())) {
				//
				if (Boolean.logicalAnd(Objects.equals(name, "toFraction"),
						Boolean.logicalOr(
								Arrays.equals(parameterTypes = m.getParameterTypes(),
										new Class<?>[] { String.class, String.class, String.class }),
								Arrays.equals(parameterTypes, new Class<?>[] { clz })))) {
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
				if (Boolean.logicalOr(
						Boolean.logicalAnd(Objects.equals(name, "actionPerformed"),
								Arrays.equals(m.getParameterTypes(), new Class<?>[] { ActionEvent.class })),
						Boolean.logicalAnd(Objects.equals(name, "changedUpdate"),
								Arrays.equals(m.getParameterTypes(), new Class<?>[] { DocumentEvent.class })))) {
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
			if (or(contains(Arrays.asList(Boolean.TYPE, Integer.TYPE), getReturnType(m)),
					Boolean.logicalAnd(
							Objects.equals(name, "createFocusTraversalPolicy"), Arrays.equals(parameterTypes,
									new Object[] { List.class })),
					Boolean.logicalAnd(Objects.equals(name, "toMathML"),
							Boolean.logicalOr(
									Arrays.equals(parameterTypes,
											new Object[] { String.class, String.class, String.class }),
									Arrays.equals(parameterTypes, new Object[] { clz }))),
					Boolean.logicalAnd(Objects.equals(name, "createDocumentFilter"),
							Arrays.equals(parameterTypes, new Object[] { Boolean.TYPE })))) {
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

	private static boolean contains(final Collection<?> instance, final Object item) {
		return instance != null && instance.contains(item);
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
		Class<?> parameterType, type = null;
		//
		Collection<Object> collection = null;
		//
		Field[] fs = null;
		//
		Field f = null;
		//
		final Class<?> clz = Class.forName("org.apache.commons.lang3.math.FractionJPanel$FractionJTextComponent");
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
				} else if (Objects.equals(parameterType, Character.TYPE)) {
					//
					add(collection, Character.valueOf(' '));
					//
				} else if (Objects.equals(parameterType, Integer.TYPE)) {
					//
					add(collection, Integer.valueOf(1));
					//
				} else if (Objects.equals(parameterType, Class.class)) {
					//
					add(collection, Class.class);
					//
				} else if (Boolean.logicalOr(Objects.equals(parameterType, JComponent.class),
						Objects.equals(parameterType, JTextComponent.class))) {
					//
					add(collection, Narcissus.allocateInstance(JTextField.class));
					//
				} else if (Objects.equals(parameterType, AbstractButton.class)) {
					//
					add(collection, Narcissus.allocateInstance(JButton.class));
					//
				} else if (Objects.equals(parameterType, Number.class)) {
					//
					add(collection, Narcissus.allocateInstance(Integer.class));
					//
				} else if (Objects.equals(parameterType, AbstractDocument.class)) {
					//
					add(collection, Narcissus.allocateInstance(PlainDocument.class));
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
							if (Objects.equals(type = f.getType(), Boolean.class)) {
								//
								Narcissus.setField(ih, f, Boolean.TRUE);
								//
							} else if (Objects.equals(type, Integer.class)) {
								//
								Narcissus.setField(ih, f, Integer.valueOf(1));
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
				if (Boolean
						.logicalAnd(
								Objects.equals(m
										.getName(), "toFraction"),
								Boolean.logicalOr(
										Arrays.equals(parameterTypes = m.getParameterTypes(),
												new Class<?>[] { String.class, String.class, String.class }),
										Arrays.equals(parameterTypes, new Class<?>[] { clz })))) {
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
				if (Boolean.logicalOr(
						Boolean.logicalAnd(Objects.equals(name = m.getName(), "actionPerformed"),
								Arrays.equals(m.getParameterTypes(), new Class<?>[] { ActionEvent.class })),
						Boolean.logicalAnd(Objects.equals(name, "changedUpdate"),
								Arrays.equals(parameterTypes, new Class<?>[] { DocumentEvent.class })))) {
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
			if (or(contains(Arrays.asList(Boolean.TYPE, Integer.TYPE), getReturnType(m)),
					Boolean.logicalAnd(Objects.equals(name = getName(m), "getClass"),
							Arrays.equals(parameterTypes, new Object[] { Object.class })),
					Boolean.logicalAnd(Objects.equals(name, "toPlainString"),
							Arrays.equals(parameterTypes, new Object[] { BigDecimal.class })),
					Boolean.logicalAnd(Objects.equals(name, "createFocusTraversalPolicy"),
							Arrays.equals(parameterTypes, new Object[] { List.class })),
					Boolean.logicalAnd(Objects.equals(name, "map"),
							Arrays.equals(parameterTypes, new Object[] { Stream.class, Function.class })),
					Boolean.logicalAnd(
							Objects.equals(name, "filter"), Arrays.equals(parameterTypes,
									new Object[] { Stream.class, Predicate.class })),
					Boolean.logicalAnd(Objects.equals(name, "toMathML"),
							Boolean.logicalOr(
									Arrays.equals(parameterTypes,
											new Object[] { String.class, String.class, String.class }),
									Arrays.equals(parameterTypes, new Object[] { clz }))),
					Boolean.logicalAnd(Objects.equals(name, "inverseBidiMap"),
							Arrays.equals(parameterTypes, new Object[] { BidiMap.class })),
					Boolean.logicalAnd(Objects.equals(name, "createDocumentFilter"),
							Arrays.equals(parameterTypes, new Object[] { Boolean.TYPE })))) {
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

	private static boolean or(final boolean a, final boolean b, final boolean... bs) {
		//
		boolean result = a || b;
		//
		if (result) {
			//
			return result;
			//
		} // if
			//
		for (int i = 0; bs != null && i < bs.length; i++) {
			//
			if (result |= bs[i]) {
				//
				return result;
				//
			} // if
				//
		} // for
			//
		return result;
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
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static Class<?> getClass(final Object instance) {
		return instance != null ? instance.getClass() : null;
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

	@Test
	public void testCreateFocusTraversalPolicy() throws Throwable {
		//
		final FocusTraversalPolicy focusTraversalPolicy1 = cast(FocusTraversalPolicy.class,
				invoke(METHOD_CREATE_FOCUS_TRAVERSAL_POLICY, null, new Object[] { null }));
		//
		if (focusTraversalPolicy1 == null) {
			//
			return;
			//
		} // if
			//
		final Method[] ms = FocusTraversalPolicy.class.getDeclaredMethods();
		//
		Method m = null;
		//
		for (int i = 0; ms != null && i < ms.length; i++) {
			//
			if ((m = ArrayUtils.get(ms, i)) == null || m.isSynthetic()) {
				//
				continue;
				//
			} // if
				//
			if (Boolean.logicalAnd(Objects.equals(getName(m), "getInitialComponent"),
					Arrays.equals(m.getParameterTypes(), new Class<?>[] { Window.class }))) {
				//
				final Method m1 = m;
				//
				Assert.assertThrows(IllegalArgumentException.class, () -> Narcissus.invokeMethod(focusTraversalPolicy1,
						m1, toArray(Collections.nCopies(m1.getParameterCount(), null))));
				//
			} else {
				//
				Assert.assertNull(Narcissus.invokeMethod(focusTraversalPolicy1, m,
						toArray(Collections.nCopies(m.getParameterCount(), null))));
				//
			} // if
				//
		} // for
			//
		final FocusTraversalPolicy focusTraversalPolicy2 = cast(FocusTraversalPolicy.class,
				invoke(METHOD_CREATE_FOCUS_TRAVERSAL_POLICY, null,
						new Object[] { Collections.singletonList(new JTextField()) }));
		//
		if (focusTraversalPolicy2 == null) {
			//
			return;
			//
		} // if
			//
		for (int i = 0; ms != null && i < ms.length; i++) {
			//
			if ((m = ArrayUtils.get(ms, i)) == null || m.isSynthetic()) {
				//
				continue;
				//
			} // if
				//
			if (Boolean.logicalAnd(Objects.equals(getName(m), "getInitialComponent"),
					Arrays.equals(m.getParameterTypes(), new Class<?>[] { Window.class }))) {
				//
				final Method m1 = m;
				//
				Assert.assertThrows(IllegalArgumentException.class, () -> Narcissus.invokeMethod(focusTraversalPolicy2,
						m1, toArray(Collections.nCopies(m1.getParameterCount(), null))));
				//
			} else {
				//
				Assert.assertNotNull(Narcissus.invokeMethod(focusTraversalPolicy2, m,
						toArray(Collections.nCopies(m.getParameterCount(), null))));
				//
			} // if
				//
		} // for
			//
	}

	@Test
	public void testDocumentFilterImpl() throws Throwable {
		//
		final DocumentFilter documentFilter = cast(DocumentFilter.class, Narcissus
				.allocateInstance(Class.forName("org.apache.commons.lang3.math.FractionJPanel$DocumentFilterImpl")));
		//
		final Method[] ms = DocumentFilter.class.getDeclaredMethods();
		//
		Method m = null;
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
				if (Objects.equals(ArrayUtils.get(parameterTypes, j), Integer.TYPE)) {
					//
					add(collection, Integer.valueOf(0));
					//
				} else {
					//
					add(collection, null);
					//
				} // if
					//
			} // for
				//
			Assert.assertNull(Narcissus.invokeMethod(documentFilter, m, toArray(collection)));
			//
		} // for
			//
		Class<?> parameterType = null;
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
				if (Objects.equals(parameterType = ArrayUtils.get(parameterTypes, j), Integer.TYPE)) {
					//
					add(collection, Integer.valueOf(0));
					//
				} else if (Objects.equals(parameterType, FilterBypass.class)) {
					//
					add(collection, Narcissus
							.allocateInstance(Class.forName("javax.swing.text.AbstractDocument$DefaultFilterBypass")));
					//
				} else if (parameterType != null && parameterType.isInterface()) {
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
			Assert.assertNull(Narcissus.invokeMethod(documentFilter, m, toArray(collection)));
			//
		} // for
			//
	}

	@Test
	public void testToMathML() throws Throwable {
		//
		final int one = 1;
		//
		final String oneString = Integer.toString(one);
		//
		Assert.assertEquals(oneString, toMathML(oneString, null, null));
		//
		Assert.assertEquals(oneString, toMathML(oneString, EMPTY, EMPTY));
		//
		Assert.assertEquals(oneString, toMathML(oneString, Integer.toString(0), oneString));
		//
		final int two = 2;
		//
		Assert.assertEquals(
				String.format("<math><mi>%1$s</mi><mfrac><mi>%1$s</mi><mn>%2$s</mn></mfrac></math>", one, two),
				toMathML(oneString, oneString, Integer.toString(two)));
		//
	}

	private static String toMathML(final String whole, final String numerator, final String denominator)
			throws Throwable {
		try {
			final Object obj = invoke(METHOD_TO_MATH_ML, null, whole, numerator, denominator);
			if (obj instanceof String) {
				return cast(String.class, obj);
			} else if (obj == null) {
				return null;
			}
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

}