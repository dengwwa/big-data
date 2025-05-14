//package com.learn.kafka;
//
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.apache.kafka.common.serialization.StringSerializer;
//
//import java.util.Properties;
//import java.util.concurrent.Future;
//
///**
// * Description:生产者
// * Author:dengww
// * Date:2024/10/27
// */
//public class Producer {
//    private static final String topic = "test";
//    private static final String brokers = "localhost:9092";
//
//    public static void main(String[] args) {
//        Properties properties = new Properties();
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
//        // “client.id，这个参数用来设定KafkaProducer对应的客户端id，默认值为“”。如果客户端不设置，则KafkaProducer会自动生成一个非空字符串，内容形式如“producer-1”“producer-2”，即字符串“producer-”与数字的拼接。”
////        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "producer.client.id.demo");
//        try {
//            KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
//            ProducerRecord<String, String> message = new ProducerRecord<>(topic, "hello kafka");
//            producer.send(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
