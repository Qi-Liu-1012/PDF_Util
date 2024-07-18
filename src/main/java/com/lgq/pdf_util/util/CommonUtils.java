package com.lgq.pdf_util.util;

import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author liuguoqiang
 * @Date 2024/2/29
 * @Version 1.0
 */
public class CommonUtils {
    public static List<Integer> splitNum(String numStr) {
        return Arrays.stream(numStr.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * 获取列表中指定索引的元素，如果索引超出范围则返回默认值。
     *
     * @param list         要操作的列表
     * @param index        要获取元素的索引
     * @param defaultValue 索引超出范围时返回的默认值
     * @return 列表中对应索引的元素，或默认值
     */
    public static <T> T getListElementOrDefault(List<T> list, int index, T defaultValue) {
        if (CollectionUtils.isEmpty(list)){
            return defaultValue;
        }
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        } else {
            return defaultValue;
        }
    }
}
