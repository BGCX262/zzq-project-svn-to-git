-- ���ȸ��Ʊ��
dev = table.copy(DEV_COMMON)
-- �޸��豸��ص�ֵ
dev["BRAND"] 				="Motorola"
dev["MODEL"] 				="K1"
dev["RAMSIZE"] 			="0500"
dev["SCREENWIDTH"] 			="176"
dev["SCREENHEIGHT"] 		="205"
dev["ICON_SIZE"] 			="15X15"
dev["NOKIA_DEVICE"]			=FALSE
dev["RESPACK"]				="176"
dev["RESPACK_WRAPPER"]			="176"
dev["SRCPACK"]				="176"
-- ��ֵ
dev["KEY_LSK"]				= -21
dev["KEY_RSK"]				= -22
dev["KEY_FIRE"]				= -20
dev["KEY_UP"]				= -1
dev["KEY_DOWN"]				= -6
dev["KEY_LEFT"]				= -2
dev["KEY_RIGHT"]			= -5
dev["KEY_VOLUMEUP"]			= 0
dev["KEY_VOLUMEDOWN"]			= 0
-- д�뵽DEVICECONFIG���ü�����
DEVICECONFIG[dev.MODEL] = dev
-- ��ʾ��Ϣ
print(string.format("\t%-20s%s",dev.BRAND,dev.MODEL))
