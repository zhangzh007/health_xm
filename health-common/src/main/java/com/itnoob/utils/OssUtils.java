package com.itnoob.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

public class OssUtils {

    String endpoint = "oss-cn-qingdao.aliyuncs.com";

    String accessKeyId = "LTAI4FqqLb8WVc5YngtiFtcC";

    String accessKeySecret = "1pxTMlNA5cQiBL4e2dIDARsIW692m3";

    String BucketName = "zhangzh-bs";

    public String upload(String filename, InputStream inputStream) throws FileNotFoundException {

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.putObject(BucketName, "pic/" + filename, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            String filepath = "https://" + BucketName + "." + endpoint + "/pic/" + filename;
            return filepath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
