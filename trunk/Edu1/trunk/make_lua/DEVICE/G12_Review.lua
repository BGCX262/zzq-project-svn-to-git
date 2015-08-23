-- 首先复制表格
dev = table.copy(DEV_COMMON)
-- 修改设备相关的值
dev["BRAND"] 				="HTC"
dev["MODEL"] 				="G12_Review"
dev["RAMSIZE"] 			="1000"
dev["SCREENWIDTH"] 			="800"
dev["SCREENHEIGHT"]			="480"
dev["PLATFORM_VERSION"] = 7
dev["PLATFORM_VERSION_STR"] = "android-7"
dev["USE_BYTECODEOPTIMIZE"] = false
dev["USE_KZIP"] = false
dev["MIDP10"] = false
dev["MIDP20"] = true
dev["NOKIA_UI"] = false
dev["WMA11"] = true
dev["WMA20"] = false
dev["CLDC10"] = true
dev["CLDC11"] = false
dev["JSR184"] = false
dev["USE_BITMAP_FONT"] = true
dev["USE_MIDP10"] = false
dev["USE_MIDP20"] = true
dev["USE_CLDC11"] = false
dev["USE_CLDC10"] = true
dev["USE_ORIENTATION_PORTRAIT"] = true
dev["SCREEN_ORIENTATION"] = "landscape"
dev["MASTER_DATA"] = "Android"
dev["ICON_SIZE"]			="64X64"
dev["NOKIA_DEVICE"]			=TRUE
dev["RESPACK"]				="240"
dev["RESPACK_WRAPPER"]			="240"
dev["SRCPACK"]				="240"
dev["PLATFORM"]				= "Android"
dev["ENABLE_TOUCH"]                     =true
dev["FPSLIMIT"]				= 12
dev["USE_CALLSERIALLY"]			= FALSE
dev["USE_SERVICEREPAINT"]		= FALSE

-- 键值
dev["KEY_LSK"]				= -6
dev["KEY_RSK"]				= -7
dev["KEY_FIRE"]				= -5
dev["KEY_UP"]				= -1
dev["KEY_DOWN"]				= -2
dev["KEY_LEFT"]				= -3
dev["KEY_RIGHT"]			= -4
dev["KEY_VOLUMEUP"]			= 0
dev["KEY_VOLUMEDOWN"]			= 0

dev["GC_BEFORE_NO_HEAP"]		=TRUE
dev["HAS_VOLUMECONTROL"]		=TRUE
-- 写入到DEVICECONFIG配置集里面
DEVICECONFIG[dev.MODEL] = dev
-- 显示信息
print(string.format("\t%-20s%s",dev.BRAND,dev.MODEL))
