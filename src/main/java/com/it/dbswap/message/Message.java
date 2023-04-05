package com.it.dbswap.message;

import com.dyuproject.protostuff.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author SunTao
 * @Date 2021/2/25
 * @description 描述完整报文的类
 */
public class Message {
    private String commandType; //报文类型
    private String vin; //车辆识别号
    private String sendingTime; //数据发送时间
    private String data; //存储16进制串

    public String getVin() {
        return vin;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(String sendingTime) {
        this.sendingTime = sendingTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Message(){

    }
    public Message(String vin, String sendingTime, String data, String commandType) {
        this.commandType = commandType;
        this.vin = vin;
        this.sendingTime = sendingTime;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "vin='" + vin + '\'' +
                ", sendingTime='" + sendingTime + '\'' +
                ", data1='" + data + '\'' +
                ", commandType='" + commandType + '\'' +
                '}';
    }
}