-- 首先复制表格
theSP = table.copy(SP_COMMON)
-- 修改设备相关的值
theSP["CHANNEL_CHSNAME"] 				="优视动景"
theSP["CHANNEL_NAME"] 					="UC"
theSP["JYWRAPPER_LIBS"]						= {
							DEFAULT = {"UCPayment_10final.jar"},
							SAMSUNG = {"UCPayment_10final_SAMSUNG.jar"},
							}
theSP["ARCHIVE_FORMAT"]					= {"Game","_","Device"}
theSP["PREPROCESS"]["CHANNEL_NAME"]= theSP["CHANNEL_NAME"]


-- 写入到DEVICECONFIG配置集里面
SPCONFIG[theSP.CHANNEL_NAME] = theSP
-- 显示信息
print(string.format("\t%-20s%s",theSP.CHANNEL_NAME,theSP.CHANNEL_CHSNAME))
