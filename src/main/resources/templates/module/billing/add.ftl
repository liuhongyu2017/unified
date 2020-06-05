<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>

</#assign>
<#assign body>
    <#assign billingType=dictUtil.value('BILLING_TYPE')>
  <div class="layui-form admin-compile">
    <form action="${basePath}/module/billing/save">
      <div class="row">
          <#if billing??>
            <input type="hidden" name="id" value="${(billing.id?c)!''}">
          </#if>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label required">金额</label>
            <div class="layui-input-inline">
              <input class="layui-input layui-input-number" type="text" name="vAmount"
                     placeholder="请输入金额"
                     aria-label="" value="${((billing.amount/100)!0)?string("##0.00")}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label required">日期</label>
            <div class="layui-input-inline">
              <input class="layui-input admin-date" type="text" name="vDate"
                     placeholder="请选择日期"
                     aria-label="" value="${(billing.date?string('yyyy-MM-dd'))!''}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label">类型</label>
            <div class="layui-input-inline">
              <select name="type" placeholder="请选择类型" aria-label="">
                  <#list billingType?keys as key>
                    <option value="${key}"
                            <#if (((billing.type?c)!'') == key)!false>selected</#if>>${billingType[key]}</option>
                  </#list>
              </select>
            </div>
          </div>
        </div>
        <div class="layui-col-sm12 layui-col-md12">
          <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
              <textarea class="layui-textarea" name="remark"
                        placeholder="请输入内容" aria-label="">${(billing.remark)!''}</textarea>
            </div>
          </div>
        </div>
      </div>
      <div class="layui-form-item admin-finally">
        <button class="layui-btn ajax-submit">
          <i class="fa fa-check-circle"></i> 保存
        </button>
        <button class="layui-btn btn-secondary close-popup">
          <i class="fa fa-times-circle"></i> 关闭
        </button>
      </div>
    </form>
  </div>
</#assign>
<#assign script>
  <script>
    layui.extend({
      numinput: 'numinput/numinput.min'
    }).use(['numinput', 'laydate'], function () {
      var numinp = layui.numinput;
      var laydate = layui.laydate;

      numinp.init({
        // 右侧功能按钮
        rightBtns: true,
        // 小数点精度
        defaultPrec: 2,
        // 监听键盘事件
        listening: true
      });

      laydate.render({
        elem: '.admin-date',
        value: $('.admin-date').val() ? $('.admin-date').val() : new Date()
      });

      $("input[name=vAmount]").on('click', function () {
        $("input[name=vAmount]").val('');
      })
    });
  </script>
</#assign>
<@layout.default title='账单管理' style=style body=body script=script/>