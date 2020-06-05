package com.azxc.unified.service;

import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.entity.Dict;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

/**
 * 字典服务
 *
 * @author lhy
 * @version 1.0 2020/3/12
 */
public interface DictService {

  /**
   * 根据字典ID查询字典数据
   *
   * @param id 字典id
   * @return 字典信息
   */
  Dict getById(Long id);

  /**
   * 根据字典标识符获取字典数据
   *
   * @param name 字典标识符
   * @return 字典信息
   */
  Dict getByNameOk(String name);

  /**
   * 获取分页列表数据
   *
   * @param example 查询实例
   * @return 分页查询结果
   */
  Page<Dict> getPageList(Example<Dict> example);

  /**
   * 字典标识符是否重复
   *
   * @param dict 字典实体类
   * @return true:重复
   */
  boolean repeatByName(Dict dict);

  /**
   * 保存/修改 字典
   *
   * @param dict 字典实体类
   * @return 保存结果
   */
  Dict save(Dict dict);

  /**
   * 批量更新状态
   *
   * @param statusEnum 数据状态
   * @param ids        主键集合
   * @return 操作结果
   */
  boolean updateStatus(StatusEnum statusEnum, List<Long> ids);

  /**
   * 判断是否有系统字典
   *
   * @param ids 主键集合
   * @return true:是系统字典
   */
  boolean systemDict(List<Long> ids);
}
