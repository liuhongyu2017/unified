package com.azxc.unified.common.mysql;

import org.hibernate.dialect.MySQL8Dialect;

/**
 * 重写数据库方言，设置默认字符集为UTF-8
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
public class MysqlDialectUTF8 extends MySQL8Dialect {

  @Override
  public String getTableTypeString() {
    return " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
  }

  /**
   * 禁用掉添加外键
   */
  @Override
  public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey,
      String referencedTable, String[] primaryKey, boolean referencesPrimaryKey) {
    return "";
  }
}
