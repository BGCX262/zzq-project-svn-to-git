-- ���ȸ��Ʊ��
dev = table.copy(DEV_COMMON)
-- �޸��豸��ص�ֵ
dev["BRAND"] 				="Nokia"
dev["MODEL"] 				="N6101"
dev["RAMSIZE"] 				="0200"
dev["SCREENWIDTH"] 			="128"
dev["SCREENHEIGHT"] 			="160"
dev["ICON_SIZE"]			="32X32"
dev["NOKIA_DEVICE"]			=TRUE
dev["ENABLE_TOUCH"]				=FALSE
dev["RESPACK"]				="128"
dev["RESPACK_WRAPPER"]			="128"
dev["SRCPACK"]				="128"
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
