<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#import '../../common/fragment.ftl' as fragment>
<#assign style>

</#assign>
<#assign body>
    <#assign dataStatus=dictUtil.value('DATA_STATUS')>
    <#assign dictType=dictUtil.value('DICT_TYPE')>
  <div class="layui-card">
    <div class="layui-card-header admin-card-header">
      <span><i class="fa fa-bars"></i> 字典管理</span>
      <i class="layui-icon layui-icon-refresh refresh-btn"></i>
    </div>
    <div class="layui-card-body">
      <div class="layui-row admin-card-screen put-row">
        <div class="pull-left layui-form layui-form-pane admin-search-box">
          <div class="layui-form-item">
            <label class="layui-form-label">字典类型</label>
            <div class="layui-input-inline">
              <select name="type" aria-label="">
                <option value="">全部</option>
                  <#list dictType?keys as key>
                    <option value="${key}"
                            <#if (RequestParameters['type'] == key)!false>selected</#if>>${dictType[key]}</option>
                  </#list>
              </select>
            </div>

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

            <label class="layui-form-label">字典标识</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="name" placeholder="请输入字典标识"
                     aria-label="" autocomplete="off" value="${RequestParameters['name']!''}">
            </div>

            <label class="layui-form-label">字典标题</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="title" placeholder="请输入字典标题"
                     aria-label="" autocomplete="off" value="${RequestParameters['title']!''}">
            </div>

            <button type="button" class="layui-btn admin-search-btn">
              <i class="fa fa-search"></i>
            </button>
          </div>
        </div>
        <div class="pull-right screen-btn-group">
          <div class="btn-group-right">
              <@shiro.hasPermission name="system:dict:add">
                <button class="layui-btn open-popup"
                        data-title="添加字典" data-size="auto"
                        data-url="${basePath}/system/dict/add">
                  <i class="fa fa-plus"></i> 添加
                </button>
              </@shiro.hasPermission>
              <@shiro.hasPermission name="system:dict:cleanCache">
                <button class="layui-btn ajax-get"
                        data-msg="<span style='color: red'>清空缓存后，字典数据将从数据库中重新读取</span>，是否继续"
                        data-url="${basePath}/system/dict/cleanCache">
                  <i class="fa fa-microchip"></i> 清除字典缓存
                </button>
              </@shiro.hasPermission>
              <@shiro.hasPermission name="system:dict:status">
                <div class="btn-group">
                  <button class="layui-btn">操作<span class="caret"></span></button>
                  <dl class="layui-nav-child layui-anim layui-anim-upbit">
                    <dd>
                      <a class="ajax-status" href="${basePath}/system/dict/status/ok">启用</a>
                    </dd>
                    <dd>
                      <a class="ajax-status" href="${basePath}/system/dict/status/freezed">冻结</a>
                    </dd>
                    <dd>
                      <a class="ajax-status" href="${basePath}/system/dict/status/delete">删除</a>
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
            <th class="sortable" data-field="name">字典标识</th>
            <th class="sortable" data-field="title">字典标题</th>
            <th class="sortable" data-field="type">字典类型</th>
            <th>字典值</th>
            <th class="sortable" data-field="createdDate">创建时间</th>
            <th class="sortable" data-field="lastModifiedDate">修改时间</th>
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
              <td>${(item.name)!''}</td>
              <td>${(item.title)!''}</td>
              <td>${(dictUtil.keyValue('DICT_TYPE',(item.type?string)!''))!''}</td>
              <td>${(item.value)!''}</td>
              <td>${(item.createdDate?string('yyyy-MM-dd HH:mm'))!''}</td>
              <td>${(item.lastModifiedDate?string('yyyy-MM-dd HH:mm'))!''}</td>
              <td>${(dictUtil.keyValue('DATA_STATUS',(item.status?string)!''))!''}</td>
              <td>
                  <@shiro.hasPermission name="system:dict:edit">
                    <a class="open-popup" href="#"
                       data-title="编辑字典"
                       data-url="${basePath}/system/dict/edit?id=${(item.id?c)!''}">编辑</a>
                  </@shiro.hasPermission>
                  <@shiro.hasPermission name="system:dict:detail">
                    <a class="open-popup" href="#"
                       data-title="详细信息"
                       data-url="${basePath}/system/dict/detail?id=${(item.id?c)!''}">详情</a>
                  </@shiro.hasPermission>
                  <@shiro.hasPermission name="system:dict:status">
                    <a class="ajax-get"
                       data-msg="您是否删除[${item.title!''}]"
                       href="${basePath}/system/dict/status/delete?ids=${(item.id?c)!''}">删除</a>
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
<@layout.page title='字典管理' style=style body=body script=script/>
