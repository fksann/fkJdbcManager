package Mapper;

public enum ConditionType {

	EQ {

		@Override
		public String getCondition(String columnName, String value) {
			return makeCondition(columnName, value, " = ");
		}

	},

	NE {

		@Override
		public String getCondition(String columnName, String value) {
			return makeCondition(columnName, value, " <> ");
		}

	},

	LT {

		@Override
		public String getCondition(String columnName, String value) {
			return makeCondition(columnName, value, " < ");
		}

	},

	LE {

		@Override
		public String getCondition(String columnName, String value) {
			return makeCondition(columnName, value, " <= ");
		}

	},

	GT {

		@Override
		public String getCondition(String columnName, String value) {
			return makeCondition(columnName, value, " > ");
		}

	},

	GE {

		@Override
		public String getCondition(String columnName, String value) {
			return makeCondition(columnName, value, " >= ");
		}

	},

	IN {

		@Override
		public String getCondition(String columnName, String values) {
			return makeConditionForIn(columnName, values, " in ");
		}

	},

	NOT_IN {

		@Override
		public String getCondition(String columnName, String values) {
			return makeConditionForIn(columnName, values, " not in ");
		}

	},

	LIKE {

		@Override
		public String getCondition(String columnName, String value) {
			return makeConditionForLike(columnName, value, " like ");
		}

	},

	NOT_LIKE {

		@Override
		public String getCondition(String columnName, String value) {
			return makeConditionForLike(columnName, value, " not like ");
		}

	},

	STARTS {

		@Override
		public String getCondition(String columnName, String value) {
			return makeConditionForLike(columnName, value + "%", " like ");
		}

	},

	NOT_STARTS {

		@Override
		public String getCondition(String columnName, String value) {
			return makeConditionForLike(columnName, value + "%", " not like ");
		}

	},

	ENDS {

		@Override
		public String getCondition(String columnName, String value) {
			return makeConditionForLike(columnName, "%" + value, " like ");
		}

	},

	NOT_ENDS {

		@Override
		public String getCondition(String columnName, String value) {
			return makeConditionForLike(columnName, "%" + value, " not like ");
		}

	},

	CONTAINS {

		@Override
		public String getCondition(String columnName, String value) {
			return makeConditionForLike(columnName, "%" + value + "%", " like ");
		}

	},

	NOT_CONTAINS {

		@Override
		public String getCondition(String columnName, String value) {
			return makeConditionForLike(columnName, "%" + value + "%", " not like ");
		}

	},

	IS_NULL {

		@Override
		public String getCondition(String columnName, String value) {
			return makeConditionForIsNull(columnName, " is null ");
		}

	},

	IS_NOT_NULL {

		@Override
		public String getCondition(String columnName, String value) {
			return makeConditionForIsNull(columnName, " is not null ");
		}

	};

	public abstract String getCondition(String columnName, String value);

	public String makeCondition(String columnName, String value, String condition) {
		return columnName + condition + "?";
	}

	public String makeConditionForIn(String columnName, String values, String condition) {
		return columnName + condition + "(" + values + ")";
	}

	public String makeConditionForLike(String columnName, String value, String condition) {
		return columnName + condition + "?";
	}

	public String makeConditionForIsNull(String columnName, String condition) {
		return columnName + condition;
	}

}
