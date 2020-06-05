package com.azxc.unified.common.utils;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.data.ParentEntity;
import com.azxc.unified.common.data.SortEntity;
import com.azxc.unified.common.data.ParentService;
import java.util.List;
import java.util.Objects;

/**
 * 排序工具类
 *
 * @author lhy
 * @version 1.0 2020/3/31
 */
public class SortUtil {

  /**
   * 带上级pid的排序
   *
   * @param <T> 实体类必须继承 {@link SortEntity}
   * @return 排序受影响的排序结果
   */
  public static <T extends ParentEntity> List<T> sortEntity(T entity, ParentService<T> parentService) {
    // 排序功能
    Long sort = entity.getSort();
    Long notId = entity.getId() != null ? entity.getId() : 0;
    List<T> levelEntity = parentService.getListByPid(entity.getPid(), notId);
    levelEntity.add(Math.toIntExact(sort), entity);
    for (int i = 1; i <= levelEntity.size(); i++) {
      levelEntity.get(i - 1).setSort((long) i);
    }
    // 如果有修改父级pid，更新子菜单pids
    if (entity.getId() != null) {
      T beEntity = parentService.getById(entity.getId());
      if (!Objects.equals(entity.getPid(), beEntity.getPid())) {
        List<T> entitys = parentService.getListByPid(entity.getId(), entity.getId());
        entitys.forEach(item -> {
          String rPids =
              StrUtil.replaceIgnoreCase(item.getPids(), beEntity.getPids(), entity.getPids());
          item.setPids(rPids);
          levelEntity.add(item);
        });
      }
    }
    return levelEntity;
  }

  /**
   * 带上级pid，获取上级序号
   */
  public static <T extends ParentEntity> void sortPids(T entity, ParentService<T> parentService) {
    // 添加/更新全部上级序号
    if (entity.getPid() != 0) {
      T pEntity = parentService.getById(entity.getPid());
      entity.setPids(pEntity.getPids() + ",[" + entity.getPid() + "]");
    } else {
      entity.setPids("[0]");
    }
  }
}
