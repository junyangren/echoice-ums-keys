var mods = [
  'element', 'sidebar', 'mockjs', 'select',
  'tabs', 'menu', 'route', 'utils', 'component', 'kit', 'echarts'
];

layui.define(mods, function(exports) {
  var element = layui.element,
    utils = layui.utils,
    $ = layui.jquery,
    _ = layui.lodash,
    route = layui.route,
    tabs = layui.tabs,
    layer = layui.layer,
    menu = layui.menu,
    component = layui.component,
    kit = layui.kit;


  var Admin = function() {
    this.config = {
      elem: '#app',
      loadType: 'TABS'
    };
    this.version = '1.0.0';
  };

  Admin.prototype.ready = function(callback) {
    var that = this,
      config = that.config;
    
    utils.localStorage.clear();
    // 初始化加载方式
    var getItem = utils.localStorage.getItem;
    var setting = getItem("KITADMIN_SETTING_LOADTYPE");
    if (setting !== null && setting.loadType !== undefined) {
      config.loadType = setting.loadType;
    }

    kit.set({
      type: config.loadType
    }).init();

    // 初始化路由
    _private.routeInit(config);
    // 初始化选项卡
    if (config.loadType === 'TABS') {
      _private.tabsInit();
    }
    // 初始化左侧菜单
    _private.menuInit(config);
    // 跳转至首页
    if (location.hash === '') {
      utils.setUrlState('主页', '#/');
    }

    // // 处理 sidebar
    // layui.sidebar.render({
    //   elem: '#ccleft',
    //   //content:'', 
    //   title: '这是左侧打开的栗子',
    //   shade: true,
    //   // shadeClose:false,
    //   direction: 'left',
    //   dynamicRender: true,
    //   url: 'components/table/teble2.html',
    //   width: '50%', //可以设置百分比和px
    // });

    // $('#cc').on('click', function() {
    //   var that = this;
    //   layui.sidebar.render({
    //     elem: that,
    //     //content:'', 
    //     title: '这是表单盒子',
    //     shade: true,
    //     // shadeClose:false,
    //     // direction: 'left'
    //     dynamicRender: true,
    //     url: 'components/form/index.html',
    //     width: '50%', //可以设置百分比和px
    //   });
    // });
    // 监听头部右侧 nav
    component.on('nav(header_right)', function(_that) {
      var target = _that.elem.attr('kit-target');
      if (target === 'setting') {
        // 绑定sidebar
        layui.sidebar.render({
          elem: _that.elem,
          //content:'', 
          title: '设置',
          shade: true,
          // shadeClose:false,
          // direction: 'left'
          dynamicRender: true,
          url: '../views/setting.html',
          // width: '50%', //可以设置百分比和px
        });
      }else if (target === 'help') {
        layer.alert('文档完善中');
      }else if (target === 'tabs') {
    	  var hrefV=$('a', _that.elem).attr('href');
          if (!tabs.existsByPath(hrefV)) {
              // 新增一个选项卡
        	  _private.addTab(hrefV, new Date().getTime());
          } else {
              // 切换到已存在的选项卡
              tabs.switchByPath(hrefV);
          }
      }
    });
    
    $('#logo-home').click(function(){
  	  var hrefV=$(this).attr('href');
      if (!tabs.existsByPath(hrefV)) {
          // 新增一个选项卡
    	  _private.addTab(hrefV, new Date().getTime());
      } else {
          // 切换到已存在的选项卡
          tabs.switchByPath(hrefV);
      }
    });

    // 注入mock
    //layui.mockjs.inject(APIs);

    // 初始化渲染
    if (config.loadType === 'SPA') {
      route.render();
    }
    that.render();

    // 执行回调函数
    typeof callback === 'function' && callback();
  }
  Admin.prototype.render = function() {
    var that = this;
    return that;
  }

  var _private = {
    routeInit: function(config) {
      var that = this;
      // route.set({
      //   beforeRender: function (route) {
      //     if (!utils.oneOf(route.path, ['/user/table', '/user/table2', '/'])) {
      //       return {
      //         id: new Date().getTime(),
      //         name: 'Unauthorized',
      //         path: '/exception/403',
      //         component: 'components/exception/403.html'
      //       };
      //     }
      //     return route;
      //   }
      // });
      // 配置路由
      var routeOpts = {
        routes: [{
          path: '/',
          component: '../views/app.html',
          name: '控制面板'
        },{
	      path: '/home',
	      component: 'home',
	      name: '主页'
        },{
          path: '/console/user/profile',
          component: '../console/user/profile',
          name: '个人中心',
          iframe:true
        },{
           path: '/console/user/password',
           component: '../console/user/password',
           name: '密码修改',
           iframe:true
        },{
           path: '/console/accssMode/index',
           component: '../console/accssMode/index',
           name: '操作管理',
           iframe:true
        },{
           path: '/console/objects/index',
           component: '../console/objects/index',
           name: '资源管理',
           iframe:true
        },{
            path: '/console/group/index',
            component: '../console/group/index',
            name: '用户组管理',
            iframe:true
        },{
            path: '/console/user/index',
            component: '../console/user/index',
            name: '用户管理',
            iframe:true
        },{
            path: '/console/role/index',
            component: '../console/role/index',
            name: '角色管理',
            iframe:true
        },{
            path: '/console/userCakey/index',
            component: '../console/userCakey/index',
            name: 'Key资产管理',
            iframe:true
         },{
            path: '/console/cakeyOrder/index',
            component: '../console/cakeyOrder/index',
            name: 'Key资产工单',
            iframe:true
         },{
            path: '/console/cakeyOrder/report',
            component: '../console/cakeyOrder/report',
            name: 'Key资产统计',
            iframe:true
         },{
             path: '/console/appinfo/index',
             component: '../console/appinfo/index',
             name: '应用管理',
             iframe:true
          }]
      };
      
      if (config.loadType === 'TABS') {
        routeOpts.onChanged = function() {
          // 如果当前hash不存在选项卡列表中
          if (!tabs.existsByPath(location.hash)) {
            // 新增一个选项卡
            that.addTab(location.hash, new Date().getTime());
          } else {
            // 切换到已存在的选项卡
            tabs.switchByPath(location.hash);
          }
        }
      }
      // 设置路由
      route.setRoutes(routeOpts);
      return this;
    },
    addTab: function(href, layid) {
      var r = route.getRoute(href);
      if(!r){
    	  //动态注册路由
    	  var croutes=route.getRoutes();
    	  var routeObj = layui.router(href);
    	  var pathV=routeObj.href.split('?')[0];
    	  var componentV=ecApp.context+pathV.substr(1);
    	  var nameV=name;
    	  if(!nameV){//针对左侧菜单名字获取
    		  var parentObj=$('a[href="'+href+'"]');
    		  nameV=$('span',parentObj).text();
    		  console.log(nameV);
    	  }
    	  r={path:pathV,component:componentV,name: nameV,iframe: true};
    	  croutes[croutes.length]=r;
    	  route.setRoutes({routes:croutes});
      }      
      if (r) {
        tabs.add({
          id: layid,
          title: r.name,
          path: href,
          component: r.component,
          rendered: false,
          icon: '&#xe62e;'
        });
      }
    },
    menuInit: function(config) {
      var that = this;
      // 配置menu
      menu.set({
        dynamicRender: true,
        isJump: config.loadType === 'SPA',
        onClicked: function(obj) {
          if (config.loadType === 'TABS') {
            if (!obj.hasChild) {
              var data = obj.data;
              var href = data.href;
              var layid = data.layid;
              that.addTab(href, layid);
            }
          }
        },
        elem: '#menu-box',
        remote: {
          url: './userMenu?rootMenuAlias=ec-ums',
          method: 'get'
        },
        cached: false
      }).render();
      return this;
    },
    tabsInit: function() {
      tabs.set({
        onChanged: function(layid) {
          // var tab = tabs.get(layid);
          // if (tab !== null) {
          //   utils.setUrlState(tab.title, tab.path);
          // }
        }
      }).render(function(obj) {
        // 如果只有首页的选项卡
        if (obj.isIndex) {
          route.render('#/');
        }
      });
    }
  }

  ecApp.addTab=function(href, layid, name){
	  tabs.remove(layid);
	  _private.addTab(href,layid,name);
  }
  
  var admin = new Admin();
  admin.ready(function() {
    console.log('Init successed.');
  });

  //输出admin接口
  exports('index', {});
});