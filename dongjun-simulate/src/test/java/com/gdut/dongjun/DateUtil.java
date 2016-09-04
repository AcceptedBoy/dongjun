package com.gdut.dongjun;

import org.junit.Test;

/**
 * Created by AcceptedBoy on 2016/9/3.
 */
public class DateUtil {

    @Test
    public void testDate() throws InterruptedException {
        String a = String.valueOf(System.currentTimeMillis());
        System.out.println(a);
        Thread.sleep(1000);
        String b = String.valueOf(System.currentTimeMillis());
        System.out.println(b);
        System.out.println(a.compareTo(b));
    }
}
