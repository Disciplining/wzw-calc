package com.wzw;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import com.wzw.entity.Circle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Main
{
    public static void main(String[] args)
    {
        TreeMap<String, Object> map = new TreeMap<>();

        map.put("a", 1231);
        map.put("b", 1232);
        map.put("c", 1233);
        map.put("b", 1234);

        Console.log("打印数据：{}", map);
    }
}
