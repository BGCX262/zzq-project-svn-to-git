-- ���ȸ��Ʊ��
theSP = table.copy(SP_COMMON)
-- �޸��豸��ص�ֵ
theSP["CHANNEL_CHSNAME"] 				="���Ӷ���"
theSP["CHANNEL_NAME"] 					="UC"
theSP["JYWRAPPER_LIBS"]						= {
							DEFAULT = {"UCPayment_10final.jar"},
							SAMSUNG = {"UCPayment_10final_SAMSUNG.jar"},
							}
theSP["ARCHIVE_FORMAT"]					= {"Game","_","Device"}
theSP["PREPROCESS"]["CHANNEL_NAME"]= theSP["CHANNEL_NAME"]


-- д�뵽DEVICECONFIG���ü�����
SPCONFIG[theSP.CHANNEL_NAME] = theSP
-- ��ʾ��Ϣ
print(string.format("\t%-20s%s",theSP.CHANNEL_NAME,theSP.CHANNEL_CHSNAME))
