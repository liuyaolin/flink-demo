package com.it.dbswap.message;


import com.it.dbswap.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class MessageParse {
    //private static final Logger logger = LoggerFactory.getLogger(SwapProcessFunction.class);

//    private static Message messageObj = new Message();  //将Message实体类定义成一个对象messageObj
//    private static String data1;
    public static Message parsing(byte[] value) {
        Message messageObj = new Message();
        StringBuilder message = new StringBuilder();
        //只转需要解析的报文，对原始报文的前20个字节
        for(int i=0;i<20;i++){
            int temp = value[i] & 0xFF;
            String hex = Integer.toHexString(temp);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            message.append(hex.toUpperCase());
        }

        //判断报文是否2323开头 对01 02 03 04命令单元的报文均可解析,只有05命令单元为05的报文没有时间戳
        if (!message.substring(0,4).equals("2323")){
            return null;
        }
        else{
            //判断命令单元 表中命令单元占一个字节
            String   commandType = message.substring(4,6);  //4-6代表两个16进制数，每个16进制数占4个位，共8位是一个字节
            messageObj.setCommandType(commandType);
            //提取底盘号
            String vin=hexToAscii(message.substring(6,40));
            messageObj.setVin(vin);
            //若命令单元为05
            if(commandType.equals("05")){
                String  timeSend = "无效数据";
                messageObj.setSendingTime(timeSend);
            }else{
                //获取数据发送时间(6Byte)
                String msgTime = new String();
                //只截取数据发送时间的原始报文转16进制串
                for(int i=24;i<31;i++){
                    int temp = value[i] & 0xFF;
                    String hex = Integer.toHexString(temp);
                    if (hex.length() == 1) {
                        hex = '0' + hex;
                    }
                    msgTime += hex.toUpperCase();
                }
                String  timeSend = hexToTime(msgTime);
                messageObj.setSendingTime(timeSend);
            }


            //原始报文
            String data1 = Arrays.toString(value);
            messageObj.setData(data1);
            return messageObj;  //parsing方法执行完后返回Message对象，这时这个对象存的则是我们解析完的数据
        }
    }

        public static Message parsing(String message){  //进行相应数据解析
            String vin;
            String timeSend;
            String commandType;
            String data1;
            Message messageObj = new Message();
        //判断报文是否2323开头
        if (!message.startsWith("2323") || (!message.substring(4,6).equals("01") && !message.substring(4,6).equals("02"))){
            return null;
        }
        else{

            //判断命令单元 表中命令单元占一个字节
            commandType = message.substring(4,6);  //4-6代表两个16进制数，每个16进制数占4个位，共8位是一个字节
            messageObj.setCommandType(commandType);
            //提取底盘号
            vin=hexToAscii(message.substring(6,40));
            messageObj.setVin(vin);

            //获取数据发送时间(6Byte)
            timeSend = hexToTime(message.substring(48,60));
            messageObj.setSendingTime(timeSend);

            //原16进制字符串
            data1 = message;
            messageObj.setData(data1);
            return messageObj;  //parsing方法执行完后返回Message对象，这时这个对象存的则是我们解析完的数据
        }

    }


    //将数据转换为Ascii编码
    private static String hexToAscii(String hexString){
        StringBuilder builder = new StringBuilder(); //新建String对象
        char[] chars = hexString.toCharArray();  //将每个16进制数（4位）按char存入数组中
        for(int i=0;i<chars.length-1;i+=2){
            String s = hexString.substring(i,i+2); //取两个16进制数，8位为1个字节
            int ss = Integer.parseInt(s,16); //将16进制字符串转换为整型
            builder.append((char)ss); //再将整型数转换为char，存入builder数组中例如：69对应的char为a
        }
        return builder.toString();
    }

    //对time进行操作
    private static String hexToTime(String hexString){
        ArrayList<String> arrs = new ArrayList<>(); //创建顺序表
        char[] chars = hexString.toCharArray(); //将每个16进制数（4位）按char存入数组中
        for(int i=0;i<chars.length-1;i+=2){
            int time = Integer.parseInt(hexString.substring(i,i+2),16);  //将1个字节的数转为整型
            arrs.add(String.valueOf(time)); //将值存入arrs中
        }
        for(int i=0;i< arrs.size();i++){
            if(arrs.get(i).length()<2){  //arrs中第i号元素的长度小于2，例如：8,3,4
                arrs.set(i,"0"+arrs.get(i)); //给这个值改为08,03,04
            }
        }
        String time = "20"+arrs.get(0).toString()  //对应年
                +"-"+arrs.get(1) //对应月
                +"-"+arrs.get(2) //对应日
                +" "+arrs.get(3) //对应时
                +":"+arrs.get(4) //对应分
                +":"+arrs.get(5); //对应秒
        //HH,24小时制；hh，12小时制
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //相应时间格式
            //LocalDate和LocalDateTime格式不一样
            LocalDateTime datetime = LocalDateTime.parse(time,formatter); //将这个格式运用给time
            return formatter.format(datetime);
        }catch (Exception e){
            //logger.warn("error",e.getMessage());
            System.out.println("error");
        }
        return "error time";
    }

    public static void main(String[] args) {
        System.out.println(Integer.parseInt("1001", 2));
    }

}
