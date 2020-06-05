layui.use(['element', 'form', 'layer', 'upload'], function () {
  var $ = layui.jquery;
  var element = layui.element; //加载element模块
  var layer = layui.layer; //加载layer模块
  var upload = layui.upload;  //加载upload模块

  /* 侧边栏开关 */
  $(".side-toggle").on("click", function (e) {
    e.preventDefault();
    var to = $(".layui-layout-admin");
    to.toggleClass("layui-side-shrink");
    to.attr("toggle") === 'on' ? to.attr("toggle", "off") : to.attr("toggle",
        "on");
  });
  $(".layui-side").on("click", function () {
    var to = $(".layui-layout-admin");
    if (to.attr("toggle") === 'on') {
      to.attr("toggle", "off");
      to.removeClass("layui-side-shrink");
    }
  });

  /* 最大化窗口 */
  $(".admin-screen-full").on("click", function (e) {
    e.preventDefault();
    if (!$(this).hasClass("full-on")) {
      var docElm = document.documentElement;
      var full = docElm.requestFullScreen || docElm.webkitRequestFullScreen ||
          docElm.mozRequestFullScreen || docElm.msRequestFullscreen;
      "undefined" !== typeof full && full && full.call(docElm);
    } else {
      document.exitFullscreen ? document.exitFullscreen()
          : document.mozCancelFullScreen ? document.mozCancelFullScreen()
          : document.webkitCancelFullScreen ? document.webkitCancelFullScreen()
              : document.msExitFullscreen && document.msExitFullscreen()
    }
    $(this).toggleClass("full-on");
  });

  /* 新建或切换标签栏 */
  var tabs = function (url) {
    var item = $('[lay-url="' + url + '"]');
    if (url !== undefined && url !== '#' && item.length > 0) {
      var bootLay = $('[lay-id="' + url + '"]');
      if (bootLay.length === 0) {
        var title = item.attr("lay-icon") === 'true' ? item.html()
            : item.children(".layui-nav-title").text();
        element.tabAdd('iframe-tabs', {
          title: title
          ,
          content: '<iframe src="' + url
              + '" frameborder="0" class="layui-layout-iframe"></iframe>'
          ,
          id: url
        });
      }
      element.tabChange('iframe-tabs', url);
    }
  };

  /* 监听导航栏事件，实现标签页的切换 */
  element.on("nav(layui-nav-side)", function ($this) {
    var url = $this.attr('lay-url');
    tabs(url);
  });

  /* 监听标签栏事件，实现导航栏高亮显示 */
  element.on("tab(iframe-tabs)", function () {
    var layId = $(this).attr("lay-id");
    $(".layui-side .layui-this").removeClass("layui-this");
    $('[lay-url="' + layId + '"]').parent().addClass("layui-this");
    // 改变地址hash值
    location.hash = this.getAttribute('lay-id');
  });

  /* 监听hash来切换选项卡*/
  window.onhashchange = function (e) {
    var url = location.hash.replace(/^#/, '');
    var index = $(".layui-layout-admin .layui-side .layui-nav-item")[0];
    $(index).children("a").attr("lay-icon", "true");
    if (url === "" || url === undefined) {
      url = $(index).children("[lay-url]").attr("lay-url");
    }
    tabs(url);
  };
  window.onhashchange();

  /* 初始化时展开子菜单 */
  $("dd.layui-this").parents(".layui-nav-child").parent()
  .addClass("layui-nav-itemed");

  /* 刷新iframe页面 */
  $(".refresh-btn").click(function () {
    location.href = window.location.protocol + '//' + window.location.host
        + window.location.pathname;
  });

  /* AJAX请求默认选项，处理连接超时问题 */
  $.ajaxSetup({
    beforeSend: function () {
      layer.load(0);
    },
    complete: function (xhr) {
      layer.closeAll('loading');
      if (xhr.status === 401) {
        layer.confirm('session连接超时，是否重新登录？', {
          btn: ['是', '否']
        }, function () {
          if (window.parent.window !== window) {
            window.top.location = window.location.pathname + '/login';
          }
        });
      }
    }
  });

  /*  漂浮消息 */
  $.fn.Messager = function (result) {
    if (result.code === 200) {
      layer.msg(result.msg, {offset: '15px', time: 3000, icon: 1});
      setTimeout(function () {
        if (result.data === 'submit[refresh]') {
          parent.location.reload();
          return;
        }
        if (result.data == null) {
          window.location.reload();
        } else {
          window.location.href = result.data
        }
      }, 2000);
    } else {
      layer.msg(result.msg, {offset: '15px', time: 3000, icon: 2});
    }
  };

  /* 提交表单数据 */
  $(document).on("click", ".ajax-submit", function (e) {
    e.preventDefault();
    var form = $(this).parents("form");
    var url = form.attr("action");
    var serializeArray = form.serializeArray();
    $.post(url, serializeArray, function (result) {
      if (result.data == null) {
        result.data = 'submit[refresh]';
      }
      $.fn.Messager(result);
    });
  });

  /* get方式异步 */
  $(document).on("click", ".ajax-get", function (e) {
    e.preventDefault();
    var msg = $(this).data("msg");
    var url = $(this).data('url');
    if (msg !== undefined) {
      layer.confirm(msg + '？', {
        title: '提示',
        btn: ['确认', '取消']
      }, function () {
        var href = e.target.href;
        if (!href) {
          href = url;
        }
        $.get(href, function (result) {
          $.fn.Messager(result);
        });
      });
    } else {
      var href = e.target.href;
      if (!href) {
        href = url;
      }
      $.get(href, function (result) {
        $.fn.Messager(result);
      });
    }
  });

  // post方式异步-操作状态
  $(".ajax-status").on("click", function (e) {
    e.preventDefault();
    var checked = [];
    var tdcheckbox = $(".admin-table td .admin-checkbox :checkbox:checked");
    if (tdcheckbox.length > 0) {
      tdcheckbox.each(function (key, val) {
        checked.push("ids=" + $(val).attr("value"));
      });
      $.get(e.target.href, checked.join("&"), function (result) {
        $.fn.Messager(result);
      });
    } else {
      layer.msg('请选择一条记录');
    }
  });

  /* 添加/修改弹出层 */
  $(document).on("click", ".open-popup, .open-popup-param", function () {
    var title = $(this).attr("data-title");
    var url = $(this).attr("data-url");
    if ($(this).hasClass("open-popup-param")) {
      var tdcheckbox = $(".admin-table td .admin-checkbox :checkbox:checked");
      var param = '';
      if (tdcheckbox.length === 0) {
        layer.msg('请选择一条记录');
        return;
      }
      if (tdcheckbox.length > 1 && $(this).data("type") === 'radio') {
        layer.msg('只允许选中一个');
        return;
      }
      tdcheckbox.each(function (key, val) {
        param += "ids=" + $(val).attr("value") + "&";
      });
      param = param.substr(0, param.length - 1);
      url += "?" + param;
    }
    var size = $(this).data("size"), layerArea;
    if (size === undefined || size === "auto" || size === "max") {
      layerArea = ['70%', '80%'];
    } else if (size.indexOf(',') !== -1) {
      var split = size.split(",");
      split[0] = /^[1-9][0-9]+$/gi.test(split[0]) ? split[0] + 'px' : split[0];
      split[1] = /^[1-9][0-9]+$/gi.test(split[1]) ? split[1] + 'px' : split[1];
      layerArea = split;
    }
    window.layerIndex = layer.open({
      type: 2,
      title: title,
      shadeClose: true,
      maxmin: true,
      area: layerArea,
      content: [url, 'on']
    });
    if (size === "max") {
      layer.full(layerIndex);
    }
  });

  /* 关闭弹出层 */
  $(".close-popup").click(function (e) {
    e.preventDefault();
    parent.layer.close(window.parent.layerIndex);
  });

  /* 下拉按钮组 */
  $(".btn-group").click(function (e) {
    e.stopPropagation();
    $this = $(this);
    $this.toggleClass("show");
    $(document).one("click", function () {
      if ($this.hasClass("show")) {
        $this.removeClass("show");
      }
    });
  });

  // 展示数据列表-多选框
  var thcheckbox = $(".admin-table th .admin-checkbox :checkbox");
  thcheckbox.on("change", function () {
    var tdcheckbox = $(".admin-table td .admin-checkbox :checkbox");
    if (thcheckbox.is(':checked')) {
      tdcheckbox.prop("checked", true);
    } else {
      tdcheckbox.prop("checked", false);
    }
  });

  // 检测列表数据是否为空
  var adminTable = $(".admin-table tbody");
  if (adminTable.length > 0) {
    var children = adminTable.children();
    if (children.length === 0) {
      var length = $(".admin-table thead th").length;
      var trNullInfo = "<tr><td class='admin-table-null' colspan='"
          + length + "'>没有找到匹配的记录</td></tr>";
      adminTable.append(trNullInfo);
    }
  }

  // 携带参数跳转
  var paramSkip = function () {
    var getSearch = "";
    // 搜索框参数
    $('.admin-search-box [name]').each(function (key, val) {
      if ($(val).val() && $(val).val() !== "" && $(val).val() !== undefined) {
        getSearch += $(val).attr("name") + "=" + $(val).val() + "&";
      }
    });

    // 页数参数
    var pageSize = $(".page-number").val();
    if (pageSize !== undefined && pageSize !== "") {
      getSearch += "size=" + pageSize + "&";
    }

    // 排序参数
    var asc = $(".sortable.asc").data("field");
    if (asc !== undefined) {
      getSearch += "orderByColumn=" + asc + "&isAsc=asc&";
    }
    var desc = $(".sortable.desc").data("field");
    if (desc !== undefined) {
      getSearch += "orderByColumn=" + desc + "&isAsc=desc&";
    }

    if (getSearch !== "") {
      getSearch = "?" + getSearch.substr(0, getSearch.length - 1);
    }
    window.location.href = window.location.pathname + getSearch;
  };

  /* 展示列表数据搜索 */
  $(document).on("click", ".admin-search-btn", function () {
    paramSkip();
  });
  /* 改变显示页数 */
  $(document).on("change", ".page-number", function () {
    paramSkip();
  });
  /* 触发字段排序 */
  $(document).on("click", ".sortable", function () {
    $(".sortable").not(this).removeClass("asc").removeClass("desc");
    if ($(this).hasClass("asc")) {
      $(this).removeClass("asc").addClass("desc");
    } else {
      $(this).removeClass("desc").addClass("asc");
    }
    paramSkip();
  });

  /* 参数化字段排序 */
  var getSearch = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
      return unescape(r[2]);
    }
    return null;
  };
  var field = getSearch("orderByColumn");
  var isAsc = getSearch("isAsc");
  if (field != null) {
    $("[data-field='" + field + "']").addClass(isAsc);
  }
});