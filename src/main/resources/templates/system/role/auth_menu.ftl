<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>
  <link href="${basePath}/static/lib/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
  <style>
    .ztree {
      margin-left: 12px;
      margin-bottom: 70px;
    }

    .admin-compile .admin-finally {
      position: fixed;
      bottom: 0;
      left: 0;
      right: 0;
      padding-bottom: 14px;
      margin-bottom: 0;
      background-color: #ffffff;
    }
  </style>
</#assign>
<#assign body>
  <div class="layui-form admin-compile">
    <ul class="ztree" id="authTree" data-url="${basePath}/system/role/authMenuList?ids=${id?c}"></ul>
    <div class="layui-form-item admin-finally">
      <button id="submit" class="layui-btn"
              data-url="${basePath}/system/role/authMenu" data-id="${id?c}">
        <i class="fa fa-check-circle"></i> 保存
      </button>
      <button class="layui-btn btn-secondary close-popup">
        <i class="fa fa-times-circle"></i> 关闭
      </button>
    </div>
  </div>
</#assign>
<#assign script>
  <script src="${basePath}/static/lib/zTree/js/jquery.ztree.core.min.js"></script>
  <script src="${basePath}/static/lib/zTree/js/jquery.ztree.excheck.min.js"></script>
  <script>
    $(function () {
      var setting = {
        check: {
          enable: true,
          chkboxType: {"Y": "ps", "N": "ps"}
        },
        data: {
          simpleData: {
            enable: true
          }
        }
      };
      $.get($("#authTree").data("url"), function (result) {
        var keyPid = [];
        result.data.forEach(function (item) {
          keyPid[item.pid] = true;
        });
        var zNodes = [];
        result.data.forEach(function (item) {
          var menus = {
            id: item.id,
            pId: item.pid,
            name: item.title
          };
          if (item.pid === 0) {
            menus.open = true;
          }
          if (item.remark === "auth:true") {
            menus.checked = true;
          }
          zNodes.push(menus);
        });
        $.fn.zTree.init($("#authTree"), setting, zNodes);
      });

      $("#submit").click(function () {
        var zTreeObj = $.fn.zTree.getZTreeObj("authTree");
        var authList = zTreeObj.getCheckedNodes(true);
        var authIds = [];
        authIds.push("id=" + $(this).data("id"));
        authList.forEach(function (item) {
          if (item.id > 0) {
            authIds.push("authId=" + item.id);
          }
        });
        $.post($(this).data("url"), authIds.join("&"), function (result) {
          if (result.data == null) {
            result.data = 'submit[refresh]';
          }
          $.fn.Messager(result);
        });
      });
    });
  </script>
</#assign>
<@layout.default title='角色管理' style=style body=body script=script/>