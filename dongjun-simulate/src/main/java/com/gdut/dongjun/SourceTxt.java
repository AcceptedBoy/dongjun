package com.gdut.dongjun;

import com.gdut.dongjun.Constant.Constant;

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
        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(this.getClass().getResourceAsStream(Constant.LOGGER_FILE),
                                Constant.READ_CHARSET));

                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(this.getClass().getResource(
                                Constant.OUTPUT_FILE).getPath()),
                                Constant.WRITE_CHARSET))
        ){

            String str;
            while((str = br.readLine()) != null) {
                if(str.contains(Constant.JUDGE_START)) {
                    bw.write(str.substring(str.lastIndexOf(Constant.JUDGE_START)
                            + Constant.JUDGE_START.length(), str.length()));
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
    public static void main(String[] args) {
        new SourceTxt().createSourceTxt();
    }
}
