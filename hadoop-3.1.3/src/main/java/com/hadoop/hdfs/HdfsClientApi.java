package com.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

/**
 * using hadoop basic api to operate hadoop cluster
 */
public class HdfsClientApi {
    private FileSystem fileSystem;


    @Before
    public void getClient() throws Exception {
        Configuration entries = new Configuration();
        // argument order: program code > configuration file in resource > self file (xxx-site.xml) in server > default file in server （xxx-default.xml）
        // setting replication
        entries.set("dfs.replication", "2");
        fileSystem = FileSystem.get(new URI("hdfs://hadoop102:8020"), entries, "meiya");


    }

    @Test
    public void makeDir() throws Exception {
        // create directory
        fileSystem.mkdirs(new Path("/hadoopApi"));
    }


    @Test
    public void deleteFileAndDirectory() throws IOException {
        // b：recursive delete
        fileSystem.delete(new Path("/hadoopApi"), true);
    }

    @Test
    public void upload() throws IOException {
        fileSystem.copyFromLocalFile(false, new Path("E:\\phone_data.txt"), new Path("/hadoopApi/phone_data.txt"));
    }


    @Test
    public void download() throws IOException {
        fileSystem.copyToLocalFile(false, new Path("/hadoopApi/phone_data.txt"), new Path("."), true);
//        fileSystem.copyToLocalFile(false, new Path("/hadoopApi/phone_data.txt"),new Path("."),false);
    }


    @Test
    public void moveOrRename() throws IOException {
        fileSystem.rename(new Path("/hadoopApi/phone_data.txt"), new Path("/hadoopApi/phone_data2.txt"));
    }


    @Test
    public void listFileInfo() throws IOException {
        RemoteIterator<LocatedFileStatus> listFiles = fileSystem.listFiles(new Path("/"), true);
        while (listFiles.hasNext()) {
            LocatedFileStatus fileStatus = listFiles.next();
            System.out.println("========" + fileStatus.getPath() + "=========");
            System.out.println(fileStatus.getPermission());
            System.out.println(fileStatus.getOwner());
            System.out.println(fileStatus.getGroup());
            System.out.println(fileStatus.getLen());
            System.out.println(fileStatus.getModificationTime());
            System.out.println(fileStatus.getReplication());
            System.out.println(fileStatus.getBlockSize());
            System.out.println(fileStatus.getPath().getName());

            // block info
            BlockLocation[] blockLocations = fileStatus.getBlockLocations();
            System.out.println(Arrays.toString(blockLocations));
        }

    }

    @Test
    public void judgeFileStatus() throws IOException {
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/"));
        for (FileStatus fileStatus : fileStatuses) {
            if (fileStatus.isFile()) {
                System.out.println("file: " + fileStatus.getPath().getName());
            } else {
                System.out.println("directory: " + fileStatus.getPath().getName());
            }
        }
    }

    @After
    public void closeClient() throws Exception {
        fileSystem.close();
    }
}
