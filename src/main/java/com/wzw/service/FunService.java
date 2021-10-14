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
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        Circle headCirc = new Circle(); // 放置第一个圆
        headCirc.setR(dataMap.get(dataList.get(0).getPoint()));
        headCirc.setIndex(8 + headCirc.getR());
        headCirc.setRightIndex(headCirc.getIndex() + headCirc.getR());
        circleList.add(headCirc);
        while (true)
        {
            Circle nextCirc = getNextCirc(circleList.get(circleList.size() - 1), dataMap);
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
            BigDecimal sum = NumberUtil.add(el.getSample1(), el.getSample2(), el.getSample3(), el.getSample4());
            long value = Math.round(NumberUtil.div(sum, 4).doubleValue());
            dataMap.put(el.getPoint(), (int) value);
        }

        return dataMap;
    }

    /**
     * 计算下一个圆
     * @param preCirc    上一个圆
     * @param dataMap    数据map
     * @return 计算出的圆，返回null的话说明都找完了.
     */
    private Circle getNextCirc(final Circle preCirc, Map<Integer, Integer> dataMap)
    {
        int rightIndex = preCirc.getRightIndex();

        for (int i = rightIndex+1; i <= Integer.MAX_VALUE; i++)
        {
            if (Objects.isNull(dataMap.get(i)))
            {
                return null;
            }

            if (Objects.isNull(dataMap.get(i+1))) // i是最后一个圆
            {
                Circle theCircle = new Circle();
                theCircle.setIndex(i);
                theCircle.setR(dataMap.get(i));
                theCircle.setRightIndex(i + dataMap.get(i));

                if (calcPos(theCircle, preCirc) == 1)
                {
                    return theCircle;
                }
            }
            else // i不是最后一个圆
            {
                Circle firstCirc = new Circle();
                firstCirc.setIndex(i);
                firstCirc.setR(dataMap.get(i));
                firstCirc.setRightIndex(i + dataMap.get(i));

                Circle secondCirc = new Circle();
                secondCirc.setIndex(i+1);
                secondCirc.setR(dataMap.get(i+1));
                secondCirc.setRightIndex((i+1) + dataMap.get(i+1));

                if (calcPos(preCirc, firstCirc) == 1)
                {
                    return firstCirc;
                }
                else if (calcPos(preCirc, firstCirc)==0 && calcPos(preCirc, secondCirc)==2)
                {
                    return firstCirc;
                }
            }
        }

        return null;
    }

    /**
     * 判断两个圆的位置
     * @param c1 第一个圆
     * @param c2 第二个圆
     * @return 0-相交 1-相切 2-相离
     */
    private int calcPos(Circle c1, Circle c2)
    {
        int pointDis = Math.abs(c1.getIndex() - c2.getIndex()); // 圆心距离
        int rDis = c1.getR() + c2.getR(); // 两个半径距离相加

        if (pointDis < rDis)
        {
            return 0;
        }
        else if (pointDis == rDis)
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }
}
