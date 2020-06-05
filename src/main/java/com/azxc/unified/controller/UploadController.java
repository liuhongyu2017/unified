package com.azxc.unified.controller;

import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.exception.ResultException;
import com.azxc.unified.common.file.FileUpload;
import com.azxc.unified.common.file.enums.UploadResultEnum;
import com.azxc.unified.common.utils.ResultVoUtil;
import com.azxc.unified.entity.Upload;
import com.azxc.unified.service.UploadService;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传
 *
 * @author lhy
 * @version 1.0 2020/3/23
 */
@RequestMapping("/system/upload")
@Controller
public class UploadController {

  private final UploadService uploadService;

  public UploadController(UploadService uploadService) {
    this.uploadService = uploadService;
  }

  /**
   * 上传web格式图片
   */
  @ResponseBody
  @PostMapping("/image")
  public ResultVo<Object> uploadImage(
      @RequestParam("image") MultipartFile multipartFile
  ) {
    Upload upload = FileUpload.getFile(multipartFile, "images");
    try {
      return saveImage(multipartFile, upload);
    } catch (IOException | NoSuchAlgorithmException e) {
      return ResultVoUtil.error("上传图片失败");
    }
  }

  /**
   * 上传web格式头像
   */
  @ResponseBody
  @PostMapping("/picture")
  public ResultVo<Object> uploadPicture(@RequestParam("picture") MultipartFile multipartFile) {
    // 创建Upload实体对象
    Upload upload = FileUpload.getFile(multipartFile, "/picture");
    try {
      return saveImage(multipartFile, upload);
    } catch (IOException | NoSuchAlgorithmException e) {
      return ResultVoUtil.error("上传头像失败");
    }
  }

  /**
   * 上传文件
   */
  @ResponseBody
  @PostMapping("/file")
  public ResultVo<Object> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
    // 创建Upload实体对象
    Upload upload = FileUpload.getFile(multipartFile, "/file");
    // 判断文件是否存在
    Upload uploadSha1 = uploadService.getBySha1(FileUpload.getFileSha1(multipartFile));
    if (uploadSha1 != null) {
      return ResultVoUtil.success(uploadSha1);
    }
    try {
      FileUpload.transferTo(multipartFile, upload);
    } catch (IOException | NoSuchAlgorithmException e) {
      return ResultVoUtil.error("上传文件失败");
    }
    // 将文件信息保存到数据库中
    uploadService.save(upload);
    return ResultVoUtil.success(upload);
  }

  /**
   * 保存上传的web格图片
   */
  private ResultVo<Object> saveImage(MultipartFile multipartFile, Upload upload)
      throws IOException, NoSuchAlgorithmException {
    // 判断是否为支持的图片格式
    String[] types = {"image/gif", "image/jpg", "image/jpeg", "image/png"};
    if (!FileUpload.isContentType(multipartFile, types)) {
      throw new ResultException(UploadResultEnum.NO_FILE_TYPE);
    }
    // 判断图片是否存在
    Upload uploadSha1 = uploadService.getBySha1(FileUpload.getFileSha1(multipartFile));
    if (uploadSha1 != null) {
      return ResultVoUtil.success(uploadSha1);
    }
    FileUpload.transferTo(multipartFile, upload);
    // 将文件信息保存到数据库中
    uploadService.save(upload);
    return ResultVoUtil.success(upload);
  }
}
