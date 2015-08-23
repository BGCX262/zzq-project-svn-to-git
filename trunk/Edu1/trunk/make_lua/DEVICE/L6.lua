-- 首先复制表格
dev = table.copy(DEV_COMMON)
-- 修改设备相关的值
dev["BRAND"] 				="Motorola"
dev["MODEL"] 				="L6"
dev["RAMSIZE"] 			="0300"
dev["SCREENWIDTH"] 			="128"
dev["SCREENHEIGHT"] 			="149"
dev["ICON_SIZE"]			="15X15"
dev["NOKIA_DEVICE"]			=FALSE
dev["RESPACK"]				="128"
dev["RESPACK_WRAPPER"]			="128"
dev["SRCPACK"]				="128"
-- 键值
dev["KEY_LSK"]				= -21
dev["KEY_RSK"]				= -22
dev["KEY_FIRE"]				= -20
dev["KEY_UP"]				= -1
dev["KEY_DOWN"]				= -6
dev["KEY_LEFT"]				= -2
dev["KEY_RIGHT"]			= -5
dev["KEY_VOLUMEUP"]			= 0
dev["KEY_VOLUMEDOWN"]			= 0
-- 写入到DEVICECONFIG配置集里面
DEVICECONFIG[dev.MODEL] = dev
-- 显示信息
print(string.format("\t%-20s%s",dev.BRAND,dev.MODEL))
