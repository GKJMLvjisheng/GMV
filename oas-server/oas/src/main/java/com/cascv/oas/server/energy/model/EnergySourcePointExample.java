package com.cascv.oas.server.energy.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EnergySourcePointExample {
    protected String orderByClause;
    protected boolean distinct;
    protected List<Criteria> oredCriteria;

    public EnergySourcePointExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSourceCodeIsNull() {
            addCriterion("source_code is null");
            return (Criteria) this;
        }

        public Criteria andSourceCodeIsNotNull() {
            addCriterion("source_code is not null");
            return (Criteria) this;
        }

        public Criteria andSourceCodeEqualTo(Integer value) {
            addCriterion("source_code =", value, "sourceCode");
            return (Criteria) this;
        }

        public Criteria andSourceCodeNotEqualTo(Integer value) {
            addCriterion("source_code <>", value, "sourceCode");
            return (Criteria) this;
        }

        public Criteria andSourceCodeGreaterThan(Integer value) {
            addCriterion("source_code >", value, "sourceCode");
            return (Criteria) this;
        }

        public Criteria andSourceCodeGreaterThanOrEqualTo(Integer value) {
            addCriterion("source_code >=", value, "sourceCode");
            return (Criteria) this;
        }

        public Criteria andSourceCodeLessThan(Integer value) {
            addCriterion("source_code <", value, "sourceCode");
            return (Criteria) this;
        }

        public Criteria andSourceCodeLessThanOrEqualTo(Integer value) {
            addCriterion("source_code <=", value, "sourceCode");
            return (Criteria) this;
        }

        public Criteria andSourceCodeIn(List<Integer> values) {
            addCriterion("source_code in", values, "sourceCode");
            return (Criteria) this;
        }

        public Criteria andSourceCodeNotIn(List<Integer> values) {
            addCriterion("source_code not in", values, "sourceCode");
            return (Criteria) this;
        }

        public Criteria andSourceCodeBetween(Integer value1, Integer value2) {
            addCriterion("source_code between", value1, value2, "sourceCode");
            return (Criteria) this;
        }

        public Criteria andSourceCodeNotBetween(Integer value1, Integer value2) {
            addCriterion("source_code not between", value1, value2, "sourceCode");
            return (Criteria) this;
        }

        public Criteria andSourceNameIsNull() {
            addCriterion("source_name is null");
            return (Criteria) this;
        }

        public Criteria andSourceNameIsNotNull() {
            addCriterion("source_name is not null");
            return (Criteria) this;
        }

        public Criteria andSourceNameEqualTo(String value) {
            addCriterion("source_name =", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotEqualTo(String value) {
            addCriterion("source_name <>", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameGreaterThan(String value) {
            addCriterion("source_name >", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameGreaterThanOrEqualTo(String value) {
            addCriterion("source_name >=", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameLessThan(String value) {
            addCriterion("source_name <", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameLessThanOrEqualTo(String value) {
            addCriterion("source_name <=", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameLike(String value) {
            addCriterion("source_name like", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotLike(String value) {
            addCriterion("source_name not like", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameIn(List<String> values) {
            addCriterion("source_name in", values, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotIn(List<String> values) {
            addCriterion("source_name not in", values, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameBetween(String value1, String value2) {
            addCriterion("source_name between", value1, value2, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotBetween(String value1, String value2) {
            addCriterion("source_name not between", value1, value2, "sourceName");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("type like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("type not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andPointSingleIsNull() {
            addCriterion("point_single is null");
            return (Criteria) this;
        }

        public Criteria andPointSingleIsNotNull() {
            addCriterion("point_single is not null");
            return (Criteria) this;
        }

        public Criteria andPointSingleEqualTo(Integer value) {
            addCriterion("point_single =", value, "pointSingle");
            return (Criteria) this;
        }

        public Criteria andPointSingleNotEqualTo(Integer value) {
            addCriterion("point_single <>", value, "pointSingle");
            return (Criteria) this;
        }

        public Criteria andPointSingleGreaterThan(Integer value) {
            addCriterion("point_single >", value, "pointSingle");
            return (Criteria) this;
        }

        public Criteria andPointSingleGreaterThanOrEqualTo(Integer value) {
            addCriterion("point_single >=", value, "pointSingle");
            return (Criteria) this;
        }

        public Criteria andPointSingleLessThan(Integer value) {
            addCriterion("point_single <", value, "pointSingle");
            return (Criteria) this;
        }

        public Criteria andPointSingleLessThanOrEqualTo(Integer value) {
            addCriterion("point_single <=", value, "pointSingle");
            return (Criteria) this;
        }

        public Criteria andPointSingleIn(List<Integer> values) {
            addCriterion("point_single in", values, "pointSingle");
            return (Criteria) this;
        }

        public Criteria andPointSingleNotIn(List<Integer> values) {
            addCriterion("point_single not in", values, "pointSingle");
            return (Criteria) this;
        }

        public Criteria andPointSingleBetween(Integer value1, Integer value2) {
            addCriterion("point_single between", value1, value2, "pointSingle");
            return (Criteria) this;
        }

        public Criteria andPointSingleNotBetween(Integer value1, Integer value2) {
            addCriterion("point_single not between", value1, value2, "pointSingle");
            return (Criteria) this;
        }

        public Criteria andPointTotalIsNull() {
            addCriterion("point_total is null");
            return (Criteria) this;
        }

        public Criteria andPointTotalIsNotNull() {
            addCriterion("point_total is not null");
            return (Criteria) this;
        }

        public Criteria andPointTotalEqualTo(Integer value) {
            addCriterion("point_total =", value, "pointTotal");
            return (Criteria) this;
        }

        public Criteria andPointTotalNotEqualTo(Integer value) {
            addCriterion("point_total <>", value, "pointTotal");
            return (Criteria) this;
        }

        public Criteria andPointTotalGreaterThan(Integer value) {
            addCriterion("point_total >", value, "pointTotal");
            return (Criteria) this;
        }

        public Criteria andPointTotalGreaterThanOrEqualTo(Integer value) {
            addCriterion("point_total >=", value, "pointTotal");
            return (Criteria) this;
        }

        public Criteria andPointTotalLessThan(Integer value) {
            addCriterion("point_total <", value, "pointTotal");
            return (Criteria) this;
        }

        public Criteria andPointTotalLessThanOrEqualTo(Integer value) {
            addCriterion("point_total <=", value, "pointTotal");
            return (Criteria) this;
        }

        public Criteria andPointTotalIn(List<Integer> values) {
            addCriterion("point_total in", values, "pointTotal");
            return (Criteria) this;
        }

        public Criteria andPointTotalNotIn(List<Integer> values) {
            addCriterion("point_total not in", values, "pointTotal");
            return (Criteria) this;
        }

        public Criteria andPointTotalBetween(Integer value1, Integer value2) {
            addCriterion("point_total between", value1, value2, "pointTotal");
            return (Criteria) this;
        }

        public Criteria andPointTotalNotBetween(Integer value1, Integer value2) {
            addCriterion("point_total not between", value1, value2, "pointTotal");
            return (Criteria) this;
        }

        public Criteria andPointIncreaseSpeedIsNull() {
            addCriterion("point_increase_speed is null");
            return (Criteria) this;
        }

        public Criteria andPointIncreaseSpeedIsNotNull() {
            addCriterion("point_increase_speed is not null");
            return (Criteria) this;
        }

        public Criteria andPointIncreaseSpeedEqualTo(BigDecimal value) {
            addCriterion("point_increase_speed =", value, "pointIncreaseSpeed");
            return (Criteria) this;
        }

        public Criteria andPointIncreaseSpeedNotEqualTo(BigDecimal value) {
            addCriterion("point_increase_speed <>", value, "pointIncreaseSpeed");
            return (Criteria) this;
        }

        public Criteria andPointIncreaseSpeedGreaterThan(BigDecimal value) {
            addCriterion("point_increase_speed >", value, "pointIncreaseSpeed");
            return (Criteria) this;
        }

        public Criteria andPointIncreaseSpeedGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("point_increase_speed >=", value, "pointIncreaseSpeed");
            return (Criteria) this;
        }

        public Criteria andPointIncreaseSpeedLessThan(BigDecimal value) {
            addCriterion("point_increase_speed <", value, "pointIncreaseSpeed");
            return (Criteria) this;
        }

        public Criteria andPointIncreaseSpeedLessThanOrEqualTo(BigDecimal value) {
            addCriterion("point_increase_speed <=", value, "pointIncreaseSpeed");
            return (Criteria) this;
        }

        public Criteria andPointIncreaseSpeedIn(List<BigDecimal> values) {
            addCriterion("point_increase_speed in", values, "pointIncreaseSpeed");
            return (Criteria) this;
        }

        public Criteria andPointIncreaseSpeedNotIn(List<BigDecimal> values) {
            addCriterion("point_increase_speed not in", values, "pointIncreaseSpeed");
            return (Criteria) this;
        }

        public Criteria andPointIncreaseSpeedBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("point_increase_speed between", value1, value2, "pointIncreaseSpeed");
            return (Criteria) this;
        }

        public Criteria andPointIncreaseSpeedNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("point_increase_speed not between", value1, value2, "pointIncreaseSpeed");
            return (Criteria) this;
        }

        public Criteria andPointEachBallIsNull() {
            addCriterion("point_each_ball is null");
            return (Criteria) this;
        }

        public Criteria andPointEachBallIsNotNull() {
            addCriterion("point_each_ball is not null");
            return (Criteria) this;
        }

        public Criteria andPointEachBallEqualTo(Integer value) {
            addCriterion("point_each_ball =", value, "pointEachBall");
            return (Criteria) this;
        }

        public Criteria andPointEachBallNotEqualTo(Integer value) {
            addCriterion("point_each_ball <>", value, "pointEachBall");
            return (Criteria) this;
        }

        public Criteria andPointEachBallGreaterThan(Integer value) {
            addCriterion("point_each_ball >", value, "pointEachBall");
            return (Criteria) this;
        }

        public Criteria andPointEachBallGreaterThanOrEqualTo(Integer value) {
            addCriterion("point_each_ball >=", value, "pointEachBall");
            return (Criteria) this;
        }

        public Criteria andPointEachBallLessThan(Integer value) {
            addCriterion("point_each_ball <", value, "pointEachBall");
            return (Criteria) this;
        }

        public Criteria andPointEachBallLessThanOrEqualTo(Integer value) {
            addCriterion("point_each_ball <=", value, "pointEachBall");
            return (Criteria) this;
        }

        public Criteria andPointEachBallIn(List<Integer> values) {
            addCriterion("point_each_ball in", values, "pointEachBall");
            return (Criteria) this;
        }

        public Criteria andPointEachBallNotIn(List<Integer> values) {
            addCriterion("point_each_ball not in", values, "pointEachBall");
            return (Criteria) this;
        }

        public Criteria andPointEachBallBetween(Integer value1, Integer value2) {
            addCriterion("point_each_ball between", value1, value2, "pointEachBall");
            return (Criteria) this;
        }

        public Criteria andPointEachBallNotBetween(Integer value1, Integer value2) {
            addCriterion("point_each_ball not between", value1, value2, "pointEachBall");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table energy_source_point
     *
     * @mbg.generated do_not_delete_during_merge Wed Aug 22 11:03:27 CST 2018
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table energy_source_point
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}