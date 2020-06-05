package com.azxc.unified.common.utils;

import cn.hutool.core.util.StrUtil;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * 通用工具方法
 *
 * @author lhy
 * @version 1.0 2020/3/20
 */
public class ToolUtil {

  private static Random random = new Random();

  private ToolUtil() {

  }

  /**
   * 获取随机位数的字符串
   *
   * @param length 随机位数
   */
  public static String getRandomString(int length) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      // 获取ascii码中的字符 数字48-57 小写65-90 大写97-122
      int range = random.nextInt(75) + 48;
      if (range < 97) {
        if (range < 65) {
          if (range > 57) {
            range = 114 - range;
          }
        } else {
          if (range > 90) {
            range = 180 - range;
          }
        }
      }
      sb.append((char) range);
    }
    return sb.toString();
  }

  /**
   * 首字母转小写
   */
  public static String lowerFirst(String word) {
    if (Character.isLowerCase(word.charAt(0))) {
      return word;
    } else {
      return Character.toLowerCase(word.charAt(0)) + word.substring(1);
    }
  }

  /**
   * 首字母转大写
   */
  public static String upperFirst(String word) {
    if (Character.isUpperCase(word.charAt(0))) {
      return word;
    } else {
      return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }
  }

  /**
   * 获取项目不同模式下的根路径
   */
  public static String getProjectPath() {
    String filePath = ToolUtil.class.getResource("").getPath();
    String projectPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    StringBuilder path = new StringBuilder();
    if (!filePath.startsWith("file:/")) {
      // 开发模式下根路径
      char[] filePathArray = filePath.toCharArray();
      char[] projectPathArray = projectPath.toCharArray();
      for (int i = 0; i < filePathArray.length; i++) {
        if (projectPathArray.length > i && filePathArray[i] == projectPathArray[i]) {
          path.append(filePathArray[i]);
        } else {
          break;
        }
      }
    } else if (!projectPath.startsWith("file:/")) {
      // 部署服务器模式下根路径
      projectPath = projectPath.replace("/WEB-INF/classes/", "");
      projectPath = projectPath.replace("/target/classes/", "");
      try {
        path.append(URLDecoder.decode(projectPath, "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        return projectPath;
      }
    } else {
      // jar包启动模式下根路径
      String property = System.getProperty("java.class.path");
      int firstIndex = property.lastIndexOf(System.getProperty("path.separator")) + 1;
      int lastIndex = property.lastIndexOf(File.separator) + 1;
      path.append(property, firstIndex, lastIndex);
    }

    File file = new File(path.toString());
    String rootPath = "/";
    try {
      rootPath = URLDecoder.decode(file.getAbsolutePath(), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return StrUtil.replace(rootPath, "\\\\", "/");
  }

  /**
   * 获取文件后缀名
   */
  public static String getFileSuffix(String fileName) {
    if (!fileName.isEmpty()) {
      int lastIndexOf = fileName.lastIndexOf('.');
      return fileName.substring(lastIndexOf);
    }
    return "";
  }

  /**
   * 将枚举转成List集合
   *
   * @param enumClass 枚举类
   */
  public static Map<Long, String> enumToMap(Class<?> enumClass) {
    Map<Long, String> map = new TreeMap<>();
    try {
      Object[] objects = enumClass.getEnumConstants();
      Method getCode = enumClass.getMethod("getCode");
      Method getMessage = enumClass.getMethod("getMessage");
      for (Object obj : objects) {
        Object iCode = getCode.invoke(obj);
        Object iMessage = getMessage.invoke(obj);
        map.put(Long.valueOf(String.valueOf(iCode)), String.valueOf(iMessage));
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return map;
  }

  /**
   * 根据枚举code获取枚举对象
   *
   * @param enumClass 枚举类
   * @param code      code值
   */
  public static Object enumCode(Class<?> enumClass, Object code) {
    try {
      Object[] objects = enumClass.getEnumConstants();
      Method getCode = enumClass.getMethod("getCode");
      for (Object obj : objects) {
        Object iCode = getCode.invoke(obj);
        if (iCode.equals(code)) {
          return obj;
        }
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 十进制转二进制，每位 1：true、0：false
   *
   * @param decimalism 十进制数
   * @return 从 0 ~ length
   */
  public static boolean[] binaryValueJudge(int decimalism) {
    String binaryString = Integer.toBinaryString(decimalism);
    boolean[] result = new boolean[binaryString.length()];
    for (int i = 0; i < binaryString.length(); i++) {
      char item = binaryString.charAt(i);
      result[i] = item == '1';
    }
    return result;
  }
}
