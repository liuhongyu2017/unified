package com.azxc.unified.service.impl;

import com.azxc.unified.common.constant.DictConst;
import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.PageSort;
import com.azxc.unified.entity.Dict;
import com.azxc.unified.repository.DictRepository;
import com.azxc.unified.service.DictService;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author lhy
 * @version 1.0 2020/3/12
 */
@CacheConfig(cacheNames = "dictionary")
@Service
public class DictServiceImpl implements DictService {

  private final DictRepository dictRepository;

  public DictServiceImpl(DictRepository dictRepository) {
    this.dictRepository = dictRepository;
  }

  @Override
  public Dict getById(Long id) {
    return dictRepository.findById(id).orElse(null);
  }

  @Cacheable(key = "#name.hashCode()")
  @Override
  public Dict getByNameOk(String name) {
    return dictRepository.findByNameAndStatus(name, StatusEnum.OK.getCode());
  }

  @Override
  public Page<Dict> getPageList(Example<Dict> example) {
    PageRequest page = PageSort.pageRequest();
    return dictRepository.findAll(example, page);
  }

  @Override
  public boolean repeatByName(Dict dict) {
    Long id = dict.getId() != null ? dict.getId() : Long.MIN_VALUE;
    return dictRepository.findByNameAndIdNot(dict.getName(), id) != null;
  }

  @Override
  public Dict save(Dict dict) {
    return dictRepository.save(dict);
  }

  @Transactional
  @Override
  public boolean updateStatus(StatusEnum statusEnum, List<Long> ids) {
    return dictRepository.updateStatus(statusEnum.getCode(), ids) > 0;
  }

  @Override
  public boolean systemDict(List<Long> ids) {
    return dictRepository.findByTypeAndIdIn(DictConst.SYSTEM_DICT_VALUE, ids).size() > 0;
  }
}
