<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#import '../../common/fragment.ftl' as fragment>
<#assign style>
  <link href="${basePath}/static/lib/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
</#assign>
<#assign body>
    <#assign userJob=dictUtil.value('USER_JOB')>
    <#assign dataStatus=dictUtil.value('DATA_STATUS')>
  <div class="layui-card">
    <div class="layui-card-header admin-card-header">
      <span><i class="fa fa-bars"></i> 用户管理</span>
      <i class="layui-icon layui-icon-refresh refresh-btn"></i>
        <@shiro.hasPermission name="system:user:export">
          <a href="${basePath}/system/user/export"><i class="fa fa-download"></i></a>
        </@shiro.hasPermission>
    </div>
    <div class="layui-card-body">
      <div class="layui-row admin-card-screen put-row">
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

            <label class="layui-form-label">用户名</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="username" placeholder="请输入用户名"
                     autocomplete="off" aria-label=""
                     value="${(RequestParameters['username'])!''}">
            </div>

            <label class="layui-form-label">用户昵称</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="nickname" placeholder="请输入用户昵称"
                     autocomplete="off" aria-label=""
                     value="${(RequestParameters['nickname'])!''}">
            </div>

            <label class="layui-form-label">所在部门</label>
            <div class="layui-input-inline">
              <input class="layui-input select-tree" type="text" name="dept" aria-label=""
                     placeholder="请选择部门" value="${(dept.title)!''}"
                     data-url="${basePath}/system/dept/list"
                     data-value="${(dept.id?c)!''}">
            </div>

            <label class="layui-form-label">职务</label>
            <div class="layui-input-inline">
              <select name="job" placeholder="请选择职务" aria-label="">
                <option value="">全部</option>
                  <#list userJob?keys as key>
                    <option value="${key}"
                            <#if (((RequestParameters['job'])!'') == key)!false>selected</#if>>${userJob[key]}</option>
                  </#list>
              </select>
            </div>

            <button type="button" class="layui-btn admin-search-btn">
              <i class="fa fa-search"></i>
            </button>
          </div>
        </div>
        <div class="pull-right screen-btn-group">
          <div class="btn-group-left">
              <@shiro.hasPermission name="system:user:pwd">
                <button class="layui-btn open-popup-param"
                        data-title="修改密码" data-url="${basePath}/system/user/pwd">
                  <i class="fa fa-refresh"></i> 修改密码
                </button>
              </@shiro.hasPermission>
              <@shiro.hasPermission name="system:user:role">
                <button class="layui-btn open-popup-param"
                        data-type="radio"
                        data-title="角色分配"
                        data-url="${basePath}/system/user/role">
                  <i class="fa fa-user-secret"></i> 角色分配
                </button>
              </@shiro.hasPermission>
              <@shiro.hasPermission name="system:user:sort">
                <button class="layui-btn open-popup"
                        data-title="用户排序"
                        data-size="max"
                        data-url="${basePath}/system/user/dept">
                  <i class="fa fa-user-secret"></i> 用户排序
                </button>
              </@shiro.hasPermission>
          </div>
          <div class="btn-group-right">
              <@shiro.hasPermission name="system:user:add">
                <button class="layui-btn open-popup"
                        data-title="添加用户"
                        data-size="auto"
                        data-url="${basePath}/system/user/add">
                  <i class="fa fa-plus"></i> 添加
                </button>
              </@shiro.hasPermission>
              <@shiro.hasPermission name="system:user:status">
                <div class="btn-group">
                  <button class="layui-btn">操作<span class="caret"></span></button>
                  <dl class="layui-nav-child layui-anim layui-anim-upbit">
                    <dd>
                      <a class="ajax-status" href="${basePath}/system/user/status/ok">启用</a>
                    </dd>
                    <dd>
                      <a class="ajax-status" href="${basePath}/system/user/status/freezed">冻结</a>
                    </dd>
                    <dd>
                      <a class="ajax-status" href="${basePath}/system/user/status/delete">删除</a>
                    </dd>
                  </dl>
                </div>
              </@shiro.hasPermission>
          </div>
        </div>
      </div>
      <div class="admin-table-wrap">
        <table class="layui-table admin-table">
          <thead>
          <tr>
            <th class="admin-table-checkbox">
              <label class="admin-checkbox">
                <input type="checkbox"><i class="layui-icon layui-icon-ok"></i>
              </label>
            </th>
            <th class="sortable" data-field="username">用户名</th>
            <th class="sortable" data-field="nickname">用户昵称</th>
            <th>部门</th>
            <th class="sortable" data-field="job">职务</th>
            <th class="sortable" data-field="sex">性别</th>
            <th class="sortable" data-field="mobilePhone">移动电话</th>
            <th class="sortable" data-field="email">邮箱</th>
            <th class="sortable" data-field="createdDate">创建时间</th>
            <th>状态</th>
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
              <td>${(item.username)!''}</td>
              <td>${(item.nickname)!''}</td>
              <td>${(item.dept.title)!''}</td>
              <td>${(dictUtil.keyValue('USER_JOB',(item.job?string)!''))!''}</td>
              <td>${(dictUtil.keyValue('USER_SEX',(item.sex?string)!''))!''}</td>
              <td>${(item.mobilePhone)!''}</td>
              <td>${(item.email)!''}</td>
              <td>${(item.createdDate?string('yyyy-MM-dd HH:mm'))!''}</td>
              <td>${(dictUtil.keyValue('DATA_STATUS',(item.status?string)!''))!''}</td>
              <td>
                  <@shiro.hasPermission name="system:user:edit">
                    <a class="open-popup" href="#"
                       data-title="编辑用户"
                       data-url="${basePath}/system/user/edit?id=${(item.id?c)!''}">编辑</a>
                  </@shiro.hasPermission>
                  <@shiro.hasPermission name="system:user:status">
                    <a class="ajax-get"
                       data-msg="您是否删除${('[' + item.nickname +']')!''}"
                       href="${basePath}/system/user/status/delete?ids=${(item.id?c)!''}">删除</a>
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
  <script src="${basePath}/static/lib/zTree/js/jquery.ztree.core.min.js"></script>
  <script src="${basePath}/static/js/adminTree.js"></script>
  <script type="text/javascript">
    // 树形菜单
    $.fn.selectTree();
  </script>
</#assign>
<@layout.page title='用户管理' style=style body=body script=script/>
