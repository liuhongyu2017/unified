<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>
  <link href="${basePath}/static/lib/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
</#assign>
<#assign body>
    <#assign userJob=dictUtil.value('USER_JOB')>
    <#assign userSex=dictUtil.value('USER_SEX')>
  <div class="layui-form admin-compile">
    <form action="${basePath}/system/user/save">
      <div class="row">
          <#if user??>
            <input type="hidden" name="id" value="${(user.id?c)!''}">
          </#if>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label required">用户名</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="username" placeholder="请输入用户名"
                     aria-label="" value="${(user.username)!''}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label required">用户昵称</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="nickname" placeholder="请输入用户昵称"
                     aria-label="" value="${(user.nickname)!''}">
            </div>
          </div>
        </div>
          <#if !((user.getId()??)!false)>
            <div class="layui-col-sm6 layui-col-md6">
              <div class="layui-form-item">
                <label class="layui-form-label required">用户密码</label>
                <div class="layui-input-inline">
                  <input class="layui-input" type="password" name="password" placeholder="请输入密码"
                         aria-label="" value="${(user.password)!''}">
                </div>
              </div>
            </div>
            <div class="layui-col-sm6 layui-col-md6">
              <div class="layui-form-item">
                <label class="layui-form-label required">确认密码</label>
                <div class="layui-input-inline">
                  <input class="layui-input" type="password" name="confirm" placeholder="确认密码"
                         aria-label="" value="${(user.username)!''}">
                </div>
              </div>
            </div>
          </#if>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label required">所在部门</label>
            <div class="layui-input-inline">
              <input class="layui-input select-tree" type="text" name="dept" aria-label=""
                     placeholder="请选择所在部门" value="${(user.dept.title)!''}"
                     data-url="${basePath}/system/dept/list"
                     data-value="${(user.dept.id?c)!''}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label">职务</label>
            <div class="layui-input-inline">
              <select name="job" placeholder="请选择职务" aria-label="">
                  <#list userJob?keys as key>
                    <option value="${key}"
                            <#if (((user.job?c)!'') == key)!false>selected</#if>>${userJob[key]}</option>
                  </#list>
              </select>
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label">性别</label>
            <div class="layui-input-inline">
              <select name="sex" placeholder="请选择性别" aria-label="">
                  <#list userSex?keys as key>
                    <option value="${key}"
                            <#if (((user.sex?c)!'') == key)!false>selected</#if>>${userSex[key]}</option>
                  </#list>
              </select>
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label">移动电话</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="mobilePhone" placeholder="请输入移动电话"
                     aria-label="" value="${(user.mobilePhone)!''}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label">办公室电话</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="officePhone" placeholder="请输入办公室电话"
                     aria-label="" value="${(user.officePhone)!''}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label">家庭电话</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="homePhone" placeholder="请输入家庭电话"
                     aria-label="" value="${(user.homePhone)!''}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label">虚拟号码</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="virtualNum" placeholder="请输入虚拟号码"
                     aria-label="" value="${(user.virtualNum)!''}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm6 layui-col-md6">
          <div class="layui-form-item">
            <label class="layui-form-label">电子邮箱</label>
            <div class="layui-input-inline">
              <input class="layui-input" type="text" name="email" placeholder="请输入电子邮箱"
                     aria-label="" value="${(user.email)!''}">
            </div>
          </div>
        </div>
        <div class="layui-col-sm12 layui-col-md12">
          <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
              <textarea class="layui-textarea" name="remark"
                        placeholder="请输入内容" aria-label="">${(user.remark)!''}</textarea>
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
  <script src="${basePath}/static/lib/zTree/js/jquery.ztree.core.min.js"></script>
  <script src="${basePath}/static/js/adminTree.js"></script>
  <script type="text/javascript">
    // 树形菜单
    $.fn.selectTree();
  </script>
</#assign>
<@layout.default title='用户管理' style=style body=body script=script/>