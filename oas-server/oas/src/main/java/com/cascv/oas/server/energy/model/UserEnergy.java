package com.cascv.oas.server.energy.model;

public class UserEnergy {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_energy.user_id
     *
     * @mbg.generated Mon Aug 27 18:14:38 CST 2018
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_energy.current_point
     *
     * @mbg.generated Mon Aug 27 18:14:38 CST 2018
     */
    private Integer currentPoint;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_energy.current_power
     *
     * @mbg.generated Mon Aug 27 18:14:38 CST 2018
     */
    private Integer currentPower;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_energy.user_id
     *
     * @return the value of user_energy.user_id
     *
     * @mbg.generated Mon Aug 27 18:14:38 CST 2018
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_energy.user_id
     *
     * @param userId the value for user_energy.user_id
     *
     * @mbg.generated Mon Aug 27 18:14:38 CST 2018
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_energy.current_point
     *
     * @return the value of user_energy.current_point
     *
     * @mbg.generated Mon Aug 27 18:14:38 CST 2018
     */
    public Integer getCurrentPoint() {
        return currentPoint;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_energy.current_point
     *
     * @param currentPoint the value for user_energy.current_point
     *
     * @mbg.generated Mon Aug 27 18:14:38 CST 2018
     */
    public void setCurrentPoint(Integer currentPoint) {
        this.currentPoint = currentPoint;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_energy.current_power
     *
     * @return the value of user_energy.current_power
     *
     * @mbg.generated Mon Aug 27 18:14:38 CST 2018
     */
    public Integer getCurrentPower() {
        return currentPower;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_energy.current_power
     *
     * @param currentPower the value for user_energy.current_power
     *
     * @mbg.generated Mon Aug 27 18:14:38 CST 2018
     */
    public void setCurrentPower(Integer currentPower) {
        this.currentPower = currentPower;
    }
}