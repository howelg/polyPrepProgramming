import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/*
 * two major problems:
 * if multiple () are used, not wrapping each other (like (5+2)*(3/2)), it breaks
 * negative numbers are broken lol
 */
public class Frame extends JPanel {
	
	private static final JFrame FRAME = new JFrame("Calculator");
	private static final ArrayList<JButton> numButtons = new ArrayList<>();
	private static Operation add = (num1, num2) -> num1 + num2;
	private static Operation sub = (num1, num2) -> num1 - num2;
	private static Operation mul = (factor1, factor2) -> factor1 * factor2;
	private static Operation div = (dividend, divisor) -> dividend / divisor;
	private static Operation pow = (base, ex) -> Math.pow(base, ex);
	private static volatile String equation = "";
	private static final String NON_NUMS = "+=*/^()n";
	private static JLabel output = new JLabel();
	
	static {
		int x = 0;
		int y = 0;
	    String[] text = {"7","8","9","+","-",
			               "4","5","6","*","/",
			               "1","2","3","^","(-)",
			               "0",".","DEL","(",")",
			               "ENTER"};

		for (int i = 0; i <= 20; i++) {
			JButton button = new JButton();
			if (i % 5 == 0) x = 0;
			if (i % 5 == 0) y++;
			
			x++;

			button.setBounds(x * 100, 100 + y * 100, 100, 100);
			button.setText(text[i]);
			
			if (i == 14) { // (-)
				button.addActionListener(e -> output.setText(equation));
				button.addActionListener(e -> equation += "n"); // n for negative
			} else if (i == 17) { // DEL
				button.addActionListener(e -> output.setText(equation));
				button.addActionListener(e ->  {
					if (equation.length() > 0) equation = equation.substring(0, equation.length() - 1);
				});
			} else if (i == 20) { // ENTER
				button.addActionListener(e -> equation = "");
				button.addActionListener(e -> output.setText(String.valueOf(calculate(equation))));
				button.setSize(500, 100);
			} else {
				button.addActionListener(e -> output.setText(equation));
				button.addActionListener(e -> equation += button.getText());
			}
			
			
			button.setVisible(true);
			numButtons.add(button);
		}
		
		output.setBounds(100, 100, 500, 100);
		output.setVisible(true);
	}
	
	public Frame() {
		FRAME.setSize(700, 800);
		this.setLayout(null);
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME.setResizable(false);
		for (JButton button : numButtons) {
			this.add(button);
		}
		this.add(output);
		FRAME.add(this);
		FRAME.setVisible(true);
	}
	
	private static double calculate(String equation) { // RAHH RECURSION
		String newEquation = "";

		// PEMDAS
		if (equation.contains("(")) {
			newEquation = equation.substring(equation.lastIndexOf("(") + 1, equation.indexOf(")"));
			equation = equation.replace("(" + newEquation + ")", 
					String.valueOf(calculate(newEquation)));
			return calculate(equation);
		} else if (equation.contains("^")) {
			newEquation = individualExpression(equation, "^");
			// wtf did I create
			equation = equation.replace(newEquation, 
					String.valueOf(pow.op(Double.parseDouble(newEquation.substring(0, newEquation.indexOf("^"))), 
					Double.parseDouble(newEquation.substring(newEquation.indexOf("^") + 1)))));
			return calculate(equation);
		} else if (equation.contains("*")) {
			newEquation = individualExpression(equation, "*");
			equation = equation.replace(newEquation, 
					String.valueOf(mul.op(Double.parseDouble(newEquation.substring(0, newEquation.indexOf("*"))), 
					Double.parseDouble(newEquation.substring(newEquation.indexOf("*") + 1)))));
			return calculate(equation);
		} else if (equation.contains("/")) {
			newEquation = individualExpression(equation, "/");
			equation = equation.replace(newEquation, 
					String.valueOf(div.op(Double.parseDouble(newEquation.substring(0, newEquation.indexOf("/"))), 
					Double.parseDouble(newEquation.substring(newEquation.indexOf("/") + 1)))));
			return calculate(equation);
		} else if (equation.contains("+")) {
			newEquation = individualExpression(equation, "+");
			equation = equation.replace(newEquation, 
					String.valueOf(add.op(Double.parseDouble(newEquation.substring(0, newEquation.indexOf("+"))), 
					Double.parseDouble(newEquation.substring(newEquation.indexOf("+") + 1)))));
			return calculate(equation);
		} else if (equation.contains("-")) {
			newEquation = individualExpression(equation, "-");
			equation = equation.replace(newEquation, 
					String.valueOf(sub.op(Double.parseDouble(newEquation.substring(0, newEquation.indexOf("-"))), 
					Double.parseDouble(newEquation.substring(newEquation.indexOf("-") + 1)))));
			return calculate(equation);
		} else {
			try {
				return Double.parseDouble(equation);
			} catch (NumberFormatException e) {
				return Double.NaN;
			}
		}
	}
	
	private static String individualExpression(String equation, String operator) {
		String individualExpression = "";
		for (int i = equation.indexOf(operator) - 1; i >= 0; i--) {
			if (!NON_NUMS.contains(String.valueOf(equation.charAt(i)))) {
				individualExpression = String.valueOf(equation.charAt(i)) + individualExpression;
			} else {
				break;
			}
		}
		individualExpression += operator;
		for (int i = equation.indexOf(operator) + 1; i < equation.length(); i++) {
			if (!NON_NUMS.contains(String.valueOf(equation.charAt(i)))) {
				individualExpression += String.valueOf(equation.charAt(i));
			} else {
				break;
			}
		}
		return individualExpression;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawRect(100, 100, 500, 100);
	}
	
	@FunctionalInterface
	private static interface Operation {
		public double op(double num1, double num2);
	}

}
