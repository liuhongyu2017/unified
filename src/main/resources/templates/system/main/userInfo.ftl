<#assign basePath=springMacroRequestContext.getContextPath()/>
<#import '../../common/layout.ftl' as layout>
<#assign style>

</#assign>
<#assign body>
    <#assign userSex=dictUtil.value('USER_SEX')>
  <div class="layui-form admin-compile user-info-page">
    <div class="user-info">
      <div class="user-avatar-box">
        <img class="user-avatar" alt="头像"
             src="${basePath}/system/user/picture?p=${(user.picture)!''}">
        <span class="edit-avatar" data-url="${basePath}/system/main/userPicture">修改头像</span>
      </div>
      <ul class="detail-info">
        <li>
          账号：<span title="${(user.username)!''}">${(user.username)!''}</span>
        </li>
        <li>
          昵称：<span title="${(user.nickname)!''}">${(user.nickname)!''}</span>
        </li>
        <li>
          性别：<span title="${(dictUtil.keyValue('USER_SEX',(user.sex?string)!''))!''}">
            ${(dictUtil.keyValue('USER_SEX',(user.sex?string)!''))!''}
          </span>
        </li>
        <li>
          电话：<span title="${(user.mobilePhone)!''}">${(user.mobilePhone)!''}</span>
        </li>
        <li>
          邮箱：<span title="${(user.email)!''}">${(user.email)!''}</span>
        </li>
      </ul>
    </div>
    <form class="user-edit" action="${basePath}/system/main/userInfo">
      <input type="hidden" name="username" value="${(user.username)!''}"/>
      <input type="hidden" name="dept" value="${(user.dept.id?c)!''}"/>
      <div class="layui-form-item">
        <label class="layui-form-label">用户昵称</label>
        <div class="layui-input-inline">
          <input class="layui-input" type="text" name="nickname" aria-label=""
                 placeholder="请输入用户昵称" value="${(user.nickname)!''}">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">移动电话</label>
        <div class="layui-input-inline">
          <input class="layui-input" type="text" name="mobilePhone" aria-label=""
                 placeholder="请输入电话号码" value="${(user.mobilePhone)!''}">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">邮箱</label>
        <div class="layui-input-inline">
          <input class="layui-input" type="text" name="email" aria-label="" placeholder="请输入邮箱"
                 value="${(user.email)!''}">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">选择性别</label>
        <div class="layui-input-inline">
          <select name="sex" placeholder="请选择性别" aria-label="">
              <#list userSex?keys as key>
                <option value="${key}"
                        <#if (((user.sex?c)!'') == key)!false>selected</#if>>${userSex[key]}</option>
              </#list>
          </select>
        </div>
      </div>
      <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">备注</label>
        <div class="layui-input-block">
        <textarea class="layui-textarea" aria-label="" placeholder="请输入内容" name="remark">
            ${(user.remark)!''}
        </textarea>
        </div>
      </div>
      <div class="layui-form-item admin-finally">
        <button class="layui-btn ajax-submit"><i class="fa fa-check-circle"></i> 保存</button>
        <a class="layui-btn btn-secondary close-popup"><i class="fa fa-times-circle"></i> 关闭</a>
      </div>
    </form>
    <!-- 编辑头像面板 -->
    <div class="canvas-panel">
      <img class="canvas-bg" src="" alt=""/>
      <div class="canvas-shade"></div>
      <canvas class="canvas-crop"></canvas>
    </div>
    <div class="canvas-group layui-btn-group">
      <button class="upload-close layui-btn layui-btn-primary layui-btn-sm">取消</button>
      <button class="upload-btn layui-btn layui-btn-primary layui-btn-sm">保存</button>
    </div>
  </div>
</#assign>
<#assign script>
  <script src="${basePath}/static/js/jquery.mousewheel.min.js"></script>
  <script>
    layui.use(['jquery', 'upload'], function () {
      var $ = layui.jquery;
      var upload = layui.upload;

      $(".user-edit").on("mousedown", function () {
        $(this).css("opacity", 1);
      });

      /* 修改头像 */
      var image = new Image();
      var panel = $(".canvas-panel");
      var bgImg = $(".canvas-bg");
      var canvas = $(".canvas-crop");

      // 激活或停止移动
      var moveEvent = false;
      var screenX = 0, screenY = 0;
      var moveTop = 0, moveLeft = 0;
      panel.on("mousedown", function (e) {
        screenX = e.screenX;
        screenY = e.screenY;
        moveTop = parseFloat(bgImg.css("top"));
        moveLeft = parseFloat(bgImg.css("left"));
        moveEvent = true;
      });
      panel.on("mouseup", function (e) {
        moveEvent = false;
      });
      panel.on("mousemove", function (e) {
        if (moveEvent) {
          bgImg.css("left", moveLeft + e.screenX - screenX);
          bgImg.css("top", moveTop + e.screenY - screenY);
          renderPanel();
        }
      });
      panel.on("mousewheel", function (event, delta) {
        var dir = delta > 0 ? 'Up' : 'Down';
        var width = parseFloat(bgImg.css("width"));
        var height = parseFloat(bgImg.css("height"));
        if (dir == 'Up') {
          delta = 1;
        } else {
          delta = -1;
        }
        bgImg.css("width", width * (1 + 0.1 * delta));
        bgImg.css("height", height * (1 + 0.1 * delta));
        bgImg.css("left", parseFloat(bgImg.css("left")) - (width * 0.1 / 2) * delta);
        bgImg.css("top", parseFloat(bgImg.css("top")) - (height * 0.1 / 2) * delta);
        renderPanel();
        return false;
      });

      // 渲染画布面板
      function renderPanel() {
        canvas[0].width = 256;
        canvas[0].height = 256;
        var imgScale = image.width / bgImg.width();
        var context = canvas[0].getContext('2d');
        var sx = (bgImg.width() * imgScale / 2) - canvas.width() / 2 * imgScale,
            sy = (bgImg.height() * imgScale / 2) - canvas.height() / 2 * imgScale,
            sw = canvas.width() * imgScale, sh = canvas.height() * imgScale;
        var moveX = panel.width() / 2 - parseFloat(bgImg.css("left")) - bgImg.width() / 2;
        var moveY = panel.height() / 2 - parseFloat(bgImg.css("top")) - bgImg.height() / 2;
        context.drawImage(image, sx + moveX * imgScale, sy + moveY * imgScale, sw, sh, 0, 0,
            canvas[0].width, canvas[0].height);
      }

      var files = [];
      var uploadInst = upload.render({
        elem: '.edit-avatar'
        , field: 'picture'
        , url: $('.edit-avatar').data('url')
        , exts: 'jpg|png|gif|jpeg'
        , auto: false
        , bindAction: ".upload-btn"
        // 选择文件回调
        , choose: function (obj) {
          obj.preview(function (index, upload, result) {
            panel.show();
            $(".canvas-group").show();
            image.src = result;
            image.onload = function () {
              bgImg.attr("src", result);
              if (bgImg.width() >= bgImg.height()) {
                bgImg.css("height", canvas.width());
              } else {
                bgImg.css("width", canvas.height());
              }
              bgImg.css("top", (panel.height() - bgImg.height()) / 2);
              bgImg.css("left", (panel.width() - bgImg.width()) / 2);
              renderPanel();
            }
          });
        }
        , before: function (obj) {
          files = obj.pushFile();
          var index, upload;
          for (index in files) {
            upload = files[index];
          }
          var dataurl = canvas[0].toDataURL(upload.type, 0.92);
          var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
              bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
          while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
          }
          files[index] = new File([u8arr], upload.name, {type: mime});
        }
        , done: function (res, index) {
          if (res.code == 200) {
            panel.hide();
            $(".canvas-group").hide();
            layer.msg("修改头像成功", {offset: '15px', time: 3000, icon: 1});
            $(".user-avatar").attr("src", canvas[0].toDataURL());
            delete files[index];
          } else {
            layer.msg(res.msg, {offset: '15px', time: 3000, icon: 2});
          }
        }
      });

      // 关闭裁切面板及清空文件
      $(".upload-close").on("click", function () {
        panel.hide();
        $(".canvas-group").hide();
      });
    });
  </script>
</#assign>
<@layout.default title='基本信息' style=style body=body script=script/>