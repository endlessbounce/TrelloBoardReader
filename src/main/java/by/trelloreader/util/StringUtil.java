package by.trelloreader.util;

// StringUtil
public final class StringUtil {

	public static boolean isEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}

	public static boolean isAnyEmpty(String... s) {
		for (String string : s) {
			if (isEmpty(string)) {
				return true;
			}
		}
		return false;
	}
}
