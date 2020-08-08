package me.vuroz.luroz;

public class Misc {
	
	public static String capitalize(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
	
	public static String fixedLength(String string, int length) {
		return String.format("%" + length + "s", string);
	}
	
	public static String[] splitStringEvery(String string, int interval) {
	    int arrayLength = (int) Math.ceil(((string.length() / (double)interval)));
	    String[] result = new String[arrayLength];

	    int j = 0;
	    int lastIndex = result.length - 1;
	    for (int i = 0; i < lastIndex; i++) {
	        result[i] = string.substring(j, j + interval);
	        j += interval;
	    }
	    result[lastIndex] = string.substring(j);

	    return result;
	}

}
