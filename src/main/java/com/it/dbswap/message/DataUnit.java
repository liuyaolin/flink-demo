package com.it.dbswap.message;

import com.dyuproject.protostuff.Tag;

/**
 * @Author SunTao
 * @Date 2021/2/19
 * @description 描述一条命令类型为数据流报文的类
 */
public class DataUnit {
    @Tag(1)
    private String dataType;
    @Tag(2)
    private String time;
    @Tag(3)
    private String speed;
    @Tag(4)
    private String ambientPressure;
    @Tag(5)
    private String torque_output;
    @Tag(6)
    private String torque_friction;
    @Tag(7)
    private String rmp_eng;
    @Tag(8)
    private String flow_fuel;
    @Tag(9)
    private String NOx_head;
    @Tag(10)
    private String NOx_rear;
    @Tag(11)
    private String urea_left;
    @Tag(12)
    private String flow_air;
    @Tag(13)
    private String temp_SCR_in;
    @Tag(14)
    private String temp_SCR_out;
    @Tag(15)
    private String pressure_DPF_diff;
    @Tag(16)
    private String temp_coolant;
    @Tag(17)
    private String fuel_left;
    @Tag(18)
    private String state_locate;
    @Tag(19)
    private String longitude;
    @Tag(20)
    private String latitude;
    @Tag(21)
    private String range;

    public DataUnit(){

    }
    public DataUnit(String dataType, String time, String speed, String ambientPressure, String torque_output, String torque_friction, String rmp_eng, String flow_fuel, String NOx_head, String NOx_rear, String urea_left, String flow_air, String temp_SCR_in, String temp_SCR_out, String pressure_DPF_diff, String temp_coolant, String fuel_left, String state_locate, String longitude, String latitude, String range) {
        this.dataType = dataType;
        this.time = time;
        this.speed = speed;
        this.ambientPressure = ambientPressure;
        this.torque_output = torque_output;
        this.torque_friction = torque_friction;
        this.rmp_eng = rmp_eng;
        this.flow_fuel = flow_fuel;
        this.NOx_head = NOx_head;
        this.NOx_rear = NOx_rear;
        this.urea_left = urea_left;
        this.flow_air = flow_air;
        this.temp_SCR_in = temp_SCR_in;
        this.temp_SCR_out = temp_SCR_out;
        this.pressure_DPF_diff = pressure_DPF_diff;
        this.temp_coolant = temp_coolant;
        this.fuel_left = fuel_left;
        this.state_locate = state_locate;
        this.longitude = longitude;
        this.latitude = latitude;
        this.range = range;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getAmbientPressure() {
        return ambientPressure;
    }

    public void setAmbientPressure(String ambientPressure) {
        this.ambientPressure = ambientPressure;
    }

    public String getTorque_output() {
        return torque_output;
    }

    public void setTorque_output(String torque_output) {
        this.torque_output = torque_output;
    }

    public String getTorque_friction() {
        return torque_friction;
    }

    public void setTorque_friction(String torque_friction) {
        this.torque_friction = torque_friction;
    }

    public String getRmp_eng() {
        return rmp_eng;
    }

    public void setRmp_eng(String rmp_eng) {
        this.rmp_eng = rmp_eng;
    }

    public String getFlow_fuel() {
        return flow_fuel;
    }

    public void setFlow_fuel(String flow_fuel) {
        this.flow_fuel = flow_fuel;
    }

    public String getNOx_head() {
        return NOx_head;
    }

    public void setNOx_head(String NOx_head) {
        this.NOx_head = NOx_head;
    }

    public String getNOx_rear() {
        return NOx_rear;
    }

    public void setNOx_rear(String NOx_rear) {
        this.NOx_rear = NOx_rear;
    }

    public String getUrea_left() {
        return urea_left;
    }

    public void setUrea_left(String urea_left) {
        this.urea_left = urea_left;
    }

    public String getFlow_air() {
        return flow_air;
    }

    public void setFlow_air(String flow_air) {
        this.flow_air = flow_air;
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

    public String getPressure_DPF_diff() {
        return pressure_DPF_diff;
    }

    public void setPressure_DPF_diff(String pressure_DPF_diff) {
        this.pressure_DPF_diff = pressure_DPF_diff;
    }

    public String getTemp_coolant() {
        return temp_coolant;
    }

    public void setTemp_coolant(String temp_coolant) {
        this.temp_coolant = temp_coolant;
    }

    public String getFuel_left() {
        return fuel_left;
    }

    public void setFuel_left(String fuel_left) {
        this.fuel_left = fuel_left;
    }

    public String getState_locate() {
        return state_locate;
    }

    public void setState_locate(String state_locate) {
        this.state_locate = state_locate;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "DataUnit{" +
                "dataType='" + dataType + '\'' +
                ", time='" + time + '\'' +
                ", speed='" + speed + '\'' +
                ", ambientPressure='" + ambientPressure + '\'' +
                ", Torque_output='" + torque_output + '\'' +
                ", Torque_friction='" + torque_friction + '\'' +
                ", rmp_eng='" + rmp_eng + '\'' +
                ", flow_fuel='" + flow_fuel + '\'' +
                ", NOx_head='" + NOx_head + '\'' +
                ", NOx_rear='" + NOx_rear + '\'' +
                ", urea_left='" + urea_left + '\'' +
                ", flow_air='" + flow_air + '\'' +
                ", temp_SCR_in='" + temp_SCR_in + '\'' +
                ", temp_SCR_out='" + temp_SCR_out + '\'' +
                ", pressure_DPF_diff='" + pressure_DPF_diff + '\'' +
                ", temp_coolant='" + temp_coolant + '\'' +
                ", fuel_left='" + fuel_left + '\'' +
                ", state_locate='" + state_locate + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", range='" + range + '\'' +
                '}';
    }
}
