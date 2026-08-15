package org.apache.commons.lang3.math;

import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.swing.AbstractButton;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.WindowConstants;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.TriFunction;
import org.meeuw.functional.TriPredicate;

import io.github.toolfactory.narcissus.Narcissus;
import net.miginfocom.swing.MigLayout;

public class FractionJPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1238012263601647765L;

	private static final String WMIN = "wmin";

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

	private AbstractButton btnExecute = null;

	private FractionJPanel() {
		//
	}

	private void init() {
		//
		setLayout(new MigLayout("debug"));
		//
		JPanel jPanel = new JPanel();
		//
		jPanel.setLayout(new MigLayout());
		//
		final int wmin = 50;
		//
		jPanel.add((fraction1 = new FractionJTextComponent()).getWhole(), String.format("wmin %1$s,spany 2", wmin));
		//
		jPanel.add(fraction1.getNumerator(), String.format("wmin %1$s,wrap", wmin));
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
		final List<Method> ms = Arrays.stream(Fraction.class.getMethods())
				.filter(m -> m != null && Arrays.equals(m.getParameterTypes(), new Class<?>[] { m.getDeclaringClass() })
						&& Objects.equals(m.getReturnType(), m.getDeclaringClass()))
				.toList();
		//
		final JComboBox<Method> jcb = new JComboBox<>(cbm = new DefaultComboBoxModel<>(ms.toArray(Method[]::new)));
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
		add(jcb);
		//
		(jPanel = new JPanel()).setLayout(new MigLayout());
		//
		jPanel.add((fraction2 = new FractionJTextComponent()).getWhole(), String.format("wmin %1$s,spany 2", wmin));
		//
		jPanel.add(fraction2.getNumerator(), String.format("wmin %1$s,wrap", wmin));
		//
		jPanel.add(fraction2.getDenominator(), StringUtils.joinWith(" ", WMIN, wmin));
		//
		add(jPanel);
		//
		add(btnExecute = new JButton("="));
		//
		btnExecute.addActionListener(this);
		//
		(jPanel = new JPanel()).setLayout(new MigLayout());
		//
		jPanel.add((answer = new FractionJTextComponent()).getWhole(), String.format("wmin %1$s,spany 2", wmin));
		//
		jPanel.add(answer.getNumerator(), String.format("wmin %1$s,wrap", wmin));
		//
		jPanel.add(answer.getDenominator(), StringUtils.joinWith(" ", WMIN, wmin));
		//
		add(jPanel);
		//
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
		if (Objects.equals(getSource(evt), btnExecute)) {
			//
			final Fraction fractionA = testAndApply((a, b, c) -> Boolean.logicalAnd(a != null, b != null) && c != null,
					testAndApply(NumberUtils::isParsable, getText(FractionJTextComponent.getWhole(fraction1)),
							NumberUtils::toInt, null),
					testAndApply(NumberUtils::isParsable, getText(FractionJTextComponent.getNumerator(fraction1)),
							NumberUtils::toInt, null),
					testAndApply(NumberUtils::isParsable, getText(FractionJTextComponent.getDenominator(fraction1)),
							NumberUtils::toInt, null),
					Fraction::getFraction, null);
			//
			final Fraction fractionB = testAndApply((a, b, c) -> Boolean.logicalAnd(a != null, b != null) && c != null,
					testAndApply(NumberUtils::isParsable, getText(FractionJTextComponent.getWhole(fraction2)),
							NumberUtils::toInt, null),
					testAndApply(NumberUtils::isParsable, getText(FractionJTextComponent.getNumerator(fraction2)),
							NumberUtils::toInt, null),
					testAndApply(NumberUtils::isParsable, getText(FractionJTextComponent.getDenominator(fraction2)),
							NumberUtils::toInt, null),
					Fraction::getFraction, null);
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
		} // if
			//
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

	private static <T, R> R testAndApply(final TriPredicate<T, T, T> predicate, final T a, final T b, final T c,
			final TriFunction<T, T, T, R> functionTrue, final TriFunction<T, T, T, R> functionFalse) {
		return predicate != null && predicate.test(a, b, c) ? apply(functionTrue, a, b, c)
				: apply(functionFalse, a, b, c);
	}

	private static <T, U, V, R> R apply(final TriFunction<T, U, V, R> instance, final T t, final U u, final V v) {
		return instance != null ? instance.apply(t, u, v) : null;
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

}