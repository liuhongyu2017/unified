<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>
  <style>
    a {
      color: #009688;
    }

    a:hover {
      color: #004a43;
      text-decoration: underline;
    }
  </style>
</#assign>
<#assign body>
  <div class="admin-detail-page">
    <table class="layui-table">
      <thead>
      <tr>
        <th>用户名</th>
        <th>用户昵称</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <#list list as item>
        <tr>
          <td>${(item.username)!''}</td>
          <td>${(item.nickname)!''}</td>
          <td><a href="${basePath}/system/user/index?id=${(item.id?c)!''}">查看</a></td>
        </tr>
      </#list>
      <#if list?size <= 0>
        <tr>
          <td style="text-align: center" colspan="3">未有分配该角色的用户</td>
        </tr>
      </#if>
      </tbody>
    </table>
  </div>
</#assign>
<#assign script>

</#assign>
<@layout.default title='角色管理' style=style body=body script=script/>