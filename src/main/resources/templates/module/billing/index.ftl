<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#import '../../common/fragment.ftl' as fragment>
<#assign style>

</#assign>
<#assign body>
  <div class="layui-card">
    <div class="layui-card-header admin-card-header">
      <span><i class="fa fa-bars"></i> 账单管理</span>
      <i class="layui-icon layui-icon-refresh refresh-btn"></i>
    </div>
    <div class="layui-card-body">
      <div class="layui-row admin-card-screen put-row">
        <div class="pull-right">
          <div class="btn-group-right">
            <button class="layui-btn open-popup"
                    data-title="统计" data-size="max"
                    data-url="${basePath}/module/billing/statistics">
              <i class="fa fa-bar-chart"></i> 统计
            </button>
            <button class="layui-btn open-popup"
                    data-title="添加账单" data-size="auto"
                    data-url="${basePath}/module/billing/add">
              <i class="fa fa-plus"></i> 添加
            </button>
          </div>
        </div>
      </div>
      <div class="admin-table-wrap">
        <table class="layui-table admin-table">
          <thead>
          <tr>
            <th>创建人</th>
            <th class="sortable" data-field="date">日期</th>
            <th>金额</th>
            <th>备注</th>
            <th>类型</th>
            <th>修改人</th>
            <th>操作</th>
          </tr>
          </thead>
          <tbody>
          <#list list as item>
            <tr>
              <td>${(item.createdBy.nickname)!''}</td>
              <td>${(item.date?string('yyyy-MM-dd'))!''}</td>
              <td>${((item.amount/100)!0)?string("##0.00")}</td>
                <#if ((item.remark)!'')?length lt 23>
                  <td>${(item.remark)!''}</td>
                <#else>
                  <td>${((item.remark)!'')?substring(0,23)}...</td>
                </#if>
              <td>${(dictUtil.keyValue('BILLING_TYPE',(item.type?string)!''))!''}</td>
              <td>${(item.lastModifiedBy.nickname)!''}</td>
              <td>
                <a class="open-popup" href="#"
                   data-title="编辑"
                   data-url="${basePath}/module/billing/edit?id=${(item.id?c)!''}">编辑</a>
                <a class="ajax-get popup-delete"
                   data-msg="您是否确定删除"
                   href="${basePath}/module/billing/delete?id=${(item.id?c)!''}">删除
                </a>
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
<@layout.page title='账单管理' style=style body=body script=script/>