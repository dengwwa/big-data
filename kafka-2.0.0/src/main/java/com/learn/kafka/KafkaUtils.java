package com.learn.kafka;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.AliHBaseUEClusterConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * Description:
 * Author:dengww
 * Date:2025/4/18
 */
public class KafkaUtils {
    public static void main(String[] args) {
        // 新建一个Configuration
        Configuration conf = HBaseConfiguration.create();
        // 集群的Java API访问地址，在控制台页面的数据库连接界面获得
        conf.set("hbase.zookeeper.quorum", "");
        // xml_template.comment.hbaseue.username_password.default
        conf.set("hbase.client.username", "");
        conf.set("hbase.client.password", "");
        // 如果您直接依赖了阿里云hbase客户端，则无需配置connection.impl参数，如果您依赖了alihbase-connector，则需要配置此参数
//        conf.set("hbase.client.connection.impl", AliHBaseUEClusterConnection.class.getName());
        try (Connection connection = ConnectionFactory.createConnection(conf);
             Admin admin = connection.getAdmin()) {

            System.out.println(admin.tableExists(TableName.valueOf("jx:team-order-daily")));
            disableTable(admin, "jx:team-order-daily");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void createTable(String tableName, String[] columnFamilies) {

    }


    public static void disableTable(Admin admin, String tableName) {
        try {
            if (!admin.tableExists(TableName.valueOf(tableName))) {
                System.out.println("table: " + tableName + " is not exist");
                return;
            }

            if (admin.isTableEnabled(TableName.valueOf(tableName))) {
                admin.disableTable(TableName.valueOf(tableName));
                admin.flush(TableName.valueOf(tableName));
                System.out.println("disabled table success");
            } else {
                System.out.println("table is disabled ");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
