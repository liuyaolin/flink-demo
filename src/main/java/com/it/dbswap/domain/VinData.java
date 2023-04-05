package com.it.dbswap.domain;


import com.dyuproject.protostuff.Tag;

public class VinData {
    @Tag(1)
    private String ambientPressure;
    @Tag(2)
    private String dataType;
    @Tag(3)
    private String flow_air;
    @Tag(4)
    private String flow_fuel;
    @Tag(5)
    private String fuel_left;
    @Tag(6)
    private String latitude;
    @Tag(7)
    private String longitude;
    @Tag(8)
    private String nOx_head;
    @Tag(9)
    private String nOx_rear;
    @Tag(10)
    private String pressure_DPF_diff;
    @Tag(11)
    private String range;
    @Tag(12)
    private String rmp_eng;
    @Tag(13)
    private String speed;
    @Tag(14)
    private String state_locate;
    @Tag(15)
    private String temp_SCR_in;
    @Tag(16)
    private String temp_SCR_out;
    @Tag(17)
    private String temp_coolant;
    @Tag(18)
    private String time;
    @Tag(19)
    private String torque_friction;
    @Tag(20)
    private String torque_output;
    @Tag(21)
    private String urea_left;

    public String getAmbientPressure() {
        return ambientPressure;
    }

    public void setAmbientPressure(String ambientPressure) {
        this.ambientPressure = ambientPressure;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getFlow_air() {
        return flow_air;
    }

    public void setFlow_air(String flow_air) {
        this.flow_air = flow_air;
    }

    public String getFlow_fuel() {
        return flow_fuel;
    }

    public void setFlow_fuel(String flow_fuel) {
        this.flow_fuel = flow_fuel;
    }

    public String getFuel_left() {
        return fuel_left;
    }

    public void setFuel_left(String fuel_left) {
        this.fuel_left = fuel_left;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getnOx_head() {
        return nOx_head;
    }

    public void setnOx_head(String nOx_head) {
        this.nOx_head = nOx_head;
    }

    public String getnOx_rear() {
        return nOx_rear;
    }

    public void setnOx_rear(String nOx_rear) {
        this.nOx_rear = nOx_rear;
    }

    public String getPressure_DPF_diff() {
        return pressure_DPF_diff;
    }

    public void setPressure_DPF_diff(String pressure_DPF_diff) {
        this.pressure_DPF_diff = pressure_DPF_diff;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRmp_eng() {
        return rmp_eng;
    }

    public void setRmp_eng(String rmp_eng) {
        this.rmp_eng = rmp_eng;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getState_locate() {
        return state_locate;
    }

    public void setState_locate(String state_locate) {
        this.state_locate = state_locate;
    }

    public String getTemp_SCR_in() {
        return temp_SCR_in;
    }

    public void setTemp_SCR_in(String temp_SCR_in) {
        this.temp_SCR_in = temp_SCR_in;
    }

    public String getTemp_SCR_out() {
        return temp_SCR_out;
    }

    public void setTemp_SCR_out(String temp_SCR_out) {
        this.temp_SCR_out = temp_SCR_out;
    }

    public String getTemp_coolant() {
        return temp_coolant;
    }

    public void setTemp_coolant(String temp_coolant) {
        this.temp_coolant = temp_coolant;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTorque_friction() {
        return torque_friction;
    }

    public void setTorque_friction(String torque_friction) {
        this.torque_friction = torque_friction;
    }

    public String getTorque_output() {
        return torque_output;
    }

    public void setTorque_output(String torque_output) {
        this.torque_output = torque_output;
    }

    public String getUrea_left() {
        return urea_left;
    }

    public void setUrea_left(String urea_left) {
        this.urea_left = urea_left;
    }
}
