package com.azxc.unified.common.file.enums;

import com.azxc.unified.common.exception.interfaces.ResultInterface;
import lombok.Getter;

/**
 * @author lhy
 * @version 1.0 2020/3/23
 */
@Getter
public enum UploadResultEnum implements ResultInterface {

  /**
   * 文件操作
   */
  NO_FILE_NULL(401, "文件不能为空"),
  NO_FILE_TYPE(402, "不支持该文件类型"),
  ;

  private Integer code;

  private String message;

  UploadResultEnum(Integer code, String message) {
    this.code = code;
    this.message = message;
  }
}
