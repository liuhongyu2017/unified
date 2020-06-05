<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#import '../../common/fragment.ftl' as fragment>
<#assign style>

</#assign>
<#assign body>
    <#assign logType=dictUtil.value('LOG_TYPE')>
  <div class="layui-card">
    <div class="layui-card-header admin-card-header">
      <span><i class="fa fa-bars"></i> 日志管理</span>
      <i class="layui-icon layui-icon-refresh refresh-btn"></i>
    </div>
    <div class="layui-card-body">
      <div class="layui-row admin-card-screen">
        <div class="pull-left layui-form layui-form-pane admin-search-box">
          <div class="layui-form-item">
            <label class="layui-form-label">类型</label>
            <div class="layui-input-inline">
              <select name="type" aria-label="">
                <option value="">全部</option>
                  <#list logType?keys as key>
                    <option value="${key}"
                            <#if (RequestParameters['type'] == key)!false>selected</#if>>${logType[key]}</option>
                  </#list>
              </select>
            </div>

            <label class="layui-form-label">日志名称 </label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="name" aria-label="" placeholder="请输入日志名称"
                     autocomplete="off" value="${(RequestParameters['name'])!''}">
            </div>

            <button class="layui-btn admin-search-btn">
              <i class="fa fa-search"></i>
            </button>
          </div>
        </div>
        <div class="pull-right screen-btn-group">
            <@shiro.hasPermission name="system:actionLog:emptyLog">
              <a class="layui-btn ajax-get"
                 data-msg="您是否确认清空日志"
                 href="${basePath}/system/actionLog/emptyLog">
                <i class="fa fa-trash"></i> 清空日志</a>
            </@shiro.hasPermission>
        </div>
      </div>
      <div class="admin-table-wrap">
        <table class="layui-table admin-table admin-table-fixed">
          <thead>
          <tr>
            <th class="sortable" data-field="name">日志名称</th>
            <th class="sortable" data-field="operBy">操作人</th>
            <th class="sortable" data-field="type">日志类型</th>
            <th class="sortable" data-field="ipaddr">IP地址</th>
            <th class="sortable" data-field="message">日志消息</th>
            <th class="sortable" data-field="createdDate">记录时间</th>
            <th>操作</th>
          </tr>
          </thead>
          <tbody>
          <#list list as item>
            <tr>
              <td>${(item.name)!''}</td>
              <td>${(item.operName)!''}</td>
              <td>${(dictUtil.keyValue('LOG_TYPE',(item.type?string)!''))!''}</td>
              <td>${(item.getIpAddr())!''}</td>
              <td>${(item.message)!''}</td>
              <td>${(item.createdDate?string('yyyy-MM-dd HH:mm'))!''}</td>
              <td>
                  <@shiro.hasPermission name="system:actionLog:detail">
                    <a class="open-popup" href="#"
                       data-title="详细信息"
                       data-url="${basePath}/system/actionLog/detail?id=${(item.id?c)!''}"
                       data-size="max">详细</a>
                  </@shiro.hasPermission>
              </td>
            </tr>
          </#list>
          </tbody>
        </table>
      </div>
        <@fragment.pageFragment page=page/>
    </div>
  </div>
</#assign>
<#assign script>

</#assign>
<@layout.page title='日志管理' style=style body=body script=script/>