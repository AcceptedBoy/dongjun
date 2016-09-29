package com.gdut.dongjun.base;

import com.gdut.dongjun.constant.Constant;

import java.io.*;

/**
 * 根据项目日志转化为报文txt
 * Created by AcceptedBoy on 2016/8/27.
 */
public class SourceTxt {

    /**
     * 生成报文txt
     */
    public void createSourceTxt() {
        try {

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(this.getClass().getResourceAsStream(Constant.LOGGER_FILE),
                            Constant.READ_CHARSET));

            String outputPath = this.getClass().getResource(Constant.OUTPUT_FILE.substring
                    (0, Constant.OUTPUT_FILE.lastIndexOf("/"))).getPath() +
                    Constant.OUTPUT_FILE.substring(Constant.OUTPUT_FILE.lastIndexOf("/"), Constant.OUTPUT_FILE.length());

            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(outputPath),
                            Constant.WRITE_CHARSET));
            String str;
            while((str = br.readLine()) != null) {
                if(str.contains(Constant.JUDGE_START)) {
                    str = str.substring(str.lastIndexOf(Constant.JUDGE_START)
                            + Constant.JUDGE_START.length(), str.length());
                    bw.write(str);
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当为新的日志文件导出格式化txt文件时，可以调用该main函数
     * @param args
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        new SourceTxt().createSourceTxt();
    }
}
