//生成菜单
var menu = Vue.extend({
	name: 'menu-item',
	props: {
		item: {}
	},
	template: [
		'<li>',
		'<a v-if="item.type === 0" href="javascript:;">',
		'<i :class="item.icon"></i>',
		'<span class="nav-label">{{item.name}}</span>',
		'<span class="fa arrow"></span>',
		'</a>',
		'<ul v-if="item.type === 0" class="nav nav-second-level">',
		'<menu-item :item="item" v-for="item in item.list"></menu-item>',
		'</ul>',
		'<a class="J_menuItem" v-if="item.type === 1" href="javascript:;" :data-url="item.url">',
		'<i :class="item.icon"></i>',
		'<span>{{item.name}}</span>',
		'</a>',
		'</li>'
	].join('')
});

//注册菜单组件
Vue.component('menuItem', menu);

var vm = new Vue({
	el: '#wrapper',
	data: {
		user: {},
		menuList: {},
		main: "common/main.html",
		navTitle: "首页"
	},
	methods: {
		getMenuList: function (event) {
			$.getJSON("sys/menu/user?_" + $.now(), function (r) {
				vm.menuList = r.menuList;
				addScript();
			});
		},
		getUser: function () {
			$.getJSON("sys/user/info?_" + $.now(), function (r) {
				vm.user = r.user;
			});
		}
	},
	created: function () {
		this.getMenuList();
		this.getUser();
	}
});

function addScript() {
	var contabs = document.createElement('script');
	contabs.async = false;
	contabs.src = 'js/contabs.js';
	var contabs_s = document.getElementsByTagName('script')[0];
	contabs_s.parentNode.insertBefore(contabs, contabs_s);

	var hplus = document.createElement('script');
	hplus.async = false;
	hplus.src = 'js/hplus.js?v=4.1.0';
	var hplus_s = document.getElementsByTagName('script')[0];
	hplus_s.parentNode.insertBefore(hplus, hplus_s);
}