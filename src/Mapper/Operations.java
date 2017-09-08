package Mapper;

public class Operations {

	public static String asc(String columnName) {
		return columnName + " asc";
	}

	public static String desc(String columnName) {
		return columnName + " desc";
	}

	public static String or(String condition1, String condition2) {
		return condition1 + " or " + condition2;
	}

	public static String eq(String col, String val) {
		return col + " = " + val;
	}
}