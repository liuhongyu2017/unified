<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#import '../../common/fragment.ftl' as fragment>
<#assign style>

</#assign>
<#assign body>
    <#assign dataStatus=dictUtil.value('DATA_STATUS')>
  <div class="layui-card">
    <div class="layui-card-header admin-card-header">
      <span><i class="fa fa-bars"></i> 角色管理</span>
      <i class="layui-icon layui-icon-refresh refresh-btn"></i>
    </div>
    <div class="layui-card-body">
      <div class="layui-row admin-card-screen">
        <div class="pull-left layui-form layui-form-pane admin-search-box">
          <div class="layui-form-item">
            <label class="layui-form-label">状态</label>
            <div class="layui-input-inline">
              <select name="status" aria-label="">
                  <#list dataStatus?keys as key>
                      <#if key != "3">
                        <option value="${key}"
                                <#if (RequestParameters['status'] == key)!false>selected</#if>>${dataStatus[key]}</option>
                      </#if>
                  </#list>
              </select>
            </div>

            <label class="layui-form-label">编号</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="name" aria-label="" autocomplete="off"
                     value="${(RequestParameters['name'])!''}" placeholder="请输入角色编号">
            </div>

            <label class="layui-form-label">名称</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="title" aria-label="" autocomplete="off"
                     value="${(RequestParameters['title'])!''}" placeholder="请输入角色名称">
            </div>

            <button class="layui-btn admin-search-btn">
              <i class="fa fa-search"></i>
            </button>
          </div>
        </div>
        <div class="pull-right screen-btn-group">
            <@shiro.hasPermission name="system:role:add">
              <button class="layui-btn open-popup"
                      data-title="添加角色"
                      data-url="${basePath}/system/role/add"
                      data-size="460,357">
                <i class="fa fa-plus"></i> 添加
              </button>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="system:role:authMenu">
              <button class="layui-btn open-popup-param"
                      data-type="radio"
                      data-title="授权功能"
                      data-url="${basePath}/system/role/authMenu"
                      data-size="300,500">
                <i class="fa fa-user-secret"></i> 授权菜单
              </button>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="system:role:authDept">
              <button class="layui-btn open-popup-param"
                      data-type="radio"
                      data-title="授权数据"
                      data-url="${basePath}/system/role/authDept"
                      data-size="300,500">
                <i class="fa fa-database"></i> 授权数据
              </button>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="system:role:status">
              <div class="btn-group">
                <button class="layui-btn">操作<span class="caret"></span></button>
                <dl class="layui-nav-child layui-anim layui-anim-upbit">
                  <dd>
                    <a class="ajax-status" href="${basePath}/system/role/status/ok">启用</a>
                  </dd>
                  <dd>
                    <a class="ajax-status" href="${basePath}/system/role/status/freezed">冻结</a>
                  </dd>
                  <dd>
                    <a class="ajax-status" href="${basePath}/system/role/status/delete">删除</a>
                  </dd>
                </dl>
              </div>
            </@shiro.hasPermission>
        </div>
      </div>
      <div class="admin-table-wrap">
        <table class="layui-table admin-table">
          <thead>
          <tr>
            <th class="admin-table-checkbox">
              <label class="admin-checkbox"><input type="checkbox">
                <i class="layui-icon layui-icon-ok"></i></label>
            </th>
            <th class="sortable" data-field="name">角色编号</th>
            <th class="sortable" data-field="title">角色名称</th>
            <th class="sortable" data-field="createdDate">创建时间</th>
            <th class="sortable" data-field="lastModifiedDate">更新时间</th>
              <@shiro.hasPermission name="system:role:userList">
                <th>查看</th>
              </@shiro.hasPermission>
            <th>操作</th>
          </tr>
          </thead>
          <tbody>
          <#list list as item>
            <tr>
              <td>
                <label class="admin-checkbox">
                  <input type="checkbox" value="${(item.id?c)!''}">
                  <i class="layui-icon layui-icon-ok"></i>
                </label>
              </td>
              <td>${(item.name)!''}</td>
              <td>${(item.title)!''}</td>
              <td>${(item.createdDate?string('yyyy-MM-dd HH:mm'))!''}</td>
              <td>${(item.lastModifiedDate?string('yyyy-MM-dd HH:mm'))!''}</td>
                <@shiro.hasPermission name="system:role:userList">
                  <td>
                    <a class="open-popup" href="#"
                       data-title="用户列表"
                       data-size="max"
                       data-url="${basePath}/system/role/userList?id=${(item.id?c)!''}">用户列表</a>
                  </td>
                </@shiro.hasPermission>
              <td>
                  <@shiro.hasPermission name="system:role:edit">
                    <a class="open-popup" href="#"
                       data-title="编辑角色"
                       data-url="${basePath}/system/role/edit?id=${(item.id?c)!''}"
                       data-size="460,357">编辑</a>
                  </@shiro.hasPermission>
                  <@shiro.hasPermission name="system:role:detail">
                    <a class="open-popup" href="#"
                       data-title="详细信息"
                       data-url="${basePath}/system/role/detail?id=${(item.id?c)!''}"
                       data-size="800,600">详细</a>
                  </@shiro.hasPermission>
                  <@shiro.hasPermission name="system:role:status">
                    <a class="ajax-get"
                       href="${basePath}/system/role/status/delete?ids=${(item.id?c)!''}"
                       data-msg="您是否确认删除">删除</a>
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
<@layout.page title='角色管理' style=style body=body script=script/>