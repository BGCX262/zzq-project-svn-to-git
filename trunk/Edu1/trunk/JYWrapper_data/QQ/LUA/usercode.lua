--QQ的手机名转换
PHONE_CONV = {
	["N97"]     = "N5800",
	["7610"]    = "N7610",
    ["6101"]    = "N6101",
    ["7370"]    = "N7370",
    ["K700"]    = "K700C",
}
function USERCODE.MapModelName(modelName)
	newName = PHONE_CONV[modelName]
	if newName == nil then
		newName = modelName
	end
	return newName
end

CHANNEL.PACK_GAME_NAME="成都嘉游+"..CHANNEL.GAME_NAME.."+T-"..USERCODE.MapModelName(MAKE.DEVICE_MODEL)

function USERCODE.updatejad (jadtable)
	--table.insert(jadtable,{ {["Media-Price"] = "单次下载8元"},false,true})
	table.insert(jadtable,{ {["MIDlet-Description"] = "ARPG," .. MAKE.DEVICE_BRAND .. USERCODE.MapModelName(MAKE.DEVICE_MODEL) .. ",成都双晨-开天辟地."},false,true})
	table.insert(jadtable,{ {["MIDlet-Install-Notify"] = "http://g3.3g.qq.com/g/s?aid=g_install&cpid=174&gameid=001&agent=".. USERCODE.MapModelName(MAKE.DEVICE_MODEL) .."&feetype=S"},false,true})	
	table.insert(jadtable,{ {["SmsCode1"] = "1782A5B9ECE9820Ec81e728d9d4c2f636f067f89cc14862c"},false,true})
	table.insert(jadtable,{ {["SmsCode2"] = "62A9BB7A09BB9FC5c4ca4238a0b923820dcc509a6f75849b"},false,true})
	table.insert(jadtable,{ {["SmsDest1"] = "ADB0FFA03412F5D869AE2EA173E23FBEd8b18e27d15de593890d5441cecfb259"},false,true})
	table.insert(jadtable,{ {["SmsDest2"] = "ADB0FFA03412F5D869AE2EA173E23FBEd8b18e27d15de593890d5441cecfb259"},false,true})
	table.insert(jadtable,{ {["FreeCode"] = "5E477C897F925939ef0d3930a7b6c95bd2b32ed45989c61f"},false,true})
	table.insert(jadtable,{ {["FreeDest"] = "06818C025AF3AF5F5E477C897F9259397a1fa43673750e5a9f1a6b14c5f1a27e"},false,true})	
	table.insert(jadtable,{ {["Term"] = USERCODE.MapModelName(MAKE.DEVICE_MODEL)},false,true})
	table.insert(jadtable,{ {["ChannelFirst"] = "FR"},false,true})
	table.insert(jadtable,{ {["ChannelSecond"] = "SEC"},false,true})
	table.insert(jadtable,{ {["InterChannel"] = "ICID"},false,true})
	table.insert(jadtable,{ {["QQ"] = "0"},false,true})
	table.insert(jadtable,{ {["GameCenterName"] = "QQ游戏中心"},false,true})
	table.insert(jadtable,{ {["GameCenterUrl"] = "http://g.3g.qq.com/g/s?aid=g_cp_adver&series=".. USERCODE.MapModelName(MAKE.DEVICE_MODEL) ..""},false,true})	
	table.insert(jadtable,{ {["FeeMode"] = "1"},false,true}) --0为短代模式，1为DO模式，动态修改
	table.insert(jadtable,{ {["DoCode"] = "22E61E6ABBA76BA7B87D12B62CB83B85B33CE2062EAA5C68C1CC479F57D205FB0F45B493B27403AC0D321A2BC51B18513D9981BE024E5B682F535995AE4818C4E3801838788E5B6B378B1CBA1DBB9A91832537C30812F124663BC082C3E71A1405ad57b5330c71e70087af1fe74ed0a7"},false,true})
	table.insert(jadtable,{ {["DoDest"] = "4C8275A93ECDA86EE731C730E522C4618F6FB0566094046FEC048DC82F788636B80D3DBE354DBDA3C3BD1E07BFE604FBCDB0031092F9BE8D5F7F4FD7CBE2CEB5db8fa0f380066d23fe873cca52ad5645"},false,true})
	
	return jadtable
end