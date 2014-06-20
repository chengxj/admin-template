
set foreign_key_checks=0;

drop table if exists `sequence`;
create table `sequence` (
  `id` bigint(11) not null auto_increment,
  `stub` char(1) not null,
  primary key (`id`),
  unique key `uni_stub` (`stub`)
) engine=myisam auto_increment=9 default charset=utf8;


insert into sys_route(route_id, name, url) values (1, '路由列表', '/sys/route/list');
insert into sys_route(route_id, name, url) values (2, '新增路由', '/sys/route/add');
insert into sys_route(route_id, name, url) values (3, '修改路由', '/sys/route/edit/:routeId');
insert into sys_route(route_id, name, url) values (4, '字典列表', '/sys/dict/list');
insert into sys_route(route_id, name, url) values (5, '新增字典', '/sys/dict/add');
insert into sys_route(route_id, name, url) values (6, '修改字典', '/sys/dict/edit/:dictCode');
insert into sys_route(route_id, name, url) values (7, '字典子项列表', '/sys/dict/items/:dictCode');
insert into sys_route(route_id, name, url) values (8, '新增字典子项', '/sys/dict/addItem/:dictCode');
insert into sys_route(route_id, name, url) values (9, '修改字典子项', '/sys/dict/editItem/:dictCode');
insert into sys_route(route_id, name, url) values (10, '用户列表', '/sys/user/list');
insert into sys_route(route_id, name, url) values (11, '新增用户', '/sys/user/add');
insert into sys_route(route_id, name, url) values (12, '修改用户', '/sys/user/edit/:userId');
insert into sys_route(route_id, name, url) values (13, '查看用户', '/sys/user/view:userId');
insert into sys_route(route_id, name, url) values (14, '角色管理', '/sys/role/list');
insert into sys_route(route_id, name, url) values (15, '新增角色', '/sys/role/add');
insert into sys_route(route_id, name, url) values (16, '修改角色', '/sys/role/edit/:roleId');
insert into sys_route(route_id, name, url) values (17, '角色权限配置', '/sys/role/permission/:roleId');
