<#assign basePath = springMacroRequestContext.getContextPath()>
<!DOCTYPE html>
<#-- 页面模板 -->
<#macro template(title,style,body,script,bodyClass)>
  <html lang="zh-CN">
  <#compress >
    <head>
      <meta charset="UTF-8">
      <meta name="renderer" content="webkit">
      <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
      <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

      <title>${title!'统一管理平台'}</title>

      <link href="${basePath}/static/css/plugins/font-awesome-4.7.0/css/font-awesome.min.css"
            rel="stylesheet">
      <link href="${basePath}/static/lib/layui/css/layui.css" rel="stylesheet">
      <link href="${basePath}/static/css/main.css" rel="stylesheet">
        ${style!''}
    </head>
    <body class="${bodyClass!''}">
    ${body!''}
    <script src="${basePath}/static/js/jquery-2.2.4.min.js"></script>
    <script src="${basePath}/static/lib/layui/layui.js"></script>
    <script src="${basePath}/static/js/main.js"></script>
    <script>
      layui.config({
        dir: '${basePath}/static/lib/layui/',
        base: '${basePath}/static/lib/layui/modules/'
      });
    </script>
    ${script!''}
    </body>
  </#compress>
  </html>
</#macro>
<#-- 页面样式 -->
<#macro page(title,style,body,script)>
    <@template title=title style=style body=body script=script bodyClass='admin-layout-page'/>
</#macro>
<#-- 默认样式 -->
<#macro default(title,style,body,script)>
    <@template title=title style=style body=body script=script bodyClass=''/>
</#macro>
<#-- 首页样式 -->
<#macro main(title,style,body,script)>
    <@template title=title style=style body=body script=script bodyClass='layui-layout-body'/>
</#macro>
<#-- 登入页样式 -->
<#macro login(title,style,body,script)>
    <@template title=title style=style body=body script=script bodyClass='layui-layout-login'/>
</#macro>
