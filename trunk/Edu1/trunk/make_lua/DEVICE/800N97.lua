-- ���ȸ��Ʊ���
dev = table.copy(DEV_COMMON)
-- �޸��豸��ص�ֵ
dev["BRAND"] 				="Nokia"
dev["MODEL"] 				="800N97"
dev["RAMSIZE"] 				="1000"
dev["SCREENWIDTH"] 			="800"
dev["SCREENHEIGHT"]			="480"
dev["ICON_SIZE"]			="64X64"
dev["RESPACK"]				="240"
dev["RESPACK_WRAPPER"]			="240"
dev["SRCPACK"]				="240"
--dev["JAVA5"]				=TRUE	
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
dev["ORIENTATION_PORTRAIT"]	=TRUE
-- д�뵽DEVICECONFIG���ü�����
DEVICECONFIG[dev.MODEL] = dev
-- ��ʾ��Ϣ
print(string.format("\t%-20s%s",dev.BRAND,dev.MODEL))