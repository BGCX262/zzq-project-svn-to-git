-- ���ȸ��Ʊ��
dev = table.copy(DEV_COMMON)
-- �޸��豸��ص�ֵ
dev["BRAND"] 				="SonyEricsson"
dev["MODEL"] 				="K700C"
dev["RAMSIZE"] 			="0800"
dev["SCREENWIDTH"] 			="176"
dev["SCREENHEIGHT"] 		="220"
dev["ICON_SIZE"] 			="15X15"
dev["NOKIA_DEVICE"]			=FALSE
dev["RESPACK"]				="176"
dev["RESPACK_WRAPPER"]			="176"
dev["SRCPACK"]				="176"
-- ��ֵ
dev["KEY_LSK"]				= -6
dev["KEY_RSK"]				= -7
dev["KEY_FIRE"]				= -5
dev["KEY_UP"]				= -1
dev["KEY_DOWN"]				= -2
dev["KEY_LEFT"]				= -3
dev["KEY_RIGHT"]			= -4
dev["KEY_VOLUMEUP"]			= 0
dev["KEY_VOLUMEDOWN"]			= 0
-- д�뵽DEVICECONFIG���ü�����
DEVICECONFIG[dev.MODEL] = dev
-- ��ʾ��Ϣ
print(string.format("\t%-20s%s",dev.BRAND,dev.MODEL))
