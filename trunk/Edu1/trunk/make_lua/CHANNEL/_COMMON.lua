SP_COMMON = {
		CHANNEL_CHSNAME	= "公共设定",
		CHANNEL_NAME	= "common",

		-- 包定义
		Vender		= "ShuangChen",
		MIDLET_CLASS	= "GameMIDlet",
		GAME_NAME	= "请在WRAPPER_DATA下usercode.lua里修改",
		PACK_GAME_NAME	= "请在WRAPPER_DATA下usercode.lua里修改",
		PACK_NAME	= "请在WRAPPER_DATA下usercode.lua里修改",
		ARCHIVE_FORMAT = {"Game","_","Device"},				-- jar包文件格式 --
		VERSION_NUMBER="1.0.0",

		--ICON_EXT的需求基本不存在。除非同一个渠道有不同的需求。所以尽量避免使用此参数
		ICON_EXT = "",  -- 与DEVICE中的ICON_SIZE一起组合成最终的ICON32X32_g.png其中  ICON_EXT = "_g

		-- 渠道相关的扩展库,以设备品牌为关键字 --
		JYWRAPPER_LIBS = {
			DEFAULT = {},
			["DEBUG"] = {
				["DEVICE_MODEL"]={"LibName.jar"},
			},
			["RELEASE"] = {
				["DEVICE_MODEL"]={"LibName.jar"},
			},
		},
		-- 第三方库
		THIRDLIB	= {
			["DEBUG"] = {
				"game_sms.jar",
				"nokiaui.jar",
			},
			["RELEASE"] = {
				"game_sms.jar",
				"nokiaui.jar",
			},
		},

		-- 进入到JAD文件中的内容 --
		TOJAD = {
			"",
			"",
		},



		-- 混淆器
		PROGUARD_OPT =
			{

			},
		-- 将库和代码一起混淆
		OBFUSCAT_LIBS = "1",
		LIB_DATAS = {
			},

		-- 预处理
		PREPROCESS =
			{
			-- 版本相关 --
			WATERMARK		= FALSE,	-- 是否有水印
			TRIAL			= FALSE,	-- 试玩版
			REVIEW			= FALSE,	-- 评审版
			FEE				= FALSE,		-- 计费点
			SHOWGAMENAME		= FALSE,	-- 游戏名称
			LOGO	                = TRUE,		-- CPlogo
			TIGER			= TRUE,		-- 老虎机开关
			ABOUTUS	                = TRUE,		-- 关于信息
			MOREGAME	        = TRUE,		-- 更多游戏
			SOUNDONOFF		= TRUE,		-- 声音开关屏
			LRSK_ORIGINAL           = TRUE,       
			["DEVICE"]	= {},		-- 渠道相关的宏，请在BBX.lua中书写,参照：bbx.lua. theSP["PREPROCESS"]["DEVICE"] 
			},
		-- 某些渠道会有和设备相关的信息 及其讨厌，就放到这里吧
		DEVICE_OPTIONS = {
			-- 这里可以增加这个渠道里特有的一些配置。格式：机型={配置名=配置内容, 配置名=配置内容...}，例如当乐的DEVICE_ID
			-- 机型如果=ALL，表示所有机型都通用的一些配置信息。例如当乐的CP_ID
			-- @NOTE: 和当乐沟通后，当乐表示，device id并不使用，所以当前表暂时空了
				ALL={},

				--Nokia
				["N73"]={},
				["6101"]={},
				["7610"]={},
				["7370"]={},
				["N97"]={},
				["E62"]={},

				--Sonyericsson
				["K790"]={},
				["S700"]={},
				["K700"]={},
				["k506"]={},

				--Motorola
				["E6"]={},
				["V8"]={},
				["E2"]={},
				["E680"]={},
				["L7"]={},
				["L6"]={},
				["K1"]={},
				["E398"]={},

				--Samsung
				["D508"]={},
				["D608"]={},
				["E258"]={},

				--LG
				["KG90n"]={}
			}
		}
print(string.format("\t%-20s%s",SP_COMMON.CHANNEL_CHSNAME,SP_COMMON.CHANNEL_NAME))
