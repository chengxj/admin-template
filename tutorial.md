## 目录

[TOC]



## 概述
本项目前端采用Bootstrap3.0、JQuery1.10、AngularJS1.2等前端类库，后台使用Spring MVC实现，本文不介绍这些类库和框架的使用，如果需要学习这些类库和框架的使用，请阅读官方文档。
## 前端UI
### 目录说明
前端的实现并未按照现在流行的前端开发模式实现，需要由前端工程师优化。

| 目录 | 说明 |
|--------|--------|
|    WEB-INF    |存放配置文件        |
|    assets	    |存放静态文件        |
|    assets/css |存放样式文件        |
|    assets/img|图片文件            |
|    assets/js|javascript脚本       |
|    assets/plugins  |引用的第三方javascript和CSS文件        |
|    app    |存放与业务有关的html和javascript        |
|    app/*    |模块的根目录，以模块名命名        |
|    app/*/ views    |html文件        |
|    app/*/ js    |javascript文件，views和js的文件名一一对应，如CustomerEditCtrl.js对应了edit.html；如果customer中还有下一级模块，可以在customer和views/js之间再新建一个模块文件夹        |
|    app/*/ partials    |内部嵌入的HTML文件        |
|    app/i18n    |国际化文件，此文件由后台管理自动生成        |
|    app/partials    |通用的HTML文件，主要与AngularJS的指令结合   |

### CSS
**编码要求**

1.	自定义的css统一放在custom.css文件中
2.	多个单词使用-分隔，如control-label
3.	同一类CSS需要使用注释与其他类别的CSS分隔，如：
    ```
/*border*/
.border1A {
    border: 1px solid #e3e6ed;
}
/*.border*/

    ```
    
    - /*border*/表示下面的样式是用于定义边框属性的样式
    - /*.border*/表示border样式的定义结束，它只比样式的起始位置多一个.
   
### Javascript
**编码要求**

1. 自定义的js统一放在main.js文件中
2. 自定义的指令（与dom处理有关）放在directives.js文件中
3. 自定义的filter放在filters.js文件中
4. 自定义的service放在services.js文件中
5. Controller的文件名必须与view一一对应

### 页面布局
页面布局采用企业管理平台的传统布局页面，再开发的过程中，我们只需要关注`<div class="container-fluid" ng-view></div>`部分的页面即可。
用户列表的页面
```
<div class="page-title" ed-page-title>
    <h4>
        <a href="javascript:;" id="toggle-menu">
            <i class="fa fa-reorder"></i>
        </a>
        <span>{{'title.sys.user' | translate}}</span>
    </h4>
    <nav>
        <span class="fa fa-home"></span>
        <a href="javascript:;">{{'title.common.home' | translate}}</a>
        <span class="fa fa-angle-right"></span>
        <a href="javascript:;">{{'title.sys.user' | translate}}</a>
    </nav>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="panel panel-default">
            <header class="panel-heading">{{'header.sys.user.list' | translate}}
                <div class="pull-right">
                    <a href="javascript:;" class="btn btn-xs btn-link panel-refresh" ed-reload
                       reload-fun="reload()">
                        <i class="fa fa-refresh"></i>
                    </a>
                </div>
            </header>
            <div class="panel-body">
            </div>
        </div>
    </div>
</div>
```
每个ng-view内部都必须定义页面标题

`<div class="page-title" ed-page-title>...</div>`

因为页面的标题在静态页面中实际上是定义在ng-view的外面，而每个ng-view的标题都不相同，有的是用户查询、有的是新增用户，所以在ng-view内部使用ed-page-title指令定义标题栏，它会将标题栏移动到ng-view外部。指令的具体用法参考指令章节

`<div class="row"><div class="col-md-12">....</div></div>`用来定义功能区，每个功能去都需要使用它们包裹，row和col-md-12的用法可以查阅Bootstrap3的官方文档

### app.js
`.config([...,function (...) {} ])`AngularJS配置方法，主要用于配置路由、http请求、国际化等属性
`.run(function () {})`AngularJS启动后运行的方法，主要用于读取用户偏好设置

#### 路由定义
AngularJS在ng-app加载的时候必须使用$routeProvider对路由进行配置。这就要求在页面加载时将每个路由对应的controller文件加载进来，随着管理平台功能的增加，需要加载的controller文件会越来越多。因为我对前端开发的模式并没有深入了解，所以采用RequireJS实现controller的动态加载
```
$.each(routes, function (i, v) {
	$routeProvider.when(v.url, route.resolve(v.basename, v.path))
});
$routeProvider.when("/home/index", route.resolve('index', '/home'));
$routeProvider.when("/home/profile/:userId", route.resolve('profile', '/home'));
$routeProvider.otherwise({redirectTo: '/order/task/list'});
```
每个路由的URL由三部分组成

1. path：模块名称，如/sys，/sys/user
2. basename：功能名称，如/add，/edit，/list
3. param：参数，如:userId

通过routeResolverProvider解析路由的URL，来查询路由的template文件和controller文件。
如

- /sys/user/list路由，对应的template是/sys/user/views/list.html，对应的controller文件是/sys/user/js/SysUserListCtrl.js
- /sys/user/edit/:userId路由，对应的template是/sys/user/views/edit.html，对应的controller文件是/sys/user/js/SysUserEditCtrl.js
- /home/profile/:userId路由，对应的template是/home/views/profile.html，对应的controller文件是/home/js/HomeProfileCtrl.js

#### 国际化
由于前端使用的html页面，无法直接使用Spring MVC的国际化功能，因此采用angular-translate来实现页面的国际化功能，本文档只介绍在本项目中如何使用angular-translate的国际化功能，对于其他用法不做描述。
在ng-app的配置方法里使用
```
var profile = data.user.profile;
$translateProvider.useStaticFilesLoader({
	prefix: 'app/i18n/locale-',
	suffix: '.json'
});
$translateProvider
	.preferredLanguage(profile.language);
// moment.lang(profile.language.toLowerCase());
```
国际化文件统一定义在i18n/locale-*.json文件中，为了便于管理，该文件统一由JAVA后台生成，一般不需要手动修改此文件，在html页面中使用`{{'label.common.op.delete' | translate}}`来实现国际化功能。

#### httpInterceptor
AngularJS对HTTP请求的拦截器
```
'request': function(config) {
                if (config && config.params) {
                    config.params.lang = profile.language;
                    config.params.random = new Date().getTime();
                } else {
                    config.params = {lang : profile.language, random:new Date().getTime()};
                }
                return config;
            }
```
为每个请求增加时间戳参数，避免ajax缓存
```
responseError: function (response) {
...
}
```
http请求错误之后调用的方法
根据response返回的值显示不同的消息

- 参数不合法
- 数据过期
- 用户未登录
- 用户权限不足
- 系统错误
- ...

### service.js

#### MessageService
消息的service对象，

- saveSuccess 保存成功
- removeSuccess 删除成功
- successMsg 成功消息
- errorMsg 错误消息

#### LocationTo
$location的service对象，

- path 跳转路径

#### ShareService
全局变量的service对象，返回一个JSON对象

### directive.js
指令的具体用法参考每个指令的注释

### filter.js
filter的具体用法参考每个filter的注释

### 列表页面
每个列表页面由三部分组成：

- 表格
- 工具栏
- 右键菜单栏

```
<div class="panel-body">
	<!--右键菜单开始-->
	<div id="context-menu">
		<ul class="dropdown-menu dropdown-arrow-left" role="menu">
		...
		</ul>
	</div>
	<!--右键菜单结束-->
	<!--工具栏开始-->
	<div class="table-tools">
		<div class="row">
			<div class="col-md-4 col-sm-12">
			...
			</div>

			<div class="col-md-8 col-sm-12">
				..
			</div>

		</div>
	</div>
	<!--工具栏结束-->
	<table class="table table-striped table-bordered table-hover table-condensed">
		<thead>
		<tr user="row">
			...
		</tr>
		</thead>
		<tbody>
		...
		</tbody>
	</table>
</div>
```
**1.增加分页查询指令**

```
<!--工具栏开始-->
<div class="table-tools">
    <div class="row">
        <div class="col-md-4 col-sm-12">
        ...
        </div>

        <div class="col-md-8 col-sm-12">
            <div ed-simple-page page-size="10"
             pagination="pagination" query-param="queryParam" query-url="sys/user/pagination"
             share-key="userList" block-el=".panel-body">
        </div>
        </div>

    </div>
</div>
<!--工具栏结束-->
```

ed-simple-page的参数说明：

- pagination：model（数据源）
- query-param：查询参数
- query-url：查询地址
- share-key：页面缓存的键，当用户在列表上做了查询、翻页后查看详情然后返回列表页面后希望看到之前的查询条件时可以配置此参数，缓存仅仅是在页面上用JS的全局变量实现，因此当页面做刷新操作时缓存会被清除
-  block-el：激活blockUI的位置

分页查询的rest接口必须返回固定的JSON格式：
```
{
    "records": [ ...], 
    "page": 1, 
    "pageSize": 10, 
    "totalRecords": 2, 
    "totalPages": 1, 
    "pageList": [
        1
    ], 
    "nextPage": 2, 
    "prevPage": 1
}
```
**2.增加表格显示**
```
<table class="table table-striped table-bordered table-hover table-condensed">
    <thead>
    <tr user="row">
        <th class="width20">
            <input type="checkbox" ed-check-all/>
        </th>
        <th>
            {{'label.sys.user.username' | translate}}
        </th>
        <th>
            {{'label.sys.user.fullname' | translate}}
        </th>
    </tr>
    </thead>
    <tbody>
    <tr ng-repeat="user in pagination.records" data-toggle="context"
        data-target="#context-menu">
        <td>
            <input name="pk" type="checkbox" ng-model="user"
                   model-array="selectUsers" ed-checkbox/>
        </td>
        <td>{{user.username}}</td>
        <td>{{user.fullName}}</td>
    </tr>
    </tbody>
</table>
```

ed-check-all指令

参数：

- el-name：全选时需要选中的checkbox的name，默认值:pk

ed-checkbox指令:列表的checkbox
参数

- ng-model:checkbox对应的model
- model-array:列表中被选中的model数组
- showcontextmenu : 是否启用右键菜单,默认为启用

` data-toggle="context" data-target="#context-menu"`右键菜单的bootsrap配置，data-target的值为右键菜单的dom选择器

**3.增加工具栏菜单**
```
<!--工具栏开始-->
<div class="table-tools">
    <div class="row">
        <div class="col-md-4 col-sm-12">
            <a class="btn btn-blue" href="#/sys/user/add" ed-tooltip
               data-original-title="{{'label.sys.user.add' | translate}}" ed-permisson perm="sys:user:**:create">
                <i class="fa fa-plus"></i>
            </a>
            <a class="btn btn-pink" type="button" href="javascript:;" ed-tooltip
               data-original-title="{{'label.common.op.delete' | translate}}"
               model-array="selectUsers" ed-delete-all
               click-fun="remove(model)" success-fun="reload()" ng-hide="selectUsers.length == 0"
               ed-permisson perm="sys:user:**:delete">
                <i class="fa fa-trash-o"></i>
            </a>
            <a class="btn btn-pink" href="#/sys/user/edit/{{user.userId}}" ed-tooltip
               data-original-title="{{'label.common.op.edit' | translate}}" ng-hide="selectUsers.length != 1"
               ed-permisson perm="sys:user:**:update">
                <i class="fa fa-edit"></i>
            </a>
        </div>

        <div class="col-md-8 col-sm-12">
            ..
        </div>

    </div>
</div>
<!--工具栏结束-->
```
ed-tooltip指令，实现元素的tooltip，data-original-title的值为提示内容
 ed-permisson指令，按钮权限指令
 参数
 
- perm：权限字符串，与数据库中保存的资源的权限字符串对应

ed-delete-all指令，删除数据的指令
参数

- click-fun :点击按钮后调用的方法,此方法需要返回$promise
```
$scope.remove = function (model) {
 return I18nService.remove({i18nId : model.i18nId, updatedTime:model.updatedTime}).$promise;
 }
```

- success-fun :删除成功后调用的方法
- model-array: 需要删除的model数字

在CSS中，定义了多种颜色的按钮，因为有一些并没有根据页面的整体布局做调整，所以按钮颜色样式建议使用下面的六种样式

- .btn-blue 新增、保存、查询按钮
- .btn-pink 删除按钮
- .btn-green 修改按钮
- .btn-orange 
- .btn-purple 查看按钮
- .btn-default 取消按钮

**4.增加右键菜单**
```
<!--右键菜单开始-->
<div id="context-menu">
    <ul class="dropdown-menu dropdown-arrow-left" role="menu">
        <li>
            <a href="javascript:;" ed-tooltip
               model-array="selectMenus" ed-delete-all
               click-fun="remove(model)" success-fun="reload()" ng-hide="selectMenus.length == 0">
                <i class="fa fa-trash-o"></i>
                {{'label.common.op.delete' | translate}}
            </a>
        </li>			
    </ul>
</div>
```
**Controller**
```
function UserListCtrl($scope, UserService) {

    $scope.queryParam = {};
    $scope.enterQueryValue = function ($event) {
        if ($event.keyCode == "13") {
            $scope.query();
        }
    };
    $scope.query = function () {
        $scope.$broadcast("query");
        $scope.$broadcast("unCheckedAll");
    };
    $scope.reload = function () {
        $scope.$broadcast("reload");
        $scope.$broadcast("unCheckedAll");
    };

    //删除
    $scope.remove = function (model) {
        return UserService.remove({userId : model.userId, updatedTime:model.updatedTime}).$promise;
    }

    //监听选择事件
    $scope.$watchCollection("selectUsers", function (value) {
        if (value) {
            $scope.user = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectUsers = [];
    });

}
UserListCtrl.$inject = [ '$scope', 'UserService'];
```

- queryParam 查询参数
- remove 删除model是方法
- $scope.$watchCollection("selectUsers", function (value) {}) 监听checkbox的选择
- $scope.$watch("pagination", function (value) {}) 监听页面的刷新

**Controller中声明的变量必须与view中的变量相同**

### 表单页面
表单页面基本上与列表页面相同，本节只描述form部分的内容
**开启校验**
`<form  name="form" novalidate>`使用novalidate将激活AngularJS的表单校验功能
保存按钮需要与表单的校验结绑定，如果表单校验不通过或者表单的内容未修改，则禁用保存按钮
```
<button class="btn btn-blue" type="button" ng-disabled="form.$invalid || isUnchanged() || clickToken" ng-click="save()">
    <i class="fa fa-check"></i>
    {{'label.common.op.save' | translate}}
</button>
```
**输入校验**
```
<div class="form-group"
	 ng-class="{'has-error': form.username.$dirty && form.username.$invalid}">
	<label class="col-md-3 control-label">{{'label.sys.user.username' | translate}}<span
			class="symbol required"></span></label>

	<div class="col-md-5">
		<input type="text" id="username" name="username" class="form-control"
			   ng-model="user.username" ed-focus required ng-minlength=3 ng-maxlength=16
			   ng-pattern="/^[a-zA-Z][0-9a-zA-Z_]*$/" ed-ensure-unique="sys/user/check/username">
		<span class='help-block'
			  ng-show="form.username.$error.required && form.username.$focused">{{'help.common.required' | translate}}</span>
		<span class='help-block'
			  ng-show="form.username.$error.pattern && form.username.$focused">{{'help.sys.user.username.pattern' | translate}}</span>
		<span class='help-block'
			  ng-show="(form.username.$error.minlength || form.username.$error.maxlength) && form.username.$focused">{{'help.sys.user.username.length' | translate}}</span>
		<span class='help-block'
			  ng-show="form.username.$error.unique && form.username.$focused">{{'help.sys.user.username.unique' | translate}}</span>
	</div>
</div>
```

- `ng-class="{'has-error': form.username.$dirty && form.username.$invalid}"`当校验不通过时使用has-error样式包裹form-group
- ed-focus指令，监听input的鼠标事件,当鼠标进入元素时,设置model的$focused为true;离开元素时,设置model的$focused为false
- required 必填项
- ng-minlength 长度校验
- ng-maxlength 长度校验
- ng-pattern 正则校验
- ed-ensure-unique 唯一性校验

**Controller**
```
function UserAddCtrl($scope, UserService, RoleService, MessageService, LocationTo) {
    $scope.master = {};
    $scope.user = angular.copy($scope.master);
    $scope.clickToken = false;


    $scope.save = function () {
        $scope.clickToken = true;
        UserService.save($scope.user, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/user/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.user, $scope.master);
    };

    $scope.roles = RoleService.query({}, function (data) {
        var options = [];
        angular.forEach(data, function (role, key) {
            options.push({id: role.roleId, text: role.roleName});
        });
        $scope.options = options
//        $(".select2").select2();
//        $scope.$broadcast("init", options);
    });
}
UserAddCtrl.$inject = [ '$scope', 'UserService', 'RoleService', 'MessageService', 'LocationTo'];
```

- isUnchanged方法 判断model是否被修改
- clickToken 用来判断保存按钮是否被点击，避免用户多次点击保存
- MessageService.saveSuccess();显示成功消息
- LocationTo.path("/sys/user/list");保存成功后跳转的地址

**Controller中声明的变量必须与view中的变量相同**

## 后台
### 目录说明

| 目录 | 说明 |
|--------|--------|
|    src/main/java		   |存放代码实现阶段应用的java代码       |
|    com.edgar.core		    |存放核心类        |
|    com.edgar.core.cache	 |缓存        |
|    com.edgar.core.command|命令模式            |
|    com.edgar.core.exception|异常      |
|   com.edgar.core.init	 |系统启动时加载        |
|    com.edgar.core.job	    |作业服务        |
|    com.edgar.core.log	    |日志记录        |
|    com.edgar.core.mail	   |邮件服务       |
|    com.edgar.core.mvc	   |Spring MVC        |
|    com.edgar.core.repository	   |持久层       |
|    com.edgar.core.shiro	    |安全认证        |
|    com.edgar.core.util	   |工具类   |
|    com.edgar.core.validator		   |校验类   |
|    com.edgar.core.view		   |视图类   |
|    com.edgar.core.workflow		   |工作流   |
|    com.csst.module.*	   |	存放每个模块的源代码   |
|    com.csst.module.*.dao		   |DAO层   |
|    com.csst.module.*. init	   |平台启动时需要启动的服务   |
|    com.csst.module.*.repository	   |持久层类   |
|    com.csst.module.*. repository.domain	   |实体类，由QueryDSL生成   |
|    com.csst.module.*. repository.querydsl	   |QueryDSL查询类，由QueryDSL生成   |
|    com.csst.module.*. service	   |业务逻辑层   |
|    com.csst.module.*.validator	   |校验类   |
|    com.csst.module.*.view		   |自定义视图类   |
|    com.csst.module.*. web		   |Rest服务   |
|    src/main/resource		   |存放代码实现阶段应用的本地资源文件和配置资源文件   |
|    src/main/resource/ehcache		   |	缓存配置文件   |
|    src/main/resource/i18n		   |	国际化文件   |
|    src/main/resource/spring		   |Spring配置文件   |
|    src/main/resource/tpl		   |Velocity模板文件（主要用于邮件发送）   |
|    src/test/java		   |存放代码测试的Junit的java代码   |
|    src/test/resource			   |存放代码测试阶段的配置资源文件  |

### Rest
Rest使用Spring MVC实现，类名统一使用Resource结尾，如：SysUserResource
Resource类中不应该出现业务逻辑的代码。
```
        @AuthHelper("Query User")
        @RequestMapping(method = RequestMethod.GET, value = "/pagination")
        @ResponseBody
        public Pagination<SysUser> pagination(
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                        @ToQueryExample(maxNumOfRecords = 5000) QueryExample example) {
                return sysUserService.pagination(example, page, pageSize);
        }
```
`@AuthHelper`资源辅助类
参数

- value 资源的名称
- isRoot 是否是root级别，root级别的资源只有超级管理员有权限访问
- AuthType 权限级别，ANON不需要认证；AUTHC：用户需要登录；REST：用户需要有rest授权。默认为REST

系统启动时会根据`AuthHelper`去维护系统资源表，生成权限认证需要的数据

`@ToQueryExample`读取request参数，并封装成QueryExample
参数

- QueryType 查询类型，EQUALS_TO =查询；CONTAIN like '%...%'查询；BEGIN_WITH like '%..'查询。默认为EQUALS_TO
- maxNumOfRecords 最多查询多少条记录。默认值0，表示不限制

### Service
#### 接口声明
```
@Validated
public interface SysUserService {
        int save(@NotNull SysUserRoleCommand sysUser);       
}
```
接口需要使用`@Validated`、`@NotNull `约束接口的方法参数。在resource调用service代码时，hibernate validator会校验参数是否合法，不合法将抛出异常
#### 接口实现
```
@Override
@Transactional
public int save(SysUserRoleCommand sysUser) {
		validator.validator(sysUser);
		sysUser.setUserId(IDUtils.getNextId());
		sysUser.setIsRoot(false);
		PasswordHelper.encryptPassword(sysUser);
		int result = sysUserDao.insert(sysUser);
		insertSysUserRoles(sysUser);
		saveDefaultProfile(sysUser.getUserId());
		return result;
}
```
业务逻辑代码执行之前，首先需要对参数进行校验，不合法的讲抛出异常。validator使用的hibernate validator，hibernate validator可以直接在实体类的属性上使用注解实现数据的校验。因为通常新增和修改需要使用的校验规则会有所不同，所以采用编程式的hibernate validator。校验类需要实现`ValidatorStrategy`接口
```
public class SysUserValidator extends AbstractValidatorTemplate {

        @Override
        public Validator createValidator() {
                HibernateValidatorConfiguration configuration = Validation.byProvider(
                                HibernateValidator.class).configure();

                ConstraintMapping constraintMapping = configuration.createConstraintMapping();

                constraintMapping
                                .type(SysUser.class)
                                .property("username", ElementType.FIELD)
                                .constraint(new NotNullDef())
                                .constraint(new SizeDef().max(16).min(3))
                                .constraint(new PatternDef().regexp("[a-zA-Z][0-9a-zA-Z_]*")
                                                .message("{msg.error.validation.username.pattern}"))
                                .property("password", ElementType.FIELD)
                                .constraint(new SizeDef().max(16).min(6))
                                .constraint(new NotNullDef())
                                .property("fullName", ElementType.FIELD)
                                .constraint(new NotNullDef()).constraint(new SizeDef().max(32))
                                .property("email", ElementType.FIELD)
                                .constraint(new SizeDef().max(64)).constraint(new EmailDef());
                return configuration.addMapping(constraintMapping).buildValidatorFactory()
                                .getValidator();
        }
}
```

### DAO
`AbstractCrudRepositoryTemplate`封装了基本的CRUD操作，DAO只需要继承`AbstractCrudRepositoryTemplate`即可。具体的方法参考代码注释
```
@Repository
public class SysUserDao extends AbstractCrudRepositoryTemplate<Integer, SysUser> {

        @Override
        public RelationalPathBase<?> getPathBase() {
                return QSysUser.sysUser;
        }

        @Override
        public boolean cacheEnabled() {
                return true;
        }

}
```
