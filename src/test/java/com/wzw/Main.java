package com.wzw;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;

import java.util.Map;

public class Main
{
    public static void main(String[] args)
    {
        Map<String, String> map = MapUtil.newHashMap();
        map.put("a", "AAA");
        map.put("b", "BBB");

        Console.log("打印数据：{}", map.get("a"));
        Console.log("打印数据：{}", map.get("cc"));
    }
}
