-- ���ȸ��Ʊ��
theSP = table.copy(SP_COMMON)
-- �޸��豸��ص�ֵ
theSP["CHANNEL_CHSNAME"] 				="����"
theSP["CHANNEL_NAME"] 					="HN"
theSP["ARCHIVE_FORMAT"] 				={"Game","-","Device","-","Version"}			-- jar���ļ���ʽ --

theSP["JYWRAPPER_LIBS"]						= {
							DEFAULT = {"SendMSG.jar"},
							}
theSP["PREPROCESS"]["CHANNEL_NAME"]= theSP["CHANNEL_NAME"]


-- д�뵽DEVICECONFIG���ü�����
SPCONFIG[theSP.CHANNEL_NAME] = theSP
-- ��ʾ��Ϣ
print(string.format("\t%-20s%s",theSP.CHANNEL_NAME,theSP.CHANNEL_CHSNAME))