SP_COMMON = {
		CHANNEL_CHSNAME	= "�����趨",
		CHANNEL_NAME	= "common",

		-- ������
		Vender		= "ShuangChen",
		MIDLET_CLASS	= "GameMIDlet",
		GAME_NAME	= "����WRAPPER_DATA��usercode.lua���޸�",
		PACK_GAME_NAME	= "����WRAPPER_DATA��usercode.lua���޸�",
		PACK_NAME	= "����WRAPPER_DATA��usercode.lua���޸�",
		ARCHIVE_FORMAT = {"Game","_","Device"},				-- jar���ļ���ʽ --
		VERSION_NUMBER="1.0.0",

		--ICON_EXT��������������ڡ�����ͬһ�������в�ͬ���������Ծ�������ʹ�ô˲���
		ICON_EXT = "",  -- ��DEVICE�е�ICON_SIZEһ����ϳ����յ�ICON32X32_g.png����  ICON_EXT = "_g

		-- ������ص���չ��,���豸Ʒ��Ϊ�ؼ��� --
		JYWRAPPER_LIBS = {
			DEFAULT = {},
			["DEBUG"] = {
				["DEVICE_MODEL"]={"LibName.jar"},
			},
			["RELEASE"] = {
				["DEVICE_MODEL"]={"LibName.jar"},
			},
		},
		-- ��������
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

		-- ���뵽JAD�ļ��е����� --
		TOJAD = {
			"",
			"",
		},



		-- ������
		PROGUARD_OPT =
			{

			},
		-- ����ʹ���һ�����
		OBFUSCAT_LIBS = "1",
		LIB_DATAS = {
			},

		-- Ԥ����
		PREPROCESS =
			{
			-- �汾��� --
			WATERMARK		= FALSE,	-- �Ƿ���ˮӡ
			TRIAL			= FALSE,	-- �����
			REVIEW			= FALSE,	-- �����
			FEE				= FALSE,		-- �Ʒѵ�
			SHOWGAMENAME		= FALSE,	-- ��Ϸ����
			LOGO	                = TRUE,		-- CPlogo
			TIGER			= TRUE,		-- �ϻ�������
			ABOUTUS	                = TRUE,		-- ������Ϣ
			MOREGAME	        = TRUE,		-- ������Ϸ
			SOUNDONOFF		= TRUE,		-- ����������
			LRSK_ORIGINAL           = TRUE,       
			["DEVICE"]	= {},		-- ������صĺ꣬����BBX.lua����д,���գ�bbx.lua. theSP["PREPROCESS"]["DEVICE"] 
			},
		-- ĳЩ�������к��豸��ص���Ϣ �������ᣬ�ͷŵ������
		DEVICE_OPTIONS = {
			-- �����������������������е�һЩ���á���ʽ������={������=��������, ������=��������...}�����統�ֵ�DEVICE_ID
			-- �������=ALL����ʾ���л��Ͷ�ͨ�õ�һЩ������Ϣ�����統�ֵ�CP_ID
			-- @NOTE: �͵��ֹ�ͨ�󣬵��ֱ�ʾ��device id����ʹ�ã����Ե�ǰ����ʱ����
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
