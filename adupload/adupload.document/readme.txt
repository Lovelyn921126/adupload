管理后台
1.登录	①：在OA登录 地址：http://glht.adupload.fang.com/autologin.html
				②：在登录页面直接登录 地址：http://glht.adupload.fang.com/login.html
						
				二者区别：方式①适用于 房天下OA用户，拥有OA账号的用户，开通后台权限可直接在系统内添加用户
									方式②适用于 没有OA账号的用户(目前只有一个)，暂不支持开通

	后台业务部分主要是对老吕接口的封装
	老吕广告上传接口规则：参照“jkDoc_laolv”
	
adupload
├── common						项目公共模块
│		├── annotation		自定义注解
│		│		└── SysLog		系统日志注解
│   ├── common
│		│		├── Config		项目公共配置读取类
│		│		├── User			用户信息(主要是和通行证接口调取)
│   │		└── MsgConfig	项目公共消息
│		├── utils					主要是通用的工具方法封装
│		└── conf					放着各种公共配置信息
├── glht							后台项目
├── jk								接口项目(目前提供岳彦磊那边使用的5个jsonp接口)
└── shiro							公共权限模块(可提取供别的项目使用)
====================================

接口 具体详见“jkDoc_ad”
1.接口地址
	①增加人气和加载点赞数量 http://jk.adupload.fang.com/api/hits/pv
	②增加点赞数 http://jk.adupload.fang.com/api/hits/up
	③提交说一说 http://jk.adupload.fang.com/api/comment/savemessage
	④获取说一说 http://jk.adupload.fang.com/api/comment/message
	⑤获取用户信息 http://jk.adupload.fang.com/api/user/info
	
	都是jsonp接口，接口内增加安全校验：ip校验，referre(防盗链)校验，cookie校验
	
	*可优化部分：现在人气数量和点赞数量都在redis中存着，后期可以考虑把这部分数据先放在redis中，定时同步到DB中，同时删掉redis中对应的数据

====================================
DB:mysql 具体详见“adupload.sql”
	测试：10.16.64.44:3307/gau_test_admin/RvDsjr4d
	正式：写 10.16.65.37:3132/glht_adupload_w/ualurQw0
				读 10.16.65.38:3132/glht_adupload_r/7eWpmRii

====================================
redis
	写：10.16.2.76
	读：10.16.2.77
	端口：3706
	密码：eitc6emCEcTsowafwjbpupfFrxquPYzsdymyc4tdd
	
	广告测试用到	database2 存放说一说数据
	广告正式用到	database3 存放说一说数据
								database1 存放session数据
====================================
服务器：南方机房 具体详见“ad_server”
	正式：10.16.134.11
				10.16.134.12
	测试：10.16.134.13
	用户名密码：webuser6/soufun.com
	