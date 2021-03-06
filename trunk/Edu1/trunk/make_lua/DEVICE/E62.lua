-- 首先复制表格
dev = table.copy(DEV_COMMON)
-- 修改设备相关的值
dev["BRAND"] 				="Nokia"
dev["MODEL"] 				="E62"
dev["RAMSIZE"] 			="1000"
dev["SCREENWIDTH"] 			="176"
dev["SCREENHEIGHT"] 		="208"
dev["ICON_SIZE"] 			="64X49"
dev["NOKIA_DEVICE"]			=TRUE
dev["RESPACK"]				="176"
dev["RESPACK_WRAPPER"]			="176"
dev["SRCPACK"]				="176"
-- 键值 未确认
dev["KEY_LSK"]				= -6
dev["KEY_RSK"]				= -7
dev["KEY_FIRE"]				= -5
dev["KEY_UP"]				= -1
dev["KEY_DOWN"]				= -2
dev["KEY_LEFT"]				= -3
dev["KEY_RIGHT"]			= -4
dev["KEY_VOLUMEUP"]			= 0
dev["KEY_VOLUMEDOWN"]			= 0
-- 写入到DEVICECONFIG配置集里面
DEVICECONFIG[dev.MODEL] = dev
-- 显示信息
print(string.format("\t%-20s%s",dev.BRAND,dev.MODEL))
