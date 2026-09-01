package org.apache.commons.lang3.math;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Properties;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.LongToIntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderWriterSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.swing.AbstractButton;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComboBox.KeySelectionManager;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
import javax.swing.text.JTextComponent;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.FailableBiConsumer;
import org.apache.commons.lang3.function.FailableBiFunction;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableFunction;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.meeuw.functional.ThrowingTriConsumer;
import org.meeuw.functional.TriPredicate;

import com.google.common.reflect.Reflection;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import io.github.toolfactory.narcissus.Narcissus;
import net.miginfocom.swing.MigLayout;

public class FractionJPanel extends JPanel
		implements ActionListener, KeySelectionManager, DocumentListener, ItemListener {

	private static final long serialVersionUID = 1238012263601647765L;

	private static final String WMIN = "wmin";

	private static final String RASTER = "raster";

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

	private transient ComboBoxModel<Method> cbmMethod = null;

	private transient ComboBoxModel<String> cbmFileSuffix = null;

	private transient ComboBoxModel<Entry<?, ?>> cbmColor = null;

	@Note("Execute")
	private AbstractButton btnExecute = null;

	@Note("Clear")
	private AbstractButton btnClear = null;

	@Note("Show Image")
	private AbstractButton btnShowImage = null;

	@Note("Copy Image")
	private AbstractButton btnCopyImage = null;

	@Note("Save Image")
	private AbstractButton btnSaveImage = null;

	@Note("Save PDF")
	private AbstractButton btnSavePdf = null;

	private AbstractButton cbChopImage = null;

	@Note("Fraction 1 Whole Document")
	private transient Document documentWhole1 = null;

	private transient Document documentWhole2 = null;

	private JLabel labelImage = null;

	@Note("Method")
	private JComboBox<?> jcbMethod = null;

	@Note("File Suffix")
	private JComboBox<?> jcbFileSuffix = null;

	private JComboBox<?> jcbColor = null;

	private Window window = null;

	private Properties properties = null;

	private JTextComponent tfFontSize = null;

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
		final ListCellRenderer lcr1 = (jcbMethod = new JComboBox<>(
				cbmMethod = new DefaultComboBoxModel<>(toArray(toList(filter(Arrays.stream(Fraction.class.getMethods()),
						m -> Boolean.logicalAnd(
								Arrays.equals(getParameterTypes(m), new Class<?>[] { getDeclaringClass(m) }),
								Objects.equals(getReturnType(m), getDeclaringClass(m))))),
						Method[]::new))))
				.getRenderer();
		//
		jcbMethod.setRenderer((arg0, value, arg2, arg3, arg4) -> {
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
			return getListCellRendererComponent(lcr1, arg0, arg0, arg2, arg3, arg4);
			//
		});
		//
		setSelectedItem(cbmMethod, null);
		//
		jcbMethod.setKeySelectionManager(this);
		//
		jcbMethod.addItemListener(this);
		//
		add(jcbMethod);
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
		add(jPanel, wrap);
		//
		(jPanel = new JPanel()).setLayout(new MigLayout());
		//
		jPanel.add(new JLabel("Font Size"));
		//
		jPanel.add(tfFontSize = new JTextField(), String.format("wmin %1$s,%2$s", 50, wrap));
		//
		setDocumentFilter(cast(AbstractDocument.class, getDocument(tfFontSize)), createDocumentFilter(orElse(
				max(mapToInt(LongStream.of(Long.MIN_VALUE, Long.MAX_VALUE), x -> StringUtils.length(Long.toString(x)))),
				0)));
		//
		jPanel.add(new JLabel("Font Color"));
		//
		final ListCellRenderer lcr2 = (jcbColor = new JComboBox<>(
				cbmColor = new DefaultComboBoxModel<>(entrySet(collect(
						filter(testAndApply(Objects::nonNull, Color.class.getDeclaredFields(), Arrays::stream, null),
								f -> and(isStatic(f), Objects.equals(getType(f), getDeclaringClass(f)),
										Objects.equals(StringUtils.upperCase(getName(f)), getName(f))))
								.sorted((a, b) -> StringUtils.compare(getName(a), getName(b), true)),
						LinkedHashMap::new, (a, b) -> put(a, b, Narcissus.getStaticField(b)), Map::putAll))
						.toArray(Entry[]::new))))
				.getRenderer();
		//
		jPanel.add(jcbColor);
		//
		cbmColor.setSelectedItem(null);
		//
		if (containsKey(properties, "org.apache.commons.lang3.math.FractionJPanel.color")) {
			//
			final List<?> list = toList(
					filter(mapToObj(IntStream.rangeClosed(0, getSize(cbmColor)), i -> getElementAt(cbmColor, i)),
							x -> equalsIgnoreCase(getName(cast(Member.class, getKey(cast(Entry.class, x)))),
									getProperty(properties, "org.apache.commons.lang3.math.FractionJPanel.color"))));
			//
			testAndRun(IterableUtils.size(list) > 1, () -> {
				//
				throw new IllegalStateException();
				//
			});
			//
			testAndAccept(x -> IterableUtils.size(x) == 1, list,
					x -> setSelectedItem(cbmColor, IterableUtils.get(x, 0)));
			//
		} // if
			//
		jcbColor.setRenderer((arg0, value, arg2, arg3, arg4) -> {
			//
			final Entry<?, ?> entry = cast(Entry.class, value);
			//
			if (entry != null) {
				//
				final JPanel jp = new JPanel();
				//
				jp.setLayout(new MigLayout());
				//
				jp.add(new JLabel(getName(cast(Member.class, getKey(entry)))));
				//
				final JLabel jLabel = new JLabel();
				//
				jLabel.setBackground(cast(Color.class, getValue(entry)));
				//
				jLabel.setOpaque(true);
				//
				jp.add(jLabel, "wmin 10,hmin 10,pushx,align right");
				//
				return jp;
				//
			} else if (value == null) {
				//
				return new JLabel();
				//
			} // if
				//
			return getListCellRendererComponent(lcr2, arg0, arg0, arg2, arg3, arg4);
			//
		});
		//
		add(jPanel, String.format("%1$s,span %2$s", wrap, 5));
		//
		(jPanel = new JPanel()).setLayout(new MigLayout());
		//
		jPanel.add(labelImage = new JLabel());
		//
		add(jPanel, String.format("%1$s,span %2$s", wrap, 5));
		//
		(jPanel = new JPanel()).setLayout(new MigLayout());
		//
		jPanel.add(cbChopImage = new JCheckBox("Chop Image"));
		//
		add(jPanel, wrap);
		//
		(jPanel = new JPanel()).setLayout(new MigLayout());
		//
		jPanel.add(btnShowImage = new JButton("Show Image"));
		//
		jPanel.add(btnCopyImage = new JButton("Copy Image"));
		//
		final Iterator<ImageWriterSpi> imageWriterSpis = getServiceProviders(IIORegistry.getDefaultInstance(),
				ImageWriterSpi.class,
				BooleanUtils.toBooleanDefaultIfNull(Boolean.valueOf(
						getProperty(properties, "javax.imageio.spi.ServiceRegistry.getServiceProviders.useOrdering")),
						true));
		//
		String[] fileSuffixes = null;
		//
		Set<String> set = null;
		//
		while (hasNext(imageWriterSpis)) {
			//
			addAll(set = ObjectUtils.getIfNull(set, LinkedHashSet::new),
					testAndApply(Objects::nonNull, getFileSuffixes(next(imageWriterSpis)), Arrays::asList, null));
			//
		} // while
			//
		fileSuffixes = toArray(set, String[]::new);
		//
		if (BooleanUtils.toBooleanDefaultIfNull(
				Boolean.valueOf(
						getProperty(properties, "org.apache.commons.lang3.math.FractionJPanel.sortFileSuffixes")),
				false)) {
			//
			Arrays.sort(fileSuffixes);
			//
		} // if
			//
		if (containsKey(properties, "org.apache.commons.lang3.math.FractionJPanel.preferredFileSuffix")) {
			//
			final String preferredFileSuffix = getProperty(properties,
					"org.apache.commons.lang3.math.FractionJPanel.preferredFileSuffix");
			//
			Arrays.sort(fileSuffixes, (a, b) -> Boolean.logicalAnd(Objects.equals(preferredFileSuffix, a),
					!Objects.equals(preferredFileSuffix, b)) ? -1 : 0);
			//
		} // if
			//
		jPanel.add(jcbFileSuffix = new JComboBox<>(cbmFileSuffix = new DefaultComboBoxModel<>(fileSuffixes)));
		//
		jPanel.add(btnSaveImage = new JButton("Save Image"));
		//
		add(jPanel, String.format("span %1$s,%2$s", 5, wrap));
		//
		(jPanel = new JPanel()).setLayout(new MigLayout());
		//
		jPanel.add(btnSavePdf = new JButton("Save PDF"));
		//
		add(jPanel);
		//
		forEach(map(
				testAndApply(Objects::nonNull, FractionJTextComponent.class.getDeclaredFields(), Arrays::stream, null),
				f -> cast(JTextComponent.class, Narcissus.getField(answer, f))), x -> setEditable(x, false));
		//
		setFocusTraversalPolicyProvider(true);
		//
		setFocusTraversalPolicy(createFocusTraversalPolicy(Arrays.asList(FractionJTextComponent.getWhole(fraction1),
				FractionJTextComponent.getNumerator(fraction1), FractionJTextComponent.getDenominator(fraction1),
				jcbMethod, FractionJTextComponent.getWhole(fraction2), FractionJTextComponent.getNumerator(fraction2),
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
				f -> cast(AbstractButton.class, testAndApply(FractionJPanel::isStatic, f, Narcissus::getStaticField,
						x -> Narcissus.getField(this, x)))),
				x -> addActionListener(x, this));
		//
		clear();
		//
		final DocumentFilter documentFilter1 = new DocumentFilterImpl(true);
		//
		forEach(Arrays.asList(FractionJTextComponent.getNumerator(fraction1),
				FractionJTextComponent.getNumerator(fraction2)),
				x -> setDocumentFilter(cast(AbstractDocument.class, getDocument(x)), documentFilter1));
		//
		final DocumentFilter documentFilter2 = new DocumentFilterImpl(false);
		//
		forEach(Arrays.asList(FractionJTextComponent.getDenominator(fraction1),
				FractionJTextComponent.getDenominator(fraction2)),
				x -> setDocumentFilter(cast(AbstractDocument.class, getDocument(x)), documentFilter2));
		//
	}

	private static <U> Stream<U> mapToObj(final IntStream instance, final IntFunction<? extends U> mapper) {
		return instance != null ? instance.mapToObj(mapper) : null;
	}

	private static boolean equalsIgnoreCase(final String a, final String b) {
		return a != null && isValidString(a) && isValidString(b) && a.equalsIgnoreCase(b);
	}

	private static DocumentFilter createDocumentFilter(final int maxLength) {
		//
		return new DocumentFilter() {

			@Override
			public void replace(final FilterBypass fb, final int offset, final int length, final String text,
					final AttributeSet attrs) throws BadLocationException {
				//
				final int overLimit = (getLength(getDocument(fb)) + StringUtils.length(text) - length) - maxLength;
				//
				if (overLimit <= 0 && fb != null) {
					//
					super.replace(fb, offset, length, text, attrs);
					//
				} else {
					//
					if (StringUtils.length(text) > overLimit) {
						//
						super.replace(fb, offset, length,
								StringUtils.substring(text, 0, StringUtils.length(text) - overLimit), attrs);
						//
					} // if
						//
				} // if
					//
			}

			@Override
			public void insertString(final FilterBypass fb, final int offset, final String string,
					final AttributeSet attr) throws BadLocationException {
				//
				if ((getLength(getDocument(fb)) + StringUtils.length(string)) <= maxLength && fb != null) {
					//
					super.insertString(fb, offset, string, attr);
					//
				} // if
					//
			}

		};
		//
	}

	private static boolean and(final boolean a, final boolean b, final boolean... bs) {
		//
		boolean result = Boolean.logicalAnd(a, b);
		//
		if (!result) {
			//
			return result;
			//
		} // if
			//
		for (int i = 0; bs != null && i < bs.length; i++) {
			//
			if (!(result &= bs[i])) {
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

	private static <T, R> R collect(final Stream<T> instance, final Supplier<R> supplier,
			final BiConsumer<R, ? super T> accumulator, final BiConsumer<R, R> combiner) {
		return instance != null ? instance.collect(supplier, accumulator, combiner) : null;
	}

	private static Class<?> getType(final Field instance) {
		return instance != null ? instance.getType() : null;
	}

	private static <E> E next(final Iterator<E> instance) {
		return instance != null ? instance.next() : null;
	}

	private static boolean hasNext(final Iterator<?> instance) {
		return instance != null && instance.hasNext();
	}

	private static <E> void addAll(final Collection<E> a, final Collection<? extends E> b) {
		if (a != null && b != null) {
			a.addAll(b);
		}
	}

	private static <E> Component getListCellRendererComponent(final ListCellRenderer<E> instance,
			final JList<? extends E> list, final E value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {
		return instance != null ? instance.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
				: null;
	}

	private static Class<?> getReturnType(final Method instance) {
		return instance != null ? instance.getReturnType() : null;
	}

	private static Class<?> getDeclaringClass(final Member instance) {
		return instance != null ? instance.getDeclaringClass() : null;
	}

	private static Class<?>[] getParameterTypes(final Executable instance) {
		//
		if (instance == null) {
			//
			return null;
			//
		} // if
			//
		try {
			//
			if (Narcissus.getField(instance, Narcissus.findField(getClass(instance), "parameterTypes")) == null) {
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
		return instance.getParameterTypes();
		//
	}

	private static int orElse(final OptionalInt instance, final int defaultValue) {
		return instance != null ? instance.orElse(defaultValue) : defaultValue;
	}

	private static OptionalInt max(final IntStream instance) {
		return instance != null ? instance.max() : null;
	}

	private static IntStream mapToInt(final LongStream instance, final LongToIntFunction mapper) {
		return instance != null ? instance.mapToInt(mapper) : null;
	}

	private static Document getDocument(final FilterBypass instance) {
		return instance != null ? instance.getDocument() : null;
	}

	private static String[] getFileSuffixes(final ImageReaderWriterSpi instnace) {
		return instnace != null ? instnace.getFileSuffixes() : null;
	}

	private static boolean containsKey(final Properties instance, final String key) {
		//
		if (instance == null || key == null) {
			//
			return false;
			//
		} // if
			//
		try {
			//
			if (Narcissus.getField(instance, Narcissus.findField(getClass(instance), "map")) == null) {
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
		return instance.containsKey(key);
		//
	}

	private static String getProperty(final Properties instance, final String key) {
		//
		if (instance == null || key == null) {
			//
			return null;
			//
		} // if
			//
		try {
			//
			if (Narcissus.getField(instance, Narcissus.findField(getClass(instance), "map")) == null) {
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
		return instance.getProperty(key);
		//
	}

	private static <T> Iterator<T> getServiceProviders(final ServiceRegistry instance, final Class<T> category,
			final boolean useOrdering) {
		//
		if (instance == null) {
			//
			return null;
			//
		} // if
			//
		try {
			//
			if (Narcissus.getField(instance, Narcissus.findField(getClass(instance), "categoryMap")) == null) {
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
		return instance.getServiceProviders(category, useOrdering);
		//
	}

	private static boolean isStatic(final Member instance) {
		return instance != null && Modifier.isStatic(instance.getModifiers());
	}

	private static void setDocumentFilter(final AbstractDocument instance, final DocumentFilter documentFilter) {
		if (instance != null) {
			instance.setDocumentFilter(documentFilter);
		}
	}

	private static class DocumentFilterImpl extends DocumentFilter {

		private static final String THIS_DOLLAR_ZERO = "this$0";

		private boolean negative;

		private DocumentFilterImpl(final boolean negative) {
			this.negative = negative;
		}

		@Override
		public void insertString(final FilterBypass fb, final int offset, final String string,
				final AttributeSet attributeSet) throws BadLocationException {
			//
			if (fb == null || isFieldNull(fb, THIS_DOLLAR_ZERO)) {
				//
				return;
				//
			} // if
				//
			if (and(getLength(fb.getDocument()) == 0, Objects.equals(string, "-"), negative)) {
				//
				fb.insertString(offset, string, attributeSet);
				//
			} else {
				//
				fb.insertString(offset, replaceAll(string, "\\D++", ""), attributeSet);
				//
			} // if
				//
		}

		private static int getLength(final Document instance) {
			return instance != null ? instance.getLength() : 0;
		}

		@Override
		public void replace(final FilterBypass fb, int offset, final int length, final String string,
				final AttributeSet attributeSet) throws BadLocationException {
			//
			if (fb == null || isFieldNull(fb, THIS_DOLLAR_ZERO)) {
				//
				return;
				//
			} // if
				//
			if (and(offset == 0, Objects.equals(string, "-"), negative)) {
				//
				fb.replace(offset, length, string, attributeSet);
				//
			} else {
				//
				fb.replace(offset, length, replaceAll(string, "\\D++", ""), attributeSet);
				//
			} // if
				//
		}

		@Override
		public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
			//
			if (FractionJPanel.and(fb, Objects::nonNull, x -> !isFieldNull(x, THIS_DOLLAR_ZERO))) {
				//
				super.remove(fb, offset, length);
				//
			} // if
				//
		}

		private static boolean isFieldNull(final Object instance, final String fieldName) {
			//
			try {
				//
				if (Narcissus.getField(instance,
						Narcissus.findField(FractionJPanel.getClass(instance), fieldName)) == null) {
					//
					return true;
					//
				} // if
					//
			} catch (final NoSuchFieldException e) {
				//
				throw new RuntimeException(e);
				//
			} // try
				//
			return false;
			//
		}

		private static String replaceAll(final String instance, final String regex, final String replacement) {
			//
			return instance != null && isValidString(instance) && regex != null && isValidString(regex)
					&& replacement != null && isValidString(replacement) ? instance.replaceAll(regex, replacement)
							: null;
			//
		}

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
		setSelectedItem(cbmMethod, null);
		//
		setIcon(labelImage, null);
		//
		forEach(Arrays.asList(cbChopImage, tfFontSize, jcbColor, btnShowImage, btnCopyImage, btnSaveImage, btnSavePdf,
				btnExecute), x -> setEnabled(x, false));
		//
		setEnabled(jcbFileSuffix, false);
		//
		pack(window);
		//
	}

	private static void setEnabled(final JComponent instance, final boolean enabled) {
		//
		try {
			//
			if (instance == null
					|| Narcissus.getField(instance, Narcissus.findField(getClass(instance), "appContext")) == null) {
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
		instance.setEnabled(enabled);
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

	public static void main(final String[] args) throws IOException {
		//
		final FractionJPanel instance = new FractionJPanel();
		//
		final String propertiesClassPath = StringUtils.join('/', replace(FractionJPanel.class.getName(), '.', '/'),
				".properties");
		//
		try (final InputStream is1 = FractionJPanel.class.getResourceAsStream(propertiesClassPath);
				final InputStream is2 = testAndApply(
						x -> and(x, FractionJPanel::exists, FractionJPanel::isFile, FractionJPanel::canRead),
						new File(StringUtils.substringAfterLast(propertiesClassPath, "/")), FileInputStream::new,
						null)) {
			//
			final Properties properties = new Properties();
			//
			testAndAccept(Objects::nonNull, is1, properties::load);
			//
			testAndAccept(Objects::nonNull, is2, properties::load);
			//
			instance.properties = properties;
			//
		} // try
			//
		final JFrame jFrame = !GraphicsEnvironment.isHeadless() ? new JFrame() : null;
		//
		if (jFrame != null) {
			//
			jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			//
			instance.init();
			//
			jFrame.add(instance);
			//
			pack(instance.window = jFrame);
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

	private static boolean exists(final File instance) {
		return instance != null && instance.getPath() != null && instance.exists();
	}

	private static boolean isFile(final File instance) {
		return instance != null && instance.getPath() != null && instance.isFile();
	}

	private static boolean canRead(final File instance) {
		return instance != null && instance.getPath() != null && instance.canRead();
	}

	private static String replace(final String instance, final char oldChar, final char newChar) {
		return instance != null && isValidString(instance) ? instance.replace(oldChar, newChar) : null;
	}

	private static void pack(final Window instnace) {
		if (instnace != null && !GraphicsEnvironment.isHeadless()) {
			instnace.pack();
		}
	}

	private static boolean isTestMode() {
		try {
			return Class.forName("org.testng.annotations.Test") != null;
		} catch (final ClassNotFoundException e) {
			return false;
		}
	}

	private static class IH implements InvocationHandler {

		private Image image = null;

		@Override
		public Object invoke(final Object instance, final Method method, Object[] arg) throws Throwable {
			//
			final String name = getName(method);
			//
			if (instance instanceof Transferable) {
				//
				if (Objects.equals(name, "getTransferDataFlavors")) {
					//
					return new DataFlavor[] { DataFlavor.imageFlavor };
					//
				} else if (Objects.equals(name, "getTransferData")) {
					//
					return image;
					//
				} // if
					//
			} // if
				//
			throw new Throwable(name);
			//
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
						testAndApply((a, b) -> Boolean.logicalAnd(a != null, b != null), fractionA, fractionB,
								(a, b) -> invoke(cast(Method.class, getSelectedItem(cbmMethod)), a, b), null));
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
				forEach(Arrays.asList(cbChopImage, tfFontSize, jcbColor, btnShowImage), x -> setEnabled(x, true));
				//
			} catch (final ReflectiveOperationException e) {
				//
				if (e instanceof InvocationTargetException ite && ite != null) {
					//
					throw new RuntimeException(ObjectUtils.getIfNull(ite.getTargetException(), ite));
					//
				} // if
					//
				throw new RuntimeException(e);
				//
			} // try
				//
			return;
			//
		} else if (Objects.equals(source, btnClear)) {
			//
			clear();
			//
			return;
			//
		} else if (Objects.equals(source, btnShowImage)) {
			//
			try (final Playwright playwright = Playwright.create();
					final Browser browser = launch(chromium(playwright));
					final Page page = newPage(browser)) {
				//
				setContent(page, toHtml());
				//
				final byte[] bs = screenshot(locator(page, "tbody"));
				//
				if (cbChopImage != null && cbChopImage.isSelected()) {
					//
					setIcon(labelImage, new ImageIcon(chopImage(toBufferedImage(bs))));
					//
				} else {
					//
					setIcon(labelImage, new ImageIcon(bs));
					//
				} // if
					//
				forEach(Arrays.asList(btnCopyImage, btnSaveImage, btnSavePdf, jcbFileSuffix), x -> setEnabled(x, true));
				//
				pack(window);
				//
			} catch (final IOException e) {
				//
				throw new RuntimeException();
				//
			} // try
				//
			return;
			//
		} // if
			//
		actionPerformed(this, source);
		//
	}

	private static BufferedImage chopImage(final BufferedImage bufferedImage) {
		//
		if (bufferedImage == null) {
			//
			return bufferedImage;
			//
		} // if
			//
		final int width = getWidth(bufferedImage);
		//
		final int height = getHeight(bufferedImage);
		//
		Integer firstColor = null, minX = null, minY = null, maxX = null, maxY = null;
		//
		for (int x = 0; x < width && bufferedImage != null; x++) {
			//
			for (int y = 0; y < height; y++) {
				//
				if (firstColor == null) {
					//
					firstColor = Integer.valueOf(bufferedImage.getRGB(x, y));
					//
					continue;
					//
				} // if
					//
				if (firstColor != null && firstColor.intValue() != bufferedImage.getRGB(x, y)) {
					//
					minX = Integer.valueOf(Math.min(intValue(minX, x), x));
					//
					maxX = Integer.valueOf(Math.max(intValue(maxX, x), x));
					//
					minY = Integer.valueOf(Math.min(intValue(minY, y), y));
					//
					maxY = Integer.valueOf(Math.max(intValue(maxY, y), y));
					//
				} // if
					//
			} // for
				//
		} // for
			//
		final Iterable<Field> fs = toList(filter(stream(FieldUtils.getAllFieldsList(getClass(bufferedImage))),
				x -> Objects.equals(getName(x), RASTER)));
		//
		testAndRun(IterableUtils.size(fs) > 1, () -> {
			//
			throw new IllegalStateException();
			//
		});
		//
		if (bufferedImage != null
				&& testAndApply(Objects::nonNull,
						testAndApply(x -> IterableUtils.size(x) == 1, fs, x -> IterableUtils.get(x, 0), null),
						x -> Narcissus.getField(bufferedImage, x), null) != null
				&& and(Objects::nonNull, minX, minY, maxX, maxY)) {
			//
			return bufferedImage.getSubimage(intValue(minX, 0), intValue(minY, 0),
					intValue(maxX, 0) - intValue(minX, 0) + 1, intValue(maxY, 0) - intValue(minY, 0) + 1);
			//
		} // if
			//
		return bufferedImage;
		//
	}

	private static <T> boolean and(final Predicate<T> predicate, final T a, final T b, final T c, final T d) {
		return and(test(predicate, a), test(predicate, b), test(predicate, c), test(predicate, d));
	}

	private static int getHeight(final BufferedImage instance) {
		//
		final Iterable<Field> fs = toList(
				filter(stream(testAndApply(Objects::nonNull, getClass(instance), FieldUtils::getAllFieldsList, null)),
						x -> Objects.equals(getName(x), RASTER)));
		//
		testAndRun(IterableUtils.size(fs) > 1, () -> {
			//
			throw new IllegalStateException();
			//
		});
		//
		final Field f = testAndApply(x -> IterableUtils.size(x) == 1, fs, x -> IterableUtils.get(x, 0), null);
		//
		return f != null && Narcissus.getField(instance, f) != null ? instance.getHeight() : 0;
		//
	}

	private static int getWidth(final BufferedImage instance) {
		//
		final Iterable<Field> fs = toList(
				filter(stream(testAndApply(Objects::nonNull, getClass(instance), FieldUtils::getAllFieldsList, null)),
						x -> Objects.equals(getName(x), RASTER)));
		//
		testAndRun(IterableUtils.size(fs) > 1, () -> {
			//
			throw new IllegalStateException();
			//
		});
		//
		final Field f = testAndApply(x -> IterableUtils.size(x) == 1, fs, x -> IterableUtils.get(x, 0), null);
		//
		return f != null && Narcissus.getField(instance, f) != null ? instance.getWidth() : 0;
		//
	}

	private static <E> Stream<E> stream(final Collection<E> instance) {
		return instance != null ? instance.stream() : null;
	}

	private static BufferedImage toBufferedImage(final byte[] bs) throws IOException {
		//
		try (final InputStream is = testAndApply(Objects::nonNull, bs, ByteArrayInputStream::new, null)) {
			//
			return testAndApply(Objects::nonNull, is, ImageIO::read, null);
			//
		} // try
			//
	}

	private static byte[] pdf(final Page instance) {
		return instance != null ? instance.pdf() : null;
	}

	private static <T, U, E extends Exception> void testAndAccept(final BiPredicate<T, U> predicate, final T t,
			final U u, final FailableBiConsumer<T, U, E> consumer) throws E {
		if (test(predicate, t, u)) {
			accept(consumer, t, u);
		}
	}

	private static <T, U, E extends Exception> void accept(final FailableBiConsumer<T, U, E> instance, final T t,
			final U u) throws E {
		if (instance != null) {
			instance.accept(t, u);
		}
	}

	private String toHtml() {
		//
		final StringBuilder sb = new StringBuilder("<html><body>");
		//
		sb.append('<');
		//
		sb.append("table");
		//
		Map<String, String> style = null;
		//
		final String fontSize = getText(tfFontSize);
		//
		if (StringUtils.isNotBlank(fontSize)) {
			//
			put(style = ObjectUtils.getIfNull(style, LinkedHashMap::new), "font-size", fontSize);
			//
		} // if
			//
		final Member color = cast(Member.class, getKey(cast(Entry.class, getSelectedItem(cbmColor))));
		//
		if (color != null) {
			//
			put(style = ObjectUtils.getIfNull(style, LinkedHashMap::new), "color", getName(color));
			//
		} // if
			//
		if (entrySet(style) != null && entrySet(style).iterator() != null) {
			//
			sb.append(" style=");
			//
			sb.append('"');
			//
			final int length = StringUtils.length(sb);
			//
			for (final Entry<?, ?> entry : entrySet(style)) {
				//
				if (entry == null) {
					//
					continue;
					//
				} // if
					//
				if (StringUtils.length(sb) > length) {
					//
					sb.append(';');
					//
				} // if
					//
				sb.append(StringUtils.joinWith(":", getKey(entry), getValue(entry)));
				//
			} // for
				//
			sb.append('"');
			//
		} // if
			//
		sb.append('>');
		//
		sb.append("<tr><td>");
		//
		sb.append(toMathML(fraction1));
		//
		final Object object = testAndApply(Objects::nonNull, getName(cast(Member.class, getSelectedItem(cbmMethod))),
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
		return Objects.toString(sb.append("</td></tr></tbody></table></body></html>"));
		//
	}

	private static <K, V> void put(final Map<K, V> instance, final K key, final V value) {
		if (instance != null) {
			instance.put(key, value);
		}
	}

	private static void setContents(final Clipboard instance, final Transferable contents, final ClipboardOwner owner) {
		if (instance != null) {
			instance.setContents(contents, owner);
		}
	}

	private static String getName(final Class<?> instance) {
		return instance != null ? instance.getName() : null;
	}

	private static void actionPerformed(final FractionJPanel instance, final Object source) {
		//
		if (instance == null) {
			//
			return;
			//
		} // if
			//
		if (Objects.equals(source, instance.btnSaveImage)) {
			//
			try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				//
				final Image image = getImage(cast(ImageIcon.class, getIcon(instance.labelImage)));
				//
				final List<Method> ms = toList(filter(
						testAndApply(Objects::nonNull, getDeclaredMethods(getClass(image)), Arrays::stream, null),
						m -> Boolean.logicalAnd(Objects.equals(getName(m), "getBufferedImage"),
								getParameterCount(m) == 0)));
				//
				testAndRun(IterableUtils.size(ms) > 1, () -> {
					//
					throw new IllegalStateException();
					//
				});
				//
				final String format = Objects.toString(getSelectedItem(instance.cbmFileSuffix));
				//
				if (IterableUtils.size(ms) == 1) {
					//
					testAndAccept(Objects::nonNull, cast(BufferedImage.class,
							testAndApply((a, b) -> Boolean.logicalAnd(a != null, b != null), image,
									testAndApply(x -> IterableUtils.size(x) == 1, ms, x -> IterableUtils.get(x, 0),
											null),
									Narcissus::invokeMethod, null)),
							x -> ImageIO.write(cast(BufferedImage.class, x), format, baos));
					//
				} else if (image instanceof BufferedImage bufferedImage) {
					//
					testAndAccept((a, b, c) -> and(a != null, b != null, c != null), bufferedImage, format, baos,
							ImageIO::write);
					//
				} // if
					//
				final JFileChooser jfc = new JFileChooser(".");
				//
				if (and(!GraphicsEnvironment.isHeadless(),
						() -> jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)) {
					//
					Files.write(toPath(jfc.getSelectedFile()), baos.toByteArray(), StandardOpenOption.CREATE,
							StandardOpenOption.TRUNCATE_EXISTING);
					//
				} // if
					//
			} catch (final IOException e) {
				//
				throw new RuntimeException(e);
				//
			} // try
				//
		} else if (Objects.equals(source, instance.btnCopyImage)) {
			//
			final IH ih = new IH();
			//
			ih.image = getImage(cast(ImageIcon.class, getIcon(instance.labelImage)));
			//
			setContents(getSystemClipboard(Toolkit.getDefaultToolkit()), Reflection.newProxy(Transferable.class, ih),
					null);
			//
		} else if (Objects.equals(source, instance.btnSavePdf)) {
			//
			try (final Playwright playwright = Playwright.create();
					final Browser browser = launch(chromium(playwright));
					final Page page = newPage(browser)) {
				//
				setContent(page, instance.toHtml());
				//
				final JFileChooser jfc = new JFileChooser(".");
				//
				if (and(!GraphicsEnvironment.isHeadless(),
						() -> jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)) {
					//
					testAndAccept((a, b) -> Boolean.logicalAnd(a != null, b != null), toPath(jfc.getSelectedFile()),
							pdf(page), (a, b) -> Files.write(a, b, StandardOpenOption.CREATE,
									StandardOpenOption.TRUNCATE_EXISTING));
					//
				} // if
					//
			} catch (final IOException e) {
				//
				throw new RuntimeException(e);
				//
			} // try
				//
		} // if
			//
	}

	private static <T, U, V, E extends Exception> void testAndAccept(final TriPredicate<T, U, V> predicate, final T t,
			final U u, final V v, final ThrowingTriConsumer<T, U, V, E> consumer) {
		//
		if (predicate != null && predicate.test(t, u, v) && consumer != null) {
			//
			consumer.accept(t, u, v);
			//
		} // if
			//
	}

	private static Clipboard getSystemClipboard(final Toolkit instance) {
		//
		return instance != null && !Objects.equals(getName(getClass(instance)), "sun.awt.HeadlessToolkit")
				? instance.getSystemClipboard()
				: null;
		//
	}

	private static boolean and(final boolean condition, final BooleanSupplier booleanSupplier) {
		return condition && booleanSupplier != null && booleanSupplier.getAsBoolean();
	}

	private static void testAndRun(final boolean condition, final Runnable runnable) {
		if (condition) {
			run(runnable);
		}
	}

	private static Icon getIcon(final JLabel instance) {
		return instance != null ? instance.getIcon() : null;
	}

	private static Image getImage(final ImageIcon instance) {
		return instance != null ? instance.getImage() : null;
	}

	private static int getParameterCount(final Executable instance) {
		//
		if (instance instanceof Method) {
			//
			try {
				//
				if (Narcissus.getField(instance, Narcissus.findField(getClass(instance), "parameterTypes")) == null) {
					//
					return 0;
					//
				} // if
					//
			} catch (final NoSuchFieldException e) {
				//
				throw new RuntimeException(e);
				//
			} // try
				//
		} // if
			//
		return instance != null ? instance.getParameterCount() : 0;
		//
	}

	private static <T, U, R, E extends Exception> R testAndApply(final BiPredicate<T, U> predicate, final T t,
			final U u, final FailableBiFunction<T, U, R, E> functionTrue,
			final FailableBiFunction<T, U, R, E> functionFalse) throws E {
		return test(predicate, t, u) ? apply(functionTrue, t, u) : apply(functionFalse, t, u);
	}

	private static <T, U, R, E extends Exception> R apply(final FailableBiFunction<T, U, R, E> instance, final T t,
			final U u) throws E {
		return instance != null ? instance.apply(t, u) : null;
	}

	private static <T, U> boolean test(final BiPredicate<T, U> instance, final T t, final U u) {
		return instance != null && instance.test(t, u);
	}

	private static Path toPath(final File instance) {
		return instance != null && instance.getPath() != null ? instance.toPath() : null;
	}

	private static Method[] getDeclaredMethods(final Class<?> instance) {
		return instance != null ? instance.getDeclaredMethods() : null;
	}

	private static Browser launch(final BrowserType instance) {
		return instance != null ? instance.launch() : null;
	}

	private static BrowserType chromium(final Playwright instance) {
		return instance != null ? instance.chromium() : null;
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
		if (Boolean
				.logicalAnd(
						and(whole, FractionJPanel::isValidString, NumberUtils::isDigits), Boolean
								.logicalOr(
										Boolean.logicalAnd(
												Boolean.logicalOr(numerator == null,
														and(numerator, FractionJPanel::isValidString,
																StringUtils::isEmpty)),
												Boolean.logicalOr(denominator == null,
														and(denominator, FractionJPanel::isValidString,
																StringUtils::isEmpty))),
										Boolean.logicalAnd(
												Boolean.logicalOr(numerator == null,
														and(numerator, FractionJPanel::isValidString,
																x -> Objects.equals(x, "0"))),
												Boolean.logicalOr(denominator == null,
														and(denominator, FractionJPanel::isValidString,
																x -> !Objects.equals(x, "0"))))))) {
			//
			return whole;
			//
		} // if
			//
		final StringBuilder sb = new StringBuilder("<math>");
		//
		final String format = "<%1$s>%2$s</%1$s>";
		//
		if (and(whole, FractionJPanel::isValidString, StringUtils::isNotBlank)) {
			//
			if (!Objects.equals(whole, "0")) {
				//
				final int index = StringUtils.indexOf(whole, '.');
				//
				final String string = testAndApply(i -> i >= 0, index, i -> StringUtils.substring(whole, i + 1), null);
				//
				testAndRun(
						Boolean.logicalAnd(index >= 0,
								Boolean.logicalOr(StringUtils.isEmpty(string), matches(string, "^0+$"))),
						() -> sb.append(String.format(format, "mi", StringUtils.substring(whole, 0, index))),
						() -> sb.append(String.format(format, "mi", whole)));
				//
			} else if (startsWith(numerator, "-")) {
				//
				sb.append(String.format(format, "mi", "-"));
				//
			} // if
				//
		} // if
			//
		if (Boolean.logicalOr(and(numerator, FractionJPanel::isValidString, StringUtils::isNotBlank),
				and(denominator, FractionJPanel::isValidString, StringUtils::isNotBlank))) {
			//
			if (Boolean.logicalAnd(StringUtils.isEmpty(whole), startsWith(numerator, "-"))) {
				//
				sb.append(String.format(format, "mi", "-"));
				//
			} // if
				//
			sb.append("<mfrac>");
			//
			if (StringUtils.isNotBlank(numerator)) {
				//
				if (Boolean.logicalOr(Boolean.logicalAnd(Objects.equals(whole, "0"), startsWith(numerator, "-")),
						Boolean.logicalAnd(StringUtils.isEmpty(whole), startsWith(numerator, "-")))) {
					//
					sb.append(String.format(format, "mi", StringUtils.substring(numerator, 1)));
					//
				} else {
					//
					sb.append(String.format(format, "mi", numerator));
					//
				} // if
					//
			} // if
				//
			testAndAccept(StringUtils::isNotBlank, denominator, x -> sb.append(String.format(format, "mn", x)));
			//
			sb.append("</mfrac>");
			//
		} // if
			//
		return Objects.toString(sb.append("</math>"));
		//
	}

	private static void testAndRun(final boolean condition, final Runnable runnableTrue, final Runnable runnableFalse) {
		if (condition) {
			run(runnableTrue);
		} else {
			run(runnableFalse);
		}
	}

	private static void run(final Runnable instance) {
		if (instance != null) {
			instance.run();
		}
	}

	private static <R> R testAndApply(final IntPredicate predicate, final int value, final IntFunction<R> functionTrue,
			final IntFunction<R> functionFalse) {
		return test(predicate, value) ? apply(functionTrue, value) : apply(functionFalse, value);
	}

	private static boolean test(final IntPredicate instance, final int value) {
		return instance != null && instance.test(value);
	}

	private static <R> R apply(final IntFunction<R> instance, final int value) {
		return instance != null ? instance.apply(value) : null;
	}

	private static boolean matches(final String instance, final String regex) {
		return instance != null && isValidString(instance) && instance.matches(regex);
	}

	private static <T, E extends Exception> void testAndAccept(final Predicate<T> predicate, final T value,
			final FailableConsumer<T, E> consumer) throws E {
		if (test(predicate, value)) {
			accept(consumer, value);
		}
	}

	private static <T, E extends Exception> void accept(final FailableConsumer<T, E> instance, final T value) throws E {
		if (instance != null) {
			instance.accept(value);
		}
	}

	private static <T> boolean and(final T value, final Predicate<T> predicateA, final Predicate<T> predicateB) {
		return test(predicateA, value) && test(predicateB, value);
	}

	private static <T> boolean and(final T value, final Predicate<T> predicateA, final Predicate<T> predicateB,
			final Predicate<T> predicateC) {
		return test(predicateA, value) && test(predicateB, value) && test(predicateC, value);
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
		if (bdWhole != null) {
			//
			if (contains(toPlainString(bdWhole.stripTrailingZeros()), ".")) {
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
			} else if (StringUtils.isEmpty(numerator) && StringUtils.isEmpty(denominator)) {
				//
				return Fraction.getFraction(whole);
				//
			} // if
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

	private static <T, R, E extends Exception> R testAndApply(final Predicate<T> predicate, final T value,
			final FailableFunction<T, R, E> functionTrue, final FailableFunction<T, R, E> functionFalse) throws E {
		return test(predicate, value) ? apply(functionTrue, value) : apply(functionFalse, value);
	}

	private static <T> boolean test(final Predicate<T> instance, final T value) {
		return instance != null && instance.test(value);
	}

	private static <T, R, E extends Exception> R apply(final FailableFunction<T, R, E> instance, final T value)
			throws E {
		return instance != null ? instance.apply(value) : null;
	}

	@Override
	public int selectionForKey(final char aKey, final ComboBoxModel<?> aModel) {
		//
		final Iterable<Entry<Integer, Member>> entrySet = entrySet(collect(
				mapToObj(IntStream.range(0, getSize(aModel)),
						i -> Pair.of(Integer.valueOf(i), cast(Member.class, getElementAt(aModel, i)))),
				LinkedHashMap::new, (a, b) -> put(a, getKey(b), getValue(b)), Map::putAll));
		//
		final Integer integer = testAndApply(x -> IterableUtils.size(x) == 1, toList(map(
				filter(StreamSupport.stream(spliterator(entrySet), false),
						x -> getValue(x) != null && getName(getValue(x)) != null
								&& StringUtils.isNotEmpty(getName(getValue(x)))
								&& (getName(getValue(x)).charAt(0) == aKey
										|| (StringUtils.upperCase(getName(getValue(x))) != null
												&& StringUtils.upperCase(getName(getValue(x))).charAt(0) == aKey))),
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
		forEach(Arrays.asList(cbChopImage, tfFontSize, jcbColor, btnShowImage, btnCopyImage, btnSaveImage, btnSavePdf,
				jcbFileSuffix), x -> setEnabled(x, false));
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
		btnExecuteSetEnabled();
		//
		pack(window);
		//
	}

	private void btnExecuteSetEnabled() {
		//
		try {
			//
			setEnabled(btnExecute, toFraction(fraction1) != null && toFraction(fraction2) != null
					&& getSelectedItem(cbmMethod) != null);
			//
		} catch (final Exception e) {
			//
			setEnabled(btnExecute, false);
			//
		} // try
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

	@Override
	public void itemStateChanged(final ItemEvent evt) {
		//
		btnExecuteSetEnabled();
		//
		if (Objects.equals(getSource(evt), jcbMethod)) {
			//
			forEach(Arrays.asList(FractionJTextComponent.getWhole(answer), FractionJTextComponent.getNumerator(answer),
					FractionJTextComponent.getDenominator(answer)), x -> setText(x, ""));
			//
			setIcon(labelImage, null);
			//
			forEach(Arrays.asList(cbChopImage, tfFontSize, jcbColor, btnShowImage, btnCopyImage, btnSaveImage,
					btnSavePdf), x -> setEnabled(x, false));
			//
		} // if
			//
	}

}