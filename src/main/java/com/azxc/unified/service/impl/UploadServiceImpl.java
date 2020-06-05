package com.azxc.unified.service.impl;

import com.azxc.unified.entity.Upload;
import com.azxc.unified.repository.UploadRepository;
import com.azxc.unified.service.UploadService;
import org.springframework.stereotype.Service;

/**
 * @author lhy
 * @version 1.0 2020/3/23
 */
@Service
public class UploadServiceImpl implements UploadService {

  private final UploadRepository uploadRepository;

  public UploadServiceImpl(UploadRepository uploadRepository) {
    this.uploadRepository = uploadRepository;
  }

  @Override
  public Upload getBySha1(String sha1) {
    return uploadRepository.findBySha1(sha1);
  }

  @Override
  public Upload save(Upload upload) {
    return uploadRepository.save(upload);
  }
}
