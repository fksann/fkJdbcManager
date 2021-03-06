package Mapper;

import java.util.ArrayList;
import java.util.List;

public class Where {

	protected String whereStr;
	protected List<String> valueList = new ArrayList<String>();

	public Where() {
	}

	public Where and(Where... whereS) {
		combineWhereClause(" and ", whereS);
		return this;
	}

	public Where or(Where... whereS) {
		combineWhereClause(" or ", whereS);
		return this;
	}

	public Where eq(String columnName, String value) {
		addCondition(ConditionType.EQ, columnName, value);
		valueList.add(value);
		return this;
	}

	public Where ne(String columnName, String value) {
		addCondition(ConditionType.NE, columnName, value);
		valueList.add(value);
		return this;
	}

	public Where lt(String columnName, String value) {
		addCondition(ConditionType.LT, columnName, value);
		valueList.add(value);
		return this;
	}

	public Where le(String columnName, String value) {
		addCondition(ConditionType.LE, columnName, value);
		valueList.add(value);
		return this;
	}

	public Where gt(String columnName, String value) {
		addCondition(ConditionType.GT, columnName, value);
		valueList.add(value);
		return this;
	}

	public Where ge(String columnName, String value) {
		addCondition(ConditionType.GE, columnName, value);
		valueList.add(value);
		return this;
	}

	public Where in(String columnName, String values) {
		String[] value = values.split(",", 0);
		for (int i = 0; i < value.length; i++) {
			if (i == 0) {
				values = "?";
			} else {
				values += ",?";
			}
			valueList.add(value[i]);
		}
		addCondition(ConditionType.IN, columnName, values);
		return this;
	}

	public Where notIn(String columnName, String values) {
		String[] value = values.split(",", 0);
		for (int i = 0; i < value.length; i++) {
			if (i == 0) {
				values = "?";
			} else {
				values += ",?";
			}
			valueList.add(value[i]);
		}
		addCondition(ConditionType.IN, columnName, values);
		return this;
	}

	public Where like(String columnName, String value) {
		addCondition(ConditionType.LIKE, columnName, value);
		valueList.add(value);
		return this;
	}

	public Where notLike(String columnName, String value) {
		addCondition(ConditionType.NOT_LIKE, columnName, value);
		valueList.add(value);
		return this;
	}

	public Where starts(String columnName, String value) {
		addCondition(ConditionType.STARTS, columnName, value);
		valueList.add(value + "%");
		return this;
	}

	public Where notStarts(String columnName, String value) {
		addCondition(ConditionType.NOT_STARTS, columnName, value);
		valueList.add(value + "%");
		return this;
	}

	public Where ends(String columnName, String value) {
		addCondition(ConditionType.ENDS, columnName, value);
		valueList.add("%" + value);
		return this;
	}

	public Where notEnds(String columnName, String value) {
		addCondition(ConditionType.NOT_ENDS, columnName, value);
		valueList.add("%" + value);
		return this;
	}

	public Where contains(String columnName, String value) {
		addCondition(ConditionType.CONTAINS, columnName, value);
		valueList.add("%" + value + "%");
		return this;
	}

	public Where notContains(String columnName, String value) {
		addCondition(ConditionType.NOT_CONTAINS, columnName, value);
		valueList.add("%" + value + "%");
		return this;
	}

	public Where isNull(String columnName) {
		addCondition(ConditionType.IS_NULL, columnName, null);
		return this;
	}

	public Where isNotNull(String columnName) {
		addCondition(ConditionType.IS_NOT_NULL, columnName, null);
		return this;
	}

	protected void combineWhereClause(String operator, Where... whereS) {
		String whereClauses = "(";
		for (int i = 0; i < whereS.length; i++) {
			if (i == 0) {
				whereClauses += whereS[i].whereStr;
			} else {
				whereClauses += operator + whereS[i].whereStr;
			}
			valueList.addAll(whereS[i].valueList);
		}
		whereStr = " where " + whereClauses.replaceAll(" where ", "")+")";
	}

	protected void addCondition(ConditionType conditionType, String columnName, String value) {
		if (this.whereStr == null) {
			this.whereStr = " where ";
		} else {
			this.whereStr = this.whereStr + " and ";
		}
		this.whereStr = this.whereStr + conditionType.getCondition(columnName, value);
	}
}
