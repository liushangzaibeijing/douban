package com.xiu.crawling.douban.bean;

import java.util.ArrayList;
import java.util.List;

public class BaiDuXueSuInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BaiDuXueSuInfoExample() {
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

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andAuthorIsNull() {
            addCriterion("author is null");
            return (Criteria) this;
        }

        public Criteria andAuthorIsNotNull() {
            addCriterion("author is not null");
            return (Criteria) this;
        }

        public Criteria andAuthorEqualTo(String value) {
            addCriterion("author =", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorNotEqualTo(String value) {
            addCriterion("author <>", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorGreaterThan(String value) {
            addCriterion("author >", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorGreaterThanOrEqualTo(String value) {
            addCriterion("author >=", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorLessThan(String value) {
            addCriterion("author <", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorLessThanOrEqualTo(String value) {
            addCriterion("author <=", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorLike(String value) {
            addCriterion("author like", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorNotLike(String value) {
            addCriterion("author not like", value, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorIn(List<String> values) {
            addCriterion("author in", values, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorNotIn(List<String> values) {
            addCriterion("author not in", values, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorBetween(String value1, String value2) {
            addCriterion("author between", value1, value2, "author");
            return (Criteria) this;
        }

        public Criteria andAuthorNotBetween(String value1, String value2) {
            addCriterion("author not between", value1, value2, "author");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoIsNull() {
            addCriterion("abstract_info is null");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoIsNotNull() {
            addCriterion("abstract_info is not null");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoEqualTo(String value) {
            addCriterion("abstract_info =", value, "abstractInfo");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoNotEqualTo(String value) {
            addCriterion("abstract_info <>", value, "abstractInfo");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoGreaterThan(String value) {
            addCriterion("abstract_info >", value, "abstractInfo");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoGreaterThanOrEqualTo(String value) {
            addCriterion("abstract_info >=", value, "abstractInfo");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoLessThan(String value) {
            addCriterion("abstract_info <", value, "abstractInfo");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoLessThanOrEqualTo(String value) {
            addCriterion("abstract_info <=", value, "abstractInfo");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoLike(String value) {
            addCriterion("abstract_info like", value, "abstractInfo");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoNotLike(String value) {
            addCriterion("abstract_info not like", value, "abstractInfo");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoIn(List<String> values) {
            addCriterion("abstract_info in", values, "abstractInfo");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoNotIn(List<String> values) {
            addCriterion("abstract_info not in", values, "abstractInfo");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoBetween(String value1, String value2) {
            addCriterion("abstract_info between", value1, value2, "abstractInfo");
            return (Criteria) this;
        }

        public Criteria andAbstractInfoNotBetween(String value1, String value2) {
            addCriterion("abstract_info not between", value1, value2, "abstractInfo");
            return (Criteria) this;
        }

        public Criteria andKeywordIsNull() {
            addCriterion("keyword is null");
            return (Criteria) this;
        }

        public Criteria andKeywordIsNotNull() {
            addCriterion("keyword is not null");
            return (Criteria) this;
        }

        public Criteria andKeywordEqualTo(String value) {
            addCriterion("keyword =", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotEqualTo(String value) {
            addCriterion("keyword <>", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordGreaterThan(String value) {
            addCriterion("keyword >", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordGreaterThanOrEqualTo(String value) {
            addCriterion("keyword >=", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLessThan(String value) {
            addCriterion("keyword <", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLessThanOrEqualTo(String value) {
            addCriterion("keyword <=", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLike(String value) {
            addCriterion("keyword like", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotLike(String value) {
            addCriterion("keyword not like", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordIn(List<String> values) {
            addCriterion("keyword in", values, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotIn(List<String> values) {
            addCriterion("keyword not in", values, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordBetween(String value1, String value2) {
            addCriterion("keyword between", value1, value2, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotBetween(String value1, String value2) {
            addCriterion("keyword not between", value1, value2, "keyword");
            return (Criteria) this;
        }

        public Criteria andCitedIsNull() {
            addCriterion("cited is null");
            return (Criteria) this;
        }

        public Criteria andCitedIsNotNull() {
            addCriterion("cited is not null");
            return (Criteria) this;
        }

        public Criteria andCitedEqualTo(String value) {
            addCriterion("cited =", value, "cited");
            return (Criteria) this;
        }

        public Criteria andCitedNotEqualTo(String value) {
            addCriterion("cited <>", value, "cited");
            return (Criteria) this;
        }

        public Criteria andCitedGreaterThan(String value) {
            addCriterion("cited >", value, "cited");
            return (Criteria) this;
        }

        public Criteria andCitedGreaterThanOrEqualTo(String value) {
            addCriterion("cited >=", value, "cited");
            return (Criteria) this;
        }

        public Criteria andCitedLessThan(String value) {
            addCriterion("cited <", value, "cited");
            return (Criteria) this;
        }

        public Criteria andCitedLessThanOrEqualTo(String value) {
            addCriterion("cited <=", value, "cited");
            return (Criteria) this;
        }

        public Criteria andCitedLike(String value) {
            addCriterion("cited like", value, "cited");
            return (Criteria) this;
        }

        public Criteria andCitedNotLike(String value) {
            addCriterion("cited not like", value, "cited");
            return (Criteria) this;
        }

        public Criteria andCitedIn(List<String> values) {
            addCriterion("cited in", values, "cited");
            return (Criteria) this;
        }

        public Criteria andCitedNotIn(List<String> values) {
            addCriterion("cited not in", values, "cited");
            return (Criteria) this;
        }

        public Criteria andCitedBetween(String value1, String value2) {
            addCriterion("cited between", value1, value2, "cited");
            return (Criteria) this;
        }

        public Criteria andCitedNotBetween(String value1, String value2) {
            addCriterion("cited not between", value1, value2, "cited");
            return (Criteria) this;
        }

        public Criteria andPublicYearIsNull() {
            addCriterion("public_year is null");
            return (Criteria) this;
        }

        public Criteria andPublicYearIsNotNull() {
            addCriterion("public_year is not null");
            return (Criteria) this;
        }

        public Criteria andPublicYearEqualTo(String value) {
            addCriterion("public_year =", value, "publicYear");
            return (Criteria) this;
        }

        public Criteria andPublicYearNotEqualTo(String value) {
            addCriterion("public_year <>", value, "publicYear");
            return (Criteria) this;
        }

        public Criteria andPublicYearGreaterThan(String value) {
            addCriterion("public_year >", value, "publicYear");
            return (Criteria) this;
        }

        public Criteria andPublicYearGreaterThanOrEqualTo(String value) {
            addCriterion("public_year >=", value, "publicYear");
            return (Criteria) this;
        }

        public Criteria andPublicYearLessThan(String value) {
            addCriterion("public_year <", value, "publicYear");
            return (Criteria) this;
        }

        public Criteria andPublicYearLessThanOrEqualTo(String value) {
            addCriterion("public_year <=", value, "publicYear");
            return (Criteria) this;
        }

        public Criteria andPublicYearLike(String value) {
            addCriterion("public_year like", value, "publicYear");
            return (Criteria) this;
        }

        public Criteria andPublicYearNotLike(String value) {
            addCriterion("public_year not like", value, "publicYear");
            return (Criteria) this;
        }

        public Criteria andPublicYearIn(List<String> values) {
            addCriterion("public_year in", values, "publicYear");
            return (Criteria) this;
        }

        public Criteria andPublicYearNotIn(List<String> values) {
            addCriterion("public_year not in", values, "publicYear");
            return (Criteria) this;
        }

        public Criteria andPublicYearBetween(String value1, String value2) {
            addCriterion("public_year between", value1, value2, "publicYear");
            return (Criteria) this;
        }

        public Criteria andPublicYearNotBetween(String value1, String value2) {
            addCriterion("public_year not between", value1, value2, "publicYear");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameIsNull() {
            addCriterion("periodical_name is null");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameIsNotNull() {
            addCriterion("periodical_name is not null");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameEqualTo(String value) {
            addCriterion("periodical_name =", value, "periodicalName");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameNotEqualTo(String value) {
            addCriterion("periodical_name <>", value, "periodicalName");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameGreaterThan(String value) {
            addCriterion("periodical_name >", value, "periodicalName");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameGreaterThanOrEqualTo(String value) {
            addCriterion("periodical_name >=", value, "periodicalName");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameLessThan(String value) {
            addCriterion("periodical_name <", value, "periodicalName");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameLessThanOrEqualTo(String value) {
            addCriterion("periodical_name <=", value, "periodicalName");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameLike(String value) {
            addCriterion("periodical_name like", value, "periodicalName");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameNotLike(String value) {
            addCriterion("periodical_name not like", value, "periodicalName");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameIn(List<String> values) {
            addCriterion("periodical_name in", values, "periodicalName");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameNotIn(List<String> values) {
            addCriterion("periodical_name not in", values, "periodicalName");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameBetween(String value1, String value2) {
            addCriterion("periodical_name between", value1, value2, "periodicalName");
            return (Criteria) this;
        }

        public Criteria andPeriodicalNameNotBetween(String value1, String value2) {
            addCriterion("periodical_name not between", value1, value2, "periodicalName");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

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