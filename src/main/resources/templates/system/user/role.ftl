<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>
  <style>
    .layui-input-block {
      margin-left: 20px;
      margin-right: 20px;
      margin-bottom: 70px;
    }

    .admin-compile .admin-finally {
      position: fixed;
      bottom: 0;
      left: 0;
      right: 0;
      padding-bottom: 14px;
      margin-bottom: 0;
      background-color: #ffffff;
    }
  </style>
</#assign>
<#assign body>
  <div class="layui-form admin-compile">
    <form action="${basePath}/system/user/role">
      <input type="hidden" name="id" value="${(id?c)!''}"/>
      <div class="layui-form-item">
        <div class="layui-input-block">
            <#list list as item>
              <input type="checkbox" name="roleId" title="${(item.title)!''}"
                     value="${(item.id?c)!''}" <#if authRoles?seq_contains(item)>checked</#if>>
            </#list>
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
<@layout.default title='用户管理' style=style body=body script=script/>