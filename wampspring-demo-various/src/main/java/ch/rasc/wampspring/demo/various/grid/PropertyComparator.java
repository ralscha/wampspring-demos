package ch.rasc.wampspring.demo.various.grid;

import java.util.Comparator;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class PropertyComparator<T> implements Comparator<T> {
	private final static SpelExpressionParser parser = new SpelExpressionParser();

	private final Expression readPropertyExpression;

	public PropertyComparator(String property) {
		this.readPropertyExpression = parser.parseExpression(property);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(T o1, T o2) {
		Object left = readPropertyExpression.getValue(o1);
		Object right = readPropertyExpression.getValue(o2);

		if (left == right) {
			return 0;
		}
		if (left == null) {
			return -1;
		}
		if (right == null) {
			return 1;
		}

		if (left instanceof String) {
			return ((String) left).compareToIgnoreCase((String) right);
		}

		return ((Comparable<Object>) left).compareTo(right);
	}

}
