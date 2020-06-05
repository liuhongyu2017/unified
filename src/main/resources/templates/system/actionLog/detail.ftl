<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>

</#assign>
<#assign body>
  <div class="admin-detail-page">
    <div class="layui-card">
      <table class="layui-table admin-detail-table">
        <tbody>
        <tr>
          <th width='100px'>日志编号</th>
          <td>${(actionLog.id?c)!''}</td>
          <th width='100px'>日志名称</th>
          <td>${(actionLog.name)!''}</td>
        </tr>
        <tr>
          <th>日志类型</th>
          <td>${(dictUtil.keyValue('LOG_TYPE',(actionLog.type?string)!''))!''}</td>
          <th>操作IP地址</th>
          <td>${(actionLog.getIpAddr())!''}</td>
        </tr>
        <tr>
          <th>产生日志的表</th>
          <td>${(actionLog.model)!''}</td>
          <th>产生日志数据id</th>
          <td>${(actionLog.recordId?c)!''}</td>
        </tr>
        <tr>
          <th>操作人</th>
          <td>${(actionLog.operName)!''} [${(actionLog.operBy.id?c)!''}]</td>
          <th>记录时间</th>
          <td>${(actionLog.createdDate?string('yyyy-MM-dd HH:mm'))!''}</td>
        </tr>
        <tr>
          <th>切入点</th>
          <td colspan="4">${(actionLog.clazz)!''}.${(actionLog.method)!''}()</td>
        </tr>
        <tr>
          <th>日志消息</th>
          <td colspan="4">
            <pre>${(actionLog.message)!''}</pre>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</#assign>
<#assign script>

</#assign>
<@layout.default title='日志管理' style=style body=body script=script/>