package com.ymt.api;

import java.util.HashMap;
import java.util.Map;

import com.wms.warehouse.WmsStorag;

public class Test {
    public static void main(String[] args) {
        WmsStorag a = new WmsStorag();
        WmsStorag b = new WmsStorag();
        a.setId("1");
        b.setId("1");
        System.out.println(a.equals(b));
        Map<WmsStorag, String> m = new HashMap<>();
        m.put(a, "test");
        System.out.println(m.get(b));

    }

}
