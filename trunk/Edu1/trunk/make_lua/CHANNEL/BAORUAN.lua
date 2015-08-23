-- 首先复制表格
theSP = table.copy(SP_COMMON)
-- 修改设备相关的值
theSP["CHANNEL_CHSNAME"] 				="宝软"
theSP["CHANNEL_NAME"] 					="BAORUAN"

theSP["JYWRAPPER_LIBS"]						= {
							DEFAULT = {"SmsTest.jar"},
							}
theSP["PREPROCESS"]["CHANNEL_NAME"]= theSP["CHANNEL_NAME"]

-- 写入到DEVICECONFIG配置集里面
SPCONFIG[theSP.CHANNEL_NAME] = theSP
-- 显示信息
print(string.format("\t%-20s%s",theSP.CHANNEL_NAME,theSP.CHANNEL_CHSNAME))

