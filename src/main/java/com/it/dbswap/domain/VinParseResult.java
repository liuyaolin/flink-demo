package com.it.dbswap.domain;

import com.alibaba.fastjson.JSONObject;
import com.dyuproject.protostuff.Tag;
import com.it.dbswap.util.ProtostuffSerializer;

public class VinParseResult {
    @Tag(1)
    private String checkCode;
    @Tag(2)
    private String commandType;
    @Tag(3)
    private String dataUnitLenth;
    @Tag(4)
    private VinData dataUnits;
    @Tag(5)
    private String encryption;
    @Tag(6)
    private String serialNumber;
    @Tag(7)
    private String softVersion;
    @Tag(8)
    private String start;

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getDataUnitLenth() {
        return dataUnitLenth;
    }

    public void setDataUnitLenth(String dataUnitLenth) {
        this.dataUnitLenth = dataUnitLenth;
    }

    public VinData getDataUnits() {
        return dataUnits;
    }

    public void setDataUnits(VinData dataUnits) {
        this.dataUnits = dataUnits;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSoftVersion() {
        return softVersion;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public static void main(String[] args) {
        String s = "{\"checkCode\":\"13\",\"commandType\":\"02\",\"dataUnitLenth\":514,\"dataUnits\":{\"ambientPressure\":\"102.0\",\"dataType\":\"02\",\"flow_air\":\"160.10000000000002\",\"flow_fuel\":\"1.2000000000000002\",\"fuel_left\":\"52.0\",\"latitude\":\"39.764528N\",\"longitude\":\"118.51923E\",\"nOx_head\":\"281.0\",\"nOx_rear\":\"238.0\",\"pressure_DPF_diff\":\"0.1\",\"range\":\"75771.90000000001\",\"rmp_eng\":\"549.5\",\"speed\":\"0.46875\",\"state_locate\":\"有效定位\",\"temp_SCR_in\":\"100.15625\",\"temp_SCR_out\":\"100.15625\",\"temp_coolant\":\"114\",\"time\":\"2021-10-28 21:49:11\",\"torque_friction\":\"3\",\"torque_output\":\"5\",\"urea_left\":\"100.0\"},\"encryption\":\"01\",\"sendingTime\":\"2021-10-28 21:49:11\",\"serialNumber\":\"299\",\"softVersion\":\"01\",\"start\":\"2323\",\"vin\":\"LZZ1CLVB2LN757710\"}";
        VinParseResult a = JSONObject.parseObject(s,VinParseResult.class);
        System.out.println(ProtostuffSerializer.serialize(a).length);
    }
}
