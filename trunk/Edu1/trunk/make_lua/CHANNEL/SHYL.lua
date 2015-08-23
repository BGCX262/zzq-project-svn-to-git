-- 首先复制表格
theSP = table.copy(SP_COMMON)
-- 修改设备相关的值
theSP["CHANNEL_CHSNAME"] 				="上海有乐"
theSP["CHANNEL_NAME"] 					="SHYL"


theSP["PREPROCESS"]["CHANNEL_NAME"]= theSP["CHANNEL_NAME"]
theSP["PREPROCESS"]["REVIEW"]= TRUE
theSP["PREPROCESS"]["FEE"]= FALSE
theSP["PREPROCESS"]["SHOWGAMENAME"]= TRUE
theSP["PREPROCESS"]["LOGO"]= FALSE
theSP["PREPROCESS"]["TIGER"]= FALSE
theSP["PREPROCESS"]["ABOUTUS"]= TRUE
theSP["PREPROCESS"]["MOREGAME"]= FALSE
theSP["PREPROCESS"]["SOUNDONOFF"]= FALSE

--theSP["PREPROCESS"]["URL_MOREGAME"]= "http://gamepie.g188.net/gamecms/go/jpgd"


theSP["ARCHIVE_FORMAT"] = {"Game"}

theSP["JYWRAPPER_LIBS"]["DEBUG"] =
			{
				--Nokia
				["DEVICEMODEL"]={"libname.jar"},

			}

theSP["JYWRAPPER_LIBS"]["RELEASE"] =
			{
				--Nokia
				["DEVICEMODEL"]={"libname.jar"},

			}
-- 写入到DEVICECONFIG配置集里面
SPCONFIG[theSP.CHANNEL_NAME] = theSP
-- 显示信息
print(string.format("\t%-20s%s",theSP.CHANNEL_NAME,theSP.CHANNEL_CHSNAME))
