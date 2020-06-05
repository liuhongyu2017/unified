<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>
  <link href="${basePath}/static/lib/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
</#assign>
<#assign body>
  <div class="layui-form admin-compile">
    <form action="${basePath}/system/dept/save">
      <div class="row">
          <#if dept??>
            <input type="hidden" name="id" value="${(dept.id?c)!''}"/>
          </#if>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label required">部门名称</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="title" aria-label="" placeholder="请输入标题"
                     value="${(dept.title)!''}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label required">父级部门</label>
            <div class="layui-input-inline">
              <input class="layui-input select-tree" type="text" name="pid" aria-label=""
                     placeholder="请输入父级部门" value="${(pDept.title)!''}"
                     data-url="${basePath}/system/dept/list"
                     data-value="${(pDept.id?c)!''}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label">排序</label>
            <div class="layui-input-inline">
              <select class="select-sort" name="sort" aria-label=""
                      data-url="${basePath}/system/dept/sortList"
                      data-id="${(dept.id?c)!''}"
                      data-sort="${(dept.sort)!''}"></select>
            </div>
            <div class="layui-input-info">（之后）</div>
          </div>
        </div>
        <div class="layui-col-sm12 layui-col-md12">
          <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
              <textarea class="layui-textarea" aria-label="" placeholder="请输入内容" name="remark">
                ${(dept.remark)!''}
              </textarea>
            </div>
          </div>
        </div>
        <div class="layui-form-item admin-finally">
          <button class="layui-btn ajax-submit"><i class="fa fa-check-circle"></i> 保存</button>
          <button class="layui-btn btn-secondary close-popup"><i class="fa fa-times-circle"></i> 关闭
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
      rootTree: '顶级',
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
  </script>
</#assign>
<@layout.default title='部门管理' style=style body=body script=script/>