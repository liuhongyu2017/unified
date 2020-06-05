<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>
  <link href="${basePath}/static/lib/layui/modules/timePicker/css/timepicker.css" rel="stylesheet">
</#assign>
<#assign body>
  <div class="admin-detail-page">
    <div class="admin-detail-title">
      <div class="layui-form-item">
        <label class="layui-form-label required">时间范围</label>
        <div class="layui-input-inline">
          <input class="layui-input" type="text" aria-label="" id="dateBetween">
        </div>
      </div>
      <div class="layui-form-item layui-form">
        <div class="layui-input-block">
            <#list users as user>
              <input type="checkbox" name="userIds" title="${(user.nickname)!''}"
                     value="${(user.id?c)!''}" checked>
            </#list>
        </div>
      </div>
    </div>
      <#if statistics??>
        <table class="layui-table admin-detail-table">
          <tr>
            <td>日期</td>
            <td>${(statistics.startDate?string('yyyy-MM-dd'))!''}
              至 ${(statistics.endDate?string('yyyy-MM-dd'))!''}</td>
            <td>总金额</td>
            <td>${((statistics.totalAmount/100)!0)?string('##00.00')}</td>
            <td>平均金额</td>
            <td>${((statistics.averageAmount/100)!0)?string('##00.00')}</td>
          </tr>
            <#list statistics.details as detail>
              <tr>
                <td>姓名</td>
                <td>${(detail.user.nickname)!''}</td>
                <td>消费金额</td>
                <td>${((detail.amount/100)!0)?string('##00.00')}</td>
                <td>减去平均金额</td>
                <td>${((detail.money/100)!0)?string('##00.00')}</td>
              </tr>
            </#list>
        </table>
      </#if>
    <div class="layui-form-item admin-finally">
      <button class="layui-btn" id="enter">
        <i class="fa fa-check-circle"></i> 确认
      </button>
      <button class="layui-btn btn-secondary close-popup">
        <i class="fa fa-times-circle"></i> 关闭
      </button>
    </div>
  </div>
</#assign>
<#assign script>
  <script>
    layui.extend({
      timePicker: 'timePicker/js/timePicker'
    }).use(['timePicker', 'element', 'form'], function () {
      var timePicker = layui.timePicker;

      var $enter = $('#enter');
      var $dateBetween = $('#dateBetween');

      timePicker.render({
        elem: '#dateBetween',
        options: {
          // true开启时间戳 开启后format就不需要配置，false关闭时间戳
          timeStamp: false,
          // 格式化时间具体可以参考moment.js官网 默认是YYYY-MM-DD HH:ss:mm
          format: 'YYYY-MM-DD'
        }
      });
      // 确认
      $enter.on('click', function () {
        var val = $dateBetween.val();
        if (!val) {
          layer.msg('请先选择统计日期', {offset: '15px', time: 3000, icon: 2});
          return;
        }
        val = val.split(' ');
        if (val.length !== 2) {
          layer.msg('日期不合法', {offset: '15px', time: 3000, icon: 2});
          return;
        }
        var url = window.location.protocol + '//' + window.location.host + window.location.pathname;
        url = url + "?start=" + val[0] + "&end=" + val[1];
        $('input[name=userIds]:checked').each(function () {
          url = url + "&userIds=" + $(this).val();
        });
        location.href = url;
      })
    });
  </script>
</#assign>
<@layout.default title='账单管理' style=style body=body script=script/>