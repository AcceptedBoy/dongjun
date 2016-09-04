package com.gdut.dongjun.base;

import com.gdut.dongjun.constant.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * Created by AcceptedBoy on 2016/8/28.
 */
public class DefaultSimulateSend extends AbstractSimulateSend {

    @Override
    public void createSourceCache() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream(Constant.OUTPUT_FILE)))){
            String str;
            while((str = br.readLine()) != null) {
                cache.add(str);
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
}
