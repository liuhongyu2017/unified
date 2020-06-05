<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>
  <style>
    .page-error {
      display: -webkit-box;
      display: flex;
      -webkit-box-align: center;
      align-items: center;
      -webkit-box-pack: center;
      justify-content: center;
      -webkit-box-orient: vertical;
      -webkit-box-direction: normal;
      flex-direction: column;
      min-height: calc(100vh - 110px);
      margin-bottom: 0;
    }
  </style>
</#assign>
<#assign body>
  <div class="page-error" style="color: #009688">
    <i class="fa fa-exclamation-circle" style="font-size: 100px"></i>
    <div style="font-size: 24px; margin-top: 14px">您的权限不足，无法访问本页面！</div>
  </div>
</#assign>
<#assign script>

</#assign>
<@layout.default title='index' style=style body=body script=script/>