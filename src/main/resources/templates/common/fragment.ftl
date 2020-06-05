<#assign basePath=springMacroRequestContext.getContextPath()/>
<#-- 分页页码 -->
<#macro pageFragment page>
    <#if pageUtil.pageInit(page)>
      <div class="layui-row">
        <div class="layui-col-sm6">
          <div class="page-info">
            <span>显示 ${(page.number*page.size)+1}/${(page.number*page.size)+(page.numberOfElements)} 条，共 ${page.totalElements} 条记录</span>
            <select class="admin-select page-number" aria-label="">
                <#list [10,20,30] as item>
                  <option value="${item}" <#if page.size == item>selected</#if>>${item}</option>
                </#list>
            </select>
          </div>
        </div>
        <div class="layui-col-sm6">
          <ul class="pagination list-page">
              <#if pageUtil.isPrevious(page)>
                <li class="page-item">
                  <a class="page-link"
                     href="${basePath}${pageUtil.pageHref((page.number)?c)}">上一页</a>
                </li>
              </#if>
              <#list pageUtil.pageCode(page) as pageCode>
                <li class="page-item${pageUtil.pageActive(page, pageCode, ' active')}">
                    <#if !pageUtil.isCode(pageCode)>
                      <a class="page-link" href="${basePath}${pageUtil.pageHref(pageCode)}">
                          ${pageCode}
                      </a>
                    </#if>
                    <#if pageUtil.isCode(pageCode)>
                      <a class="page-link breviary">…</a>
                    </#if>
                </li>
              </#list>
              <#if pageUtil.isNext(page)>
                <li class="page-item">
                  <a class="page-link"
                     href="${basePath}${pageUtil.pageHref((page.number+2)?c)}">下一页</a>
                </li>
              </#if>
          </ul>
        </div>
      </div>
    </#if>
</#macro>
