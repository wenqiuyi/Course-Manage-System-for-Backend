package com.coursemanage.module.accountmanage.util;

import com.alibaba.excel.EasyExcel;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class EasyExcelUtil {
    public <T> List<T> readExcel(MultipartFile file, Class<T> clazz) {
        try {
            return EasyExcel.read(file.getInputStream())
                    .head(clazz)
                    .sheet()
                    .doReadSync();
        } catch (IOException e) {
            throw new RuntimeException("读取Excel失败", e);
        }
    }

    public <T> List<T> readExcel(File file, Class<T> clazz) {
        return EasyExcel.read(file)
                .head(clazz)
                .sheet()
                .doReadSync();
    }
}
