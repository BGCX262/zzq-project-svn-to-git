-- ���ȸ��Ʊ��
theSP = table.copy(SP_COMMON)
-- �޸��豸��ص�ֵ
theSP["CHANNEL_CHSNAME"] 				="�Ϻ�����"
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
-- д�뵽DEVICECONFIG���ü�����
SPCONFIG[theSP.CHANNEL_NAME] = theSP
-- ��ʾ��Ϣ
print(string.format("\t%-20s%s",theSP.CHANNEL_NAME,theSP.CHANNEL_CHSNAME))
