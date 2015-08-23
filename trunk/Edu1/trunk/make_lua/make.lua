--[[
TODO: ÿ���׶ε��ݴ��Լ�顣ÿ���׶εĹؼ����󱨸�
TODO: Tag����
TODO: TEXT_ENCODING ������Ҫ�ŵ��豸�������棬������ENV����
TODO:
]]--
require "lfs"
require "common"
require "help"
ShowInfo("/**************************************************************\\")
ShowInfo("                                  	                           ")
ShowInfo("                        ͳһ����ϵͳ	                           ")
ShowInfo("       		����: ��� (��ɶ����������������)                  ")
ShowInfo("              �ɶ�˫����Ϣ�������޹�˾ ��Ȩ����                  ")
ShowInfo("                                  	                           ")
ShowInfo("\\**************************************************************/")
ShowInfo("...��ʼ��...")

require "env"
require "pl.path"
--[[
if pl.path.exists("..\\make_lua_data\\project.lua") then
	package.path = package.path .. ";..\\make_lua_data\\project.lua"
	require "project"
end
]]--

--==================================================================--
--����:tag
function tag()
	ShowInfo("Tag ��ʼ")
--    %TOOLS_PATH%\tag_tool\tag.exe -w . -n !ARCHIVE_NAME! -t tags/%BUILD_CONF% -q
--	δ��� os.execute(GetVar("TAG_PATH") .. "tag.exe" .. " -w . -n !ARCHIVE_NAME! -t tags/%BUILD_CONF% -q)
	ShowInfo("Tag ���")
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
	gName = string.format("%s��%d��������Ϸ",year,sMonth)
	CHANNEL.GAME_NAME=iconv(gName,"GBK","UTF-8")

	pName = year .. month .. "05"
	CHANNEL.PACK_GAME_NAME=pName
	CHANNEL.PACK_NAME=pName
]]--
end

--==================================================================--
--����:optimize on / off
function optimize_on()
	DEVICE.OPTIMIZE = true
	table.dump(MAKE)
end
--==================================================================--
--����:optimize on / off
function optimize_off()
	DEVICE.OPTIMIZE = false
	table.dump(MAKE)
end
--==================================================================--
--����:cpp_off
function cpp_off()
	MAKE.CPP = false
	table.dump(MAKE)
end
--==================================================================--
--����:cpp_on
function cpp_on()
	MAKE.CPP = true
	table.dump(MAKE)
end
--==================================================================--
--����:encodedata_off
function encodedata_off()
	MAKE.ENCODEDATA = false
	table.dump(MAKE)
end
--==================================================================--
--����:encodedata_on
function encodedata_on()
	MAKE.ENCODEDATA = true
	table.dump(MAKE)
end

--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--Ԥ�������� pb = prebuild
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--

--==================================================================--
--����:pb_clean()
function pb_clean()
	ShowInfo(string.format("...pb_clean..."))
	lfs.rmdir(ENV.BLD)
end

function pb_packdata()
	function addtolist(filename,inilist)
		table.insert(inilist,0,filename)
	end
	ShowInfo("...pb_packdata...")
	ShowInfo("...�ϲ�ini�ļ�...")
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
	ShowInfo("...Ԥ����ini�ļ����޳�ע��...")
	local defines = TableToString(DEVICE) .. TableToString(CHANNEL.PREPROCESS)
	os.execute(string.format("call \"%s\\cpp\\cpp.exe\" -C -P %s \"%s\"\\data_beforePP.ini \"%s\"\\data.ini",	ENV.TOOLS_PATH,defines,ENV.BLD_DATA_RAW,ENV.BLD_DATA_RAW))
	lfs.copyfile(string.format("%s\\empty.xxx",ENV.BLD_DATA_RAW),ENV.BLD_DATA_TOPACK)
	os.execute(string.format("call \"%s\\bin\\java.exe\" -cp \"%s\\packFile\" packFile -i %s\\ -h %s\\ -d %s\\ -c %s\\compress\\ -p F %s\\data.ini",
		ENV.JDK_PATH,ENV.TOOLS_PATH,ENV.BLD_DATA_TOPACK,ENV.BLD_DATA_H,ENV.BLD_DATA_OUT,ENV.TOOLS_PATH,ENV.BLD_DATA_RAW))

end
--==================================================================--
--����:pb_data()
function pb_data()

		--==========================================================--
		--����:ObfuscateData()
		function ObfuscateData()
			if MAKE.ENCODEDATA	then
				ShowInfo("...ObfuscateData...")
				pushd()
				-- ����������ʹ��lua����д
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
		--����:makefont()
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
	-- ���ﱣ��һ�µ�ǰ��ENV.BLD_DATA_OUT
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
	-- ������Դ
	lfs.xcopy(ENV.DATA_COMMON,ENV.BLD_DATA_RAW)		-- ������Դ
	lfs.xcopy(ENV.DATA,ENV.BLD_DATA_RAW)			-- ָ����Դ
	lfs.xcopy(ENV.DATA_DEVICE,ENV.BLD_DATA_RAW)		-- �ض��豸��Դ (������Ҫʹ��)
	pushd()
	-- ������Դ
	lfs.chdir(ENV.BLD_DATA_RAW)
	local batname = "_compile.bat"
	WriteTable({"@echo off"},batname,false)
	WriteTable(ENV,batname,true)
	WriteTable({string.format("call make.bat \"%s\" \"%s\" \"%s\"",ENV.BLD_DATA_TOPACK,ENV.BLD_DATA_OUT,ENV.BLD_DATA_H)},
		batname,
		true)
	WriteTable({"@echo on"},batname,true)
	os.execute(string.format("call %s",batname))
	-- ��������
	makefont()
	-- ��������
	ObfuscateData()
	popd()
	-- ����Դ���ɵĴ���ŵ�java��Ҫ��package����
	Java.AddPackageName(ENV.BLD_DATA_H,MAKE.PACKAGE_NAME)


	ShowInfo(string.format("Data BIN Ŀ¼: %s",ENV.BLD_DATA_OUT))
	ShowInfo(string.format("Data SRC Ŀ¼: %s",ENV.BLD_DATA_H))
	-- ����ָ���ǰ��ENV.BLD_DATA_OUT
	if (DEVICE.PLATFORM =="Android") then
	ENV.BLD_DATA_OUT = ENV.BLD_DATA_OUT_TEMP
	end
end
--==================================================================--
--����:pb_build
function pb_build()
		--==========================================================--
		--����:pb_collect_src
		function pb_collect_src()
			ShowInfo("...pb_collect_src...")
			lfs.xcopy(ENV.JYLIB_SRC,ENV.BLD_SRC_RAW)
			lfs.xcopy(ENV.COMMON_SRC,ENV.BLD_SRC_RAW)
			lfs.xcopy(ENV.SPECIFIC_SRC,ENV.BLD_SRC_RAW)
			lfs.xcopy(ENV.BLD_DATA_H,ENV.BLD_SRC_RAW)
			lfs.xcopy(ENV.JYWRAPPER_COMMON_SRC,ENV.BLD_SRC_RAW)
			lfs.xcopy(ENV.JYWRAPPER_SPECIFIC_SRC,ENV.BLD_SRC_RAW)
			--��wrapper_data�еĴ������
			lfs.xcopy(ENV.PP_DATA_H,ENV.BLD_SRC_RAW)
			popd()
		end
		--==========================================================--
		--����:pb_collect_preprocess
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
		--����:pb_preprocess
		function pb_preprocess()
			ShowInfo("...pb_preprocess...")
			-- Ŀǰ֧�� CPP �� Antenna ����Ԥ����Ϊ�˺�Eclipse��������ϣ������ʹ�� Antenna����Ԥ����Ҳ����MTJ
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
		--����:pb_compile
		function pb_compile()
			ShowInfo("...pb_compile...")
			Java.compile(ENV.BLD_SRC_PREPROCESS,ENV.BLD_SRC_COMPILED)
		end
		--==========================================================--
		--����:pb_buildpack
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
--���������� pp = postprocess
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--

--==================================================================--
--����:pp_clean()
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
--����ϵͳ(pp_*)
--==================================================================--
--����:appendExterOptions()
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
--����:pp_data()
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
	ShowInfo(string.format("Data BIN Ŀ¼: %s",ENV.PP_DATA_OUT))
	ShowInfo(string.format("Data SRC Ŀ¼: %s",ENV.PP_DATA_H))

	--@TODO ��ϵͳ��δ���
	--��config.iniβ����������Զ��������,��������make_lua\Channel\����.lua�е�DEVICE_OPTIONS����
	appendExterOptions(CHANNEL.DEVICE_OPTIONS, "ALL", ENV.PP_DATA_OUT, "config.ini")
	appendExterOptions(CHANNEL.DEVICE_OPTIONS, MAKE.DEVICE_MODEL, ENV.PP_DATA_OUT, "config.ini")

	if SPECIAL_DEVICE_OPTIONS then
	--��config.iniβ�������Ŀ�Զ��������
		appendExterOptions(SPECIAL_DEVICE_OPTIONS, "ALL", ENV.PP_DATA_OUT, "config.ini")
		appendExterOptions(SPECIAL_DEVICE_OPTIONS, MAKE.DEVICE_MODEL, ENV.PP_DATA_OUT, "config.ini")
	end
	--����Դ�ļ�������package
	Java.AddPackageName(ENV.PP_DATA_H,MAKE.PACKAGE_NAME)
	popd()
end
--==================================================================--
--����:pp_build()
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
				--�ظ�����ǰ�Ѿ������
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
				--Ϊoptimize����Ŀ¼��
				os.execute(string.format("xcopy /T/Y \"%s\" \"%s\" > NUL", ENV.PP_SRC_MERGED, ENV.PP_SRC_OPTIMIZED))

				--�����д���ϲ���tmpĿ¼
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

			--�����3����һ�����,���Ƚ�libs����ѹ���ǵ�injarĿ¼����lib������һ�����
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

			--����ǵ�������һ��������򲻽�ѹ��libs������obfuscatedĿ¼
			if (CHANNEL.OBFUSCAT_LIBS~="1") then
				for _,v in pairs(CHANNEL:Lib()) do
					Java.expand(v,ENV.PP_SRC_OBFUSCATED)
					Java.expand(v,ENV.PP_SRC_PREVERIFIED)
				end
			end

			--�����lib��Դ�İ������ѹ���������ǵ�ָ��Ŀ¼
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
			-- ��ʽ: ����,MANIFEST��Ч,JAD��Ч
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
				if v[2] then	-- д��MANIFEST.MF
					for kk,vv in pairs(v[1]) do
						manifest[kk] = vv
					end
				end
				if v[3] then	-- д��jad.txt
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
	ShowInfo("���� .classpath")
	WriteString(folder .. "\\.classpath",tabletoxml(CreateCLASSPATH()))
	ShowInfo("���� .project")
	WriteString(folder .. "\\.project",tabletoxml(CreateProject()))
	ShowInfo("���뵽Workspace")
	lfs.mkdir(prjfolderinWorkspace)
	ImportToWorkspace(prjfolderinWorkspace,folder,prjname)
	ShowInfo("���� .DebugSetting")
	lfs.mkdir(string.format("%s\\.metadata\\.plugins\\org.eclipse.debug.core\\.launches",ENV.WORKSPACE))
	WriteString(string.format("%s\\.metadata\\.plugins\\org.eclipse.debug.core\\.launches\\"..prjname..".launch", ENV.WORKSPACE),tabletoxml(CreateDebugSetting(prjname)))
	ShowInfo("�޶����뼯ΪUTF-8")
	lfs.mkdir(folder .."\\.settings")
	WriteString(folder .."\\.settings\\org.eclipse.core.resources.prefs",iconv("eclipse.preferences.version=1\nencoding/<project>=UTF-8","GBK","UTF-8"))
	if (DEVICE.PLATFORM =="Android") then
		CreateAndroidDirectory(folder)
	end
	ShowInfo("���� .mtj")
	WriteString(folder .. "\\.mtj",tabletoxml(CreateMTJ()))
end

--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
-- �����ں� ���п��
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--
--==================================================================--


--==================================================================--
-- ��ǰϵͳ֧�ֵ������б�
Tasks = {
		optimize_on		= {name="optimize_on",		func = optimize_on,	internal=true,	depends={},						desc="����������Ż�"},
		optimize_off	= {name="optimize_off",		func = optimize_off,internal=true,	depends={},						desc="�رձ������Ż�"},
		review			= {name="review",			func = setreview_macro,internal=true,	depends={},					desc="�趨Ϊ�����"},

		encodedata_on	= {name="encodedata_on",	func = encodedata_on,	internal=false,depends={},				desc="�������ݻ���,Ŀǰ��RPG��ʹ��"},
		encodedata_off	= {name="encodedata_off",	func = encodedata_off,	internal=false,depends={},				desc="�ر����ݻ���,Ŀǰ��HMM��ʹ��"},


		cpp_on			= {name="cpp_on",		func = cpp_on,		internal=false,	depends={},					desc="����CPPԤ�������"},
		cpp_off			= {name="cpp_off",		func = cpp_off,		internal=false,	depends={},					desc="�ر�CPPԤ�������"},

		help			= {name="help",		func = help,			internal=false,depends={},						desc="����: ��δ���"},
		pb_clean		= {name="pb_clean",	func = pb_clean,		internal=false,depends={},						desc="Ԥ����: ������"},
		pb_data 		= {name="pb_data",	func = pb_data,			internal=false,depends={},						desc="Ԥ����: ���ݱ���"},
		pb_packdata 	= {name="pb_packdata",	func = pb_packdata,	internal=false,depends={},						desc="Ԥ����: ���ݴ��(packfile)"},

		pb_build		= {name="pb_build",	func = pb_build,		internal=false,depends={},						desc="Ԥ����: �����ڲ���(���ں���)"},

		pp_clean		= {name="pp_clean",	func = pp_clean,		internal=false,depends={},						desc="����: ������",},
		pp_data 		= {name="pp_data",	func = pp_data,			internal=false,depends={},						desc="����: ���ݱ���"},
		pp_build		= {name="pp_build",	func = pp_build,		internal=false,depends={},						desc="����: ���뷢�а�"},

		alldata			= {name="alldata",	func = nil,				internal=false,depends={},						desc="������: �����ݴ��"},
		clean			= {name="clean",	func = nil,				internal=false,depends={},						desc="ȫ������"},
		tag				= {name="tag",		func = tag,				internal=false,depends={},						desc="tag the current working copy using the tag tool.",},

		workspace		= {name="workspace",func = workspace,		internal=false,depends={},						desc="����Workspace�Ĺ����趨.",},
	}

--==================================================================--
-- �����û������������룬����main()�����Ĳ��������������� �����б�
local Commands = {}
-- ���һ��ִ��ָ��������������ִ��������ͬ������
local LastCommand = ""

--==================================================================--
-- ��������в���
	function usage()
		ShowInfo(
[[ͳһ����ϵͳʹ�÷���
make.lua ����1[ ����2][ ����3][ ����N] ���� Ʒ�� �ͺ�]]
		)
		ShowInfo("�����б�")
		for k,v in pairs(Tasks) do
			if v.internal ==false then
				ShowInfo(string.format("\t%-20s%s",v.name,v.desc))
			end
		end
		ShowInfo("�豸�б�","�ͺ�","Ʒ��")
		for k,v in pairs(DEVICECONFIG) do
			if k~="Init" then
				ShowInfo("	",k,v.BRAND)
			end
		end
		ShowInfo("ͨ���б�")
		for k,v in pairs(SPCONFIG) do
			if k~="Init" then
				ShowInfo("	",k)
			end
		end
		ShowInfo(
[[
Ԥ����Ŀ¼˵����
	.1-Build			Ԥ�������ʹ��
	.pack_internal		Ԥ�������ɵİ� <�˰����������Ͻ��⴫>
����Ŀ¼˵����
	.2-PP				�������ʹ��
	.pack				���հ����ɹ�����

����:
  ���ߺͷ���
    Ԥ����
      make.lua pb_clean pb_data pb_build JY Nokia N73
	����
      make.lua pp_clean pp_data pp_build DANGLE Nokia N73
  ����
    Ԥ����
      make.lua cpp_on encodedata_off pb_clean pb_data pb_packdata pb_build JY Nokia N73
    ����
      make.lua pp_clean pp_data pp_build DANGLE Nokia N73
]]
		)
	end
local function checkarg(params)

	--  ����Ƿ����Ϊ�Ϸ�(�Ƿ��ΪTasks�еĹؼ���)
	local maxn = table.maxn(params);
	if( maxn <4) then	-- ���� 4 ������
		usage()
		return
	end

	for k,v in pairs(params) do
		if (k>=1 and k<=maxn - 3)	 then			-- �����Ǵ�1��ʼ��
			if Tasks[v]==nil then	-- ��������������Tasks�б���
				ShowError("��������:",v)
				usage()
				return
			else
				table.insert(Commands,v)
			end
		end
	end
--  �趨���3������
	MAKE.CHANNEL_NAME 				= params[maxn-2]
	MAKE.DEVICE_BRAND				= params[maxn-1]
	MAKE.DEVICE_MODEL				= params[maxn]


	ShowInfo("�ƻ�ִ����������")
	for k,v in pairs(Commands) do
	ShowInfo("	",k,v,Tasks[v].desc)
	end
	ShowInfo("��������趨")
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
		ShowError(string.format("�豸�ͺŴ���: %s",tostring(MAKE.DEVICE_MODEL)))
		usage()
		return
	end
	if string.upper(DEVICE.BRAND) ~=  string.upper(MAKE.DEVICE_BRAND) then
		ShowError(string.format("�豸Ʒ�ƴ���: %s",tostring(DEVICE.BRAND)))
		usage()
		return
	end
	if SPCONFIG[MAKE.CHANNEL_NAME] ==  nil then
		ShowError(string.format("����������: %s",tostring(MAKE.CHANNEL_NAME)))
		usage()
		return
	end




	RunTasks(Commands)
end
-- �������
ShowInfo("...initialized...")
main()
