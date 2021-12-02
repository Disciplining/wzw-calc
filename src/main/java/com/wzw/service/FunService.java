package com.wzw.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.wzw.common.CommonResult;
import com.wzw.entity.Circle;
import com.wzw.entity.ExcelDataItem;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FunService
{
    public CommonResult<List<Circle>> uploadFileAndCalc(MultipartFile file)
    {
        // ①获取文件输入流
        InputStream inputStream;
        try
        {
            inputStream = file.getInputStream();
        }
        catch (IOException e)
        {
            return CommonResult.errorMsg("生成输入流出现异常：{}", e.getCause());
        }

        // ②读取excel中的数据
        List<ExcelDataItem> dataList = CollUtil.newArrayList();
        EasyExcel.read
        (
            inputStream,
            ExcelDataItem.class,
            new AnalysisEventListener<ExcelDataItem>()
            {
                @Override
                public void invoke(ExcelDataItem data, AnalysisContext context)
                {
                    dataList.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context)
                {
                }
            }
        ).sheet().doRead();

        // ③关闭输入流
        if (Objects.nonNull(inputStream))
        {
            try
            {
                inputStream.close();
            }
            catch (Exception e)
            {
                return CommonResult.errorMsg("关闭输入流出现异常：{}", e.getCause());
            }
        }

        // ④转换为map
        Map<Integer, Integer> dataMap = this.list2map(dataList);

        // ⑤计算圆
        List<Circle> circleList = CollUtil.newArrayList();
        Circle headCirc = new Circle(); // 放置第一个圆，第一个圆的圆心不是excel文件中的是计算出来的，后边的圆圆心是excel文件中的.
        headCirc.setR(dataMap.get(dataList.get(0).getPoint()));
        headCirc.setIndex(8 + headCirc.getR());
        headCirc.setRightIndex(headCirc.getIndex() + headCirc.getR());
        circleList.add(headCirc);
        while (true)
        {
            Circle nextCirc = this.getNextCirc(circleList.get(circleList.size() - 1), dataMap);
            if (Objects.isNull(nextCirc))
            {
                break;
            }
            else
            {
                circleList.add(nextCirc);
            }
        }

        return CommonResult.successData(circleList);
    }

    /**
     * 将读取的数据转换成list，以取样点为k，取样点对应的半径为v.
     * @param dataItemList list
     * @return map
     */
    private Map<Integer, Integer> list2map(List<ExcelDataItem> dataItemList)
    {
        Map<Integer, Integer> dataMap = MapUtil.newHashMap(dataItemList.size());

        for (ExcelDataItem el : dataItemList)
        {
            BigDecimal sum = NumberUtil.add(el.getSample1(), el.getSample2(), el.getSample3(), el.getSample4(), el.getSample5(), el.getSample6());
            long value = Math.round(NumberUtil.div(sum, 6).doubleValue());
            dataMap.put(el.getPoint(), (int) value);
        }

        return dataMap;
    }

    /**
     * 在所有的圆中找下一个圆
     * @param preCirc    上一个圆
     * @param dataMap    数据map
     * @return 计算出的圆，有可能为null. 返回null，说明程序寻找结束.
     */
    private Circle getNextCirc(final Circle preCirc, Map<Integer, Integer> dataMap)
    {
        // ①找相切与相交的圆
        List<Circle> tangentialCircleList = CollUtil.newArrayList();
        List<Circle> intersectCircleExtendList = CollUtil.newArrayList();
        int rightIndex = preCirc.getRightIndex();
        for (int i = rightIndex+1; i<= 420; i++)
        {
            if (Objects.nonNull(dataMap.get(i)))
            {
                Circle theCircle = new Circle();
                theCircle.setIndex(i);
                theCircle.setR(dataMap.get(i));
                theCircle.setRightIndex(i + dataMap.get(i));

                if (this.calcPos(preCirc, theCircle) == 0)
                {
                    tangentialCircleList.add(theCircle);
                }
                else if (this.calcPos(preCirc, theCircle) > 0)
                {
                    intersectCircleExtendList.add(theCircle);
                }
            }
        }

        // ②有相切的，返回相切当中半径最大的.
        if (CollUtil.isNotEmpty(tangentialCircleList))
        {
            Collections.sort(tangentialCircleList);
            return tangentialCircleList.get(0);
        }


        // ②没有相切的，返回相交的当中半径最大的.
        if (CollUtil.isNotEmpty(intersectCircleExtendList))
        {
            Collections.sort(intersectCircleExtendList);
            return intersectCircleExtendList.get(0);
        }

        // ③即没有相切的，又没有相交的，返回null.
        return null;
    }

    /**
     * 判断两个圆的位置
     * @param c1 第一个圆
     * @param c2 第二个圆
     * @return >0，两个圆相交，数值越大表示两个圆相交的部分越多.
     *         =0，两个圆相切
     *         <0，两个圆相离，数值越小表示两个圆越离得越远.
     */
    private long calcPos(Circle c1, Circle c2)
    {
        long pointDis = Math.abs(c1.getIndex() - c2.getIndex()); // 圆心距离
        long rDis = c1.getR() + c2.getR(); // 两个半径距离相加

        return rDis - pointDis;
    }
}
