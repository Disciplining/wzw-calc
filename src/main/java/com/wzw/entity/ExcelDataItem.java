package com.wzw.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * excel一行数据
 */
@Data
public class ExcelDataItem
{
    @ExcelProperty("取样点")
    private Integer point;

    @ExcelProperty("样本1")
    private Double sample1;

    @ExcelProperty("样本2")
    private Double sample2;

    @ExcelProperty("样本3")
    private Double sample3;

    @ExcelProperty("样本4")
    private Double sample4;
}
