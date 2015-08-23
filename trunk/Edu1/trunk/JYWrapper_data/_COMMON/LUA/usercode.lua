--==================================================================--
--本函数被编译系统自动调用，提供修改JAD表的能力
CHANNEL.GAME_NAME="双晨教育"
CHANNEL.PACK_GAME_NAME="Edu1"
CHANNEL.PACK_NAME = "Edu1"
--弱联网开关
CHANNEL.PREPROCESS.WEAKNETWORK = "FALSE"
--游戏本身名称
CHANNEL.PREPROCESS.GAME_NAME = "EDU1"
--教育开关
CHANNEL.PREPROCESS.EDUCATION ="TRUE"
if MAKE.DEVICE_MODEL == "N97" then
	CHANNEL.GAME_NAME="双晨教育"
end

SPECIAL_DEVICE_OPTIONS = {
--这里可以增加这个渠道里特有的一些配置。格式：机型={配置名=配置内容, 配置名=配置内容...}，例如当乐的DEVICE_ID
-- 机型如果=ALL，表示所有机型都通用的一些配置信息。例如当乐的CP_ID
	ALL={},

	--Nokia
	["N73"]={},
	["6101"]={},
	["7610"]={},
	["7370"]={},
	["N97"]={},
	["E62"]={},

	--Sonyericsson
	["K790"]={},
	["S700"]={},
	["K700"]={},
	["K506"]={},

	--Motorola
	["E6"]={},
	["V8"]={},
	["E2"]={},
	["E680"]={},
	["L7"]={},
	["L6"]={},
	["K1"]={},
	["E398"]={},

	--Samsung
	["D508"]={},
	["D608"]={},
	["E258"]={},

	--LG
	["KG90n"]={}
}



function USERCODE.updatejad (jadtable)
	table.insert(jadtable,{ {["MIDlet-Description"] = "ARPG," .. CHANNEL.GAME_NAME.. "," .. MAKE.DEVICE_BRAND .. MAKE.DEVICE_MODEL},false,true})
	table.insert(jadtable,{{["Media-Price"] = "单次下载10元"},false,true})
	return jadtable
end
