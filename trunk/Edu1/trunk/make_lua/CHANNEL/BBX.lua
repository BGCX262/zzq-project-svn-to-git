-- ���ȸ��Ʊ��
theSP = table.copy(SP_COMMON)
-- �޸��豸��ص�ֵ
theSP["CHANNEL_CHSNAME"] 				="�ٱ���"
theSP["CHANNEL_NAME"] 					="BBX"

theSP["PREPROCESS"]["CHANNEL_NAME"]= theSP["CHANNEL_NAME"]
theSP["PREPROCESS"]["URL_MOREGAME"]= "http://g.10086.cn/gamecms/go/jpgd"

theSP["PREPROCESS"]["DEVICE"] = {
				--Nokia
				["N73"]		={WEAKONLINE_SMS_SCORESENDING = "FALSE",WEAKONLINE_NET_SCORESENDING = "TRUE",	WEAKONLINE_MOREGAME = "TRUE"},
				["N6101"]	={WEAKONLINE_SMS_SCORESENDING = "TRUE",	WEAKONLINE_NET_SCORESENDING = "FALSE",	WEAKONLINE_MOREGAME = "FALSE"},
				["N7610"]	={WEAKONLINE_SMS_SCORESENDING = "TRUE",	WEAKONLINE_NET_SCORESENDING = "FALSE",	WEAKONLINE_MOREGAME = "FALSE"},
				["N7370"]	={WEAKONLINE_SMS_SCORESENDING = "FALSE",WEAKONLINE_NET_SCORESENDING = "TRUE",	WEAKONLINE_MOREGAME = "TRUE"},
				["N97"]		={WEAKONLINE_SMS_SCORESENDING = "TRUE",	WEAKONLINE_NET_SCORESENDING = "FALSE",	WEAKONLINE_MOREGAME = "FALSE"},
				["E62"]		={WEAKONLINE_SMS_SCORESENDING = "FALSE",WEAKONLINE_NET_SCORESENDING = "TRUE",	WEAKONLINE_MOREGAME = "TRUE"},

				--Sonyericsson
				["K790"]	={WEAKONLINE_SMS_SCORESENDING = "FALSE",WEAKONLINE_NET_SCORESENDING = "TRUE",	WEAKONLINE_MOREGAME = "TRUE"},
				["S700"]	={WEAKONLINE_SMS_SCORESENDING = "FALSE",WEAKONLINE_NET_SCORESENDING = "TRUE",	WEAKONLINE_MOREGAME = "TRUE"},
				["K700C"]	={WEAKONLINE_SMS_SCORESENDING = "TRUE",	WEAKONLINE_NET_SCORESENDING = "FALSE",	WEAKONLINE_MOREGAME = "TRUE"},
				["K506"]	={WEAKONLINE_SMS_SCORESENDING = "TRUE",	WEAKONLINE_NET_SCORESENDING = "FALSE",	WEAKONLINE_MOREGAME = "TRUE"},

				--Motorola
				["E6"]		={WEAKONLINE_SMS_SCORESENDING = "TRUE",WEAKONLINE_NET_SCORESENDING = "FALSE",	WEAKONLINE_MOREGAME = "FALSE"},
				["V8"]		={WEAKONLINE_SMS_SCORESENDING = "FALSE",WEAKONLINE_NET_SCORESENDING = "TRUE",	WEAKONLINE_MOREGAME = "TRUE"},
				["E2"]		={WEAKONLINE_SMS_SCORESENDING = "FALSE",WEAKONLINE_NET_SCORESENDING = "TRUE",	WEAKONLINE_MOREGAME = "TRUE"},
				["E680"]	={WEAKONLINE_SMS_SCORESENDING = "TRUE",WEAKONLINE_NET_SCORESENDING = "FALSE",	WEAKONLINE_MOREGAME = "FALSE"},
				["L7"]		={WEAKONLINE_SMS_SCORESENDING = "TRUE",WEAKONLINE_NET_SCORESENDING = "FALSE",	WEAKONLINE_MOREGAME = "FALSE"},
				["L6"]		={WEAKONLINE_SMS_SCORESENDING = "TRUE",WEAKONLINE_NET_SCORESENDING = "FALSE",	WEAKONLINE_MOREGAME = "TRUE"},
				["K1"]		={WEAKONLINE_SMS_SCORESENDING = "TRUE",WEAKONLINE_NET_SCORESENDING = "FALSE",	WEAKONLINE_MOREGAME = "FALSE"},
				["E398"]	={WEAKONLINE_SMS_SCORESENDING = "TRUE",WEAKONLINE_NET_SCORESENDING = "FALSE",	WEAKONLINE_MOREGAME = "FALSE"},

				--Samsung
				["D508"]	={WEAKONLINE_SMS_SCORESENDING = "TRUE",	WEAKONLINE_NET_SCORESENDING = "FALSE",	WEAKONLINE_MOREGAME = "FALSE"},
				["D608"]	={WEAKONLINE_SMS_SCORESENDING = "FALSE",WEAKONLINE_NET_SCORESENDING = "TRUE",	WEAKONLINE_MOREGAME = "TRUE"},
				["E258"]	={WEAKONLINE_SMS_SCORESENDING = "TRUE",	WEAKONLINE_NET_SCORESENDING = "FALSE",	WEAKONLINE_MOREGAME = "TRUE"},

				--LG
				["KG90n"]	={WEAKONLINE_SMS_SCORESENDING = "FALSE",WEAKONLINE_NET_SCORESENDING = "TRUE",	WEAKONLINE_MOREGAME = "TRUE"},
}

theSP["ARCHIVE_FORMAT"] = {"Game"}

theSP["JYWRAPPER_LIBS"]["DEBUG"] =
			{
				--Nokia
				["N73"]={"gcksdk_NET_MOREGAME_BIG.jar"},
				["N6101"]={"gcksdk_SMS_COMMON.jar"},
				["N7610"]={"gcksdk_SMS_COMMON.jar"},
				["N7370"]={"gcksdk_NET_MOREGAME_BIG.jar"},
				["N97"]={"gcksdk_SMS_COMMON.jar"},
				["E62"]={"gcksdk_NET_MOREGAME_BIG.jar"},

				--Sonyericsson
				["K790"]={"gcksdk_SMS_COMMON.jar"},
				["S700"]={"gcksdk_NET_MOREGAME_BIG.jar"},
				["K700C"]={"gcksdk_SMS_COMMON.jar"},
				["K506"]={"gcksdk_SMS_MOREGAME_MIDDLE.jar"},

				--Motorola
				["E6"]={"gcksdk_SMS_COMMON.jar"},
				["V8"]={"gcksdk_NET_MOREGAME_BIG.jar"},
				["E2"]={"gcksdk_NET_MOREGAME_BIG.jar"},
				["E680"]={"gcksdk_SMS_COMMON.jar"},
				["L7"]={"gcksdk_SMS_COMMON.jar"},
				["L6"]={"gcksdk_SMS_MOREGAME_MIDDLE.jar"},
				["K1"]={"gcksdk_SMS_COMMON.jar"},
				["E398"]={"gcksdk_SAMSUNG.jar"},

				--Samsung
				["D508"]={"gcksdk_SMS_COMMON.jar"},
				["D608"]={"gcksdk_NET_MOREGAME_BIG.jar"},
				["E258"]={"gcksdk_SAMSUNG.jar"},

				--LG
				["KG90n"]={}
			}

theSP["JYWRAPPER_LIBS"]["RELEASE"] =
			{
				--Nokia
				["N73"]={"gcksdk_NET_INT.jar"},
				["N6101"]={"gcksdk_SMS_COM.jar"},
				["N7610"]={"gcksdk_SMS_COM.jar"},
				["N7370"]={"gcksdk_NET_INT.jar"},
				["N97"]={"gcksdk_SMS_COM.jar"},
				["E62"]={"gcksdk_NET_INT.jar"},

				--Sonyericsson
				["K790"]={"gcksdk_NET_INT.jar"},
				["S700"]={"gcksdk_NET_INT.jar"},
				["K700C"]={"gcksdk_SMS_INT.jar"},
				["K506"]={"gcksdk_SMS_INT.jar"},

				--Motorola
				["E6"]={"gcksdk_SMS_COM.jar"},
				["V8"]={"gcksdk_NET_INT.jar"},
				["E2"]={"gcksdk_NET_INT.jar"},
				["E680"]={"gcksdk_SMS_COM.jar"},
				["L7"]={"gcksdk_SMS_COM.jar"},
				["L6"]={"gcksdk_SMS_INT.jar"},
				["K1"]={"gcksdk_SMS_COM.jar"},
				["E398"]={"gcksdk_SMS_COM.jar"},

				--Samsung
				["D508"]={"gcksdk_SAMSUNG.jar"},
				["D608"]={"gcksdk_NET_INT.jar"},
				["E258"]={"gcksdk_SMS_INT.jar"},

				--LG
				["KG90n"]={"gcksdk_NET_INT.jar"}
			}
-- д�뵽DEVICECONFIG���ü�����
SPCONFIG[theSP.CHANNEL_NAME] = theSP
-- ��ʾ��Ϣ
print(string.format("\t%-20s%s",theSP.CHANNEL_NAME,theSP.CHANNEL_CHSNAME))
