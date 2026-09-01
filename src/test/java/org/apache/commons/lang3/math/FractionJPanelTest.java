package org.apache.commons.lang3.math;

import java.awt.Color;
import java.awt.FocusTraversalPolicy;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import javax.imageio.spi.ImageReaderWriterSpi;
import javax.swing.AbstractButton;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
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
import org.apache.commons.lang3.function.FailableBiFunction;
import org.apache.commons.lang3.function.FailableFunction;
import org.apache.commons.lang3.function.TriFunction;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.meeuw.functional.ThrowingTriConsumer;
import org.meeuw.functional.TriPredicate;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Predicates;
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
			METHOD_TO_MATH_ML, METHOD_TO_PATH, METHOD_GET_PARAMETER_COUNT, METHOD_MATCHES, METHOD_IS_STATIC,
			METHOD_CONTAINS_KEY, METHOD_GET_PROPERTY, METHOD_AND2, METHOD_AND3, METHOD_AND4, METHOD_TO_HTML,
			METHOD_GET_PARAMETER_TYPES, METHOD_ADD_ALL, METHOD_HAS_NEXT, METHOD_EXISTS, METHOD_IS_FILE, METHOD_CAN_READ,
			METHOD_EQUALS_IGNORE_CASE, METHOD_CHOP_IMAGE, METHOD_TEST_AND_ACCEPT = null;

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
		(METHOD_TO_PATH = clz.getDeclaredMethod("toPath", File.class)).setAccessible(true);
		//
		(METHOD_GET_PARAMETER_COUNT = clz.getDeclaredMethod("getParameterCount", Executable.class)).setAccessible(true);
		//
		(METHOD_MATCHES = clz.getDeclaredMethod("matches", String.class, String.class)).setAccessible(true);
		//
		(METHOD_IS_STATIC = clz.getDeclaredMethod("isStatic", Member.class)).setAccessible(true);
		//
		(METHOD_CONTAINS_KEY = clz.getDeclaredMethod("containsKey", Properties.class, String.class))
				.setAccessible(true);
		//
		(METHOD_GET_PROPERTY = clz.getDeclaredMethod("getProperty", Properties.class, String.class))
				.setAccessible(true);
		//
		(METHOD_AND2 = clz.getDeclaredMethod("and", Boolean.TYPE, BooleanSupplier.class)).setAccessible(true);
		//
		(METHOD_AND3 = clz.getDeclaredMethod("and", Boolean.TYPE, Boolean.TYPE, boolean[].class)).setAccessible(true);
		//
		(METHOD_AND4 = clz.getDeclaredMethod("and", Object.class, Predicate.class, Predicate.class, Predicate.class))
				.setAccessible(true);
		//
		(METHOD_TO_HTML = clz.getDeclaredMethod("toHtml")).setAccessible(true);
		//
		(METHOD_GET_PARAMETER_TYPES = clz.getDeclaredMethod("getParameterTypes", Executable.class)).setAccessible(true);
		//
		(METHOD_ADD_ALL = clz.getDeclaredMethod("addAll", Collection.class, Collection.class)).setAccessible(true);
		//
		(METHOD_HAS_NEXT = clz.getDeclaredMethod("hasNext", Iterator.class)).setAccessible(true);
		//
		(METHOD_EXISTS = clz.getDeclaredMethod("exists", File.class)).setAccessible(true);
		//
		(METHOD_IS_FILE = clz.getDeclaredMethod("isFile", File.class)).setAccessible(true);
		//
		(METHOD_CAN_READ = clz.getDeclaredMethod("canRead", File.class)).setAccessible(true);
		//
		(METHOD_EQUALS_IGNORE_CASE = clz.getDeclaredMethod("equalsIgnoreCase", String.class, String.class))
				.setAccessible(true);
		//
		(METHOD_CHOP_IMAGE = clz.getDeclaredMethod("chopImage", BufferedImage.class)).setAccessible(true);
		//
		(METHOD_TEST_AND_ACCEPT = clz.getDeclaredMethod("testAndAccept", TriPredicate.class, Object.class, Object.class,
				Object.class, ThrowingTriConsumer.class)).setAccessible(true);
		//
	}

	private static class IH implements InvocationHandler {

		private Boolean test, addAll, booleanValue, hasNext = null;

		private Integer size, length, modifiers = null;

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
			if (Boolean.logicalAnd(Objects.equals(name, "toString"), getParameterCount(method) == 0)) {
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
			} else if (Boolean.logicalAnd(proxy instanceof Member, Objects.equals(name, "getModifiers"))) {
				//
				return modifiers;
				//
			} // if
				//
			if (proxy instanceof Collection) {
				//
				if (contains(Arrays.asList("toArray", "stream"), name)) {
					//
					return null;
					//
				} else if (Objects.equals(name, "addAll")) {
					//
					return addAll;
					//
				} // if
					//
			} // if
				//
			if (Boolean.logicalAnd(or(proxy instanceof Predicate, proxy instanceof BiPredicate,
					proxy instanceof IntPredicate, proxy instanceof TriPredicate), Objects.equals(name, "test"))) {
				//
				return test;
				//
			} else if (Boolean.logicalAnd(
					or(proxy instanceof FailableFunction, proxy instanceof FailableBiFunction,
							proxy instanceof TriFunction, proxy instanceof IntFunction),
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
			} else if (Boolean.logicalAnd(proxy instanceof Stream,
					contains(Arrays.asList("map", "toList", "collect"), name))) {
				//
				return null;
				//
			} else if (Boolean.logicalAnd(proxy instanceof Member,
					contains(Arrays.asList("getName", "getDeclaringClass"), name))) {
				//
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
			} else if (Boolean.logicalAnd(proxy instanceof Map,
					contains(Arrays.asList("get", "entrySet", "put"), name))) {
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
			} else if (Boolean.logicalAnd(proxy instanceof Page, contains(Arrays.asList("locator", "pdf"), name))) {
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
			} else if (Boolean.logicalAnd(proxy instanceof BooleanSupplier, Objects.equals(name, "getAsBoolean"))) {
				//
				return booleanValue;
				//
			} else if (Boolean.logicalAnd(proxy instanceof IntStream, Objects.equals(name, "max"))) {
				//
				return null;
				//
			} else if (Boolean.logicalAnd(proxy instanceof LongStream, Objects.equals(name, "mapToInt"))) {
				//
				return null;
				//
			} else if (Boolean.logicalAnd(proxy instanceof IntStream, Objects.equals(name, "mapToObj"))) {
				//
				return null;
				//
			} else if (Boolean.logicalAnd(proxy instanceof ListCellRenderer,
					Objects.equals(name, "getListCellRendererComponent"))) {
				//
				return null;
				//
			} else if (proxy instanceof Iterator) {
				//
				if (Objects.equals(name, "hasNext")) {
					//
					return hasNext;
					//
				} else if (Objects.equals(name, "next")) {
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

	private static Class<?>[] getParameterTypes(final Executable instance) throws Throwable {
		try {
			final Object obj = invoke(METHOD_GET_PARAMETER_TYPES, null, instance);
			if (obj instanceof Class<?>[]) {
				return cast(Class[].class, obj);
			} else if (obj == null) {
				return null;
			}
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static int getParameterCount(final Executable instance) throws Throwable {
		try {
			final Object obj = invoke(METHOD_GET_PARAMETER_COUNT, null, instance);
			if (obj instanceof Integer integer && integer != null) {
				return integer.intValue();
			}
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static String getName(final Member instance) {
		return instance != null ? instance.getName() : null;
	}

	private IH ih = null;

	private FractionJPanel instance = null;

	private Properties properties = null;

	@BeforeMethod
	void beforeMethod() throws Throwable {
		//
		ih = new IH();
		//
		instance = cast(FractionJPanel.class, Narcissus.allocateInstance(FractionJPanel.class));
		//
		properties = new Properties();
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
					|| (parameterTypes = getParameterTypes(m)) == null
					|| Boolean.logicalAnd(Objects.equals(name = getName(m), "and"), Arrays.equals(parameterTypes,
							new Class<?>[] { Boolean.TYPE, Boolean.TYPE, boolean[].class }))) {
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
			if (Modifier.isStatic(m.getModifiers())) {
				//
				if (Boolean.logicalAnd(Objects.equals(name, "toFraction"),
						Boolean.logicalOr(
								Arrays.equals(parameterTypes,
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
								Arrays.equals(parameterTypes, new Class<?>[] { ActionEvent.class })),
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
					Boolean.logicalAnd(
							Objects.equals(name, "createFocusTraversalPolicy"), Arrays.equals(parameterTypes,
									new Object[] { List.class })),
					Boolean.logicalAnd(Objects.equals(name, "toMathML"),
							Boolean.logicalOr(
									Arrays.equals(parameterTypes,
											new Object[] { String.class, String.class, String.class }),
									Arrays.equals(parameterTypes, new Object[] { clz }))),
					Boolean.logicalAnd(Objects.equals(name, "createDocumentFilter"),
							Arrays.equals(parameterTypes, new Object[] { Boolean.TYPE })),
					Boolean.logicalAnd(Objects.equals(name, "toHtml"), getParameterCount(m) == 0),
					Boolean.logicalAnd(Objects.equals(name, "createDocumentFilter"),
							Arrays.equals(parameterTypes, new Object[] { Integer.TYPE })))) {
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
					|| (parameterTypes = getParameterTypes(m)) == null) {
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
				} else if (Objects.equals(parameterType, Executable.class)) {
					//
					add(collection, Narcissus.allocateInstance(Method.class));
					//
				} else if (Objects.equals(parameterType, ImageReaderWriterSpi.class)) {
					//
					add(collection,
							Narcissus.allocateInstance(Class.forName("com.sun.imageio.plugins.bmp.BMPImageWriterSpi")));
					//
				} else if (Objects.equals(parameterType, FilterBypass.class)) {
					//
					add(collection, Narcissus
							.allocateInstance(Class.forName("javax.swing.text.AbstractDocument$DefaultFilterBypass")));
					//
				} else if (Objects.equals(parameterType, Toolkit.class)) {
					//
					add(collection, Toolkit.getDefaultToolkit());
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
										Arrays.equals(parameterTypes = getParameterTypes(m),
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
								Arrays.equals(getParameterTypes(m), new Class<?>[] { ActionEvent.class })),
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
							Arrays.equals(parameterTypes, new Object[] { Boolean.TYPE })),
					Boolean.logicalAnd(Objects.equals(name, "getDeclaredMethods"),
							Arrays.equals(parameterTypes, new Object[] { Class.class })),
					Boolean.logicalAnd(Objects.equals(name, "getName"),
							Arrays.equals(parameterTypes, new Object[] { Class.class })),
					Boolean.logicalAnd(Objects.equals(name, "toHtml"), getParameterCount(m) == 0),
					Boolean.logicalAnd(Objects.equals(name, "createDocumentFilter"),
							Arrays.equals(parameterTypes, new Object[] { Integer.TYPE })),
					Boolean.logicalAnd(Objects.equals(name, "chopImage"),
							Arrays.equals(parameterTypes, new Object[] { BufferedImage.class })))) {
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
					Arrays.equals(getParameterTypes(m), new Class<?>[] { Window.class }))) {
				//
				final Method m1 = m;
				//
				Assert.assertThrows(IllegalArgumentException.class, () -> Narcissus.invokeMethod(focusTraversalPolicy1,
						m1, toArray(Collections.nCopies(getParameterCount(m1), null))));
				//
			} else {
				//
				Assert.assertNull(Narcissus.invokeMethod(focusTraversalPolicy1, m,
						toArray(Collections.nCopies(getParameterCount(m), null))));
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
					Arrays.equals(getParameterTypes(m), new Class<?>[] { Window.class }))) {
				//
				final Method m1 = m;
				//
				Assert.assertThrows(IllegalArgumentException.class, () -> Narcissus.invokeMethod(focusTraversalPolicy2,
						m1, toArray(Collections.nCopies(getParameterCount(m1), null))));
				//
			} else {
				//
				Assert.assertNotNull(Narcissus.invokeMethod(focusTraversalPolicy2, m,
						toArray(Collections.nCopies(getParameterCount(m), null))));
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
					|| (parameterTypes = getParameterTypes(m)) == null) {
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
					|| (parameterTypes = getParameterTypes(m)) == null) {
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
		Assert.assertEquals(String.format("<math><mi>-</mi><mfrac><mi>%1$s</mi><mn>%2$s</mn></mfrac></math>", one, two),
				toMathML(EMPTY, Integer.toString(Math.negateExact(one)), Integer.toString(two)));
		//
		Assert.assertEquals(String.format("<math><mi>%1$s</mi></math>", one),
				toMathML(String.join(".", Integer.toString(one), "0"), null, null));
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

	@Test
	public void testActionPerformed() throws Exception {
		//
		if (instance == null) {
			//
			return;
			//
		} // if
			//
			// btnClear
			//
		final AbstractButton btnClear = new JButton();
		//
		FieldUtils.writeDeclaredField(instance, "btnClear", btnClear, true);
		//
		instance.actionPerformed(new ActionEvent(btnClear, 0, null));
		//
		// btnShowImage
		//
		final AbstractButton btnShowImage = new JButton();
		//
		FieldUtils.writeDeclaredField(instance, "btnShowImage", btnShowImage, true);
		//
		instance.actionPerformed(new ActionEvent(btnShowImage, 0, null));
		//
		// btnCopyImage
		//
		final AbstractButton btnCopyImage = new JButton();
		//
		FieldUtils.writeDeclaredField(instance, "btnCopyImage", btnCopyImage, true);
		//
		instance.actionPerformed(new ActionEvent(btnCopyImage, 0, null));
		//
		// btnSaveImage
		//
		final AbstractButton btnSaveImage = new JButton();
		//
		FieldUtils.writeDeclaredField(instance, "btnSaveImage", btnSaveImage, true);
		//
		instance.actionPerformed(new ActionEvent(btnSaveImage, 0, null));
		//
		// btnSavePdf
		//
		final AbstractButton btnSavePdf = new JButton();
		//
		FieldUtils.writeDeclaredField(instance, "btnSavePdf", btnSavePdf, true);
		//
		instance.actionPerformed(new ActionEvent(btnSavePdf, 0, null));
		//
	}

	@Test
	public void testToPath() throws Throwable {
		//
		Assert.assertNotNull(toPath(new File(".")));
		//
	}

	private static Path toPath(final File instance) throws Throwable {
		try {
			final Object obj = invoke(METHOD_TO_PATH, null, instance);
			if (obj instanceof Path) {
				return cast(Path.class, obj);
			} else if (obj == null) {
				return null;
			}
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	public void testMatches() throws Throwable {
		//
		Assert.assertFalse(matches(EMPTY, "\\d+"));
		//
	}

	private static boolean matches(final String instance, final String regex) throws Throwable {
		try {
			final Object obj = invoke(METHOD_MATCHES, null, instance, regex);
			if (obj instanceof Boolean b && b != null) {
				return b.booleanValue();
			}
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	public void testIsStatic() throws Throwable {
		//
		Assert.assertTrue(isStatic(Objects.class.getDeclaredMethod("toString", Object.class)));
		//
	}

	private static boolean isStatic(final Member instance) throws Throwable {
		try {
			final Object obj = invoke(METHOD_IS_STATIC, null, instance);
			if (obj instanceof Boolean b && b != null) {
				return b.booleanValue();
			}
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	public void testContainsKey() throws Throwable {
		//
		Assert.assertFalse(containsKey(properties, null));
		//
		Assert.assertFalse(containsKey(properties, EMPTY));
		//
	}

	private static boolean containsKey(final Properties instance, final String key) throws Throwable {
		try {
			final Object obj = invoke(METHOD_CONTAINS_KEY, null, instance, key);
			if (obj instanceof Boolean booleanValue && booleanValue != null) {
				return booleanValue.booleanValue();
			}
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	public void testGetProperty() throws Throwable {
		//
		Assert.assertNull(getProperty(properties, null));
		//
		Assert.assertNull(getProperty(properties, EMPTY));
		//
	}

	private static String getProperty(final Properties instance, final String key) throws Throwable {
		try {
			final Object obj = invoke(METHOD_GET_PROPERTY, null, instance, key);
			if (obj == null) {
				return null;
			} else if (obj instanceof String string) {
				return string;
			}
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	public void testAnd() throws Throwable {
		//
		Assert.assertFalse(and(true, () -> false));
		//
		Assert.assertTrue(and(true, true, null));
		//
		Assert.assertFalse(and(true, true, false));
		//
		final Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
		//
		Assert.assertFalse(and(null, alwaysTrue, null, null));
		//
		Assert.assertFalse(and(null, alwaysTrue, alwaysTrue, null));
		//
		Assert.assertTrue(and(null, alwaysTrue, alwaysTrue, alwaysTrue));
		//
	}

	private static boolean and(final boolean condition, final BooleanSupplier booleanSupplier) throws Throwable {
		try {
			final Object obj = invoke(METHOD_AND2, null, condition, booleanSupplier);
			if (obj instanceof Boolean booleanValue && booleanValue != null) {
				return booleanValue.booleanValue();
			}
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static boolean and(final boolean a, final boolean b, final boolean... bs) throws Throwable {
		try {
			final Object obj = invoke(METHOD_AND3, null, a, b, bs);
			if (obj instanceof Boolean booleanValue && booleanValue != null) {
				return booleanValue.booleanValue();
			}
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static <T> boolean and(final T value, final Predicate<T> predicateA, final Predicate<T> predicateB,
			final Predicate<T> predicateC) throws Throwable {
		try {
			final Object obj = invoke(METHOD_AND4, null, value, predicateA, predicateB, predicateC);
			if (obj instanceof Boolean booleanValue && booleanValue != null) {
				return booleanValue.booleanValue();
			}
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	public void testToHtml() throws Throwable {
		//
		// tfFontSize
		//
		final String fontSize = "1";
		//
		FieldUtils.writeDeclaredField(instance, "tfFontSize", new JTextField(fontSize), true);
		//
		Assert.assertEquals(toHtml(), String.format(
				"<html><body><table style=\"font-size:%1$s\"><tr><td><math></math>null<math></math>=<math></math></td></tr></tbody></table></body></html>",
				fontSize));
		//
		// cbmColor
		//
		final String color = "RED";
		//
		FieldUtils.writeDeclaredField(instance, "cbmColor",
				new DefaultComboBoxModel<>(new Object[] { Map.entry(Color.class.getDeclaredField(color), "") }), true);
		//
		Assert.assertEquals(toHtml(), String.format(
				"<html><body><table style=\"font-size:%1$s;color:%2$s\"><tr><td><math></math>null<math></math>=<math></math></td></tr></tbody></table></body></html>",
				fontSize, color));
		//
	}

	private String toHtml() throws Throwable {
		try {
			final Object obj = invoke(METHOD_TO_HTML, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof String string) {
				return string;
			}
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	public void testAddAll() throws Throwable {
		//
		addAll(new ArrayList<>(), null);
		//
	}

	private static <E> void addAll(final Collection<E> a, final Collection<? extends E> b) throws Throwable {
		try {
			invoke(METHOD_ADD_ALL, null, a, b);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	public void testHasNext() throws Throwable {
		//
		if ((ih = ObjectUtils.getIfNull(ih, IH::new)) != null) {
			//
			ih.hasNext = Boolean.FALSE;
			//
		} // if
			//
		Assert.assertFalse(hasNext(Reflection.newProxy(Iterator.class, ih)));
		//
	}

	private static boolean hasNext(final Iterator<?> instance) throws Throwable {
		try {
			final Object obj = invoke(METHOD_HAS_NEXT, null, instance);
			if (obj instanceof Boolean booleanValue && booleanValue != null) {
				return booleanValue.booleanValue();
			}
			throw new Throwable(Objects.toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	public void testExists() throws IllegalAccessException, InvocationTargetException {
		//
		Assert.assertEquals(invoke(METHOD_EXISTS, null, new File("pom.xml")), Boolean.TRUE);
		//
	}

	@Test
	public void testIsFile() throws IllegalAccessException, InvocationTargetException {
		//
		Assert.assertEquals(invoke(METHOD_IS_FILE, null, new File("pom.xml")), Boolean.TRUE);
		//
		Assert.assertEquals(invoke(METHOD_IS_FILE, null, new File(".")), Boolean.FALSE);
		//
	}

	@Test
	public void testCanRead() throws IllegalAccessException, InvocationTargetException {
		//
		Assert.assertEquals(invoke(METHOD_CAN_READ, null, new File("pom.xml")), Boolean.TRUE);
		//
	}

	@Test
	public void testEqualsIgnoreCase() throws IllegalAccessException, InvocationTargetException {
		//
		Assert.assertEquals(invoke(METHOD_EQUALS_IGNORE_CASE, null, EMPTY, Narcissus.allocateInstance(String.class)),
				Boolean.FALSE);
		//
		Assert.assertEquals(invoke(METHOD_EQUALS_IGNORE_CASE, null, EMPTY, EMPTY), Boolean.TRUE);
		//
		Assert.assertEquals(invoke(METHOD_EQUALS_IGNORE_CASE, null, EMPTY, "A"), Boolean.FALSE);
		//
	}

	@Test
	public void testChopImage() throws IllegalAccessException, InvocationTargetException {
		//
		final BufferedImage bufferedImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		//
		bufferedImage.setRGB(1, 1, 1);
		//
		Assert.assertNotNull(invoke(METHOD_CHOP_IMAGE, null, bufferedImage));
		//
	}

	@Test
	public void testTestAndAccept() throws IllegalAccessException, InvocationTargetException {
		//
		if ((ih = ObjectUtils.getIfNull(ih, IH::new)) != null) {
			//
			ih.test = Boolean.FALSE;
			//
		} // if
			//
		final TriPredicate<?, ?, ?> triPredicate = Reflection.newProxy(TriPredicate.class, ih);
		//
		Assert.assertNull(invoke(METHOD_TEST_AND_ACCEPT, null, triPredicate, null, null, null, null));
		//
		if (ih != null) {
			//
			ih.test = Boolean.TRUE;
			//
		} // if
			//
		Assert.assertNull(invoke(METHOD_TEST_AND_ACCEPT, null, triPredicate, null, null, null, null));
		//
		final ThrowingTriConsumer<?, ?, ?, ?> throwingTriConsumer = Reflection.newProxy(ThrowingTriConsumer.class, ih);
		//
		Assert.assertNull(invoke(METHOD_TEST_AND_ACCEPT, null, triPredicate, null, null, null, throwingTriConsumer));
		//
	}

	@Test
	public void testCreateDocumentFilter() throws Throwable {
		//
		final DocumentFilter documentFilter = cast(DocumentFilter.class, Narcissus
				.invokeStaticMethod(FractionJPanel.class.getDeclaredMethod("createDocumentFilter", Integer.TYPE), 0));
		//
		if (documentFilter != null) {
			//
			documentFilter.insertString(null, 0, EMPTY, null);
			//
			documentFilter.replace(null, 0, 0, EMPTY, null);
			//
		} // if
			//
	}

	@Test
	public void testIH() throws Throwable {
		//
		final InvocationHandler invocationHandler = cast(InvocationHandler.class,
				Narcissus.allocateInstance(Class.forName("org.apache.commons.lang3.math.FractionJPanel$IH")));
		//
		if (invocationHandler != null) {
			//
			Assert.assertThrows(Throwable.class, () -> invocationHandler.invoke(null, null, null));
			//
			final Transferable transferable = Reflection.newProxy(Transferable.class, invocationHandler);
			//
			Assert.assertNotNull(invocationHandler.invoke(transferable,
					Transferable.class.getDeclaredMethod("getTransferDataFlavors"), null));
			//
			Assert.assertNull(invocationHandler.invoke(transferable,
					Transferable.class.getDeclaredMethod("getTransferData", DataFlavor.class), null));
			//
			Assert.assertThrows(Throwable.class, () -> invocationHandler.invoke(transferable, null, null));
			//
		} // if
			//
	}

}