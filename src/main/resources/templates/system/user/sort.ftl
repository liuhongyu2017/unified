<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>
  <link href="${basePath}/static/lib/dad/jquery.dad.css" rel="stylesheet">
  <style>
    .sort-box {
      padding: 5px;
    }

    .sort-box .sort-item {
      box-sizing: border-box;
      width: 20%;
      float: left;
      display: block;
      position: relative;
      padding: 5px;
    }

    .sort-box .sort-item .sort-text {
      display: block;
      height: 50px;
      line-height: 50px;
      text-align: center;
      font-size: 1.5em;
      font-weight: bold;
      background: #3977CE;
      color: white;
    }
  </style>
</#assign>
<#assign body>
  <div class="admin-compile">
    <div class="layui-row">
      <div class="sort-box" data-url="${basePath}/system/user/sort?id=${(dept.id?c)!''}">
          <#list list as item>
            <div class="sort-item"
                 data-id="${(item.id?c)!''}"
                 data-sort="${(item.sort?c)!''}">
              <div class="sort-text">${(item.nickname)!''}</div>
            </div>
          </#list>
      </div>
    </div>
    <div class="admin-finally">
      <button class="layui-btn" id="save"><i class="fa fa-check-circle"></i> 保存</button>
      <button class="layui-btn btn-secondary close-popup"><i class="fa fa-times-circle"></i>
        关闭
      </button>
    </div>
  </div>
</#assign>
<#assign script>
  <script src="${basePath}/static/lib/dad/jquery.dad.js"></script>
  <script>
    $(document).ready(function () {
      var sort = $('.sort-box');

      var dad = sort.dad({
        callback: function (e) {

        }
      });

      // 保存
      $('#save').click(function () {
        var ids = [];
        $('.sort-item').each(function () {
          var id = $(this).data('id');
          var sort = $(this).data('sort');
          var dadId = $(this).data('data-id');
          var dadPosition = $(this).data('dad-position');
          ids.push(id);
        });
        $.post(sort.data('url'), {
          ids: ids
        }, function (result) {
          if (result.data == null) {
            result.data = 'submit[refresh]';
          }
          $.fn.Messager(result);
        })
      })
    })
  </script>
</#assign>
<@layout.default title='用户管理' style=style body=body script=script/>