<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>

</#assign>
<#assign body>
  <div class="layui-form admin-compile">
    <form action="${basePath}/system/role/save">
        <#if role??>
          <input type="hidden" name="id" value="${(role.id?c)!''}"/>
        </#if>
      <div class="layui-form-item">
        <label class="layui-form-label required">角色标识</label>
        <div class="layui-input-inline">
          <input class="layui-input" type="text" name="name" aria-label="" placeholder="请输入角色编号"
                 value="${(role.name)!''}">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label required">角色名称</label>
        <div class="layui-input-inline">
          <input class="layui-input" type="text" name="title" aria-label="" placeholder="请输入角色名称"
                 value="${(role.title)!''}">
        </div>
      </div>
      <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">备注</label>
        <div class="layui-input-block">
          <textarea class="layui-textarea" aria-label="" placeholder="请输入内容" name="remark">
              ${(role.remark)!''}
          </textarea>
        </div>
      </div>
      <div class="layui-form-item admin-finally">
        <button class="layui-btn ajax-submit"><i class="fa fa-check-circle"></i> 保存</button>
        <button class="layui-btn btn-secondary close-popup"><i class="fa fa-times-circle"></i> 关闭
        </button>
      </div>
    </form>
  </div>
</#assign>
<#assign script>

</#assign>
<@layout.default title='角色管理' style=style body=body script=script/>