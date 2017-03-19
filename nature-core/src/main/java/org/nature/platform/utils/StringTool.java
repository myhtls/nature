package org.nature.platform.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;



public class StringTool {
	
	public final static String PATTERN_URL = "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)"
			+ "(\\w+:\\w+@)?(([-\\w]+\\.)+(jpg|jpeg|png|gif|com|org|net|gov"
			+ "|mil|biz|info|mobi|name|aero|jobs|museum" + "|travel|[a-z]{2}))(:[\\d]{1,5})?"
			+ "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" + "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
			+ "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" + "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
			+ "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" + "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b";

	/**
	 * 在文本中搜索网址并用锚定替换
	 *
	 * Example: http://www.test.com returns
	 * <a href="http://www.test.com" target="_blank">http://www.test.com</a>
	 *
	 * @param message
	 * @return
	 */
	public static String replaceUrlByAnchor(String message) {
		String strRegex = "((((https?|ftp|telnet|file):((//)|(\\\\))+)|(www.))+[\\w\\d:#@%/;$()~_?\\+-=\\\\.&]*)";

		if (message != null) {
			StringBuffer str = new StringBuffer(message.length());
			Pattern pattern = Pattern.compile(strRegex);
			Matcher matcher = pattern.matcher(message);

			// Replace urls with anchor tags to the url
			while (matcher.find()) {
				String url = matcher.group(0);
				String httpURL = url;
				if (!url.substring(0, 4).equalsIgnoreCase("http") && !url.substring(0, 3).equalsIgnoreCase("ftp")
						&& !url.substring(0, 4).equalsIgnoreCase("teln")
						&& !url.substring(0, 4).equalsIgnoreCase("file")) {
					httpURL = "http://" + url;
				}

				matcher.appendReplacement(str, Matcher
						.quoteReplacement("" + "<a href=\"" + httpURL + "\" target=\"_blank\" >" + url + "</a>" + ""));
			}
			matcher.appendTail(str);
			message = str.toString();
		}
		return message;
	}

	/**
	 * 从字符串中删除HTML标记
	 *
	 * @param string
	 * @return
	 */
	public static String removeHTML(String string) {
		return string.replaceAll("\\<.*?\\>", "");
	}

	/**
	 * 仅返回 数字, "," 和 "." 字符串
	 *
	 * @param string
	 * @return
	 */
	public static String getOnlyDecimalNumbers(String string) {
		return string.replaceAll("([^0-9|^,|^.])", "");
	}

	/**
	 * 仅返回String的数字字符
	 *
	 * @param string
	 * @return
	 */
	public static String getOnlyIntegerNumbers(String string) {
		return string.replaceAll("([^0-9])", "");
	}

	/**
	 * 仅返回字符串的字母数字字符
	 *
	 * @param string
	 * @return
	 */
	public static String getAlphaNumeric(String string) {
		return string.replaceAll("[^a-z0-9A-Z -]", "");
	}

	/**
	 * 删除String和trim()多余的空格
	 * 
	 *
	 * Example: "This is a Example" return "This is a Example"
	 *
	 * @param string
	 * @return
	 */
	public static String removeDuplicateWhiteSpace(String string) {
		Pattern pattern = Pattern.compile("\\s{2,}");
		Matcher matcher = pattern.matcher(string);
		return matcher.replaceAll(" ").trim();
	}

	/**
	 * 在String中提取URL模式的字符串
	 *
	 * @param string
	 * @return
	 */
	public static String extractUrl(String string) {
		return extractByRegex(string, PATTERN_URL);
	}

	/**
	 * 通过正则表达式提取字符串
	 *
	 * @param string
	 * @param expressaoRegular
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String extractByRegex(String string, String expressaoRegular) throws PatternSyntaxException {

		String novaString = "";

		try {
			Pattern pattern = Pattern.compile(expressaoRegular);

			Matcher matcher = pattern.matcher(string);
			while (matcher.find()) {
				novaString = matcher.group();
			}
		} catch (PatternSyntaxException ex) {
		
		}

		return novaString;
	}

	/**
	 * 将字符串转换成Double
	 *
	 * @param string
	 * @return
	 * @throws NumberFormatException
	 */
	public static Double stringToNumber(String string) throws NumberFormatException {

		Double valor = null;

		if (string == null || string.trim().isEmpty()) {
			return null;
		}
		try {
			if (!string.contains(".") && !string.contains(",")) {
				return Double.valueOf(StringTool.getOnlyIntegerNumbers(string));
			}

			if (string.contains(".") && !string.contains(",")) {

				String decimals = string.substring(string.lastIndexOf("."), string.length() - 1);

				if (decimals.length() == 2 || decimals.length() == 1) {
					return Double.valueOf(string);
				} else {
					return Double.valueOf(string.replace(".", ""));
				}
			}

			if (!string.contains(".") && string.contains(",")) {
				return Double.valueOf(string.replace(",", "."));
			}

			if (string.contains(".") && string.contains(",")) {
				if (string.indexOf(".") > string.indexOf(",")) {
					return Double.valueOf(string.replace(",", ""));
				} else {
					return Double.valueOf(string.replace(".", "").replace(",", "."));
				}
			}
		} catch (NumberFormatException ex) {
			throw new NumberFormatException("Número inválido: " + string);
		}

		return valor;
	}

	/**
	 * 仅返回字母字符
	 *
	 * @param string
	 * @return
	 */
	public static String onlyAlpha(String string) {
		return string.replaceAll("[^A-Za-zçáàãâäéèêëíìîïóòõôöúùûüÇÁÀÃÂÄÉÈÊËÍÌÎÏÓÒÕÔÖÚÙÛÜ ,.]", "");
	}

	/**
	 * 将字符串转换为Camelcase
	 *
	 * @param string
	 * @return
	 */
	public static String converteToCamelCase(String string) {
		StringBuilder stringCamelCase = new StringBuilder();
		string = removeDuplicateWhiteSpace(string);

		String[] quebrada = string.split(" ");
		if (quebrada.length == 0) {
			stringCamelCase.append(string.toLowerCase());
		} else {
			for (int i = 0; i < quebrada.length; i++) {
				if (i == 0) {
					stringCamelCase.append(quebrada[i].toLowerCase());
				} else {
					stringCamelCase.append(quebrada[i].substring(0, 1).toUpperCase());
					stringCamelCase.append(quebrada[i].substring(1, quebrada[i].length()).toLowerCase());
				}
			}
		}
		return stringCamelCase.toString();
	}

	/**
	 * 用左零填充一个数字
	 *
	 * @param number
	 * @param size
	 *            size of generated String
	 * @return
	 */
	public static String fillZeros(int number, int size) {
		return fillZeros(String.valueOf(number), size);
	}

	/**
	 * 用左零填充字符串
	 *
	 * @param string
	 * @param size
	 *            size of generated String
	 * @return
	 */
	public static String fillZeros(String string, int size) {
		String value = "";
		if (string != null && !string.trim().isEmpty()) {
			value = string;
			for (int x = value.length(); x < size; x++) {
				value = "0" + value;
			}
		}
		return value;
	}

	/**
	 * 
	 * 将第一个字符串的首写小写
	 *
	 * @param string
	 * @return
	 */
	public static String getLowerFirstLetter(String string) {
		if (string.length() == 1) {
			return string.toLowerCase();
		}
		if (string.length() > 1) {
			return string.substring(0, 1).toLowerCase() + "" + string.substring(1, string.length());
		}
		return "";
	}

	/**
	 * 
	 * 将第一个字符串的首写大写
	 *
	 * @param string
	 * @return
	 */
	public static String getUpperFirstLetter(String string) {
		if (string.length() == 1) {
			return string.toUpperCase();
		}
		if (string.length() > 1) {
			return string.substring(0, 1).toUpperCase() + "" + string.substring(1, string.length());
		}
		return "";
	}

	/**
	 * 如果字符串只包含字母，则返回true
	 *
	 * @param string
	 * @return
	 */
	public static boolean isOnlyLetter(String string) {
		char[] chars = string.toCharArray();
		for (char c : chars) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}

}
