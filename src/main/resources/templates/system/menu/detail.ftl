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
        <th width='100px'>功能编号</th>
        <td>${(menu.id?c)!''}</td>
        <th width='100px'>功能标题</th>
        <td>${(menu.title)!''}</td>
      </tr>
      <tr>
        <th width='100px'>图标</th>
        <td>${(menu.icon)!''}</td>
        <th width='100px'>功能父编号</th>
        <td>${(menu.pid?c)!}</td>
      </tr>
      <tr>
        <th width='100px'>URl地址</th>
        <td>${(menu.url)!''}</td>
        <th width='100px'>权限标识</th>
        <td>${(menu.perms)!''}</td>
      </tr>
      <tr>
        <th width='100px'>功能类型</th>
        <td>${(dictUtil.keyValue('FUN_TYPE',(menu.type?string)!''))!''}</td>
        <th width='100px'>本级排序</th>
        <td>${(menu.sort)!''}</td>
      </tr>
      <tr>
        <th>创建者</th>
        <td>${(menu.createdBy.nickname)!''}</td>
        <th>修改者</th>
        <td>${((menu.lastModifiedBy.nickname)!'')}</td>
      </tr>
      <tr>
        <th>创建时间</th>
        <td>${(menu.createdDate?string('yyyy-MM-dd HH:mm'))!''}</td>
        <th>最后修改</th>
        <td>${(menu.lastModifiedDate?string('yyyy-MM-dd HH:mm'))!''}</td>
      </tr>
      <tr>
        <th>备注</th>
        <td colspan="4">${(menu.remark)!''}</td>
      </tr>
      </tbody>
    </table>
  </div>
</#assign>
<#assign script>

</#assign>
<@layout.default title='功能管理' style=style body=body script=script/>