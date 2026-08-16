package org.apache.commons.lang3.math;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.swing.AbstractButton;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComboBox.KeySelectionManager;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import io.github.toolfactory.narcissus.Narcissus;
import net.miginfocom.swing.MigLayout;

public class FractionJPanel extends JPanel implements ActionListener, KeySelectionManager, DocumentListener {

	private static final long serialVersionUID = 1238012263601647765L;

	private static final String WMIN = "wmin";

	private static final BidiMap<Character, String> BIDI_MAP = new TreeBidiMap<>(
			Map.of(Character.valueOf('+'), "add", Character.valueOf('-'), "subtract", Character.valueOf('*'),
					"multiplyBy", Character.valueOf('/'), "divideBy"));

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Note {
		String value();
	}

	private static class FractionJTextComponent {

		@Note("Whole")
		private JTextComponent whole = null;

		@Note("Numerator")
		private JTextComponent numerator = null;

		private JTextComponent denominator = null;

		private JTextComponent getWhole() {
			if (whole == null) {
				whole = new JTextField();
			}
			return whole;
		}

		private JTextComponent getNumerator() {
			if (numerator == null) {
				numerator = new JTextField();
			}
			return numerator;
		}

		private JTextComponent getDenominator() {
			if (denominator == null) {
				denominator = new JTextField();
			}
			return denominator;
		}

		private static JTextComponent getWhole(final FractionJTextComponent instance) {
			return instance != null ? instance.getWhole() : null;
		}

		private static JTextComponent getNumerator(final FractionJTextComponent instance) {
			return instance != null ? instance.getNumerator() : null;
		}

		private static JTextComponent getDenominator(final FractionJTextComponent instance) {
			return instance != null ? instance.getDenominator() : null;
		}

	}

	@Note("Fraction 1")
	private transient FractionJTextComponent fraction1 = null;

	@Note("Fraction 2")
	private transient FractionJTextComponent fraction2 = null;

	private transient FractionJTextComponent answer = null;

	private transient ComboBoxModel<Method> cbm = null;

	@Note("Execute")
	private AbstractButton btnExecute = null;

	@Note("Clear")
	private AbstractButton btnClear = null;

	private AbstractButton btnShowImage = null;

	@Note("Fraction 1 Whole Document")
	private transient Document documentWhole1 = null;

	private transient Document documentWhole2 = null;

	private JLabel labelImage = null;

	private FractionJPanel() {
		//
	}

	private void init() {
		//
		setLayout(new MigLayout());
		//
		JPanel jPanel = new JPanel();
		//
		jPanel.setLayout(new MigLayout());
		//
		final int wmin = 50;
		//
		jPanel.add((fraction1 = new FractionJTextComponent()).getWhole(), String.format("wmin %1$s,spany 2", wmin));
		//
		final String wrap = "wrap";
		//
		jPanel.add(fraction1.getNumerator(), String.format("wmin %1$s,%2$s", wmin, wrap));
		//
		jPanel.add(fraction1.getDenominator(), StringUtils.joinWith(" ", WMIN, wmin));
		//
		try {
			//
			if (Narcissus.getObjectField(this, Container.class.getDeclaredField("component")) == null) {
				//
				return;
				//
			} // if
				//
		} catch (final NoSuchFieldException e) {
			//
			throw new RuntimeException(e);
			//
		} // try
			//
		add(jPanel);
		//
		final JComboBox<Method> jcb = new JComboBox<>(
				cbm = new DefaultComboBoxModel<>(toArray(toList(filter(Arrays.stream(Fraction.class.getMethods()),
						m -> m != null && Arrays.equals(m.getParameterTypes(), new Class<?>[] { m.getDeclaringClass() })
								&& Objects.equals(m.getReturnType(), m.getDeclaringClass()))),
						Method[]::new)));
		//
		final ListCellRenderer lcr = jcb.getRenderer();
		//
		jcb.setRenderer((arg0, value, arg2, arg3, arg4) -> {
			//
			final Method method = cast(Method.class, value);
			//
			if (method != null) {
				//
				return new JLabel(method.getName());
				//
			} else if (value == null) {
				//
				return new JLabel();
				//
			} // if
				//
			return lcr != null ? lcr.getListCellRendererComponent(arg0, arg0, arg2, arg3, arg4) : null;
			//
		});
		//
		jcb.setSelectedItem(null);
		//
		jcb.setKeySelectionManager(this);
		//
		add(jcb);
		//
		(jPanel = new JPanel()).setLayout(new MigLayout());
		//
		jPanel.add((fraction2 = new FractionJTextComponent()).getWhole(), String.format("wmin %1$s,spany 2", wmin));
		//
		jPanel.add(fraction2.getNumerator(), String.format("wmin %1$s,%2$s", wmin, wrap));
		//
		jPanel.add(fraction2.getDenominator(), StringUtils.joinWith(" ", WMIN, wmin));
		//
		add(jPanel);
		//
		add(btnExecute = new JButton("="));
		//
		(jPanel = new JPanel()).setLayout(new MigLayout());
		//
		jPanel.add((answer = new FractionJTextComponent()).getWhole(), String.format("wmin %1$s,spany 2", wmin));
		//
		jPanel.add(answer.getNumerator(), String.format("wmin %1$s,%2$s", wmin, wrap));
		//
		jPanel.add(answer.getDenominator(), StringUtils.joinWith(" ", WMIN, wmin));
		//
		add(jPanel, wrap);
		//
		(jPanel = new JPanel()).setLayout(new MigLayout());
		//
		jPanel.add(btnClear = new JButton("Clear"));
		//
		jPanel.add(btnShowImage = new JButton("Show Image"));
		//
		jPanel.add(labelImage = new JLabel());
		//
		add(jPanel, String.format("span %1$s", 3));
		//
		forEach(map(
				testAndApply(Objects::nonNull, FractionJTextComponent.class.getDeclaredFields(), Arrays::stream, null),
				f -> cast(JTextComponent.class, Narcissus.getField(answer, f))), x -> setEditable(x, false));
		//
		setFocusTraversalPolicyProvider(true);
		//
		setFocusTraversalPolicy(createFocusTraversalPolicy(Arrays.asList(FractionJTextComponent.getWhole(fraction1),
				FractionJTextComponent.getNumerator(fraction1), FractionJTextComponent.getDenominator(fraction1), jcb,
				FractionJTextComponent.getWhole(fraction2), FractionJTextComponent.getNumerator(fraction2),
				FractionJTextComponent.getDenominator(fraction2), btnExecute)));
		//
		forEach(Arrays.asList(documentWhole1 = getDocument(FractionJTextComponent.getWhole(fraction1)),
				getDocument(FractionJTextComponent.getNumerator(fraction1)),
				getDocument(FractionJTextComponent.getDenominator(fraction1)),
				documentWhole2 = getDocument(FractionJTextComponent.getWhole(fraction2)),
				getDocument(FractionJTextComponent.getNumerator(fraction2)),
				getDocument(FractionJTextComponent.getDenominator(fraction2))), x -> addDocumentListener(x, this));
		//
		forEach(map(Arrays.stream(FractionJPanel.class.getDeclaredFields()),
				f -> cast(AbstractButton.class,
						f != null && Modifier.isStatic(f.getModifiers()) ? Narcissus.getStaticField(f)
								: Narcissus.getField(this, f))),
				x -> addActionListener(x, this));
		//
		clear();
		//
	}

	private void clear() {
		//
		forEach(Arrays.asList(FractionJTextComponent.getWhole(fraction1),
				FractionJTextComponent.getNumerator(fraction1), FractionJTextComponent.getDenominator(fraction1),
				FractionJTextComponent.getWhole(fraction2), FractionJTextComponent.getNumerator(fraction2),
				FractionJTextComponent.getDenominator(fraction2), FractionJTextComponent.getWhole(answer),
				FractionJTextComponent.getNumerator(answer), FractionJTextComponent.getDenominator(answer)),
				x -> setText(x, ""));
		//
		setSelectedItem(cbm, null);
		//
		setIcon(labelImage, null);
		//
	}

	private static void setIcon(final JLabel instance, final Icon icon) {
		//
		if (instance == null) {
			//
			return;
			//
		} // if
			//
		try {
			//
			if (Narcissus.getField(instance, Narcissus.findField(getClass(instance), "objectLock")) == null) {
				//
				return;
				//
			} // if
				//
		} catch (final NoSuchFieldException e) {
			//
			throw new RuntimeException(e);
			//
		} // try
			//
		instance.setIcon(icon);
		//
	}

	private static void setSelectedItem(final ComboBoxModel<?> instance, final Object selectedItem) {
		if (instance != null) {
			instance.setSelectedItem(selectedItem);
		}
	}

	private static void addActionListener(final AbstractButton instance, final ActionListener actionListener) {
		//
		if (instance == null) {
			//
			return;
			//
		} // if
			//
		try {
			//
			if (Narcissus.getField(instance, Narcissus.findField(getClass(instance), "listenerList")) == null) {
				//
				return;
				//
			} // if
				//
		} catch (final NoSuchFieldException e) {
			//
			throw new RuntimeException(e);
			//
		} // try
			//
		instance.addActionListener(actionListener);
		//
	}

	private static void addDocumentListener(final Document instance, final DocumentListener documentListener) {
		if (instance != null) {
			instance.addDocumentListener(documentListener);
		}
	}

	private static Document getDocument(final JTextComponent instance) {
		return instance != null ? instance.getDocument() : null;
	}

	private static <T> T[] toArray(final Collection<T> instance, final IntFunction<T[]> generator) {
		return instance != null ? instance.toArray(generator) : null;
	}

	private static <T> Stream<T> filter(final Stream<T> instance, final Predicate<? super T> predicate) {
		return instance != null ? instance.filter(predicate) : instance;
	}

	private static <T> List<T> toList(final Stream<T> instance) {
		return instance != null ? instance.toList() : null;
	}

	private static FocusTraversalPolicy createFocusTraversalPolicy(final List<Component> components) {
		//
		return new FocusTraversalPolicy() {

			@Override
			public Component getLastComponent(final Container aContainer) {
				//
				return testAndApply(CollectionUtils::isNotEmpty, components,
						x -> IterableUtils.get(x, IterableUtils.size(x) - 1), null);
				//
			}

			@Override
			public Component getFirstComponent(final Container aContainer) {
				//
				return testAndApply(CollectionUtils::isNotEmpty, components, x -> IterableUtils.get(x, 0), null);
				//
			}

			@Override
			public Component getDefaultComponent(final Container aContainer) {
				//
				return getFirstComponent(aContainer);
				//
			}

			@Override
			public Component getComponentBefore(final Container aContainer, final Component aComponent) {
				//
				if (IterableUtils.isEmpty(components)) {
					//
					return null;
					//
				} // if
					//
				final int index = components != null ? components.indexOf(aComponent) : null;
				//
				return testAndApply(x -> index > 0, components, x -> IterableUtils.get(components, index - 1),
						x -> getLastComponent(aContainer));
				//
			}

			@Override
			public Component getComponentAfter(final Container aContainer, final Component aComponent) {
				//
				if (IterableUtils.isEmpty(components)) {
					//
					return null;
					//
				} // if
					//
				final int index = components != null ? components.indexOf(aComponent) : null;
				//
				return testAndApply(x -> IterableUtils.size(x) - 1 > index, components,
						x -> IterableUtils.get(components, index + 1), x -> getFirstComponent(aContainer));
				//
			}

		};
		//
	}

	private static <T> void forEach(final Stream<T> instance, final Consumer<? super T> action) {
		if (instance != null) {
			instance.forEach(action);
		}
	}

	private static void setEditable(final JTextComponent instance, final boolean editable) {
		//
		if (instance == null) {
			//
			return;
			//
		} // if
			//
		try {
			//
			if (Narcissus.getField(instance, Narcissus.findField(getClass(instance), "objectLock")) == null) {
				//
				return;
				//
			} // if
				//
		} catch (final NoSuchFieldException e) {
			//
			throw new RuntimeException(e);
			//
		} // try
			//
		instance.setEditable(editable);
		//
	}

	private static <T, R> Stream<R> map(final Stream<T> instance, final Function<? super T, ? extends R> mapper) {
		return instance != null ? instance.map(mapper) : null;
	}

	private static <T> T cast(final Class<T> clz, final Object instance) {
		return clz != null && clz.isInstance(instance) ? clz.cast(instance) : null;
	}

	public static void main(final String[] args) {
		//
		final JFrame jFrame = !GraphicsEnvironment.isHeadless() ? new JFrame() : null;
		//
		if (jFrame != null) {
			//
			jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			//
			final FractionJPanel instance = new FractionJPanel();
			//
			instance.init();
			//
			jFrame.add(instance);
			//
			jFrame.pack();
			//
			if (!isTestMode()) {
				//
				jFrame.setVisible(true);
				//
			} // if
				//
		} // if
			//
	}

	private static boolean isTestMode() {
		try {
			return Class.forName("org.testng.annotations.Test") != null;
		} catch (final ClassNotFoundException e) {
			return false;
		}
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		//
		final Object source = getSource(evt);
		//
		if (Objects.equals(source, btnExecute)) {
			//
			final Fraction fractionA = toFraction(fraction1);
			//
			final Fraction fractionB = toFraction(fraction2);
			//
			try {
				//
				final Fraction fraction = cast(Fraction.class,
						Boolean.logicalAnd(fractionA != null, fractionB != null)
								? invoke(cast(Method.class, getSelectedItem(cbm)), fractionA, fractionB)
								: null);
				//
				if (fraction != null) {
					//
					setText(FractionJTextComponent.getWhole(answer), Integer.toString(fraction.getProperWhole()));
					//
					final StringBuilder properNumerator = new StringBuilder(
							Integer.toString(fraction.getProperNumerator()));
					//
					if (Boolean.logicalAnd(fraction.floatValue() < 0, fraction.getProperWhole() == 0)) {
						//
						properNumerator.insert(0, '-');
						//
					} // if
						//
					setText(FractionJTextComponent.getNumerator(answer), Objects.toString(properNumerator));
					//
					setText(FractionJTextComponent.getDenominator(answer), Integer.toString(fraction.getDenominator()));
					//
				} // if
					//
			} catch (final IllegalAccessException e) {
				//
				throw new RuntimeException(e);
				//
			} catch (final InvocationTargetException e) {
				//
				throw new RuntimeException(ObjectUtils.getIfNull(e.getTargetException(), e));
				//
			} // try
				//
		} else if (Objects.equals(source, btnClear)) {
			//
			clear();
			//
		} else if (Objects.equals(source, btnShowImage)) {
			//
			final StringBuilder sb = new StringBuilder("<html><body><table><tbody><tr><td>");
			//
			sb.append(toMathML(fraction1));
			//
			final Object object = testAndApply(Objects::nonNull, getName(cast(Member.class, getSelectedItem(cbm))),
					x -> get(inverseBidiMap(BIDI_MAP), x), null);
			//
			sb.append(object);
			//
			sb.append(toMathML(fraction2));
			//
			sb.append('=');
			//
			sb.append(toMathML(answer));
			//
			sb.append("</td></tr></tbody></table></body></html>");
			//
			try (final Playwright playwright = Playwright.create();
					final Browser browser = playwright != null
							? playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true))
							: null;
					final Page page = newPage(browser)) {
				//
				setContent(page, Objects.toString(sb));
				//
				setIcon(labelImage, new ImageIcon(screenshot(locator(page, "tbody"))));
				//
			} // try
				//
		} // if
			//
	}

	private static Locator locator(final Page instance, final String selector) {
		return instance != null ? instance.locator(selector) : null;
	}

	private static void setContent(final Page instance, final String content) {
		if (instance != null) {
			instance.setContent(content);
		}
	}

	private static byte[] screenshot(final Locator instance) {
		return instance != null ? instance.screenshot() : null;
	}

	private static Page newPage(final Browser instance) {
		return instance != null ? instance.newPage() : null;
	}

	private static <K, V> BidiMap<V, K> inverseBidiMap(final BidiMap<K, V> instance) {
		return instance != null ? instance.inverseBidiMap() : null;
	}

	private static String toMathML(final FractionJTextComponent instance) {
		//
		return toMathML(getText(FractionJTextComponent.getWhole(instance)),
				getText(FractionJTextComponent.getNumerator(instance)),
				getText(FractionJTextComponent.getDenominator(instance)));
		//
	}

	private static String toMathML(final String whole, final String numerator, final String denominator) {
		//
		final StringBuilder sb = new StringBuilder("<math>");
		//
		if (and(whole, FractionJPanel::isValidString, StringUtils::isNotBlank)) {
			//
			if (!Objects.equals(whole, "0")) {
				//
				sb.append(String.format("<%1$s>%2$s</%1$s>", "mi", whole));
				//
			} else if (startsWith(numerator, "-")) {
				//
				sb.append(String.format("<%1$s>%2$s</%1$s>", "mi", "-"));
				//
			} // if
				//
		} // if
			//
		if (Boolean.logicalOr(isValidString(numerator) && StringUtils.isNotBlank(numerator),
				isValidString(denominator) && StringUtils.isNotBlank(denominator))) {
			//
			sb.append("<mfrac>");
			//
			if (StringUtils.isNotBlank(numerator)) {
				//
				if (Boolean.logicalAnd(Objects.equals(whole, "0"), startsWith(numerator, "-"))) {
					//
					sb.append(String.format("<%1$s>%2$s</%1$s>", "mi", StringUtils.substring(numerator, 1)));
					//
				} else {
					//
					sb.append(String.format("<%1$s>%2$s</%1$s>", "mi", numerator));
					//
				} // if
					//
			} // if
				//
			if (StringUtils.isNotBlank(denominator)) {
				//
				sb.append(String.format("<%1$s>%2$s</%1$s>", "mn", denominator));
				//
			} // if
				//
			sb.append("</mfrac>");
			//
		} // if
			//
		return Objects.toString(sb.append("</math>"));
		//
	}

	private static <T> boolean and(final T value, final Predicate<T> predicateA, final Predicate<T> predicateB) {
		return test(predicateA, value) && test(predicateB, value);
	}

	private static boolean startsWith(final String instance, final String prefix) {
		return instance != null && isValidString(instance) && instance.startsWith(prefix);
	}

	private static Fraction toFraction(final FractionJTextComponent instance) {
		//
		return toFraction(getText(FractionJTextComponent.getWhole(instance)),
				getText(FractionJTextComponent.getNumerator(instance)),
				getText(FractionJTextComponent.getDenominator(instance)));
		//
	}

	private static Fraction toFraction(final String whole, final String numerator, final String denominator) {
		//
		final Predicate<String> predicate = x -> isValidString(x) && NumberUtils.isCreatable(x);
		//
		final BigDecimal bdWhole = testAndApply(predicate, whole, BigDecimal::new, null);
		//
		if (bdWhole != null && contains(toPlainString(bdWhole.stripTrailingZeros()), ".")) {
			//
			if (StringUtils.isNotEmpty(numerator)) {
				//
				throw new IllegalStateException("numerator is not empty");
				//
			} else if (StringUtils.isNotEmpty(denominator)) {
				//
				throw new IllegalStateException("denominator is not empty");
				//
			} // if
				//
			return Fraction.getFraction(bdWhole.doubleValue());
			//
		} // if
			//
		final BigDecimal bdNumerator = testAndApply(predicate, numerator, BigDecimal::new, null);
		//
		if (bdNumerator == null) {
			//
			throw new IllegalStateException("numerator is not a valid number");
			//
		} else if (contains(toPlainString(bdNumerator.stripTrailingZeros()), ".")) {
			//
			throw new IllegalStateException("numerator is not an integer");
			//
		} // if
			//
		final BigDecimal bdDenominator = testAndApply(NumberUtils::isCreatable, denominator, BigDecimal::new, null);
		//
		if (bdDenominator == null) {
			//
			throw new IllegalStateException("denominator is not a valid number");
			//
		} else if (contains(toPlainString(bdDenominator.stripTrailingZeros()), ".")) {
			//
			throw new IllegalStateException("denominator is not an integer");
			//
		} // if
			//
		if (bdWhole != null) {
			//
			return Fraction.getFraction(bdWhole.intValue(), bdNumerator.intValue(), bdDenominator.intValue());
			//
		} // if
			//
		return Fraction.getFraction(bdNumerator.intValue(), bdDenominator.intValue());
		//
	}

	private static boolean isValidString(final String instance) {
		//
		if (instance == null) {
			//
			return false;
			//
		} // if
			//
		try {
			//
			if (Narcissus.getField(instance, Narcissus.findField(getClass(instance), "value")) == null) {
				//
				return false;
				//
			} // if
				//
		} catch (final NoSuchFieldException e) {
			//
			throw new RuntimeException(e);
			//
		} // try
			//
		return true;
		//
	}

	private static boolean contains(final String instance, final CharSequence s) {
		//
		if (!isValidString(instance)) {
			//
			return false;
			//
		} // if
			//
		return instance.contains(s);
		//
	}

	private static String toPlainString(final BigDecimal instance) {
		return instance != null ? instance.toPlainString() : null;
	}

	private static Object invoke(final Method method, final Object instance, final Object... args)
			throws IllegalAccessException, InvocationTargetException {
		return method != null && method.getDeclaringClass() != null ? method.invoke(instance, args) : null;
	}

	private static Object getSelectedItem(final ComboBoxModel<?> instance) {
		return instance != null ? instance.getSelectedItem() : null;
	}

	private static Object getSource(final EventObject instance) {
		return instance != null ? instance.getSource() : null;
	}

	private static void setText(final JTextComponent instance, final String text) {
		//
		if (instance == null) {
			//
			return;
			//
		} // if
			//
		try {
			//
			if (Narcissus.getField(instance, Narcissus.findField(getClass(instance), "model")) == null) {
				//
				return;
				//
			} // if
				//
		} catch (final NoSuchFieldException e) {
			//
			throw new RuntimeException(e);
			//
		} // try
			//
		instance.setText(text);
		//
	}

	private static Class<?> getClass(final Object instance) {
		return instance != null ? instance.getClass() : null;
	}

	private static String getText(final JTextComponent instance) {
		//
		if (instance == null) {
			//
			return null;
			//
		} // if
			//
		try {
			//
			if (Narcissus.getField(instance, Narcissus.findField(getClass(instance), "model")) == null) {
				//
				return null;
				//
			} // if
				//
		} catch (final NoSuchFieldException e) {
			//
			throw new RuntimeException(e);
			//
		} // try
			//
		return instance.getText();
		//
	}

	private static <T, R> R testAndApply(final Predicate<T> predicate, final T value, final Function<T, R> functionTrue,
			final Function<T, R> functionFalse) {
		return test(predicate, value) ? apply(functionTrue, value) : apply(functionFalse, value);
	}

	private static <T> boolean test(final Predicate<T> instance, final T value) {
		return instance != null && instance.test(value);
	}

	private static <T, R> R apply(final Function<T, R> instance, final T value) {
		return instance != null ? instance.apply(value) : null;
	}

	@Override
	public int selectionForKey(final char aKey, final ComboBoxModel<?> aModel) {
		//
		final Iterable<Entry<Integer, Member>> entrySet = entrySet(IntStream.range(0, getSize(aModel))
				.mapToObj(i -> Pair.of(Integer.valueOf(i), cast(Member.class, getElementAt(aModel, i))))
				.collect(LinkedHashMap::new, (a, b) -> a.put(getKey(b), getValue(b)), Map::putAll));
		//
		final Integer integer = testAndApply(x -> IterableUtils.size(x) == 1, toList(map(filter(
				StreamSupport.stream(spliterator(entrySet), false),
				x -> getValue(x) != null && getName(getValue(x)) != null && StringUtils.isNotEmpty(getName(getValue(x)))
						&& getName(getValue(x)).charAt(0) == aKey),
				FractionJPanel::getKey)), x -> IterableUtils.get(x, 0), null);
		//
		if (integer != null) {
			//
			return integer.intValue();
			//
		} // if
			//
		return intValue(
				testAndApply(x -> IterableUtils.size(x) == 1,
						toList(map(
								filter(StreamSupport.stream(spliterator(entrySet), false),
										x -> getValue(x) != null && getName(getValue(x)) != null
												&& Objects.equals(getName(getValue(x)),
														get(BIDI_MAP, Character.valueOf(aKey)))),
								FractionJPanel::getKey)),
						x -> IterableUtils.get(x, 0), null),
				-1);
		//
	}

	private static int intValue(final Number instance, final int defaultValue) {
		return instance != null ? instance.intValue() : defaultValue;
	}

	private static <T> Spliterator<T> spliterator(final Iterable<T> instance) {
		return instance != null ? instance.spliterator() : null;
	}

	private static <K, V> Collection<Entry<K, V>> entrySet(final Map<K, V> instance) {
		return instance != null ? instance.entrySet() : null;
	}

	private static <V> V get(final Map<?, V> instance, final Object key) {
		return instance != null ? instance.get(key) : null;
	}

	private static <K> K getKey(final Entry<K, ?> instance) {
		return instance != null ? instance.getKey() : null;
	}

	private static <V> V getValue(final Entry<?, V> instance) {
		return instance != null ? instance.getValue() : null;
	}

	private static String getName(final Member instance) {
		return instance != null ? instance.getName() : null;
	}

	private static Object getElementAt(final ComboBoxModel<?> instance, final int index) {
		return instance != null ? instance.getElementAt(index) : null;
	}

	private static int getSize(final ComboBoxModel<?> instance) {
		return instance != null ? instance.getSize() : 0;
	}

	@Override
	public void changedUpdate(final DocumentEvent evt) {
		//
		throw new IllegalStateException();
		//
	}

	@Override
	public void insertUpdate(final DocumentEvent evt) {
		//
		insertOrRemove(getDocument(evt));
		//
	}

	private void insertOrRemove(final Document document) {
		//
		setIcon(labelImage, null);
		//
		forEach(Arrays.asList(FractionJTextComponent.getWhole(answer), FractionJTextComponent.getNumerator(answer),
				FractionJTextComponent.getDenominator(answer)), x -> setText(x, ""));
		//
		if (Objects.equals(document, documentWhole1)) {
			//
			try {
				//
				final BigDecimal bd = testAndApply(NumberUtils::isCreatable,
						getText(documentWhole1, 0, getLength(documentWhole1)), BigDecimal::new, null);
				//
				final Iterable<JTextComponent> iterable = Arrays.asList(FractionJTextComponent.getNumerator(fraction1),
						FractionJTextComponent.getDenominator(fraction1));
				//
				if (bd != null && contains(toPlainString(bd.stripTrailingZeros()), ".")) {
					//
					forEach(iterable, x -> {
						//
						setText(x, "");
						//
						setEditable(x, false);
						//
					});
					//
				} else {
					//
					forEach(iterable, x -> setEditable(x, true));
					//
				} // if
					//
			} catch (final BadLocationException e) {
				//
				throw new RuntimeException(e);
				//
			} // try
				//
		} else if (Objects.equals(document, documentWhole2)) {
			//
			try {
				//
				final BigDecimal bd = testAndApply(NumberUtils::isCreatable,
						getText(documentWhole2, 0, getLength(documentWhole2)), BigDecimal::new, null);
				//
				final Iterable<JTextComponent> iterable = Arrays.asList(FractionJTextComponent.getNumerator(fraction2),
						FractionJTextComponent.getDenominator(fraction2));
				//
				if (bd != null && contains(toPlainString(bd.stripTrailingZeros()), ".")) {
					//
					forEach(iterable, x -> {
						//
						setText(x, "");
						//
						setEditable(x, false);
						//
					});
					//
				} else {
					//
					forEach(iterable, x -> setEditable(x, true));
					//
				} // if
					//
			} catch (final BadLocationException e) {
				//
				throw new RuntimeException(e);
				//
			} // try
				//
		} // if
			//

	}

	private static int getLength(final Document instance) {
		return instance != null ? instance.getLength() : 0;
	}

	private static String getText(final Document instance, final int offset, final int length)
			throws BadLocationException {
		return instance != null ? instance.getText(offset, length) : null;
	}

	private static Document getDocument(final DocumentEvent instance) {
		return instance != null ? instance.getDocument() : null;
	}

	@Override
	public void removeUpdate(final DocumentEvent evt) {
		//
		insertOrRemove(getDocument(evt));
		//
	}

	private static <T> void forEach(final Iterable<T> instance, final Consumer<T> consumer) {
		if (instance != null) {
			instance.forEach(consumer);
		}
	}

}