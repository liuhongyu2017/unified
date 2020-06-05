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
    <ul class="ztree" id="deptTree"
        data-url="${basePath}/system/user/deptList"></ul>
    <div class="layui-form-item admin-finally">
      <button id="btn" class="layui-btn"
              data-url="${basePath}/system/user/sort">
        <i class="fa fa-check-circle"></i> 确认
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
          chkStyle: "radio",
          radioType: "all",
          chkboxType: {"Y": "ps", "N": "ps"}
        },
        data: {
          simpleData: {
            enable: true
          }
        }
      };
      $.get($("#deptTree").data("url"), function (result) {
        var keyPid = [];
        result.data.forEach(function (item) {
          keyPid[item.pid] = true;
        });
        var zNodes = [];
        result.data.forEach(function (item) {
          var menus = {
            id: item.id,
            pId: item.pid,
            name: item.title,
            open: true
          };
          zNodes.push(menus);
        });
        $.fn.zTree.init($("#deptTree"), setting, zNodes);
      });

      $("#btn").click(function () {
        var zTreeObj = $.fn.zTree.getZTreeObj("deptTree");
        var deptList = zTreeObj.getCheckedNodes(true);
        if (!(deptList.length > 0 && deptList[0] && deptList[0].id)) {
          layer.msg("请选择部门！", {offset: '15px', time: 3000, icon: 1});
        } else {
          var id = deptList[0].id;
          window.location.href = $(this).data("url") + "?id=" + id;
        }
      });
    });
  </script>
</#assign>
<@layout.default title='用户管理' style=style body=body script=script/>