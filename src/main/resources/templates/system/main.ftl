<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../common/layout.ftl' as layout>
<#import '../common/fragment.ftl' as fragment>
<#assign style>

</#assign>
<#assign body>
  <div class="layui-layout layui-layout-admin">
    <!-- 导航栏 -->
    <div class="layui-header">
      <a href="#" class="layui-logo">
        <span class="layui-logo-mini">统一</span>
        <span class="layui-logo-lg">统一管理后台</span>
      </a>
      <a class="side-toggle layui-layout-left" href="#">
        <i class="layui-icon layui-icon-shrink-right"></i>
        <i class="layui-icon layui-icon-spread-left"></i>
      </a>
      <ul class="layui-nav layui-layout-right">
        <li class="layui-nav-item">
          <a class="admin-screen-full" href="#">
            <i class="fa layui-icon layui-icon-screen-full"></i>
          </a>
        </li>
        <li class="layui-nav-item admin-nav-user">
          <a class="admin-header-nickname">
              <@shiro.principal property="nickname"/>
          </a>
          <div class="layui-nav-child">
            <div class="admin-nav-child-box">
              <div>
                <a class="open-popup"
                   data-title="个人信息"
                   data-url="${basePath}/system/main/userInfo"
                   data-size="680,464">
                  <i class="fa fa-user-o"></i>个人信息
                </a>
              </div>
              <div>
                <a class="open-popup"
                   data-title="修改密码" data-url="${basePath}/system/main/editPwd" data-size="456,296">
                  <i class="fa fa-lock" style="font-size:17px;width:12px;"></i>修改密码
                </a>
              </div>
              <div>
                <a href="${basePath}/logout">
                  <i class="fa fa-power-off"></i>退出登录
                </a>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <!-- 侧边栏 -->
    <div class="layui-side layui-bg-black">
      <div class="layui-side-scroll">
        <div class="layui-side-user">
          <img class="layui-side-user-avatar open-popup" alt="头像"
               src="${basePath}/system/user/picture?p=<@shiro.principal property="picture"/>"
               data-url="${basePath}/system/main/userInfo"
               data-size="680,464">
          <div>
            <p class="layui-side-user-name"><@shiro.principal property="nickname"/></p>
          </div>
        </div>
        <!-- 导航区域 -->
        <ul class="layui-nav layui-nav-tree" lay-filter="layui-nav-side" lay-shrink="all">
            <#list treeMenu?keys as key>
                <#assign item = treeMenu[key]>
              <li class="layui-nav-item">
                  <#-- 一级菜单 -->
                <a href="javascript:;"
                   <#if item.type==2>lay-url="<#if ((item.url)!'')?substring(3) != 'http'>${basePath}</#if>${(item.url)!''}"</#if>>
                  <i class="${(item.icon)!''}"></i>
                  <span class="layui-nav-title">${(item.title)!''}</span>
                </a>
                  <#if (item.children?size) gt 0>
                    <dl class="layui-nav-child">
                        <#list item.children?keys as sKey>
                            <#assign sItem = item.children[sKey]>
                          <dd>
                              <#-- 二级菜单 -->
                            <a href="javascript:;"
                               <#if sItem.type==2>lay-url="<#if (sItem.url!'')?length gt 3 && ((sItem.url)!'')?substring(3) != 'http'>${basePath}</#if>${(sItem.url)!''}"</#if>>
                              <span class="layui-nav-title">${(sItem.title)!''}</span>
                            </a>
                              <#if (sItem.children?size) gt 0>
                                <dl class="layui-nav-child">
                                    <#list sItem.children?keys as tKey>
                                        <#assign tItem = sItem.children[tKey]>
                                      <dd>
                                          <#-- 三级菜单 -->
                                        <a href="javascript:;"
                                           <#if tItem.type==2>lay-url="<#if ((tItem.url)!'')?substring(3) != 'http'>${basePath}</#if>${(tItem.url)!''}"</#if>>
                                          <span class="layui-nav-title">${(tItem.title)!''}</span>
                                        </a>
                                      </dd>
                                    </#list>
                                </dl>
                              </#if>
                          </dd>
                        </#list>
                    </dl>
                  </#if>
              </li>
            </#list>
        </ul>
      </div>
    </div>
    <!-- 主体区域 -->
    <div class="layui-body layui-tab layui-tab-brief"
         lay-allowclose="true" lay-filter="iframe-tabs">
      <!-- 标签栏 -->
      <ul class="layui-tab-title"></ul>
      <!-- 内容区域 -->
      <div class="layui-tab-content"></div>
    </div>
  </div>
</#assign>
<#assign script>

</#assign>
<@layout.main title='统一管理平台' style=style body=body script=script/>
