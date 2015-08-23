--[[
ϵͳ�趨��Ϊ��
	ENV 	��������
	MAKE 	�������
	CHANNEL ��������	������ȡ	�μ�CHANNEL.lua
	DEVICE	�豸����	������ȡ	�μ�DEVICE.lua
]]--
--===============================================================--
-- ʵ����Ŀ�Ĵ���ռ�
--===============================================================--
USERCODE = {}
--===============================================================--
-- �豸��ص�������Ŀ
-- λ��: make_lua\DEVICE
-- _COMMON.LUA��������װ��
--===============================================================--
DEVICECONFIG = {}
DEVICE={}
	-- װ������DEVICE ���� CHANNEL ���ã�ע�� _COMMON.LUA���������ڵ�һ��
	function mydofile(filename)
		if string.find(string.upper(filename),"_COMMON.LUA")==nil then
			dofile(filename)
		end
	end
--------------------------------------------------------
	-- make.lua��������ʱ�򣬵��ñ�����
	function DEVICECONFIG_Init(ChannelName,Brand,Device)
		DEVICE = DEVICECONFIG[Device]
		if (DEVICE==nil) then
			ShowError("����","�豸"..Device.."������,�Զ�����ΪN73.")
			DEVICE = DEVICECONFIG["N73"]
		end
		return DEVICE
	end
	function LoadAllDEVICECONFIG(deviceroot)
		ShowInfo(string.format("...װ�� %s ���豸(DEVICE)...",deviceroot))
		dofile(string.format("%s\\_COMMON.LUA",deviceroot))
		ForAllFile(deviceroot,{".lua",".wlua"},mydofile)
		ShowInfo(string.format("...װ�����..."))
	end

LoadAllDEVICECONFIG("DEVICE")


--===============================================================--
-- ������ص�������Ŀ
--===============================================================--
SPCONFIG = {}
CHANNEL={}
--------------------------------------------------------
	-- make.lua��������ʱ�򣬵��ñ�����
	function SPCONFIG_Init(ChannelName,Brand,Device)
		CHANNEL = SPCONFIG[ChannelName]
		if (CHANNEL==nil) then
			ShowError("����","ͨ��"..ChannelName.."������.")
			CHANNEL = SPCONFIG["JY"]
		end
		--
		CHANNEL.ArchiveName = ChannelArchiveName
		CHANNEL.LibPath = ChannelLibPath
		CHANNEL.Lib	= ChannelLib
		return CHANNEL
	end
	-- װ������CHANNEL���ã�ע�� _COMMON.LUA���������ڵ�һ��
	function LoadAllChannelConfig(sproot)
		ShowInfo(string.format("...װ�� %s ��ͨ��(CHANNEL)...",sproot))
		dofile(string.format("%s\\_COMMON.LUA",sproot))
		ForAllFile(sproot,{".lua",".wlua"},mydofile)
		ShowInfo(string.format("...װ�����..."))
	end
	-- ��������ָ��������
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
	-- ������������Jar��·�������ӵ�һ��
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
			ShowInfo("�޷��ҵ� JYWRAPPER_LIBS " .. MAKE.BUILD_TYPE .. " " .. MAKE.DEVICE_MODEL )
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
	-- �����������еĿ�ı�
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
-- ������ص�������Ŀ
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

			tryTorun("env_user.lua")	-- ���ļ��޸� ������ļ���·�� CUSTOM_BUILD_ROOT




			ENV.ICON_SIZE			= DEVICE.ICON_SIZE .. CHANNEL.ICON_EXT
			ENV.TEXT_ENCODING		=	"UTF-8" -- TODO :: move to device.
			ENV.WTK					="C:\\Java\\WTK22"
			ENV.JDK_PATH			="C:\\Java\\j2sdk1.6.0"
			ENV.JDK_HOME			="C:\\Java\\j2sdk1.6.0"
			ENV.ADK_HOME			="C:\\Java\\Android.Win"
			-- ��Ŀ·��
			ENV.PROJ_DIR				=string.gsub (lfs.currentdir(), "(\\make_lua)","")
			ENV.WORKSPACE				=ENV.PROJ_DIR.."\\workspace"
			ENV.MAKE_PATH				=ENV.PROJ_DIR.."\\make_lua"
			-- Ant & Antenna ���·��
			ENV.ANT_BUILD_XML			=ENV.MAKE_PATH.."\\preprocess"
			ENV.ANT						="C:\\Java\\_Tools\\ant\\1.7.1\\bin"
			-- workspace ���·��
			ENV.MAKE_WORKSPACE			=ENV.PROJ_DIR.."\\make_lua_data\\for_workspace"	-- ��Ŀ¼��ʱδ��
			ENV.DEVICE_WORKSPACE		=ENV.MAKE_WORKSPACE.."\\"..Device
			-- ����·��
			ENV.TOOLS_PATH				="C:\\Java\\_Tools"
			ENV.PYTHON_PATH				=ENV.TOOLS_PATH.."\\Python"
			ENV.TAG						=ENV.TOOLS_PATH.."\\tag_tool"
			-- LIB ·��
			ENV.THIRDPARTY_LIBS			=ENV.PROJ_DIR.."\\3rdlibs"
			ENV.JAVALIB					=ENV.TOOLS_PATH.."\\java_lib"
			ENV.CLASSPATH				=ENV.TOOLS_PATH.."\\java_lib\\genericClasspath.jar"
			ENV.JYLIB_SRC				=ENV.PROJ_DIR.."\\_JYLib"
			ENV.KEMULATOR				=ENV.TOOLS_PATH .. "\\KEmulator\\kemulator.jar"

			-- Դ����·��
			ENV.COMMON_SRC				=ENV.PROJ_DIR.."\\src\\_common"
			ENV.SPECIFIC_SRC			=ENV.PROJ_DIR.."\\src\\"..DEVICE.SRCPACK
			ENV.PREPROCESS_DEFINE		=ENV.WORKSPACE.."\\"..Device.."\\mtj-build"

			-- Android J2ME Wrapper ���·��
			ENV.J2MEWRAPPER				=ENV.PROJ_DIR.."\\AndMEWrapper"

			-- JYWrapper���·��
			ENV.JYWRAPPER				=ENV.PROJ_DIR.."\\JYWrapper"
			ENV.JYWRAPPER_COMMON_SRC	=ENV.JYWRAPPER.."\\code\\src\\_common"
			ENV.JYWRAPPER_SPECIFIC_SRC	=ENV.JYWRAPPER.."\\code\\src\\" .. ChannelName
			ENV.JYWRAPPER_3RD_LIB		=ENV.JYWRAPPER.."\\code\\3rdlib\\" .. ChannelName

			ENV.JYWRAPPER_COMMON			=ENV.PROJ_DIR.."\\JYWrapper_data\\_COMMON"
			ENV.JYWRAPPER_DATA_COMMON		=ENV.PROJ_DIR.."\\JYWrapper_data\\" .. ChannelName .. "\\_COMMON"
			ENV.JYWRAPPER_DATA				=ENV.PROJ_DIR.."\\JYWrapper_data\\" .. ChannelName .. "\\" .. DEVICE.RESPACK_WRAPPER
			ENV.JYWRAPPER_USERCODE			=ENV.PROJ_DIR.."\\JYWrapper_data\\" .. ChannelName .. "\\lua"
			-- Դ����·��
			ENV.DATA_COMMON				=ENV.PROJ_DIR.."\\data\\_COMMON"
			ENV.DATA					=ENV.PROJ_DIR.."\\data\\"..DEVICE.RESPACK
			ENV.DATA_DEVICE				=ENV.PROJ_DIR.."\\data\\data_device\\"..DEVICE.MODEL
	-- 		���ݱ������·��

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
			ENV.DATA_PATH_RAW			=ENV.BLD_DATA_RAW			-- ����Ŀ��
			ENV.DATA_TOPACK_PATH		=ENV.BLD_DATA_TOPACK		-- ����Ŀ��
			ENV.DATA_OUT_PATH			=ENV.BLD_DATA_OUT			-- ����Ŀ��
			ENV.DATA_H_PATH				=ENV.BLD_DATA_H				-- ����Ŀ��
	--		�������ʹ�õ�·��
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
	--		����ʹ�õ�·��
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
-- �������������������
--===============================================================--

MAKE = {}
MAKE.PACKAGE_NAME				= "cn.thirdgwin.app"
MAKE.CHANNEL_NAME 				= "SC"
MAKE.DEVICE_BRAND				= "Nokia"
MAKE.DEVICE_MODEL				= "N73"
MAKE.CPP 					= false
MAKE.ENCODEDATA					= false
MAKE.BUILD_TYPE						= "RELEASE"	-- DEBUG/RELEASE
