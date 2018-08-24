package com.cascv.oas.server.energy.model;

import java.math.BigDecimal;

public class EnergySourcePoint {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column energy_source_point.id
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column energy_source_point.source_code
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    private Integer sourceCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column energy_source_point.source_name
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    private String sourceName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column energy_source_point.type
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    private String type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column energy_source_point.point_single
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    private Integer pointSingle;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column energy_source_point.point_total
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    private Integer pointTotal;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column energy_source_point.point_increase_speed
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    private BigDecimal pointIncreaseSpeed;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column energy_source_point.point_each_ball
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    private Integer pointEachBall;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column energy_source_point.id
     *
     * @return the value of energy_source_point.id
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column energy_source_point.id
     *
     * @param id the value for energy_source_point.id
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column energy_source_point.source_code
     *
     * @return the value of energy_source_point.source_code
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public Integer getSourceCode() {
        return sourceCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column energy_source_point.source_code
     *
     * @param sourceCode the value for energy_source_point.source_code
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public void setSourceCode(Integer sourceCode) {
        this.sourceCode = sourceCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column energy_source_point.source_name
     *
     * @return the value of energy_source_point.source_name
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column energy_source_point.source_name
     *
     * @param sourceName the value for energy_source_point.source_name
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName == null ? null : sourceName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column energy_source_point.type
     *
     * @return the value of energy_source_point.type
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column energy_source_point.type
     *
     * @param type the value for energy_source_point.type
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column energy_source_point.point_single
     *
     * @return the value of energy_source_point.point_single
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public Integer getPointSingle() {
        return pointSingle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column energy_source_point.point_single
     *
     * @param pointSingle the value for energy_source_point.point_single
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public void setPointSingle(Integer pointSingle) {
        this.pointSingle = pointSingle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column energy_source_point.point_total
     *
     * @return the value of energy_source_point.point_total
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public Integer getPointTotal() {
        return pointTotal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column energy_source_point.point_total
     *
     * @param pointTotal the value for energy_source_point.point_total
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public void setPointTotal(Integer pointTotal) {
        this.pointTotal = pointTotal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column energy_source_point.point_increase_speed
     *
     * @return the value of energy_source_point.point_increase_speed
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public BigDecimal getPointIncreaseSpeed() {
        return pointIncreaseSpeed;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column energy_source_point.point_increase_speed
     *
     * @param pointIncreaseSpeed the value for energy_source_point.point_increase_speed
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public void setPointIncreaseSpeed(BigDecimal pointIncreaseSpeed) {
        this.pointIncreaseSpeed = pointIncreaseSpeed;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column energy_source_point.point_each_ball
     *
     * @return the value of energy_source_point.point_each_ball
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public Integer getPointEachBall() {
        return pointEachBall;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column energy_source_point.point_each_ball
     *
     * @param pointEachBall the value for energy_source_point.point_each_ball
     *
     * @mbg.generated Wed Aug 22 11:03:27 CST 2018
     */
    public void setPointEachBall(Integer pointEachBall) {
        this.pointEachBall = pointEachBall;
    }
}