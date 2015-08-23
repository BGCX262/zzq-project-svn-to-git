--[[
系统设定分为：
	ENV 	环境变量
	MAKE 	编译变量
	CHANNEL 渠道变量	函数获取	参见CHANNEL.lua
	DEVICE	设备变量	函数获取	参见DEVICE.lua
]]--
--===============================================================--
-- 实际项目的代码空间
--===============================================================--
USERCODE = {}
--===============================================================--
-- 设备相关的配置项目
-- 位置: make_lua\DEVICE
-- _COMMON.LUA必须最先装入
--===============================================================--
DEVICECONFIG = {}
DEVICE={}
	-- 装入所有DEVICE 或者 CHANNEL 配置，注意 _COMMON.LUA必须排序在第一个
	function mydofile(filename)
		if string.find(string.upper(filename),"_COMMON.LUA")==nil then
			dofile(filename)
		end
	end
--------------------------------------------------------
	-- make.lua在启动的时候，调用本函数
	function DEVICECONFIG_Init(ChannelName,Brand,Device)
		DEVICE = DEVICECONFIG[Device]
		if (DEVICE==nil) then
			ShowError("警告","设备"..Device.."不存在,自动调整为N73.")
			DEVICE = DEVICECONFIG["N73"]
		end
		return DEVICE
	end
	function LoadAllDEVICECONFIG(deviceroot)
		ShowInfo(string.format("...装入 %s 下设备(DEVICE)...",deviceroot))
		dofile(string.format("%s\\_COMMON.LUA",deviceroot))
		ForAllFile(deviceroot,{".lua",".wlua"},mydofile)
		ShowInfo(string.format("...装入完毕..."))
	end

LoadAllDEVICECONFIG("DEVICE")


--===============================================================--
-- 渠道相关的配置项目
--===============================================================--
SPCONFIG = {}
CHANNEL={}
--------------------------------------------------------
	-- make.lua在启动的时候，调用本函数
	function SPCONFIG_Init(ChannelName,Brand,Device)
		CHANNEL = SPCONFIG[ChannelName]
		if (CHANNEL==nil) then
			ShowError("警告","通道"..ChannelName.."不存在.")
			CHANNEL = SPCONFIG["JY"]
		end
		--
		CHANNEL.ArchiveName = ChannelArchiveName
		CHANNEL.LibPath = ChannelLibPath
		CHANNEL.Lib	= ChannelLib
		return CHANNEL
	end
	-- 装入所有CHANNEL配置，注意 _COMMON.LUA必须排序在第一个
	function LoadAllChannelConfig(sproot)
		ShowInfo(string.format("...装入 %s 下通道(CHANNEL)...",sproot))
		dofile(string.format("%s\\_COMMON.LUA",sproot))
		ForAllFile(sproot,{".lua",".wlua"},mydofile)
		ShowInfo(string.format("...装入完毕..."))
	end
	-- 生成渠道指定的名称
	function ChannelArchiveName(self)
		local arch_name=""
		for _,v in pairs(self.ARCHIVE_FORMAT) do
			local v = string.upper(v)
			if v == "GAME" then
				arch_name = arch_name .. self.PACK_GAME_NAME
			elseif v == "DEVICE" then
				dvModel = MAKE.DEVICE_MODEL
				if USERCODE.MapModelName ~= nil then
					dvModel = USERCODE.MapModelName(dvModel)
				end
				arch_name = arch_name .. dvModel
			elseif v == "BRAND" then
				arch_name = arch_name .. MAKE.DEVICE_BRAND
			elseif v == "VERSION" then
				arch_name = arch_name .. self.VERSION_NUMBER
			elseif v == "VENDERNAME" then
				arch_name = arch_name .. self.Name
			else
				arch_name = arch_name .. v

			end
		end
		return arch_name
	end
	-- 将渠道的所有Jar的路径名链接到一起
	function ChannelLibPath(self)
		local path = ""
		local sep = ""
		ShowInfo("------------------------------------------")
		ShowInfo(MAKE.THIRDLIB_TYPE)
		ShowInfo("------------------------------------------")
		ShowInfo(MAKE.DEVICE_MODEL)
		ShowInfo("------------------------------------------")
		local libs 
		libs = self.JYWRAPPER_LIBS[MAKE.BUILD_TYPE][MAKE.DEVICE_MODEL]
		if (libs) then
			for _,v in pairs(libs) do
				path = path .. string.format("%s%s\\%s\\%s",sep,ENV.JYWRAPPER_3RD_LIB,MAKE.BUILD_TYPE,v)
				sep = ";"
			end
		else
			ShowInfo("无法找到 JYWRAPPER_LIBS " .. MAKE.BUILD_TYPE .. " " .. MAKE.DEVICE_MODEL )
		end
		if (CHANNEL.THIRDLIB[MAKE.BUILD_TYPE]) then
			for _,v in pairs(CHANNEL.THIRDLIB[MAKE.BUILD_TYPE]) do
				path = path .. string.format("%s%s\\%s\\%s",sep,ENV.THIRDPARTY_LIBS,MAKE.BUILD_TYPE,v)
				sep = ";"
			end
		end
		ShowInfo(path)

		return path
	end
	-- 返回渠道所有的库的表
	function ChannelLib(self)
		
		local libs 
			libs = self.JYWRAPPER_LIBS[MAKE.BUILD_TYPE][MAKE.DEVICE_MODEL]
		local retV = {}
		if(libs) then
			for k,v in pairs(libs) do
				table.insert(retV,string.format("%s\\%s\\%s",ENV.JYWRAPPER_3RD_LIB,MAKE.BUILD_TYPE,v))
			end
		end
		if(CHANNEL.THIRDLIB[MAKE.BUILD_TYPE]) then
			for _,v in pairs(CHANNEL.THIRDLIB[MAKE.BUILD_TYPE]) do
				table.insert(retV,string.format("%s\\%s\\%s",ENV.THIRDPARTY_LIBS,MAKE.BUILD_TYPE,v))
			end
		end
		return retV
	end
LoadAllChannelConfig("Channel")

--===============================================================--
-- 环境相关的配置项目
--===============================================================--

ENV = {
	__myindex = __index,
	__index = function(key)
		local ret = __myindex(key)
		if (ret==nil) then
			ShowError("Try to Access Uninited ENV %s",key)
			ret = ""
		end
		return ret
	end
	}

	function ENV.Init(ChannelName,Brand,Device)

			tryTorun("env_user.lua")	-- 此文寄修改 编译后文件的路径 CUSTOM_BUILD_ROOT




			ENV.ICON_SIZE			= DEVICE.ICON_SIZE .. CHANNEL.ICON_EXT
			ENV.TEXT_ENCODING		=	"UTF-8" -- TODO :: move to device.
			ENV.WTK					="C:\\Java\\WTK22"
			ENV.JDK_PATH			="C:\\Java\\j2sdk1.6.0"
			ENV.JDK_HOME			="C:\\Java\\j2sdk1.6.0"
			ENV.ADK_HOME			="C:\\Java\\Android.Win"
			-- 项目路径
			ENV.PROJ_DIR				=string.gsub (lfs.currentdir(), "(\\make_lua)","")
			ENV.WORKSPACE				=ENV.PROJ_DIR.."\\workspace"
			ENV.MAKE_PATH				=ENV.PROJ_DIR.."\\make_lua"
			-- Ant & Antenna 相关路径
			ENV.ANT_BUILD_XML			=ENV.MAKE_PATH.."\\preprocess"
			ENV.ANT						="C:\\Java\\_Tools\\ant\\1.7.1\\bin"
			-- workspace 相关路径
			ENV.MAKE_WORKSPACE			=ENV.PROJ_DIR.."\\make_lua_data\\for_workspace"	-- 此目录暂时未用
			ENV.DEVICE_WORKSPACE		=ENV.MAKE_WORKSPACE.."\\"..Device
			-- 工具路径
			ENV.TOOLS_PATH				="C:\\Java\\_Tools"
			ENV.PYTHON_PATH				=ENV.TOOLS_PATH.."\\Python"
			ENV.TAG						=ENV.TOOLS_PATH.."\\tag_tool"
			-- LIB 路径
			ENV.THIRDPARTY_LIBS			=ENV.PROJ_DIR.."\\3rdlibs"
			ENV.JAVALIB					=ENV.TOOLS_PATH.."\\java_lib"
			ENV.CLASSPATH				=ENV.TOOLS_PATH.."\\java_lib\\genericClasspath.jar"
			ENV.JYLIB_SRC				=ENV.PROJ_DIR.."\\_JYLib"
			ENV.KEMULATOR				=ENV.TOOLS_PATH .. "\\KEmulator\\kemulator.jar"

			-- 源代码路径
			ENV.COMMON_SRC				=ENV.PROJ_DIR.."\\src\\_common"
			ENV.SPECIFIC_SRC			=ENV.PROJ_DIR.."\\src\\"..DEVICE.SRCPACK
			ENV.PREPROCESS_DEFINE		=ENV.WORKSPACE.."\\"..Device.."\\mtj-build"

			-- Android J2ME Wrapper 相关路径
			ENV.J2MEWRAPPER				=ENV.PROJ_DIR.."\\AndMEWrapper"

			-- JYWrapper相关路径
			ENV.JYWRAPPER				=ENV.PROJ_DIR.."\\JYWrapper"
			ENV.JYWRAPPER_COMMON_SRC	=ENV.JYWRAPPER.."\\code\\src\\_common"
			ENV.JYWRAPPER_SPECIFIC_SRC	=ENV.JYWRAPPER.."\\code\\src\\" .. ChannelName
			ENV.JYWRAPPER_3RD_LIB		=ENV.JYWRAPPER.."\\code\\3rdlib\\" .. ChannelName

			ENV.JYWRAPPER_COMMON			=ENV.PROJ_DIR.."\\JYWrapper_data\\_COMMON"
			ENV.JYWRAPPER_DATA_COMMON		=ENV.PROJ_DIR.."\\JYWrapper_data\\" .. ChannelName .. "\\_COMMON"
			ENV.JYWRAPPER_DATA				=ENV.PROJ_DIR.."\\JYWrapper_data\\" .. ChannelName .. "\\" .. DEVICE.RESPACK_WRAPPER
			ENV.JYWRAPPER_USERCODE			=ENV.PROJ_DIR.."\\JYWrapper_data\\" .. ChannelName .. "\\lua"
			-- 源数据路径
			ENV.DATA_COMMON				=ENV.PROJ_DIR.."\\data\\_COMMON"
			ENV.DATA					=ENV.PROJ_DIR.."\\data\\"..DEVICE.RESPACK
			ENV.DATA_DEVICE				=ENV.PROJ_DIR.."\\data\\data_device\\"..DEVICE.MODEL
	-- 		数据编译过程路径

			if CUSTOM_BUILD_ROOT then
				ENV.BLD						=CUSTOM_BUILD_ROOT.."\\.1-Build\\"..ChannelName.."\\"..Device
			else
				ENV.BLD						=ENV.PROJ_DIR.."\\.1-Build\\"..ChannelName.."\\"..Device
			end
			ENV.BLD_DATA				=ENV.BLD.."\\data"
			ENV.BLD_DATA_RAW			=ENV.BLD.."\\data\\0-raw"
			ENV.BLD_DATA_TOPACK			=ENV.BLD.."\\data\\1-ToPack"
			ENV.BLD_DATA_OUT			=ENV.BLD.."\\data\\2-Out"
			ENV.BLD_DATA_OUT_AND			=ENV.BLD.."\\data\\2-Out\\assets"
			ENV.BLD_DATA_OUT_AND_MENURES		=ENV.BLD.."\\data\\2-Out\\assets\\_menu"
			ENV.BLD_DATA_OUT_AND_SOUNDRES		=ENV.BLD.."\\data\\2-Out\\assets\\_sound"
			ENV.BLD_DATA_H				=ENV.BLD.."\\data\\2-Src"
			ENV.DATA_PATH_RAW			=ENV.BLD_DATA_RAW			-- 兼容目的
			ENV.DATA_TOPACK_PATH		=ENV.BLD_DATA_TOPACK		-- 兼容目的
			ENV.DATA_OUT_PATH			=ENV.BLD_DATA_OUT			-- 兼容目的
			ENV.DATA_H_PATH				=ENV.BLD_DATA_H				-- 兼容目的
	--		代码编译使用的路径
			ENV.BLD_SRC					=ENV.BLD.."\\src"
			ENV.BLD_SRC_RAW				=ENV.BLD.."\\src\\0-raw"
			ENV.BLD_SRC_PREPROCESS			=ENV.BLD.."\\src\\1-preprocess"
			ENV.BLD_SRC_PREPROCESS_TMP 		=ENV.BLD.."\\src\\1-preprocess\\tmp"
			ENV.BLD_SRC_RAW_FOR_CPP_APP		=ENV.BLD.."\\src\\0-raw\\com\\joyomobile\\app"
			ENV.BLD_SRC_RAW_FOR_CPP_LIB		=ENV.BLD.."\\src\\0-raw\\com\\joyomobile\\lib"
			ENV.BLD_SRC_PREPROCESS_FOR_CPP_APP	=ENV.BLD_SRC_PREPROCESS_TMP.."\\com\\joyomobile\\app"
			ENV.BLD_SRC_PREPROCESS_FOR_CPP_LIB	=ENV.BLD_SRC_PREPROCESS_TMP.."\\com\\joyomobile\\lib"
			ENV.BLD_SRC_COMPILED			=ENV.BLD.."\\src\\2-compiled"
			ENV.BLD_SRC_PREVERIFIED			=ENV.BLD.."\\src\\3-preverified"
			ENV.BLD_PACK				=ENV.PROJ_DIR.."\\.pack_internal\\"..Device
			ENV.BLD_PACK_JARFILE			=ENV.PROJ_DIR.."\\.pack_internal\\"..Device.."\\"..Device..".jar"
	--		后处理使用的路径
			if CUSTOM_BUILD_ROOT then
				ENV.PP						=CUSTOM_BUILD_ROOT.."\\.2-PP\\"..ChannelName.."\\"..Device
			else
				ENV.PP						=ENV.PROJ_DIR.."\\.2-PP\\"..ChannelName.."\\"..Device
			end
			ENV.PP_DATA_RAW				=ENV.PP.."\\data\\0-raw"
			ENV.PP_DATA_OUT				=ENV.PP.."\\data\\1-Out"
			ENV.PP_DATA_H				=ENV.PP.."\\data\\1-Src"
			ENV.PP_SRC_RAW				=ENV.PP.."\\src\\0-raw"
			ENV.PP_SRC_COMPILED			=ENV.PP.."\\src\\1-compiled"
			ENV.PP_SRC_MERGED			=ENV.PP.."\\src\\1-merged"
			ENV.PP_SRC_OPTIMIZED		=ENV.PP.."\\src\\2-optimized"
			ENV.PP_SRC_OBFUSCATED		=ENV.PP.."\\src\\3-obfuscated"
			ENV.PP_SRC_PREVERIFIED		=ENV.PP.."\\src\\4-preverified"
			ENV.PP_MANIFEST				=ENV.PP.."\\src\\4-preverified\\META-INF"
			ENV.PP_PACK					=ENV.PROJ_DIR.."\\.pack\\"..ChannelName.."\\"..Device
	end
--===============================================================--
-- 编译参数，来自命令行
--===============================================================--

MAKE = {}
MAKE.PACKAGE_NAME				= "cn.thirdgwin.app"
MAKE.CHANNEL_NAME 				= "SC"
MAKE.DEVICE_BRAND				= "Nokia"
MAKE.DEVICE_MODEL				= "N73"
MAKE.CPP 					= false
MAKE.ENCODEDATA					= false
MAKE.BUILD_TYPE						= "RELEASE"	-- DEBUG/RELEASE
