package com.wzw.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.core.lang.Console;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import com.wzw.common.CommonResult;
import com.wzw.entity.CircleVo;
import com.wzw.entity.ExcelDataItem;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class FunService
{
    public CommonResult<List<CircleVo>> uploadFileAndCalc(MultipartFile file)
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
        EasyExcel.read
        (
            inputStream,
            ExcelDataItem.class,
            new AnalysisEventListener<ExcelDataItem>()
            {
                @Override
                public void invoke(ExcelDataItem data, AnalysisContext context)
                {
                    Console.log("{} 被调用 data:{} context:{}", DateUtil.date(), data, context);
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

        return null;
    }
}
