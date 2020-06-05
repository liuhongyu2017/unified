<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../common/layout.ftl' as layout>
<#assign style>
  <link href="${basePath}/static/css/login.css" rel="stylesheet">
</#assign>
<#assign body>
  <div class="login-content">
    <h1 class="login-box-title">
      <i class="fa fa-fw fa-user"></i>登录
    </h1>
    <form class="layui-form" action="${basePath}/login" method="post">
      <div class="layui-form-item">
        <label class="layui-icon layui-icon-username" for="username"></label>
        <input class="layui-input" type="text" name="username" id="username" placeholder="用户名">
      </div>
      <div class="layui-form-item">
        <label class="layui-icon layui-icon-password" for="password"></label>
        <input class="layui-input" type="password" name="password" id="password" placeholder="密码">
      </div>
      <div class="layui-form-item">
        <input type="checkbox" name="rememberMe" title="记住我" lay-skin="primary">
        <a class="layui-layout-right forget-password" href="javascript:alert('请联系超级管理员！')">忘记密码?</a>
      </div>
      <button class="layui-btn layui-btn-fluid ajax-login" type="submit"><i
            class="fa fa-sign-in fa-lg fa-fw"></i> 登录
      </button>
    </form>
    <div class="layui-layer-loading login-page-loading">
      <div class="layui-layer-content"></div>
    </div>
  </div>
</#assign>
<#assign script>
  <script src="${basePath}/static/js/login.js"></script>
</#assign>
<@layout.login title='登入页' style=style body=body script=script/>