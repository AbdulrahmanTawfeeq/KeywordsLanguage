import java.util.ArrayList;
import java.util.Arrays;

public class KeywordsLanguage {
	private String input;
	private String result = null;
	private String[] reservedWords = new String[] { "plus", "minus", "True", "False" };
	private ArrayList<String[]> sybmbolTable = new ArrayList<String[]>();

	public KeywordsLanguage() {

	}

	public void reset() {
		input = null;
		result = null;
		sybmbolTable = new ArrayList<String[]>();
	}

	public void input(String input, int line) {
		this.input = input;
		operations(this.input, line);
	}

	private void operations(String input, int line) {
		input = input.trim();
		if (input.charAt(input.length() - 1) == ')' && input.contains("(")) {
			String function = input.split("[(]")[0].trim();
			String parameter = null;
			if (input.split("[(]")[1].trim().split("[)]").length > 0) {
				parameter = input.split("[(]")[1].trim().split("[)]")[0].trim();
			}
			switchFunctions(function, parameter, line);
		} else if (input.contains("=")) {
			String[] expr = input.split("=");
			if (expr.length >= 2) {
				if (expr[0].trim().length() > 0) {
					if (Arrays.toString(reservedWords).contains(expr[0].trim()) == false) {
						String variable = expr[0].trim();
						String value = expr[1].trim();
						if (Character.isLetter(variable.toCharArray()[0])) {
							assignVal2VarInSymbolTable(value, variable, line);
						} else {
							setResult("Error at line " + line + " : variable name must starts with a letter!");
						}
					} else {
						setResult("Error at line " + line + " : Reserved word was missused as variable name!");
					}
				} else {
					setResult("Error at line " + line + " : wrong declaring and instatiating the variable! ");
				}
			} else {
				setResult("Error at line " + line + " : wrong declaring and instatiating the variable! ");
			}
		} else {
			setResult("Error input at line " + line);
		}
	}

	private void assignVal2VarInSymbolTable(String value, String variable, int line) {
		String type = null;
		if (value.contains("plus") || value.contains("minus")) {
			String result = plusMinus(value, line);
			if (result.split(" ")[0].equals("Error")) {
				System.out.println(result);
				setResult(result);
			} else {
				type = setType(result, line);
				if (type.equals("int")) {
					result = result.split("\\.")[0];
				}
				value = result;
			}
		} else {
			type = setType(value, line);
		}
		if (type != null) {
			String[] array = { variable, type, value };
			int index = isVarExists(variable);
			if (index != -1) {
				sybmbolTable.set(index, array);
			} else {
				sybmbolTable.add(array);
			}
		}
	}

	private void switchFunctions(String function, String parameter, int line) {
		switch (function) {
		case "print":
			int index = isVarExists(parameter);
			if (index != -1) {
				setResult(sybmbolTable.get(index)[2]);
			} else if (parameter == null) {
				setResult("null");
			} else if (parameter.charAt(0) == '\'' && parameter.charAt(parameter.length() - 1) == '\'') {
				setResult(parameter.substring(1, parameter.length() - 1));
			} else if (parameter.charAt(0) != '\'' && parameter.charAt(parameter.length() - 1) != '\'') {
				setResult(plusMinus(parameter, line));
			} else {
				setResult("Error input at line " + line);
			}
			break;

		case "type":
			if (parameter == null) {
				setResult("null");
			} else {
				int index2 = isVarExists(parameter);
				if (index2 != -1) {
					setResult(getType(parameter));
				} else {
					setResult(setType(parameter, line));
				}
			}
			break;
		default:
			setResult("Error at line " + line + " Undefined function");
			break;
		}
	}

	private String plusMinus(String parameter, int line) {
		input = parameter;
		String[] tokens = input.split(" ");
		String[] numbers = new String[(int) (tokens.length / 2) + 1];
		String[] operators = new String[(int) tokens.length / 2];

		if (check(tokens, numbers, operators)) {
			double sum = Double.parseDouble(numbers[0]);
			for (int i = 1; i < numbers.length; i++) {
				if (operators[i - 1].equals("plus") && numbers[i] != null) {
					sum += Double.parseDouble(numbers[i]);
				} else if (operators[i - 1].equals("minus") && numbers[i] != null) {
					sum -= Double.parseDouble(numbers[i]);
				}
			}
			return String.valueOf(sum);
		} else {
			return "Error input at line " + line;
		}
	}

	private int isVarExists(String var) {
		int x = -1;
		for (int i = 0; i < sybmbolTable.size(); i++) {
			if (sybmbolTable.get(i)[0].equals(var)) {
				x = i;
			}
		}
		return x;
	}

	private String setType(String value, int line) {
		String type = null;
		if (value.equals("False") || value.equals("True")) {
			type = "boolean";
		} else if (value.length() == 3 && value.startsWith("'") && value.endsWith("'")
				&& Character.isLetter(value.toCharArray()[1])) {
			type = "char";
		} else if (isNumeric(value)) {
			if (value.contains(".") && !value.split("\\.")[1].equals("0")) {
				type = "double";
			} else {
				type = "int";
			}
		} else if (value.startsWith("'") && value.endsWith("'")) {
			type = "String";
		} else {
			type = "Error at line " + line + " : Undefined type!";
		}
		return type;
	}

	private String getType(String value) {
		for (int i = 0; i < sybmbolTable.size(); i++) {
			if (sybmbolTable.get(i)[0].equals(value)) {
				return sybmbolTable.get(i)[1];
			}
		}
		return null;
	}

	/**
	 * takes the array of tokens to put the numbers in the numbers
	 * array((int)(tokens.length/2)+1) and the operations in operators
	 * array((int)(tokens.length/2)). Uses {@link #isNumeric(String)}.
	 * 
	 * @param tokens
	 * @param numbers
	 * @param operators
	 * @return true in case of numerics and operators of (plus or minus), false
	 *         otherwise.
	 */
	private boolean check(String[] tokens, String[] numbers, String[] operators) {
		boolean check = true;
		int num = 0;
		int op = 0;
		for (int i = 0; i < tokens.length; i++) {
			if (i % 2 == 0) {
				if (!isNumeric(tokens[i])) {
					int value = isVarExists(tokens[i]);
					if (value != -1) {
						if (isNumeric(sybmbolTable.get(value)[2])) {
							numbers[num] = sybmbolTable.get(value)[2];
							num++;
						}
					} else {
						check = false;
						break;
					}
				} else if (isNumeric(tokens[i])) {
					numbers[num] = tokens[i];
					num++;
				}
			} else if (i % 2 == 1) {
				if (tokens[i].equals("plus") || tokens[i].equals("minus")) {
					operators[op] = new String(tokens[i]);
					op++;
				} else {
					check = false;
					break;
				}
			}
		}
		return check;
	}

	/**
	 * 
	 * @param str
	 * @return true if the str can be parsed as Double, false otherwise.
	 */
	private boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

//	public static void main(String[] args) {
//		KeywordsLanguage language = new KeywordsLanguage();
//		language.input("print(3 plus 4)");
//	}

}
