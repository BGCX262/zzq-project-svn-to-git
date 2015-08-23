新建一个渠道所需要做的事：
1. 外链JYWrapper到原游戏trunk下。(https://svn.jyinfo.org/svn/J2ME/JYWrapper/trunk)
2. 原游戏trunk下新建名为 "_channel_data"的文件夹，第一层_channel_data\渠道名，如 _channel_data\EASOU,第二层_channel_data\渠道名\屏幕大小,如_channel_data\EASOU\176,这下面就是各个资源。
3.JYWrapper\buildConfiguration下新建三个渠道配置的bat,如EASOU_128.bat，EASOU_176.bat，EASOU_240.bat。这里面配置渠道的属性： USE_PAY_WRAPPER：是否paywrapper类实现支付功能，THRD_JAR：渠道商提供的第三方jar包，只需填写文件名，不需要全路径。并将以些文件名命名的jar包放在JYWrapper\lib下。这个jar包会被更名为channel_lib.jar。供原游戏的批处理编译或混淆时调用。 CHANNEL_NAME：如EASOU,请游戏，本文档提到的渠道名请全部统一成一个。
4.JYWrapper\make 下新建三个渠道打包的批处理。这里面的处理可以单独使用，也可以供原游戏的批处理调用。请参考其它渠道的写法。
5.如果要用到付费功能，请在src文件夹下建立以渠道名命名的文件夹，将_master_src下的PayWrapper.java复制进去，并完成它。如需要新建其它的java文件，也可以放到 src\渠道名\ 下。
以下为原游戏需要改动的地方：
1.trunk\make下新建打渠道批处理的文件夹，如trunk\make\EASOU,并赶写两个属性CHANNEL_NAME如EASOU(和JYWRAPPER里的一致)，SCREEN_SERIES：暂时有128,176,240三种，并调用make.bat打包。参考其它渠道。
2.其它游戏名的修改，splash的修改，等请自行处理。


可用接口：
1. 机型相关配置接口：
修改make_lua.增加两个可用接口。包括实现155魔掌的需求和当乐的需求。
可增加“机型相关配置” 以及 “游戏机型相关配置”
前者： 所有游戏里都一样。该配置只和机型相关。可以在make_lua\CHANNEL\%Channel%.lua中修改DEVICE_OPTIONS
后者： 所有游戏都不一样。该配置不光和机型相关，还和具体游戏相关。可以在JYWrapper_data\%Channel%\LUA\userCode.lua中修改SPECIAL_DEVICE_OPTIONS

其中，机型为ALL的表示所有机型都共用的。此接口可以实现给 “该渠道所有机型” 或者 “该渠道该游戏所有机型” 统一增加配置。例如 CPID(该渠道下所有游戏所有机型通用) 或者 GAMEID (该渠道下该游戏所有机型通用)

格式如下：

DEVICE_OPTIONS = {
 ALL={},	//所有机型共用配置

 --Nokia
 N73={},	//具体机型的配置
 ["6101"]={},
 ["7610"]={},
 ["7370"]={},
 N97={},
...
