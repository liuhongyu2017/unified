<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>

</#assign>
<#assign body>
  <div class="admin-detail-page">
    <div class="admin-detail-title">基本信息</div>
    <table class="layui-table admin-detail-table">
      <tbody>
      <tr>
        <th width='100px'>角色ID</th>
        <td>${(role.id?c)!''}</td>
        <th width='100px'>角色编号名称</th>
        <td>${(role.title)!''}（${(role.name)!''}）</td>
      </tr>
      <tr>
        <th>创建者</th>
        <td>${(role.createdBy.nickname)!''}</td>
        <th>修改者</th>
        <td>${((role.lastModifiedBy.nickname)!'')}</td>
      </tr>
      <tr>
        <th>创建时间</th>
        <td>${(role.createdDate?string('yyyy-MM-dd HH:mm'))!''}</td>
        <th>最后修改</th>
        <td>${(role.lastModifiedDate?string('yyyy-MM-dd HH:mm'))!''}</td>
      </tr>
      <tr>
        <th>备注</th>
        <td colspan="4">${(role.remark)!''}</td>
      </tr>
      </tbody>
    </table>
  </div>
</#assign>
<#assign script>

</#assign>
<@layout.default title='角色管理' style=style body=body script=script/>