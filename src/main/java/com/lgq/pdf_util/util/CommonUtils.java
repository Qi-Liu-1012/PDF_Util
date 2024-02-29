package com.lgq.pdf_util.util;

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
}
