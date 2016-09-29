package com.gdut.dongjun;

import org.junit.Test;

import java.util.*;

/**
 * Created by AcceptedBoy on 2016/8/28.
 */
public class SocketTest {

    @Test
    public void testListSort() {
        List<String> list = new LinkedList<>();
        list.add("86");
        list.add("121212");
        list.add("12");
        list.add("2323");
        list.add("23");
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if(o1.length() > o2.length()) {
                    return 1;
                } else if(o1.length() < o2.length()){
                    return -1;
                }
                return o1.compareTo(o2);
            }
        });
        System.out.println(list);
    }
}
