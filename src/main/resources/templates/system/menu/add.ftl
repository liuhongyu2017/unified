<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>
  <link href="${basePath}/static/lib/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
</#assign>
<#assign body>
    <#assign funType = dictUtil.value('FUN_TYPE')>
  <div class="layui-form admin-compile">
    <form action="${basePath}/system/menu/save">
      <div class="row">
          <#if menu??>
            <input type="hidden" name="id" value="${(menu.id?c)!''}"/>
          </#if>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label required">标题</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="title" aria-label="" placeholder="请输入标题"
                     value="${(menu.title)!''}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label required">URL地址</label>
            <div class="layui-input-inline">
              <input class="layui-input url-input" type="text" name="url" aria-label=""
                     placeholder="请输入URL地址" value="${(menu.url)!''}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label required">权限标识</label>
            <div class="layui-input-inline">
              <input class="layui-input perms-input" type="text" name="perms" aria-label=""
                     placeholder="请输入权限标识" value="${(menu.perms)!''}">
            </div>
            <button class="layui-btn layui-btn-primary layui-btn-xs perms-refresh"
                    style="margin-top: 8px">
              <i class="layui-icon layui-icon-refresh" style="margin-right: 0"></i>
            </button>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label">功能图标</label>
            <div class="layui-input-inline">
              <input class="layui-input icon-input" type="text" name="icon" aria-label=""
                     placeholder="请输入功能图标" value="${(menu.icon)!''}">
            </div>
              <#if (menu.icon)??>
                <i class="icon-show ${(menu.icon)!''}" style="line-height: 38px;"></i>
              </#if>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label required">父级功能</label>
            <div class="layui-input-inline">
              <input class="layui-input select-tree" type="text" name="pid" aria-label=""
                     placeholder="请输入父级功能" value="${(pMenu.title)!'顶级功能'}"
                     data-url="${basePath}/system/menu/list" data-value="${(pMenu.id?c)!'0'}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label required">功能类型</label>
            <div class="layui-input-inline">
              <select name="type" aria-label="">
                  <#list funType?keys as key>
                    <option value="${key}"
                            <#if (((menu.type?c)!'') == key)!false>selected</#if>>${funType[key]}</option>
                  </#list>
              </select>
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label">排序</label>
            <div class="layui-input-inline">
              <select class="select-sort" name="sort" aria-label=""
                      data-url="${basePath}/system/menu/sortList"
                      data-id="${(menu.id?c)!''}"
                      data-sort="${(menu.sort)!''}"></select>
            </div>
            <div class="layui-input-info">（之后）</div>
          </div>
        </div>
        <div class="layui-col-sm12 layui-col-md12">
          <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
            <textarea class="layui-textarea" aria-label="" placeholder="请输入内容" name="remark">
              ${(menu.remark)!''}
            </textarea>
            </div>
          </div>
        </div>
        <div class="layui-form-item admin-finally">
          <button class="layui-btn ajax-submit">
            <i class="fa fa-check-circle"></i>
            保存
          </button>
          <button class="layui-btn btn-secondary close-popup">
            <i class="fa fa-times-circle"></i>
            关闭
          </button>
        </div>
      </div>
    </form>
  </div>
</#assign>
<#assign script>
  <script src="${basePath}/static/lib/zTree/js/jquery.ztree.core.min.js"></script>
  <script src="${basePath}/static/js/adminTree.js"></script>
  <script>
    layui.use(['form'], function () {
      window.form = layui.form;
      // 初始化排序下拉选项
      var pid = $(".select-tree").data('value');
      if (pid !== undefined) {
        sortRender({id: pid});
      }
    });

    // 初始化下拉树
    $.fn.selectTree({
      rootTree: '顶级功能',
      // 选中后事件
      onSelected: sortRender
    });

    // 更新渲染排序下拉选项
    function sortRender(treeNode) {
      var pid = treeNode.id;
      var sort = $(".select-sort");
      var id = sort.data('id') ? sort.data('id') : 0;
      var url = sort.data('url') + "/" + pid + "/" + id;
      $.get(url, function (result) {
        var options = '';
        var sortNum = Object.keys(result).length;
        if (pid === $(".select-tree").data('value') && sort.data('sort')) {
          sortNum = sort.data('sort') - 1;
        }
        result[0] = "首位";
        for (var key in result) {
          var selected = sortNum == key ? "selected=''" : "";
          options += "<option value='" + key + "' " + selected + ">" + result[key] + "</option>";
        }
        sort.html(options);
        form.render('select');
      });
    }

    // 监听变动图标
    $(".icon-input").on("input propertychange", function () {
      $(".icon-show").attr("class", "icon-show " + $(this).val());
    });

    // 同步操作权限输入框
    var $perms = $(".perms-input").val();
    $(".url-input").on("input propertychange", function () {
      if ($perms === '') {
        $(".perms-refresh").click();
      }
    });

    // 更新权限标识
    $(".perms-refresh").on("click", function (e) {
      e.preventDefault();
      var $perms = $(".perms-input");
      var url = $(".url-input").val().substr(1);
      var perms = url.replace(new RegExp('/', "g"), ':');
      $perms.val(perms);
    })
  </script>
</#assign>
<@layout.default title='功能管理' style=style body=body script=script/>