package Mapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BindValue {

	// ひとまず
	// Double,Float,Integer,Long,Date,Stringのバインドに対応

	public static void setParam(int index, Object object, Class<?> class1, PreparedStatement ps) throws SQLException {
		if (class1 == Double.class) {
			ps.setDouble(index, (Double) object);

		} else if (class1 == Float.class) {
			ps.setFloat(index, (Float) object);

		} else if (class1 == Integer.class) {
			ps.setInt(index, (Integer) object);

		} else if (class1 == Long.class) {
			ps.setLong(index, (Long) object);

		} else if (class1 == String.class) {
			ps.setString(index, (String) object);

		} else if (class1 == java.util.Date.class) {
			java.util.Date d1 = (java.util.Date)object;
			Date d2 = new Date(d1.getTime());
			ps.setDate(index, d2);
		}

	}
}
