package com.ecode.utils;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;

import java.util.List;

/**
 * 文本差异对比工具类
 */
public class TextDiffUtil {

    /**
     * 对比两段文本的差异
     *
     * @param text1 文本1
     * @param text2 文本2
     * @return 文本差异列表
     */
    public static List<AbstractDelta<String>> compare(String text1, String text2) {
        if (text1 == null || text2 == null) {
            throw new IllegalArgumentException("输入文本不能为空");
        }

        // 按行分割文本
        List<String> originalLines = List.of(text1.split("\\n"));
        List<String> revisedLines = List.of(text2.split("\\n"));

        // 生成差异对比
        Patch<String> patch = DiffUtils.diff(originalLines, revisedLines);

        return patch.getDeltas();
    }

    /**
     * 格式化差异结果为可读字符串
     *
     * @param deltas 文本差异列表
     * @return 格式化后的差异字符串
     */
    public static String formatDeltas(List<AbstractDelta<String>> deltas) {
        StringBuilder result = new StringBuilder();

        for (AbstractDelta<String> delta : deltas) {
            DeltaType type = delta.getType();
            result.append("差异类型: ").append(type).append("\n");
            result.append("原始部分: ").append(delta.getSource()).append("\n");
            result.append("修订部分: ").append(delta.getTarget()).append("\n");
            result.append("--------------------------------\n");
        }

        return result.toString();
    }

    /**
     * 使用 UnifiedDiffUtils 生成统一的差异格式
     *
     * @param text1 文本1
     * @param text2 文本2
     * @return 统一差异格式的字符串
     */
    public static String generateUnifiedDiff(String text1, String text2) {
        if (text1 == null || text2 == null) {
            throw new IllegalArgumentException("输入文本不能为空");
        }

        List<String> originalLines = List.of(text1.split("\\n"));
        List<String> revisedLines = List.of(text2.split("\\n"));

        Patch<String> patch = DiffUtils.diff(originalLines, revisedLines);

        // 使用 UnifiedDiffUtils 生成统一差异格式
        List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff(
                "标准输出", "用户输出", originalLines, patch, 0);

        return String.join("\n", unifiedDiff);
    }

    /**
     * 示例方法
     */
    public static void main(String[] args) {
        String text1 = "这是第一行\n这是第二行\n这是第三行";
        String text2 = "这是第一行\n这是修订后的第二行\n这是第三行\n这是新增的第四行";

        // 比较差异
        List<AbstractDelta<String>> deltas = compare(text1, text2);

        // 输出差异
        String formattedResult = formatDeltas(deltas);
        System.out.println(formattedResult);

        // 输出统一差异格式
        String unifiedDiff = generateUnifiedDiff(text1, text2);
        System.out.println(unifiedDiff.isEmpty());
        System.out.println("统一差异格式:\n" + unifiedDiff);
    }
}
