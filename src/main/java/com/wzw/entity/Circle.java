package com.wzw.entity;

import lombok.Data;

/**
 * 一个圆
 */
@Data
public class Circle
{
    /**
     * 圆心位置
     */
    private Integer index;

    /**
     * 圆的半径
     */
    private Integer r;

    /**
     * 这个圆最右边的坐标
     */
    private Integer rightIndex;
}
