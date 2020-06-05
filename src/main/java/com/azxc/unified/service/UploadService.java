package com.azxc.unified.service;

import com.azxc.unified.entity.Upload;

/**
 * 上传
 *
 * @author lhy
 * @version 1.0 2020/3/23
 */
public interface UploadService {

  /**
   * 获取文件sha1值记录
   *
   * @param sha1 sha1值
   * @return 查询结果
   */
  Upload getBySha1(String sha1);

  /**
   * 保存文件
   *
   * @param upload 文件实体类
   * @return 保存结果
   */
  Upload save(Upload upload);
}
