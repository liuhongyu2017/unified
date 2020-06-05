package com.azxc.unified.controller;

import cn.hutool.core.util.StrUtil;
import com.azxc.unified.common.actionLog.action.SaveAction;
import com.azxc.unified.common.actionLog.action.base.StatusAction;
import com.azxc.unified.common.actionLog.annotation.ActionLog;
import com.azxc.unified.common.actionLog.annotation.EntityParam;
import com.azxc.unified.common.constant.AdminConst;
import com.azxc.unified.common.constant.DictConst;
import com.azxc.unified.common.constant.StatusEnum;
import com.azxc.unified.common.data.ResultVo;
import com.azxc.unified.common.exception.ResultEnum;
import com.azxc.unified.common.exception.ResultException;
import com.azxc.unified.common.utils.DictUtil;
import com.azxc.unified.common.utils.EntityBeanUtil;
import com.azxc.unified.common.utils.ResultVoUtil;
import com.azxc.unified.common.utils.ShiroUtil;
import com.azxc.unified.common.utils.StatusUtil;
import com.azxc.unified.entity.Dict;
import com.azxc.unified.entity.User;
import com.azxc.unified.service.DictService;
import com.azxc.unified.validator.DictValid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 字典
 *
 * @author lhy
 * @version 1.0 2020/3/17
 */
@RequestMapping("/system/dict")
@Controller
public class DictController {

  private final DictService dictService;

  public DictController(DictService dictService) {
    this.dictService = dictService;
  }

  /**
   * 列表首页
   */
  @RequiresPermissions({"system:dict:index"})
  @GetMapping("/index")
  public String index(Model model, Dict dict) {
    ExampleMatcher exampleMatcher = ExampleMatcher.matching()
        // 字典名称模糊查询
        .withMatcher("title", GenericPropertyMatcher::contains);

    Example<Dict> example = Example.of(dict, exampleMatcher);
    Page<Dict> page = dictService.getPageList(example);

    model.addAttribute("list", page.getContent());
    model.addAttribute("page", page);
    return "/system/dict/index";
  }

  /**
   * 跳转到添加页面
   */
  @RequiresPermissions({"system:dict:add"})
  @GetMapping("/add")
  public String toAdd() {
    return "/system/dict/add";
  }

  /**
   * 跳转到修改页面
   */
  @RequiresPermissions({"system:dict:edit"})
  @GetMapping("/edit")
  public String edit(Model model, @RequestParam("id") Dict dict) {

    model.addAttribute("dict", dict);
    return "/system/dict/add";
  }

  /**
   * 保存/修改 字典
   */
  @ActionLog(name = "字典管理", message = "字典：${title}", action = SaveAction.class)
  @RequiresPermissions({"system:dict:add", "system:dict:edit"})
  @ResponseBody
  @PostMapping("/save")
  public ResultVo<Object> save(@Validated DictValid dictValid, @EntityParam Dict dict) {
    dict.setValue(StrUtil.trim(dict.getValue()));
    User user = ShiroUtil.getSubject();
    if (dictService.repeatByName(dict)) {
      throw new ResultException(ResultEnum.DICT_EXIST);
    }
    Map<String, String> values = DictUtil.splitDictValue(dict.getValue());
    if (values == null) {
      throw new ResultException(ResultEnum.DICT_VALUES_ERROR);
    }
    dict.setValue(DictUtil.joinDictValue(values));
    if (dict.getId() != null) {
      Dict beDict = dictService.getById(dict.getId());
      // 非超级管理员不允许修改系统字典
      if (Objects.equals(beDict.getType(), DictConst.SYSTEM_DICT_VALUE)
          && !Objects.equals(user.getId(), AdminConst.ADMIN_ID)) {
        throw new ResultException(ResultEnum.DICT_SYSTEM_ERROR);
      }
      EntityBeanUtil.copyProperties(beDict, dict);
    }
    dictService.save(dict);
    if (StrUtil.isNotEmpty(dict.getName())) {
      DictUtil.clearCache(dict.getName());
    }
    return ResultVoUtil.SAVE_SUCCESS;
  }

  /**
   * 跳转到字典详情页
   */
  @RequiresPermissions({"system:dict:detail"})
  @GetMapping("/detail")
  public String toDetail(Model model, @RequestParam("id") Dict dict) {

    model.addAttribute("dict", dict);
    return "/system/dict/detail";
  }

  /**
   * 设置一条或者多条数据的状态
   */
  @ActionLog(name = "字典状态", action = StatusAction.class)
  @RequiresPermissions({"system:dict:status"})
  @ResponseBody
  @GetMapping("/status/{param}")
  public ResultVo<Object> status(
      @PathVariable("param") String param,
      @RequestParam(value = "ids", required = false) List<Long> ids
  ) {
    StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
    User user = ShiroUtil.getSubject();
    // 非超级管理员不允许修改系统字典
    if (dictService.systemDict(ids) && !Objects.equals(user.getId(), AdminConst.ADMIN_ID)) {
      throw new ResultException(ResultEnum.DICT_SYSTEM_ERROR);
    }
    if (dictService.updateStatus(statusEnum, ids)) {
      // 清空全部缓存
      DictUtil.clearAllCache();
      return ResultVoUtil.success(statusEnum.getMessage() + "成功");
    } else {
      return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
    }
  }

  /**
   * 清空字典缓存
   */
  @ActionLog(name = "字典管理", message = "清空字典缓存")
  @RequiresPermissions({"system:dict:cleanCache"})
  @ResponseBody
  @GetMapping("/cleanCache")
  public ResultVo<Object> cleanCache() {
    DictUtil.clearAllCache();
    return ResultVoUtil.success("字典缓存清空成功");
  }
}
