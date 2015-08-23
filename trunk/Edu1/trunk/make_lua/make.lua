--[[
TODO: 每个阶段的容错性检查。每个阶段的关键错误报告
TODO: Tag任务
TODO: TEXT_ENCODING 可能需要放到设备配置里面，而不是ENV里面
TODO:
]]--
require "lfs"
require "common"
require "help"
ShowInfo("/**************************************************************\\")
ShowInfo("                                  	                           ")
ShowInfo("                        统一编译系统	                           ")
ShowInfo("       		开发: 桂军 (有啥不懂的最终找他问)                  ")
ShowInfo("              成都双晨信息技术有限公司 版权所有                  ")
ShowInfo("                                  	                           ")
ShowInfo("\\**************************************************************/")
ShowInfo("...初始化...")

require "env"
require "pl.path"
--[[
if pl.path.exists("..\\make_lua_data\\project.lua") then
	package.path = package.path .. ";..\\make_lua_data\\project.lua"
	require "project"
end
]]--

--==================================================================--
--任务:tag
function tag()
	ShowInfo("Tag 开始")
--    %TOOLS_PATH%\tag_tool\tag.exe -w . -n !ARCHIVE_NAME! -t tags/%BUILD_CONF% -q
--	未完成 os.execute(GetVar("TAG_PATH") .. "tag.exe" .. " -w . -n !ARCHIVE_NAME! -t tags/%BUILD_CONF% -q)
	ShowInfo("Tag 完成")
end
function setreview_macro()
	CHANNEL["PREPROCESS"]["REVIEW"]= TRUE
	CHANNEL["PREPROCESS"]["FEE"]= FALSE
	CHANNEL["PREPROCESS"]["SHOWGAMENAME"]= TRUE
	CHANNEL["PREPROCESS"]["LOGO"]= FALSE
	CHANNEL["PREPROCESS"]["TIGER"]= FALSE
	CHANNEL["PREPROCESS"]["ABOUTUS"]= FALSE
	CHANNEL["PREPROCESS"]["MOREGAME"]= FALSE
	CHANNEL["PREPROCESS"]["SOUNDONOFF"]= FALSE
--[[
	year = os.date("%Y")
	month = os.date("%m")
	sMonth = month - 0
	gName = string.format("%s年%d月评审游戏",year,sMonth)
	CHANNEL.GAME_NAME=iconv(gName,"GBK","UTF-8")

	pName = year .. month .. "05"
	CHANNEL.PACK_GAME_NAME=pName
	CHANNEL.PACK_NAME=pName
]]--
end

--==================================================================--
--任务:optimize on / off
function optimize_on()
	DEVICE.OPTIMIZE = true
	table.dump(MAKE)
end
--==================================================================--
--任务:optimize on / off
function optimize_off()
	DEVICE.OPTIMIZE = false
	table.dump(MAKE)
end
--==================================================================--
--任务:cpp_off
function cpp_off()
	MAKE.CPP = false
	table.dump(MAKE)
end
--==================================================================--
--任务:cpp_on
function cpp_on()
	MAKE.CPP = true
	table.dump(MAKE)
end
--==================================================================--
--任务:encodedata_off
function encodedata_off()
	MAKE.ENCODEDATA = false
	table.dump(MAKE)
end
--==================================================================--
--任务:encodedata_on
function encodedata_on()
	MAKE.ENCODEDATA = true
	table.dump(MAKE)
end

--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--预编译任务集 pb = prebuild
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--

--==================================================================--
--任务:pb_clean()
function pb_clean()
	ShowInfo(string.format("...pb_clean..."))
	lfs.rmdir(ENV.BLD)
end

function pb_packdata()
	function addtolist(filename,inilist)
		table.insert(inilist,0,filename)
	end
	ShowInfo("...pb_packdata...")
	ShowInfo("...合并ini文件...")
	inilist = {}
	lfs.chdir(ENV.BLD_DATA_RAW)
	lfs.delfile(ENV.BLD_DATA_RAW.."\\data_beforePP.ini")
	lfs.delfile(ENV.BLD_DATA_RAW.."\\data.ini")
	local battext = string.format(
[[
	@echo off
	set DATA_PATH_RAW=%s
	dir /B /ON %%DATA_PATH_RAW%%\\data*.ini > #dir.txt
	echo //data.ini file > %%DATA_PATH_RAW%%\\data.ini
	for /F %%%%i in (#dir.txt) do (
		copy %%DATA_PATH_RAW%%\\data.ini + %%%%i /a > NUL
	)
	del #dir.txt
	copy %%DATA_PATH_RAW%%\\data.ini %%DATA_PATH_RAW%%\\data_beforePP.ini
	@echo on
]]
		,ENV.BLD_DATA_RAW)

	local filename = ENV.BLD_DATA_RAW.."\\_mergeini.bat"
	WriteString(filename,battext,false)
	os.execute(string.format("call %s",filename))

	lfs.delfile(filename)
	ShowInfo("...预处理ini文件，剔除注释...")
	local defines = TableToString(DEVICE) .. TableToString(CHANNEL.PREPROCESS)
	os.execute(string.format("call \"%s\\cpp\\cpp.exe\" -C -P %s \"%s\"\\data_beforePP.ini \"%s\"\\data.ini",	ENV.TOOLS_PATH,defines,ENV.BLD_DATA_RAW,ENV.BLD_DATA_RAW))
	lfs.copyfile(string.format("%s\\empty.xxx",ENV.BLD_DATA_RAW),ENV.BLD_DATA_TOPACK)
	os.execute(string.format("call \"%s\\bin\\java.exe\" -cp \"%s\\packFile\" packFile -i %s\\ -h %s\\ -d %s\\ -c %s\\compress\\ -p F %s\\data.ini",
		ENV.JDK_PATH,ENV.TOOLS_PATH,ENV.BLD_DATA_TOPACK,ENV.BLD_DATA_H,ENV.BLD_DATA_OUT,ENV.TOOLS_PATH,ENV.BLD_DATA_RAW))

end
--==================================================================--
--任务:pb_data()
function pb_data()

		--==========================================================--
		--任务:ObfuscateData()
		function ObfuscateData()
			if MAKE.ENCODEDATA	then
				ShowInfo("...ObfuscateData...")
				pushd()
				-- 这个代码可以使用lua来编写
				lfs.chdir(ENV.BLD_DATA_RAW)
				lfs.xcopy(ENV.BLD_DATA_TOPACK,ENV.BLD_DATA_OUT)
				RemoveFiles_bat = [[
					@echo off
					dir /B/S %s\*.*|FINDSTR "Cover" > #Res
					dir /B/S %s\*.*|FINDSTR "Roof" >> #Res
					dir /B/S %s\*.*|FINDSTR ".mask" >> #Res
					dir /B/S %s\*.*|FINDSTR ".log" >> #Res
					for /F %%%%i in (#Res) do (
						del /Q %%%%i
					)
					@echo on
				]]

				local batname = "RemoveFiles.bat"
				WriteTable({
				string.format(RemoveFiles_bat,ENV.BLD_DATA_OUT,ENV.BLD_DATA_OUT,ENV.BLD_DATA_OUT,ENV.BLD_DATA_OUT)
				},batname,false)
				os.execute(batname)


				os.execute(string.format("%s\\bin\\java.exe -cp %s\\ResourceMix confuseFile -r .bsprite=.jpg,.cinematics=.avi,.layers=.obj,.h=.A,.flags=.B,.bin=.C,level1_=B,Location=C,Stage=D,Ground=E,Physic=P,Shadow=G,Roof=R,Cover=C -i %s",
					ENV.JDK_PATH,ENV.TOOLS_PATH,ENV.BLD_DATA_OUT))
				popd()
			else
				lfs.xcopy(ENV.BLD_DATA_TOPACK,ENV.BLD_DATA_OUT)
			end
		end

		--==========================================================--
		--任务:makefont()
		function makefont()
		ShowInfo("...makefont...")
		pushd()
		lfs.chdir(ENV.BLD_DATA_RAW)
		os.execute(string.format("%s\\bin\\java.exe -jar %s\\BitmapFont\\BuildBitmapFont.jar -f font_big.def -border -t AllCharInThisGame.txt -o %s\\jy.png -sChars specialChars.txt -sCharsFont \"MS UI Gothic\"",
			ENV.JDK_PATH,ENV.TOOLS_PATH,ENV.BLD_DATA_OUT))
		lfs.copyfile(ENV.BLD_DATA_OUT.."\\jy.png",ENV.BLD_DATA_TOPACK)
		popd()
		end

	ShowInfo("...pb_data...")
	-- 这里保存一下当前的ENV.BLD_DATA_OUT
	if (DEVICE.PLATFORM =="Android") then
		ENV.BLD_DATA_OUT_TEMP = ENV.BLD_DATA_OUT
		ENV.BLD_DATA_OUT = ENV.BLD_DATA_OUT_AND
	end

	lfs.rmdir(ENV.BLD_DATA)
	lfs.mkdir(ENV.BLD_DATA)
	lfs.mkdir(ENV.BLD_DATA_RAW)
	lfs.mkdir(ENV.BLD_DATA_OUT)
	lfs.mkdir(ENV.BLD_DATA_TOPACK)
	lfs.mkdir(ENV.BLD_DATA_H)
	-- 复制资源
	lfs.xcopy(ENV.DATA_COMMON,ENV.BLD_DATA_RAW)		-- 公共资源
	lfs.xcopy(ENV.DATA,ENV.BLD_DATA_RAW)			-- 指定资源
	lfs.xcopy(ENV.DATA_DEVICE,ENV.BLD_DATA_RAW)		-- 特定设备资源 (尽量不要使用)
	pushd()
	-- 编译资源
	lfs.chdir(ENV.BLD_DATA_RAW)
	local batname = "_compile.bat"
	WriteTable({"@echo off"},batname,false)
	WriteTable(ENV,batname,true)
	WriteTable({string.format("call make.bat \"%s\" \"%s\" \"%s\"",ENV.BLD_DATA_TOPACK,ENV.BLD_DATA_OUT,ENV.BLD_DATA_H)},
		batname,
		true)
	WriteTable({"@echo on"},batname,true)
	os.execute(string.format("call %s",batname))
	-- 制作字体
	makefont()
	-- 混淆数据
	ObfuscateData()
	popd()
	-- 把资源生成的代码放到java需要的package里面
	Java.AddPackageName(ENV.BLD_DATA_H,MAKE.PACKAGE_NAME)


	ShowInfo(string.format("Data BIN 目录: %s",ENV.BLD_DATA_OUT))
	ShowInfo(string.format("Data SRC 目录: %s",ENV.BLD_DATA_H))
	-- 这里恢复当前的ENV.BLD_DATA_OUT
	if (DEVICE.PLATFORM =="Android") then
	ENV.BLD_DATA_OUT = ENV.BLD_DATA_OUT_TEMP
	end
end
--==================================================================--
--任务:pb_build
function pb_build()
		--==========================================================--
		--任务:pb_collect_src
		function pb_collect_src()
			ShowInfo("...pb_collect_src...")
			lfs.xcopy(ENV.JYLIB_SRC,ENV.BLD_SRC_RAW)
			lfs.xcopy(ENV.COMMON_SRC,ENV.BLD_SRC_RAW)
			lfs.xcopy(ENV.SPECIFIC_SRC,ENV.BLD_SRC_RAW)
			lfs.xcopy(ENV.BLD_DATA_H,ENV.BLD_SRC_RAW)
			lfs.xcopy(ENV.JYWRAPPER_COMMON_SRC,ENV.BLD_SRC_RAW)
			lfs.xcopy(ENV.JYWRAPPER_SPECIFIC_SRC,ENV.BLD_SRC_RAW)
			--将wrapper_data中的代码放入
			lfs.xcopy(ENV.PP_DATA_H,ENV.BLD_SRC_RAW)
			popd()
		end
		--==========================================================--
		--任务:pb_collect_preprocess
		function pb_createSymbols()

			ShowInfo("...pb_createSymbols...")
			--local symbolFileName = "DefaultColorPhone.symbols"
			local symbol = {}
			TableToAnntenaSymbolSet(DEVICE,symbol)
			TableToAnntenaSymbolSet(CHANNEL.PREPROCESS,symbol)
			local sp_device_define = CHANNEL.PREPROCESS.DEVICE[MAKE.DEVICE_MODEL]
			if sp_device_define then
				TableToAnntenaSymbolSet(sp_device_define,symbol)
			end

			WriteTable(symbol,string.format(ENV.BLD_SRC_RAW .. "\\DefaultColorPhone.symbols"),false)

		end

		function pb_collect_preprocess()
			ShowInfo("...pb_collect_preprocess...")
			srccopy({ENV.ANT_BUILD_XML},{".properties",".XML"},ENV.BLD_SRC_RAW)
			ShowInfo("...BUILD Symbols...")
			pb_createSymbols()
			--srccopy({ENV.PREPROCESS_DEFINE},{".symbols",".h"},ENV.BLD_SRC_RAW)
		end
		--==========================================================--
		--任务:pb_preprocess
		function pb_preprocess()
			ShowInfo("...pb_preprocess...")
			-- 目前支持 CPP 或 Antenna 进行预处理，为了和Eclipse等完整结合，请务必使用 Antenna进行预处理，也就是MTJ
			if MAKE.CPP then
				ShowInfo("...using CPP...")
				pushd()
				lfs.mkdir(ENV.BLD_SRC_PREPROCESS_FOR_CPP_APP)
				lfs.mkdir(ENV.BLD_SRC_PREPROCESS_FOR_CPP_LIB)
				lfs.xcopy(ENV.BLD_SRC_RAW_FOR_CPP_APP,ENV.BLD_SRC_PREPROCESS_FOR_CPP_APP)
				lfs.xcopy(ENV.BLD_SRC_RAW_FOR_CPP_LIB,ENV.BLD_SRC_PREPROCESS_FOR_CPP_LIB)
				lfs.copyfile(ENV.BLD_SRC_RAW,ENV.BLD_SRC_PREPROCESS_TMP)
				local defines = TableToString(DEVICE.PREPROCESS) .. TableToString(CHANNEL.PREPROCESS)
				Java.Cpp(ENV.BLD_SRC_RAW_FOR_CPP_APP,{"*.java"},ENV.BLD_SRC_PREPROCESS_FOR_CPP_APP,defines)
				Java.Cpp(ENV.BLD_SRC_RAW_FOR_CPP_LIB,{"*.java"},ENV.BLD_SRC_PREPROCESS_FOR_CPP_LIB,defines)

				Java.AddPackageName(ENV.BLD_SRC_PREPROCESS_TMP,MAKE.PACKAGE_NAME)
				lfs.copyfile(ENV.BLD_SRC_PREPROCESS_TMP,ENV.BLD_SRC_PREPROCESS_FOR_CPP_APP)

				lfs.chdir(ENV.BLD_SRC_PREPROCESS_TMP)
				os.execute(string.format("call \"%s\\ant\" -f \"mtj-build.xml\"" , ENV.ANT))
				popd()
			else
				ShowInfo("...using Antenna(MTJ)...")
				--ForAllFile(ENV.BLD_SRC_RAW,{},lfs.copyfile,ENV.BLD_SRC_PREPROCESS_TMP)
				lfs.xcopy(ENV.BLD_SRC_RAW,ENV.BLD_SRC_PREPROCESS_TMP)
				pushd()
				lfs.chdir(ENV.BLD_SRC_PREPROCESS_TMP)
				os.execute(string.format("call \"%s\\ant\" -f \"mtj-build.xml\"" , ENV.ANT))
				popd()
			end
			lfs.rmdir(ENV.BLD_SRC_PREPROCESS_TMP)
		end
		--==========================================================--
		--任务:pb_compile
		function pb_compile()
			ShowInfo("...pb_compile...")
			Java.compile(ENV.BLD_SRC_PREPROCESS,ENV.BLD_SRC_COMPILED)
		end
		--==========================================================--
		--任务:pb_buildpack
		function pb_buildpack()
			ShowInfo("...pb_buildpack...\n")
			pushd()
			lfs.chdir(ENV.BLD_PACK)
			local fullname = string.format("%s\\%s.jar",ENV.BLD_PACK,MAKE.DEVICE_MODEL)

			lfs.delfile(fullname)

			Java.PackFile(fullname,ENV.BLD_SRC_COMPILED,false,false,nil)
			Java.PackFile(fullname,ENV.BLD_DATA_OUT,false,true,nil)
			ShowInfo(string.format("\nPlease find internalPack here: %s\n",fullname))
			popd()
		end
	pushd()
	ShowInfo("...pb_build...")
	lfs.rmdir(ENV.BLD_SRC)
	lfs.rmdir(ENV.BLD_PACK)
	lfs.mkdir(ENV.BLD_SRC)
	lfs.mkdir(ENV.BLD_SRC_RAW)
	lfs.mkdir(ENV.BLD_SRC_PREPROCESS)
	lfs.mkdir(ENV.BLD_SRC_PREPROCESS_TMP)
	lfs.mkdir(ENV.BLD_SRC_COMPILED)
	lfs.mkdir(ENV.BLD_PACK)

	--if (DEVICE.PLATFORM ~= "Android") then
		pb_collect_src()
		pb_collect_preprocess()
		pb_preprocess()
		pb_compile()
		pb_buildpack()
	--end
	popd()
end


--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--后处理译任务集 pp = postprocess
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--

--==================================================================--
--任务:pp_clean()
function pp_clean()
	ShowInfo("...pp_clean...")
	lfs.rmdir(ENV.PP)
	lfs.rmdir(ENV.PP_PACK)
	lfs.mkdir(ENV.PP)
	lfs.mkdir(ENV.PP_DATA_RAW)
	lfs.mkdir(ENV.PP_DATA_OUT)
	lfs.mkdir(ENV.PP_DATA_H)
	lfs.mkdir(ENV.PP_SRC_RAW)
	lfs.mkdir(ENV.PP_SRC_COMPILED)
	lfs.mkdir(ENV.PP_SRC_MERGED)
	lfs.mkdir(ENV.PP_SRC_OPTIMIZED)
	lfs.mkdir(ENV.PP_SRC_OBFUSCATED)
	lfs.mkdir(ENV.PP_SRC_PREVERIFIED)
	lfs.mkdir(ENV.PP_MANIFEST)
	lfs.mkdir(ENV.PP_PACK)
end
--==================================================================--
--后处理系统(pp_*)
--==================================================================--
--任务:appendExterOptions()
function appendExterOptions(deviceTable, device, dataOutDir, destFile)
	if (deviceTable) then
		optionTable = deviceTable[device]

		if (optionTable) then
			lfs.chdir(dataOutDir)
			local h = io.open(destFile,"a")
			io.output(h)

			for k,v in pairs (optionTable) do
				--ShowInfo("infor : ",MAKE.DEVICE_MODEL,OPID.OPID)
				io.write("\n"..k.."="..v)
			end

			io.close()
		end
	end
end

--==================================================================--
--任务:pp_data()
function pp_data()
	ShowInfo("...pp_data...")
	lfs.xcopy(ENV.JYWRAPPER_DATA_COMMON,ENV.PP_DATA_RAW)
	lfs.xcopy(ENV.JYWRAPPER_DATA,ENV.PP_DATA_RAW)
	pushd()
	lfs.chdir(ENV.PP_DATA_RAW)
	local batname = "_compile.bat"
	WriteTable({"@echo off"},batname,false)
	WriteTable(ENV,batname,true)
	if CHANNEL["PREPROCESS"]["REVIEW"] then
		WriteTable({string.format("set REVIEW=%s","true")},batname,true)
	end
	WriteTable({string.format("call make.bat \"%s\" \"%s\" \"%s\" \"%s\"",
		ENV.PP_DATA_OUT,ENV.PP_DATA_OUT,ENV.PP_DATA_H,MAKE.DEVICE_MODEL)},
		batname,
		true)
	WriteTable({"@echo on"},batname,true)
	os.execute(string.format("call %s",batname))
	ShowInfo(string.format("Data BIN 目录: %s",ENV.PP_DATA_OUT))
	ShowInfo(string.format("Data SRC 目录: %s",ENV.PP_DATA_H))

	--@TODO 此系统尚未完成
	--给config.ini尾部添加渠道自定义的配置,该配置在make_lua\Channel\渠道.lua中的DEVICE_OPTIONS配置
	appendExterOptions(CHANNEL.DEVICE_OPTIONS, "ALL", ENV.PP_DATA_OUT, "config.ini")
	appendExterOptions(CHANNEL.DEVICE_OPTIONS, MAKE.DEVICE_MODEL, ENV.PP_DATA_OUT, "config.ini")

	if SPECIAL_DEVICE_OPTIONS then
	--给config.ini尾部添加项目自定义的配置
		appendExterOptions(SPECIAL_DEVICE_OPTIONS, "ALL", ENV.PP_DATA_OUT, "config.ini")
		appendExterOptions(SPECIAL_DEVICE_OPTIONS, MAKE.DEVICE_MODEL, ENV.PP_DATA_OUT, "config.ini")
	end
	--所有源文件，增加package
	Java.AddPackageName(ENV.PP_DATA_H,MAKE.PACKAGE_NAME)
	popd()
end
--==================================================================--
--任务:pp_build()
function pp_build()
		function pp_compile()
			local function pp_collect_src()
				ShowInfo("...pp_collect_src...")
				--local SrcList = {}
				--table.insert(SrcList,ENV.JYWRAPPER_COMMON_SRC)
				--table.insert(SrcList,ENV.JYWRAPPER_SPECIFIC_SRC)
				--pushd()
				--srccopy(SrcList,{".JAVA",".H",".JPP"},ENV.PP_SRC_RAW)
				--popd()
				--重复，此前已经编译过
				--lfs.xcopy(ENV.JYWRAPPER_COMMON_SRC,ENV.PP_SRC_RAW)
				--lfs.xcopy(ENV.JYWRAPPER_SPECIFIC_SRC,ENV.PP_SRC_RAW)
			end
			local function pp_compile()
				ShowInfo("...pp_compile...")
				Java.compile(ENV.PP_SRC_RAW,ENV.PP_SRC_COMPILED)
			end
			pp_collect_src()
			pp_compile()
		end
		function pp_merge()
			ShowInfo("...pp_merge...")
			pushd()
			lfs.chdir(ENV.PP_SRC_MERGED)
			Java.expand(ENV.BLD_PACK_JARFILE,ENV.PP_SRC_MERGED)
			lfs.xcopy(ENV.PP_SRC_COMPILED,ENV.PP_SRC_MERGED)
			lfs.xcopy(ENV.PP_DATA_OUT,ENV.PP_SRC_MERGED)
			popd()
		end
		function pp_optimize()
			ShowInfo("...pp_optimize...")
			if DEVICE.USE_BYTECODEOPTIMIZE then
				--为optimize拷贝目录树
				os.execute(string.format("xcopy /T/Y \"%s\" \"%s\" > NUL", ENV.PP_SRC_MERGED, ENV.PP_SRC_OPTIMIZED))

				--将所有代码合并到tmp目录
				local srcDir = ENV.PP_SRC_MERGED
				local destDir = srcDir.."\\tmp"
				lfs.mkdir(destDir)
				ForAllFile(srcDir, {".class"}, lfs.copyfile, destDir)

				ShowInfo("...pp_optimize...")
				os.execute(string.format("%s\\bin\\java.exe -classpath \"%s\\GLASM\\GLASM.jar\" GLASM -verbose -src \"%s\" -dst \"%s\" -c \"%s\\glasm_config.ini\"",
					ENV.JDK_PATH,ENV.TOOLS_PATH,destDir,ENV.PP_SRC_OPTIMIZED,ENV.MAKE_PATH))
			else
				lfs.xcopy(ENV.PP_SRC_MERGED,ENV.PP_SRC_OPTIMIZED)
			end
			ForAllFile(ENV.PP_SRC_MERGED,{".class"},lfs.delfile)
		end
		function pp_obfuscate()
			ShowInfo("...pp_obfuscate...")
			pushd()
			lfs.chdir(ENV.PP_SRC_OPTIMIZED)
			local inputs = {}
			table.insert(inputs,ENV.PP_SRC_OPTIMIZED)

			--如果第3方库一起混淆,则先将libs包解压覆盖到injar目录，让lib跟代码一起混淆
			if (CHANNEL.OBFUSCAT_LIBS=="1") then
				for _,v in pairs(CHANNEL:Lib()) do
					Java.expand(v,ENV.PP_SRC_OPTIMIZED)
				end
			end

			Java.obfuscate(inputs,ENV.PP_SRC_OBFUSCATED,CHANNEL.PROGUARD_OPT)

			popd()
		end
		function pp_preverify()
			ShowInfo("...preverifying...")

			--如果是第三方库一起混淆，则不解压缩libs包覆盖obfuscated目录
			if (CHANNEL.OBFUSCAT_LIBS~="1") then
				for _,v in pairs(CHANNEL:Lib()) do
					Java.expand(v,ENV.PP_SRC_OBFUSCATED)
					Java.expand(v,ENV.PP_SRC_PREVERIFIED)
				end
			end

			--如果有lib资源的包，则解压缩出来覆盖到指定目录
			if (CHANNEL.LIB_DATAS~=nil) then
				for _,v in pairs(CHANNEL.LIB_DATAS) do
					libdata = string.format("%s\\%s",ENV.JYWRAPPER_3RD_LIB,v)
					Java.expand(libdata,ENV.PP_SRC_OBFUSCATED)
					Java.expand(libdata,ENV.PP_SRC_PREVERIFIED)
				end
			end

			lfs.xcopy(ENV.PP_SRC_MERGED,ENV.PP_SRC_PREVERIFIED)
			lfs.xcopy(ENV.PP_DATA_OUT,ENV.PP_SRC_PREVERIFIED)
			lfs.xcopy(ENV.PP_SRC_OBFUSCATED,ENV.PP_SRC_PREVERIFIED)
			Java.preverify(ENV.PP_SRC_OBFUSCATED,ENV.PP_SRC_PREVERIFIED)
		end
		function pp_manifest_jad()
			ShowInfo("...pp_manifest_jad...")
			pushd()
			lfs.chdir(ENV.PP_PACK)
			-- 格式: 内容,MANIFEST有效,JAD有效
			local text={}
			table.insert(text,{{["Manifest-Version"]				="1.0"},										true,					false})

			table.insert(text,{{["MIDlet-Icon"] 				= "/icon.png"},							true,					true})

			table.insert(text,{{["MIDlet-Vendor"] 				= string.format("%s",CHANNEL.Vender)},			true,					true})
			table.insert(text,{{["MIDlet-Name"]					= string.format("%s",CHANNEL.GAME_NAME)},		true,					true})

			if(DEVICE.USE_MIDP10) then
			table.insert(text,{{["MicroEdition-Profile"]		= string.format("MIDP-1.0")},					true,					true})
			end
			if(DEVICE.USE_MIDP20) then
			table.insert(text,{{["MicroEdition-Profile"]		= string.format("MIDP-2.0")},					true,					true})
			end
			if(DEVICE.USE_CLDC10) then
			table.insert(text,{{["MicroEdition-Configuration"]	= string.format("CLDC-1.0")},					true,					true})
			end
			if(DEVICE.USE_CLDC11) then
			table.insert(text,{{["MicroEdition-Configuration"]	= string.format("CLDC-1.1")},					true,					true})
			end
			table.insert(text,{{["MIDlet-Version"]				= string.format("%s",CHANNEL.VERSION_NUMBER)},	true,					true})
			if MAKE.PACKAGE_NAME~="" then
				table.insert(text,{{["MIDlet-1"]					= CHANNEL.GAME_NAME ..", /icon.png, "..MAKE.PACKAGE_NAME .. "." .. CHANNEL.MIDLET_CLASS},true,true})
			else
				table.insert(text,{{["MIDlet-1"]					= CHANNEL.GAME_NAME ..", /icon.png, "..CHANNEL.MIDLET_CLASS},true,true})
			end
			if(DEVICE.NOKIA_DEVICE) then
			table.insert(text,{{["Nokia-MIDlet-Category"] 		= "Game"},										false,					true})
			end
			table.insert(text,{{["MIDlet-Jar-URL"]				= string.format("%s.jar",CHANNEL:ArchiveName())},false,				true})

            if DEVICE.USE_ORIENTATION_PORTRAIT then
                table.insert(text,{{["Nokia-MIDlet-App-Orientation"] 		= "portrait"},										true,					false})
            end
	    if DEVICE.MODEL == "E62" then
		table.insert(text,{{["Nokia-MIDlet-Original-Display-Size"] = "176,208"},true,false})
		table.insert(text,{{["Nokia-MIDlet-Target-Display-Size"] = "320,240"},true,false})
	    end
	    if DEVICE.MODEL == "N97" then
		table.insert(text,{{["Nokia-MIDlet-Original-Display-Size"] = "240,320"},true,false})
		table.insert(text,{{["Nokia-MIDlet-Target-Display-Size"] = "360,360"},true,false})
		table.insert(text,{{["Nokia-MIDlet-App-Orientation"] = "portrait"},true,false})
	    end

			if USERCODE.updatejad then
				text = USERCODE.updatejad(text)
			end

			local manifest = {}
			local jad = {}
			for _,v in pairs(text) do
				if v[2] then	-- 写入MANIFEST.MF
					for kk,vv in pairs(v[1]) do
						manifest[kk] = vv
					end
				end
				if v[3] then	-- 写入jad.txt
					for kk,vv in pairs(v[1]) do
						jad[kk] = vv
					end
				end
			end
			table.insert(jad,"")
			if(DEVICE.NOKIA_DEVICE) then
				table.insert(jad,"")
			end
			local jadfilename = string.format("%s.jad",CHANNEL:ArchiveName())
            if CHANNEL.CHANNEL_NAME == "QQ" then
                jadfilename = iconv(jadfilename,"UTF-8","GBK")
            end
			lfs.delfile(ENV.PP_MANIFEST.."\\MANIFEST.MF")
			lfs.delfile(jadfilename)
			WriteTable(manifest,ENV.PP_MANIFEST.."\\MANIFEST.MF",false,": ","")
			WriteTable(jad,jadfilename,false,": ","")
			popd()
		end
		function pp_release()
			pushd()
			ShowInfo("...pp_release...")
			local arch_name = CHANNEL:ArchiveName()
            if CHANNEL.CHANNEL_NAME == "QQ" then
                arch_name = iconv(arch_name,"UTF-8","GBK")
            end
			local fullname = string.format("%s\\%s.jar",ENV.PP_PACK,arch_name)
			lfs.delfile(fullname)
			lfs.chdir(ENV.PP_PACK)
			if DEVICE.USE_KZIP and false then
				ShowInfo("...kziping...")
				Java.PackFile(fullname,ENV.PP_SRC_PREVERIFIED,false,false,ENV.PP_MANIFEST.."\\MANIFEST.MF")
				local tmp = ENV.PP_PACK.."\\tmp"
				lfs.mkdir(tmp)
				Java.expand(fullname,tmp)
				lfs.delfile(fullname)
				lfs.chdir(tmp)
				os.execute(string.format("call \"%s\\compress\\kzip.exe\" /r /b0 ..\\%s *.* > NUL",ENV.TOOLS_PATH,arch_name .. ".jar"))
				lfs.chdir(ENV.PP_PACK)
				lfs.rmdir(tmp)
			elseif DEVICE.USE_JAR then
				ShowInfo("...jaring...")
				Java.PackFile(arch_name .. ".jar",ENV.PP_SRC_PREVERIFIED,true,false,ENV.PP_MANIFEST.."\\MANIFEST.MF")
			else
				ShowInfo("...7za...")
				Java.PackFile(fullname,ENV.PP_SRC_PREVERIFIED,false,false,ENV.PP_MANIFEST.."\\MANIFEST.MF")
				local tmp = ENV.PP_PACK.."\\tmp"
				lfs.mkdir(tmp)
				Java.expand(fullname,tmp)
				lfs.delfile(fullname)
				lfs.chdir(tmp)
				os.execute(string.format("call \"%s\\compress\\7za.exe\" a -tzip -r -mfb=192 -mpass=4 ..\\%s * > NUL",ENV.TOOLS_PATH,arch_name .. ".jar"))
				lfs.chdir(ENV.PP_PACK)
				lfs.rmdir(tmp)
			end
			ShowInfo("...updating jar size...")
			os.execute(string.format("\"%s\\UpdateJAD\\UpdateJAD.exe\" %s.jad -f MIDlet-Jar-Size 0 > NUL",ENV.TOOLS_PATH,arch_name))
			os.execute(string.format("\"%s\\UpdateJAD\\UpdateJAD.exe\" %s.jad -s -j %s.jar -n> NUL",ENV.TOOLS_PATH,arch_name,arch_name))
			ShowInfo(string.format("Please find REDIST Build here: %s",fullname))
			popd()
		end
	pp_compile()
	pp_merge()
	pp_optimize()
	pp_obfuscate()
	pp_preverify()
	pp_manifest_jad()
	pp_release()
end

function workspace()
	local folder
	if CHANNEL["PREPROCESS"]["REVIEW"] then
		folder = string.format("%s\\%s\\%s_%s_Review", ENV.WORKSPACE,MAKE.CHANNEL_NAME,MAKE.DEVICE_BRAND,MAKE.DEVICE_MODEL)
	else
		folder = string.format("%s\\%s\\%s_%s", ENV.WORKSPACE,MAKE.CHANNEL_NAME,MAKE.DEVICE_BRAND,MAKE.DEVICE_MODEL)
	end
	local prjname = string.format("%s_%s_%s",MAKE.CHANNEL_NAME,MAKE.DEVICE_MODEL,CHANNEL.PACK_NAME)
	local prjfolderinWorkspace = string.format("%s\\.metadata\\.plugins\\org.eclipse.core.resources\\.projects\\%s",ENV.WORKSPACE,prjname)
	lfs.rmdir(folder)
	lfs.mkdir(folder)
	ShowInfo("创建 .classpath")
	WriteString(folder .. "\\.classpath",tabletoxml(CreateCLASSPATH()))
	ShowInfo("创建 .project")
	WriteString(folder .. "\\.project",tabletoxml(CreateProject()))
	ShowInfo("导入到Workspace")
	lfs.mkdir(prjfolderinWorkspace)
	ImportToWorkspace(prjfolderinWorkspace,folder,prjname)
	ShowInfo("创建 .DebugSetting")
	lfs.mkdir(string.format("%s\\.metadata\\.plugins\\org.eclipse.debug.core\\.launches",ENV.WORKSPACE))
	WriteString(string.format("%s\\.metadata\\.plugins\\org.eclipse.debug.core\\.launches\\"..prjname..".launch", ENV.WORKSPACE),tabletoxml(CreateDebugSetting(prjname)))
	ShowInfo("限定编码集为UTF-8")
	lfs.mkdir(folder .."\\.settings")
	WriteString(folder .."\\.settings\\org.eclipse.core.resources.prefs",iconv("eclipse.preferences.version=1\nencoding/<project>=UTF-8","GBK","UTF-8"))
	if (DEVICE.PLATFORM =="Android") then
		CreateAndroidDirectory(folder)
	end
	ShowInfo("创建 .mtj")
	WriteString(folder .. "\\.mtj",tabletoxml(CreateMTJ()))
end

--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
-- 引擎内核 运行框架
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--


--==================================================================--
-- 当前系统支持的任务列表
Tasks = {
		optimize_on		= {name="optimize_on",		func = optimize_on,	internal=true,	depends={},						desc="允许编译器优化"},
		optimize_off	= {name="optimize_off",		func = optimize_off,internal=true,	depends={},						desc="关闭编译器优化"},
		review			= {name="review",			func = setreview_macro,internal=true,	depends={},					desc="设定为评审版"},

		encodedata_on	= {name="encodedata_on",	func = encodedata_on,	internal=false,depends={},				desc="允许数据混淆,目前供RPG组使用"},
		encodedata_off	= {name="encodedata_off",	func = encodedata_off,	internal=false,depends={},				desc="关闭数据混淆,目前供HMM组使用"},


		cpp_on			= {name="cpp_on",		func = cpp_on,		internal=false,	depends={},					desc="开启CPP预处理代码"},
		cpp_off			= {name="cpp_off",		func = cpp_off,		internal=false,	depends={},					desc="关闭CPP预处理代码"},

		help			= {name="help",		func = help,			internal=false,depends={},						desc="帮助: 尚未完成"},
		pb_clean		= {name="pb_clean",	func = pb_clean,		internal=false,depends={},						desc="预编译: 清理环境"},
		pb_data 		= {name="pb_data",	func = pb_data,			internal=false,depends={},						desc="预编译: 数据编译"},
		pb_packdata 	= {name="pb_packdata",	func = pb_packdata,	internal=false,depends={},						desc="预编译: 数据打包(packfile)"},

		pb_build		= {name="pb_build",	func = pb_build,		internal=false,depends={},						desc="预编译: 编译内部包(用于后处理)"},

		pp_clean		= {name="pp_clean",	func = pp_clean,		internal=false,depends={},						desc="后处理: 清理环境",},
		pp_data 		= {name="pp_data",	func = pp_data,			internal=false,depends={},						desc="后处理: 数据编译"},
		pp_build		= {name="pp_build",	func = pp_build,		internal=false,depends={},						desc="后处理: 编译发行包"},

		alldata			= {name="alldata",	func = nil,				internal=false,depends={},						desc="调试用: 仅数据打包"},
		clean			= {name="clean",	func = nil,				internal=false,depends={},						desc="全部清理"},
		tag				= {name="tag",		func = tag,				internal=false,depends={},						desc="tag the current working copy using the tag tool.",},

		workspace		= {name="workspace",func = workspace,		internal=false,depends={},						desc="创造Workspace的工程设定.",},
	}

--==================================================================--
-- 根据用户的命令行输入，或者main()函数的参数，解析出来的 任务列表
local Commands = {}
-- 最近一次执行指令名，避免连续执行两个相同的任务
local LastCommand = ""

--==================================================================--
-- 检查命令行参数
	function usage()
		ShowInfo(
[[统一编译系统使用方法
make.lua 任务1[ 任务2][ 任务3][ 任务N] 渠道 品牌 型号]]
		)
		ShowInfo("任务列表")
		for k,v in pairs(Tasks) do
			if v.internal ==false then
				ShowInfo(string.format("\t%-20s%s",v.name,v.desc))
			end
		end
		ShowInfo("设备列表","型号","品牌")
		for k,v in pairs(DEVICECONFIG) do
			if k~="Init" then
				ShowInfo("	",k,v.BRAND)
			end
		end
		ShowInfo("通道列表")
		for k,v in pairs(SPCONFIG) do
			if k~="Init" then
				ShowInfo("	",k)
			end
		end
		ShowInfo(
[[
预编译目录说明：
	.1-Build			预编译过程使用
	.pack_internal		预编译生成的包 <此包不能运行严禁外传>
后处理目录说明：
	.2-PP				后处理过程使用
	.pack				最终包，可供发行

举例:
  勇者和焚天
    预编译
      make.lua pb_clean pb_data pb_build JY Nokia N73
	后处理
      make.lua pp_clean pp_data pp_build DANGLE Nokia N73
  神马
    预编译
      make.lua cpp_on encodedata_off pb_clean pb_data pb_packdata pb_build JY Nokia N73
    后处理
      make.lua pp_clean pp_data pp_build DANGLE Nokia N73
]]
		)
	end
local function checkarg(params)

	--  检查是否参数为合法(是否均为Tasks中的关键字)
	local maxn = table.maxn(params);
	if( maxn <4) then	-- 至少 4 个参数
		usage()
		return
	end

	for k,v in pairs(params) do
		if (k>=1 and k<=maxn - 3)	 then			-- 参数是从1开始的
			if Tasks[v]==nil then	-- 如果输入参数不在Tasks列表中
				ShowError("参数错误:",v)
				usage()
				return
			else
				table.insert(Commands,v)
			end
		end
	end
--  设定最后3个参数
	MAKE.CHANNEL_NAME 				= params[maxn-2]
	MAKE.DEVICE_BRAND				= params[maxn-1]
	MAKE.DEVICE_MODEL				= params[maxn]


	ShowInfo("计划执行下列任务")
	for k,v in pairs(Commands) do
	ShowInfo("	",k,v,Tasks[v].desc)
	end
	ShowInfo("任务参数设定")
	for k,v in pairs(MAKE) do
	ShowInfo("	",k,v)
	end
end
-------------------------------------------------------------------------
function	RunTasks(_target)
	for _,v in pairs(_target) do
		if table.maxn(Tasks[v].depends) > 0 then
			RunTasks(Tasks[v].depends)
		end
		if(Tasks[v].func) and LastCommand ~=v then
			Tasks[v].func()
			LastCommand = v
		end
	end
end
function main(params)
--[[
	if pl.path.exists("..\\make_lua_data\\project.lua") then
		PROJECT.Init()
	end
]]--
--[[
    table.insert(arg,"cpp_on")
	table.insert(arg,"encodedata_off")


	table.insert(arg,"pb_clean")
	table.insert(arg,"pb_data")
	table.insert(arg,"pb_packdata")
	table.insert(arg,"pb_build")
		table.insert(arg,"JY")
		table.insert(arg,"Nokia")
		table.insert(arg,"N73")
]]--
	params = params or arg
	checkarg(params)
	SPCONFIG_Init(MAKE.CHANNEL_NAME,MAKE.DEVICE_BRAND,MAKE.DEVICE_MODEL)
	DEVICECONFIG_Init(MAKE.CHANNEL_NAME,MAKE.DEVICE_BRAND,MAKE.DEVICE_MODEL)
	ENV.Init(MAKE.CHANNEL_NAME,MAKE.DEVICE_BRAND,MAKE.DEVICE_MODEL)

	dofile(ENV.JYWRAPPER_COMMON .. "\\LUA\\Usercode.lua")
	dofile(ENV.JYWRAPPER_USERCODE .. "\\Usercode.lua")


	if DEVICECONFIG[MAKE.DEVICE_MODEL] == nil then
		ShowError(string.format("设备型号错误: %s",tostring(MAKE.DEVICE_MODEL)))
		usage()
		return
	end
	if string.upper(DEVICE.BRAND) ~=  string.upper(MAKE.DEVICE_BRAND) then
		ShowError(string.format("设备品牌错误: %s",tostring(DEVICE.BRAND)))
		usage()
		return
	end
	if SPCONFIG[MAKE.CHANNEL_NAME] ==  nil then
		ShowError(string.format("渠道不存在: %s",tostring(MAKE.CHANNEL_NAME)))
		usage()
		return
	end




	RunTasks(Commands)
end
-- 程序入口
ShowInfo("...initialized...")
main()
