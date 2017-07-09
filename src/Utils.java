import java.util.List;
import java.util.Map;

public class Utils {

	public static String arrayToStringRegex(String[] array, String separator) {
		String str = "";
		int i = 0;
		for (; i < array.length - 1; i++) {
			if (array[i].equals("+"))
				str += "\\";
			str += array[i] + separator;
		}
		str += array[i];
		return str;
	}

	public static boolean arrayContains(String[] array, String str) {
		for (String s : array)
			if (s.equals(str))
				return true;
		return false;
	}

	public static String removeExpressions(String str, String[] array) {
		for (String expr : array)
			str = str.replace(expr, "");
		return str;
	}

	// checks if two lists have the same elements
	public static boolean equalLists(List<String> list1, List<String> list2) {
		return list1.containsAll(list2) && list2.containsAll(list1);
	}

	public static Object getKeyFromValue(@SuppressWarnings("rawtypes") Map map, String value) {
		for (Object o : map.keySet())
			if (map.get(o).equals(value))
				return o;
		return -1;
	}
}
