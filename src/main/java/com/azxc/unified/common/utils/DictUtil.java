package com.azxc.unified.common.utils;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.entity.Dict;
import com.azxc.unified.service.DictService;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

/**
 * 字典工具类
 *
 * @author lhy
 * @version 1.0 2020/3/13
 */
public class DictUtil {

  private static Cache dictCache = EhCacheUtil.getDictCache();

  /**
   * 获取字典值集合
   *
   * @param label 字典标识符
   */
  public static Map<String, String> value(String label) {
    Map<String, String> values = new LinkedHashMap<>();
    DictService dictService = SpringContextUtil.getBean(DictService.class);
    Dict dict = dictService.getByNameOk(label);
    if (dict != null) {
      values = splitDictValue(dict.getValue());
    }
    return values;
  }

  /**
   * 根据选项编码获取选项值
   *
   * @param label 字典标识
   * @param code  选项编码
   */
  public static String keyValue(String label, String code) {
    Map<String, String> list = DictUtil.value(label);
    return list.isEmpty() ? "" : list.get(code);
  }

  /**
   * 封装数据状态
   *
   * @param status 状态
   */
  public static String dataStatus(Byte status) {
    String label = "DATA_STATUS";
    return DictUtil.keyValue(label, String.valueOf(status));
  }

  /**
   * 清空缓存中指定的数据
   *
   * @param label 字典标识
   */
  public static void clearCache(String label) {
    ValueWrapper dict = dictCache.get(label.hashCode());
    if (dict != null) {
      dictCache.evict(label.hashCode());
    }
  }

  /**
   * 清空所有字典缓存
   */
  public static void clearAllCache() {
    dictCache.clear();
  }

  /**
   * 拆分字典值
   *
   * @param value 字典值
   * @return 拆分结果
   */
  public static Map<String, String> splitDictValue(String value) {
    Map<String, String> values = null;
    if (StrUtil.isNotEmpty(value)) {
      values = new LinkedHashMap<>();
      String[] outerSplit = StrUtil.split(value, ",");
      for (String osp : outerSplit) {
        String[] split = StrUtil.split(osp, ":");
        if (split.length > 1) {
          values.put(split[0], split[1]);
        }
      }
    }
    return values;
  }

  /**
   * 拼接字典值
   *
   * @param values 字典值集合
   * @return 拼接结果
   */
  public static String joinDictValue(Map<String, String> values) {
    String value = "";
    if (!values.isEmpty()) {
      StringBuilder item = new StringBuilder();
      for (String key : values.keySet()) {
        item.append(key).append(":").append(values.get(key)).append(",");
      }
      value = item.toString();
      value = StrUtil.sub(value, 0, value.length() - 1);
    }
    return value;
  }
}
