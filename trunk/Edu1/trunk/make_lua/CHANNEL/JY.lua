
tempdefine = {
		Name = "�ɶ�����",
		ID = "JY",
		Vender = "JOYU",
		MIDLET_CLASS = "GameMIDlet",
		GAME_NAME = "����WRAPPER_DATA��usercode.lua���޸�",
		PACK_GAME_NAME = "����WRAPPER_DATA��usercode.lua���޸�",
		PACK_NAME = "����WRAPPER_DATA��usercode.lua���޸�",
		ICON_EXT = "",
		JYWRAPPER_LIBS = {
			DEFAULT = {},
			--SAMSUNG = {"UCPayment_10final_SAMSUNG.jar"},
		},
		ARCHIVE_FORMAT = {"Game","",""},
		VERSION_NUMBER="1.0.0",
		TOJAD = {
			"",
			"",
		},
		PREPROCESS =
			{
				SP_JY = "1",
			},
		PROGUARD_OPT =
			{
			},
		OBFUSCAT_LIBS = "1",
		LIB_DATAS = {
			}
		}
SPCONFIG[tempdefine.ID] = tempdefine
print(string.format("\t%-20s%s",tempdefine.ID,tempdefine.Name))
