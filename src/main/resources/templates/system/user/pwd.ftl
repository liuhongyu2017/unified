<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>

</#assign>
<#assign body>
  <div class="layui-form admin-compile">
    <form action="${basePath}/system/user/pwd">
        <#list idList as id>
          <input type="hidden" name="ids" value="${(id?c)!''}"/>
        </#list>
      <div class="layui-form-item">
        <label class="layui-form-label">新密码</label>
        <div class="layui-input-inline">
          <input class="layui-input" type="password" name="password" aria-label=""
                 placeholder="请输入新密码">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">确认密码</label>
        <div class="layui-input-inline">
          <input class="layui-input" type="password" name="confirm" aria-label=""
                 placeholder="再一次输入密码">
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