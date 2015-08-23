
--修改以下5项 价格，游戏名，包名，游戏类型，vendor
PRICE=8
CHANNEL.GAME_NAME="天才养成计划E"
CHANNEL.PACK_GAME_NAME="Edu1"
GAME_TYPE = "教育类"
function USERCODE.Exec()
	if CHANNEL["PREPROCESS"]["REVIEW"] then
		CHANNEL.Vender = "UNKOWN"
	else
		CHANNEL.Vender = "erfolg"
	end
end

CHANNEL.PACK_GAME_NAME = CHANNEL.PACK_GAME_NAME .. "_" .. MAKE.DEVICE_MODEL
CHANNEL.PACK_NAME = CHANNEL.PACK_GAME_NAME
function USERCODE.updatejad (jadtable)
	table.insert(jadtable,{ {["MIDlet-Description"] = GAME_TYPE .. "," .. CHANNEL.GAME_NAME.. "," .. MAKE.DEVICE_BRAND .. MAKE.DEVICE_MODEL},false,true})
	if not CHANNEL["PREPROCESS"]["REVIEW"] then
		table.insert(jadtable,{{["Media-Price"] = "单次下载" .. PRICE .. "元"},false,true})
	end
	return jadtable
end
