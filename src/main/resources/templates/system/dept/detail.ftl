<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>

</#assign>
<#assign body>
  <div class="admin-detail-page">
    <div class="admin-detail-title">基本信息</div>
    <table class="layui-table admin-detail-table">
      <colgroup>
        <col width="100px">
        <col>
        <col width="100px">
        <col>
      </colgroup>
      <tbody>
      <tr>
        <th>主键ID</th>
        <td>${(dept.id?c)!''}</td>
        <th>部门名称</th>
        <td>${(dept.title)!''}</td>
      </tr>
      <tr>
        <th>父级编号</th>
        <td>${(dept.pid?c)!''}</td>
        <th>排序</th>
        <td>${(dept.sort?c)!''}</td>
      </tr>
      <tr>
        <th>创建者</th>
        <td>${(dept.createdBy.nickname)!''}</td>
        <th>修改者</th>
        <td>${((dept.lastModifiedBy.nickname)!'')}</td>
      </tr>
      <tr>
        <th>创建时间</th>
        <td>${(dept.createdDate?string('yyyy-MM-dd HH:mm'))!''}</td>
        <th>最后修改</th>
        <td>${(dept.lastModifiedDate?string('yyyy-MM-dd HH:mm'))!''}</td>
      </tr>
      <tr>
        <th>备注</th>
        <td colspan="4">${(dept.remark)!''}</td>
      </tr>
      </tbody>
    </table>
  </div>
</#assign>
<#assign script>

</#assign>
<@layout.default title='部门管理' style=style body=body script=script/>