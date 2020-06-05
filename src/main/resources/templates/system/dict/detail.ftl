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
        <th width='100px'>字典编号</th>
        <td>${(dict.id?c)!''}</td>
        <th width='100px'>字典标识</th>
        <td>${(dict.name)!''}</td>
      </tr>
      <tr>
        <th width='100px'>字典标题</th>
        <td>${(dict.title)!''}</td>
        <th width='100px'>字典类型</th>
        <td>${(dictUtil.keyValue('DICT_TYPE',(dict.type?c)!'')!'')}</td>
      </tr>
      <tr>
        <th>字典值</th>
        <td colspan="4">
          <div class="detail-remark">${(dict.value)!''}</div>
        </td>
      </tr>
      <tr>
        <th>创建者</th>
        <td>${(dict.createdBy.nickname)!''}</td>
        <th>修改者</th>
        <td>${((dict.lastModifiedBy.nickname)!'')}</td>
      </tr>
      <tr>
        <th>创建时间</th>
        <td>${(dict.createdDate?string('yyyy-MM-dd HH:mm'))!''}</td>
        <th>最后修改</th>
        <td>${(dict.lastModifiedDate?string('yyyy-MM-dd HH:mm'))!''}</td>
      </tr>
      <tr>
        <th>备注</th>
        <td colspan="4">${(dict.remark)!''}</td>
      </tr>
      </tbody>
    </table>
  </div>
</#assign>
<#assign script>

</#assign>
<@layout.default title='字典管理' style=style body=body script=script/>