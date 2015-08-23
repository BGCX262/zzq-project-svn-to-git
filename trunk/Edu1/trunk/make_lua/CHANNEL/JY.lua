
tempdefine = {
		Name = "成都嘉游",
		ID = "JY",
		Vender = "JOYU",
		MIDLET_CLASS = "GameMIDlet",
		GAME_NAME = "请在WRAPPER_DATA下usercode.lua里修改",
		PACK_GAME_NAME = "请在WRAPPER_DATA下usercode.lua里修改",
		PACK_NAME = "请在WRAPPER_DATA下usercode.lua里修改",
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
