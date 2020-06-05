package com.azxc.unified.repository;

import com.azxc.unified.entity.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 文件
 *
 * @author lhy
 * @version 1.0 2020/3/23
 */
public interface UploadRepository extends JpaRepository<Upload, Long> {

  /**
   * 根据sha1查询
   *
   * @param sha1 sha1值
   * @return 查询结果
   */
  Upload findBySha1(String sha1);
}
