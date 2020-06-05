<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>
  <style>
    .page-error {
      display: -webkit-box;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-align: center;
      -ms-flex-align: center;
      align-items: center;
      -webkit-box-pack: center;
      -ms-flex-pack: center;
      justify-content: center;
      -webkit-box-orient: vertical;
      -webkit-box-direction: normal;
      -ms-flex-direction: column;
      flex-direction: column;
      min-height: calc(100vh - 110px);
      margin-bottom: 0;
    }
  </style>
</#assign>
<#assign body>
  <div class="page-error" style="color: #009688">
    <div style="font-size: 120px">${statusCode!''}</div>
    <div style="font-size: 24px">${msg!''}</div>
  </div>
</#assign>
<#assign script>

</#assign>
<@layout.page title='index' style=style body=body script=script/>