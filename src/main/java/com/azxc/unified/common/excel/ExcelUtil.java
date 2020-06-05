package com.azxc.unified.common.excel;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.excel.annotation.Excel;
import com.azxc.unified.common.utils.DictUtil;
import com.azxc.unified.common.utils.HttpServletUtil;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;

/**
 * @author lhy
 * @version 1.0 2020/3/31
 */
public class ExcelUtil {

  private static int dataRow = 2;

  /**
   * 获取通用样式
   */
  private static XSSFCellStyle getCellStyle(XSSFWorkbook workbook) {
    XSSFCellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setAlignment(HorizontalAlignment.LEFT);
    XSSFFont font = workbook.createFont();
    font.setFontName("Microsoft YaHei UI");
    cellStyle.setFont(font);
    return cellStyle;
  }

  /**
   * 功能模板（标题及表头）
   */
  private static XSSFWorkbook getCommon(String sheetTitle, List<Field> fields) {
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet(sheetTitle);
    // 设置列宽度
    for (int i = 0; i < fields.size(); i++) {
      sheet.setColumnWidth(i, 16 * 256);
    }
    // 通用样式
    XSSFCellStyle cellStyle = getCellStyle(workbook);
    // 标题样式
    XSSFCellStyle titleStyle = workbook.createCellStyle();
    titleStyle.cloneStyleFrom(cellStyle);
    titleStyle.setAlignment(HorizontalAlignment.CENTER);
    titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    XSSFFont titleFont = workbook.createFont();
    titleFont.setFontName(cellStyle.getFont().getFontName());
    titleFont.setBold(true);
    titleFont.setFontHeight(14);
    titleStyle.setFont(titleFont);
    // 表头样式
    XSSFCellStyle thStyle = workbook.createCellStyle();
    thStyle.cloneStyleFrom(titleStyle);
    thStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    thStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
    XSSFFont thFont = workbook.createFont();
    thFont.setFontName(cellStyle.getFont().getFontName());
    thFont.setBold(titleFont.getBold());
    thFont.setColor(IndexedColors.WHITE.getIndex());
    thStyle.setFont(thFont);
    // 创建标题样式、表格表头
    XSSFRow titleRow = sheet.createRow(0);
    XSSFRow thsRow = sheet.createRow(1);
    for (int i = 0; i < fields.size(); i++) {
      Excel excel = fields.get(i).getAnnotation(Excel.class);
      XSSFCell title = titleRow.createCell(i);
      title.setCellStyle(titleStyle);
      XSSFCell th = thsRow.createCell(i);
      th.setCellValue(excel.value());
      th.setCellStyle(thStyle);
    }
    // 绘制标题
    titleRow.setHeight((short) (26 * 20));
    XSSFCell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(sheetTitle);
    titleCell.setCellStyle(titleStyle);
    // 合并标题单元格
    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, fields.size() - 1));
    return workbook;
  }

  /**
   * 获取实体类带有@Excel的属性
   */
  private static List<Field> getExcelList(Class<?> entity) {
    List<Field> list = new ArrayList<>();
    Field[] fields = entity.getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(Excel.class)) {
        list.add(field);
      }
    }
    return list;
  }

  /**
   * 获取实体类带有@Excel字段名
   */
  private static List<String> getFieldName(List<Field> fields) {
    List<String> list = new ArrayList<>();
    for (Field field : fields) {
      list.add(field.getName());
    }
    return list;
  }

  /**
   * 下载操作
   *
   * @param workbook 表格对象
   * @param fileName 文件名
   */
  private static void download(XSSFWorkbook workbook, String fileName) {
    try {
      fileName = URLEncoder.encode(fileName + ".xlsx", "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    HttpServletResponse response = HttpServletUtil.getResponse();
    response.setCharacterEncoding("utf-8");
    response.setContentType("multipart/form-data");
    response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
    OutputStream ros = null;
    try {
      ros = response.getOutputStream();
      workbook.write(ros);
      ros.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (ros != null) {
        try {
          ros.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (workbook != null) {
        try {
          workbook.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 导出Excel数据
   *
   * @param entity 实体类Class
   * @param list   导出的数据列表
   */
  public static <T> void exportExcel(Class<?> entity, List<T> list) {
    Excel excel = entity.getAnnotation(Excel.class);
    if (excel != null) {
      exportExcel(entity, list, excel.value());
    } else {
      exportExcel(entity, list, entity.getSimpleName());
    }
  }

  /**
   * 导出Excel数据
   *
   * @param entity     实体类Class
   * @param list       导出的数据列表
   * @param sheetTitle 工作组标题（文件名称）
   */
  public static <T> void exportExcel(Class<?> entity, List<T> list, String sheetTitle) {
    List<Field> fields = getExcelList(entity);
    List<String> fns = getFieldName(fields);

    XSSFWorkbook workbook = getCommon(sheetTitle, fields);
    XSSFSheet sheet = workbook.getSheet(sheetTitle);
    XSSFCellStyle cellStyle = getCellStyle(workbook);
    // 时间样式
    XSSFCellStyle dateStyle = workbook.createCellStyle();
    dateStyle.cloneStyleFrom(cellStyle);
    XSSFDataFormat format = workbook.createDataFormat();
    dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm"));

    for (int i = 0; i < list.size(); i++) {
      XSSFRow row = sheet.createRow(i + dataRow);
      T item = list.get(i);
      // 通过反射机制获取实体对象的状态
      try {
        final BeanInfo bi = Introspector.getBeanInfo(item.getClass());
        for (final PropertyDescriptor pd : bi.getPropertyDescriptors()) {
          if (fns.contains(pd.getName())) {
            Object value = pd.getReadMethod().invoke(item, (Object[]) null);
            int index = fns.indexOf(pd.getName());
            XSSFCell cell = row.createCell(index);
            if (value != null) {
              Excel excel = fields.get(index).getAnnotation(Excel.class);
              // 字典值转换
              String dict = excel.dict();
              if (!dict.isEmpty()) {
                value = DictUtil.keyValue(dict, StrUtil.toString(value));
              }
              // 获取关联对象指定的值
              String joinField = excel.joinField();
              if (!joinField.isEmpty()) {
                PropertyDescriptor sourcePd =
                    BeanUtils.getPropertyDescriptor(value.getClass(), joinField);
                value = sourcePd.getReadMethod().invoke(value, (Object[]) null);
              }
              // 给单元格赋值
              if (value instanceof Number) {
                cell.setCellValue((Double.valueOf(String.valueOf(value))));
              } else if (value instanceof Date) {
                cell.setCellValue((Date) value);
                cell.setCellStyle(dateStyle);
                continue;
              } else {
                cell.setCellValue(String.valueOf(value));
              }
            }
            cell.setCellStyle(cellStyle);
          }
        }
      } catch (InvocationTargetException | IntrospectionException | IllegalAccessException e) {
        String message = "导入失败：字段名称匹配失败！";
        throw new IllegalArgumentException(message, e);
      }
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    download(workbook, sheetTitle + dateFormat.format(new Date()));
  }
}
