-- ���ȸ��Ʊ��
theSP = table.copy(SP_COMMON)
-- �޸��豸��ص�ֵ
theSP["CHANNEL_CHSNAME"] 				="Ѳ��"
theSP["CHANNEL_NAME"] 					="XY"
theSP["JYWRAPPER_LIBS"]						= {
							DEFAULT = {"XunYangPayV7.4.jar"},
							}
theSP["ARCHIVE_FORMAT"]					= {"Game","_","Device"}
theSP["PREPROCESS"]["CHANNEL_NAME"]= theSP["CHANNEL_NAME"]


theSP["PROGUARD_OPT"]					={
							"-keeppackagenames",
							"-keep public class xunyang.PayFactory",
							"-keep public class xunyangpay.inter.Pay",
							"-keep public class a",
							"-keep public class b",
							"-keep public class c",
							"-keep public class d",
							"-keep public class e",
							"-keep public class f",
							"-keep public class g",
							"-keep public class h",
							"-keep public class i",
							"-keep public class j",
							"-keep public class k",
							"-keep public class l",
							"-keep public class m",
							"-keep public class n",
							"-keep public class o",
							"-keep public class p",
							"-keep public class q",
							"-keep public class r",
							"-keep public class s",
							"-keep public class t",
							"-keep public class u",
							"-keep public class v",
							"-keep public class w",
							"-keep public class x",
							}
theSP["LIB_DATAS"]					={
							"XunYangPayV7.4_data.jar",
							}

-- д�뵽DEVICECONFIG���ü�����
SPCONFIG[theSP.CHANNEL_NAME] = theSP
-- ��ʾ��Ϣ
print(string.format("\t%-20s%s",theSP.CHANNEL_NAME,theSP.CHANNEL_CHSNAME))
