//package com.it.dbswap.util;
//
//import com.it.dbswap.util.proputil.EsPropertiesUtil;
//import com.sf.bdp.rdmp.common.exception.MyRuntimeException;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @description: 这个类用于管理对es客户的获取，当程序需要使用多个es集群时，每个es集群只要初始化一个es客户端
// * @author: huangchm-01328365
// * @date: 2020-04-16 10:50
// */
//public class EsClientPool {
//
//    private static final Logger logger = LoggerFactory.getLogger(EsClientPool.class);
//
//    //host
//    private static ConcurrentHashMap<String, TransportClient> esPool = new ConcurrentHashMap<>();
//
//
//    public static TransportClient getEsClient(String name) {
//
//
//        String hosts = EsPropertiesUtil.getProperty(name.trim() + ".esHosts");
//        TransportClient client = esPool.get(hosts);
//
//        if (client == null) {
//            synchronized (EsClientPool.class) {
//                if (client == null) {
//
//                    String port = EsPropertiesUtil.getProperty(name + ".esPort");
//
//                    String[] hostArr =hosts.split(",");
//                    Settings settings = Settings.builder()
//                            .put("cluster.name", EsPropertiesUtil.getProperty(name+".cluster.name"))
//                            .put("client.transport.sniff", true)
//                            .put("client.transport.ping_timeout", "10s").build();
//                    client = new PreBuiltTransportClient(settings);
//
//                    for (String host : hostArr) {
//
//                        try {
//                            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), Integer.valueOf(port)));
//                        } catch (UnknownHostException e) {
//                            logger.error("Initialize ElasticSearch Client fails.{}", e);
//                            throw new MyRuntimeException(e);
//                        }
//                    }
//
//                    esPool.put(hosts,client);
//                }
//            }
//        }
//
//        return client;
//    }
//
//
//    public static void removeEsClient(String name){
//
//        String hosts = EsPropertiesUtil.getProperty(name + ".esHosts");
//
//        if(!esPool.contains(hosts)){
//            return;
//        }
//
//        synchronized (EsClientPool.class){
//            esPool.put(hosts,null);
//        }
//    }
//
//
//}
