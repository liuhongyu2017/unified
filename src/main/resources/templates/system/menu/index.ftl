<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>
  <link href="${basePath}/static/lib/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
</#assign>
<#assign body>
    <#assign dataStatus=dictUtil.value('DATA_STATUS')>
  <div class="layui-card admin-tree"
       data-url="${basePath}/system/menu/list${('?' + search)!''}">
    <div class="layui-card-header admin-card-header">
        <span>
          <i class="fa fa-bars"></i>
          功能管理
        </span>
      <i class="layui-icon layui-icon-refresh refresh-btn"></i>
    </div>
    <div class="layui-card-body layui-row">
      <div class="layui-col-lg2 layui-col-sm3">
        <div class="layui-card admin-nav-tree">
          <div class="layui-card-header">功能结构</div>
          <div class="layui-card-body">
            <ul class="ztree"></ul>
          </div>
        </div>
      </div>
      <div class="layui-col-lg10 layui-col-sm9">
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

              <label class="layui-form-label">名称</label>
              <div class="layui-input-inline">
                <input class="layui-input" type="text" name="title"
                       aria-label="" placeholder="请输入功能名称" autocomplete="off"
                       value="${(RequestParameters['title'])!''}">
              </div>

              <label class="layui-form-label">URL地址</label>
              <div class="layui-input-inline">
                <input class="layui-input" type="text" name="url"
                       aria-label="" placeholder="请输入功能URL地址" autocomplete="off"
                       value="${(RequestParameters['url'])!''}">
              </div>

              <button class="layui-btn admin-search-btn">
                <i class="fa fa-search"></i>
              </button>
            </div>
          </div>
          <div class="pull-right screen-btn-group">
              <@shiro.hasPermission name="system:menu:add">
                <button class="layui-btn open-popup popup-add"
                        data-title="添加功能"
                        data-url="${basePath}/system/menu/add">
                  <i class="fa fa-plus"></i>添加
                </button>
              </@shiro.hasPermission>
              <@shiro.hasPermission name="system:menu:status">
                <div class="btn-group">
                  <button class="layui-btn">操作
                    <span class="caret"></span>
                  </button>
                  <dl class="layui-nav-child layui-anim layui-anim-upbit">
                    <dd>
                      <a class="ajax-status" href="${basePath}/system/menu/status/ok">启用</a>
                    </dd>
                    <dd>
                      <a class="ajax-status" href="${basePath}/system/menu/status/freezed">冻结</a>
                    </dd>
                    <dd>
                      <a class="ajax-status" href="${basePath}/system/menu/status/delete">删除</a>
                    </dd>
                  </dl>
                </div>
              </@shiro.hasPermission>
          </div>
        </div>
        <div class="admin-table-wrap">
          <table class="layui-table admin-table admin-tree-table">
            <thead>
            <tr>
              <th class="admin-table-checkbox">
                <label class="admin-checkbox">
                  <input type="checkbox">
                  <i class="layui-icon layui-icon-ok"></i>
                </label>
              </th>
              <th>名称</th>
              <th>URL地址</th>
              <th>权限标识</th>
              <th>功能类型</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody class="tree-hide">
            <tr
                class="{{$hide}}"
                tree-pid="{{pid}}"
                tree-id="{{id}}">
              <td>
                <label class="admin-checkbox">
                  <input
                      type="checkbox"
                      value="{{id}}">
                  <i class="layui-icon layui-icon-ok"></i>
                </label>
              </td>
              <td>{{title}}</td>
              <td>{{url}}</td>
              <td>{{perms}}</td>
              <td>{{type}}</td>
              <td>{{status}}</td>
              <td>
                  <@shiro.hasPermission name="system:menu:edit">
                    <a class="open-popup popup-edit" href="#"
                       data-title="编辑功能"
                       data-url="${basePath}/system/menu/edit?id={{id}}">编辑</a>
                  </@shiro.hasPermission>
                  <@shiro.hasPermission name="system:menu:detail">
                    <a class="open-popup" href="#"
                       data-title="详细信息"
                       data-url="${basePath}/system/menu/detail?id={{id}}"
                       data-size="800,600">详细</a>
                  </@shiro.hasPermission>
                  <@shiro.hasPermission name="system:menu:status">
                    <a class="ajax-get popup-delete"
                       data-msg="您是否确定删除"
                       href="${basePath}/system/menu/status/delete?ids={{id}}">删除
                    </a>
                  </@shiro.hasPermission>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</#assign>
<#assign script>
  <script src="${basePath}/static/lib/zTree/js/jquery.ztree.core.min.js"></script>
  <script src="${basePath}/static/lib/zTree/js/jquery.ztree.exedit.min.js"></script>
  <script src="${basePath}/static/js/adminTree.js"></script>
  <script>
    $.fn.adminTree();
  </script>
</#assign>
<@layout.page title='功能管理' style=style body=body script=script/>