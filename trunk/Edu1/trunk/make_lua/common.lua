require "lfs"
require "tablexml"
require"pack"
--require "tabledump"
--[[
�����ַ�(��^$()%.[]*+-?��): ����ַ��������
.(��): ���κ��ַ����
%a: ���κ���ĸ���
%c: ���κο��Ʒ����(����\n)
%d: ���κ��������
%l: ���κ�Сд��ĸ���
%p: ���κα��(punctuation)���
%s: ��հ��ַ����
%u: ���κδ�д��ĸ���
%w: ���κ���ĸ/�������
%x: ���κ�ʮ�����������
%z: ���κδ���0���ַ����
%x(�˴�x�Ƿ���ĸ�������ַ�): ���ַ�x���. ��Ҫ����������ʽ���й��ܵ��ַ�(^$()%.[]*+-?)���������, ����%%��%���
[�����ַ���]: ���κ�[]�а������ַ������. ����[%w_]���κ���ĸ/����, ���»��߷���(_)���
[^�����ַ���]: ���κβ�������[]�е��ַ������. ����[^%s]���κηǿհ��ַ����

(����һ��ת������%f. %f������Frontier Pattern, ��ΪһЩԭ��û�б�д��Lua�ı�׼�ĵ���. ����Ȥ�����ѿ��Կ�lua-users wiki: Frontier Pattern)

���������ַ����ô�д��дʱ, ��ʾ��Ǵ��ַ�����κ��ַ����. ����, %S��ʾ���κηǿհ��ַ����.

��Ա��ʽ���������ַ�����ż����ض�ѡ��ɵ�, ��Щ�ض�ѡ�����:

�����κ�ѡ��(����"%a"): �뵥�������ַ����
���*��(����"%a*"): ��0�����������ַ����. ֻ������ַ����з���Ҫ�����Ӵ����.
���+��(����"%a+"): ��1�����������ַ����. ֻ������ַ����з���Ҫ�����Ӵ����.
���+��(����"%a-"): ��0�����������ַ����. ֻ������ַ����з���Ҫ�������Ӵ����.
���?��(����"%a?"): ��0����1�������ַ����.

��Ա��ʽ���ɰ�������������Ա:

%bxy: ����x, y���ַ�. ��x��ʼ, y����, ����x, y���ַ�����ƽ����Ե��ַ������.
ƽ����Ա�ʾ: ��һ��ʼֵΪ��ļ�����, �Ӹ����ַ���������Ҳ������ȡ�ַ�, ÿ��ȡһ��x, ������+1, ÿ��ȡһ��y, ������-1, ��ô���һ��yǡ���ǵ�һ��ʹ�����������y.
Ҳ����˵, ��%bxy�ɹ���Ե��ַ�����, �Ҳ������̵����ַ���ʹ������%bxy���.
%n: ����n��1~9������. �벶��(������)�ĵ�n������ִ����.

����: �����ִ�"abc ABC 123 !@# \n \000 %"

��Ա��ʽ                                    ��Խ��

%a                          a b c A B C
%a*                         abc ABC
%a+                         abc ABC
%a-%s                       abc ABC
%a?                         a b c A B C
%ba3                        abc ABC 123

����Ա��ʽ�л��������ê��Ͳ�����.

ê�����^��$����, ^��ʾ�ַ�����ʼ��, $��ʾ�ַ���������. ����^MYADDON:.+��ʾ��MYADDON:��ͷ���ַ���.

��������Ա��ʽ�еĳɶԵ�Բ���ű�ʾ����(capture)���. ÿһ��Բ�����еı��ʽ�ɹ���Ե��ַ������ᱻ��������, ���ҿ�����%n��ȡ(�μ�ǰ��). ����Բ���ſ���Ƕ��, ���Բ���ŵı��˳������������Ϊ׼��, �������ŵĳ����Ⱥ�˳����. ������Ա��ʽ"(a*(.)%w(%s*))"��, a*(.)%w(%s*)�ᱻ��Ϊ1�Ų�׽���, .��%s*��ֱ���2�š�3�Ų�׽���.

������һЩ���Ӧ�ø�����Ա��ʽ��Ա������:

���ʽ               ��Խ��

%S+                ��ԭ�ִ����зǿհ׳�Ա���(�����ո������ÿ������)
^MYADDON:(.)       ������MYADDON:��ͷ���ַ������, ���Ҳ������Ĳ���
(%d+%.?%d*)        �����к��л򲻺���С�����ֵ��������, ��������������
(%w+)=(%S+)        �����������������ĸ���, �Ҳ����޿ո��ִ���ɵĵ�ʽ���,
                   ���ֱ𲶻��ʽ������.
%b''               �����е������ڵ��ַ������(���������ű���)
%S+$               ��ԭ�ִ������һ���ǿհ׳�Ա���

]]--
----------------------------------------------------------
-- ϵͳ��Ϣ��ʾ (��ʱֱ����ϵͳ��print
ShowError=print
ShowInfo=print
ShowWarning=print
ShowDebug=print
TRUE=true
FALSE=false
--==========================================================--
-- �ļ�ִ�в��������һ���ļ��Ƿ���ڣ�������ڣ���ִ����
--==========================================================--
function tryTorun(filename)
	local handle = io.open(filename,"r")
	if handle~=nil then
		io.close(handle)
		dofile (filename)
	end
end

--==========================================================--
-- table����
--==========================================================--
--�����
function table.copy(t)
	local result = {}
	local k,v
	for k,v in pairs(t) do
		if type(v)=="table" then
		result[k] = table.copy(v)
		else
		result[k] = v
		end
	end
	return result
end
function table.dump(table)
	ShowInfo("**************************************************************")
	if USERCODE.Exec then
		USERCODE.Exec(Exec)
	end
	for k,v in pairs(table) do
	ShowInfo("	",k,v)
	end
end
--==========================================================--
-- ��һ���������ݣ�ת��ΪAnntena�ķ��ż��ı��
--==========================================================--
function TableToAnntenaSymbolSet(src,dst)
	for k,v in pairs(src) do
		vtype = type(v)
		local k_str = k
		local v_str = v
		if v==true then
			v_str = "TRUE"
		elseif v==false then
			v_str = "FALSE"
		elseif v_type~="table" then
			v_str = tostring(v)
		end
		table.insert(dst,string.format("%s=\'%s\'",tostring(k),v_str))--"symbol",{name=tostring(k),value=tostring(v)}))
	end
end
--==========================================================--
-- ��һ���������ݣ�ת��ΪMTJ�ķ��ż��ı��
--==========================================================--
function TableToMTJSymbolSet(src,dst)
	for k,v in pairs(src) do
		vtype = type(v)
		local k_str = k
		local v_str = v
		if v==true then
			v_str = "TRUE"
		elseif v==false then
			v_str = "FALSE"
		elseif v_type~="table" then
			v_str = tostring(v)
		end
		table.insert(dst,XMLItem("symbol",{name=tostring(k),value=tostring(v_str)}))
	end
end
--==========================================================--
-- LFSϵͳ��չ
--==========================================================--
_Mychdir=lfs.chdir
lfs.chdir = function (arg1,...)
--	ShowInfo(string.format("CWD:%s",arg1))
	_Mychdir(arg1,...)
end
function lfs.delfile(filename)
			os.execute(string.format("del /F /S /Q \"%s\" > NUL",filename))
end
function lfs.rmdir(dirname)
	os.execute(string.format("rmdir /S /Q %s > NUL",dirname))
end
function lfs.mkdir(dirname)
	os.execute(string.format("mkdir %s > NUL",dirname))
end
function lfs.xcopy(srcdir,dstdir)
	os.execute(string.format("xcopy /S /Y \"%s\"\\*.* \"%s\"\\ > NUL", srcdir,dstdir))

end
function lfs.copyfile(src,dstfolder)
	os.execute(string.format("copy /y \"%s\" \"%s\\\" > NUL",src,dstfolder))
end
function lfs.movefile(filename,targetdir)
			os.execute(string.format("move %s %s\\ > NUL",filename,targetdir))
end
--��ȡ��չ��
function lfs.getextension(filename)
	local retv = filename:match("(\.[^\.]+)$")
	if (retv==nil) then retv="" end
	return retv
end
function stripfilename(filename)
--	return string.match(filename, "(.*)/[^/]*%.%w+$") -�� *nix system
	return string.match(filename, "(.*)\\[^\\]*%.%w+$") --windows
end

--��ȡ�ļ���
function strippath(filename)
--	return string.match(filename, ".+/([^/]*%.%w+)$") --*nix system
	return string.match(filename, ".+\\([^\\]*%.%w+)$") --*windows system
end

--ȥ����չ��
function stripextension(filename)
	local idx = filename:match(".+()%.%w+$")
	if(idx) then
		return filename:sub(1, idx-1)
	else
		return filename
	end
end

function iconv( src ,src_encoding, target_encoding)
		tempFile = "___ICONV.TXT"
		tempFileOut = "___ICONV2.TXT"
		WriteString(tempFile,src)
		local lines = {}
		table.insert(lines,"@echo off")
		table.insert(lines,string.format("%s\\iconv\\iconv -f %s -t %s < %s > %s",
		ENV.TOOLS_PATH,src_encoding,target_encoding,tempFile,tempFileOut))
		table.insert(lines,"@echo on")
		local fname = "_iconv.bat"
		WriteTable(lines,fname,false)
		os.execute(string.format("call %s",fname))
		fin = io.open(tempFileOut,"r")
		result = fin:read("*a")
		fin:close()
		os.execute(string.format("del /q %s del /q %s del /q %s",tempFile,tempFileOut,fname))
		return result
end


--==========================================================--
-- OSϵͳ��չ
--==========================================================--
-- �滻��ϵͳ��execute,���Ӵ�ӡִ�е�ָ��
_MyExecute = os.execute
os.execute = function ( arg1,...)
	_MyExecute(arg1,...)
end
--==========================================================--
-- ϵͳ��չ
--==========================================================--
-- ·����ջ
local DirStack = {}
function pushd()
	table.insert(DirStack,lfs.currentdir())
end
function popd()
	local dir = DirStack[table.maxn(DirStack)]
	if (dir~=nil) then
		lfs.chdir(dir)
	end
end
function IsAllowed(ext,exttab)
	if exttab == nil or table.maxn(exttab)<=0 then
		return true
	else
		for _,v in pairs(exttab) do
			if string.upper(v)==string.upper(ext) then return true end
		end
		return false
	end
end
--------------------------------------------------------
--	��ָ��Ŀ¼�µ������ļ���ִ��һ������func,�ú����Ĳ���Ϊ (·����,Ŀ¼��,���Ӳ���)
function ForAllFile(dir,exttab,func,...)
	for k,_ in lfs.dir (dir) do
		if (k~="." and k~="..") then
			local fullname = dir.."\\"..k
			if (lfs.attributes(fullname).mode=="directory") then
				ForAllFile(fullname,exttab,func,...)
			else
				local ext = lfs.getextension(k)
				if IsAllowed(ext,exttab) then
					func(fullname,...)
				end
			end
		end
	end
end

--	��ָ��Ŀ¼�µ�����Ŀ¼��ִ��һ������func,�ú����Ĳ���Ϊ (·����,Ŀ¼��,���Ӳ���)
function ForAllDir(dir,func,...)
	for k,_ in lfs.dir (dir) do
		if (k~="." and k~="..") then
			local fullname = dir.."\\"..k
			if (lfs.attributes(fullname).mode=="directory") then
				ForAllDir(fullname,func,...)
				func(dir,k,...)
			end
		end
	end
end
-- ��һ��Table�����ݰ���CPP.exe����Ҫ�ĸ�ʽ
function TableToString(tab)
	local ret = ""
	if (tab~=nil)  then
		for k,v in pairs(tab) do
			local typev = type(v)
			if typev == "boolean" then
				if v == true then
					ret = ret .. " -D" .. k
				end
			elseif typev == "number" then
					ret = ret .. " -D" .. k .."="..v
			elseif typev == "string" then
					ret = ret .. " -D" .. k .."="..v
			end
		end
	end
	return ret
end

-- ��һ��Table������д��һ���ļ���
function WriteTable(tab,fname,append,sep,prefix)
--	ShowDebug("WriteTable",tab,fname,append)
	if sep ==nil then sep="=" end
	if prefix==nil then prefix="set " end
	local fout = nil
	if (append) then
		fout,msg = io.open(fname,"r+")
		fout:seek("end")
	else
		fout,msg = io.open(fname,"w")
	end
	for k,v in pairs(tab) do
		if k~=nil then
			if tonumber(k) then
				fout:write(v,"\n")
			else
					fout:write(prefix .. k .. sep .. tostring(v) .. "\n")
			end
		end
	end
	fout:flush()
	fout:close()
end
-- ��һ��String����д��һ���ļ���
function WriteString(fname,info)
	local fout = io.open(fname,"w")
	fout:write(info)
	fout:flush()
	fout:close()
end
-- ��ԴĿ¼(������Ŀ¼)�µ�����ָ����չ�����ļ���ȫ�����Ƶ�ָ��Ŀ¼
function srccopy(folderlist,exttab,targetdir)
	for _,v in pairs(folderlist) do
	ShowInfo(string.format("...Copying..."))
	ShowInfo(string.format("Src Folder:%s",v))
	ShowInfo(string.format("Dst Folder:%s",targetdir))
		ForAllFile(v,exttab,lfs.copyfile,targetdir)
	end
end



--==========================================================--
-- Javaϵͳ��չ
--==========================================================--
Java = {}
function Java.AddPackageName(path,packagename)
	ShowInfo("...AddPackageName..." .. path )
	if (packagename=="") then
		return
	end
	pushd()
	lfs.chdir(path)
	local foldername,_ = string.gsub(packagename,"(%.)","\\")
	os.execute(string.format("md %s",foldername))
	AddHead = [[
		@echo off
		dir /B *.java > #Res
		for /F %%%%i in (#Res) do (
			copy /y %%%%i %%%%i.bak
			echo %s > %%%%i
			type %%%%i.bak >> %%%%i
			del /q %%%%i.bak
   		)
   		del /q #Res
	]]
	WriteString("do.bat",string.format(AddHead,"package " .. packagename .. "\;"))
	os.execute("call do.bat")
	os.execute(string.format("md %s",foldername))
	os.execute(string.format("copy /y *.java .\\%s\\ > NUL",foldername))
	os.execute("del /q *.java")
	os.execute("del /q do.bat")
	popd()
end
function Java.expand(srcfile,dstfolder)
	pushd()
	lfs.chdir(dstfolder)
	os.execute(string.format("call \"%s\\bin\\jar\" -xf \"%s\"",
		ENV.JDK_PATH,srcfile))
	popd()
end
function Java.Cpp(srcfolder,exttab,dstfolder,def)
		local lines = {}
		table.insert(lines,"@echo off")
		table.insert(lines,string.format("for %%%%i in (*.java) do (\"%s\\cpp\\cpp.exe\" -C -P %s %%%%i \"%s\"\\%%%%i)",
		ENV.TOOLS_PATH,def,dstfolder))
		table.insert(lines,"@echo on")
		pushd()
		lfs.chdir(dstfolder)
		--lfs.mkdir(ENV.BLD_SRC_PREPROCESS.."\\com\\joyomobile\\app")
		local fname = "_cpp.bat"
		WriteTable(lines,fname,false)
		os.execute(string.format("call %s",fname))
		popd()
end
function Java.PackFile(jarname,src,shouldcompress,isUpdate,manifest)
	local cmd
	local mf = ""
	local mffile=""
	local compress="0"
	if (isUpdate) then
		cmd = "u"
	else
		cmd = "c"
		if (manifest~="" and manifest~=nil) then
			mf = "m"
			mffile=manifest
		end
	end

	if (shouldcompress==true) then
		compress = ""
	end
	local cmdline = string.format("call \"%s\\bin\\jar.exe\" %sf%s%s \"%s\" %s -C \"%s\" .",
		ENV.JDK_PATH,cmd,compress,mf,jarname,mffile,src)
--	ShowInfo(cmdline)
-- ��ע��jar��ָ���˳�򣬱��밴�ռȶ���˳��ָ������cf0m,����jar�����
	os.execute(cmdline)
end
-- �г����а�����java�ļ���Ŀ¼�����ص�һ������
function Java.MakeSrcDirTable(src)
	FolderList = {}
	ForAllFile(src,{".java"},function(srcfilename)
		local old_fld = stripfilename(srcfilename)
		fld = string.match(old_fld,"preprocess\\(.+)$")	-- Ԥ����׶�
		if (fld==nil) then
			fld= string.match(old_fld,"raw\\(.+)$")		-- ����׶�
		end
		if fld==nil then
			fld="."
		end
			if (FolderList[fld]==nil) then
				FolderList[fld] = 1
			end
	end
	)
	return FolderList
end
function Java.compile(src,dst)
	pushd()
	local COMPILER_OPTION="-g:none"
	lfs.chdir(src)
	-- �ҳ�������java�ļ���Ŀ¼������ƴ��javac������
	srcflders = Java.MakeSrcDirTable(src)
	srcfiles = ""
	-- ��������Դ������ļ��������ӳ�������
	for k,_ in pairs(srcflders) do
		srcfiles = srcfiles .. " " .. k .. "\\*.java"
	end
	-- ����
	local source = ""
	local target = ""
	if DEVICE.JAVA5 then
		source = "5"
		target = "5"
	else
		source = "1.3"
		target = "1.1"
	end
	local classpath = ENV.CLASSPATH
	local compile_str = string.format(
		"call %s\\bin\\javac.exe -encoding %s -target %s -source %s -classpath %s;%s;%s;%s\\ -bootclasspath %s %s -sourcepath %s\\ %s -d %s\\ ",
		ENV.JDK_PATH,"UTF-8",target,source,
			classpath,CHANNEL:LibPath(),ENV.BLD_PACK_JARFILE,dst,	-- classpath
			classpath,	-- boot class path
			COMPILER_OPTION,
			src,				-- sourcepath
			srcfiles,			-- source files
			dst)
	ShowInfo(compile_str)
	os.execute(compile_str)
	popd()
end

function Java.obfuscate(input,dstdir)
		Java.obfuscate(inputs,dstdir,nil)
end

function Java.obfuscate(input,dstdir,extern_opts)
		local proguard_option = {}
		for k,v in pairs(input) do
			table.insert(proguard_option,string.format("-injars \"%s\"",v))
		end
--[[ �Ѿ�ֱ������source��
		if (DEVICE.PLATFORM =="Android") then
			table.insert(proguard_option,string.format("-injars %s\\java_lib\\android_wrapper.jar",ENV.TOOLS_PATH))
		end
]]--
		if (DEVICE.PLATFORM =="Android") then
			table.insert(proguard_option,string.format("-libraryjars %s\\Android\\platforms\\%s\\android.jar",ENV.TOOLS_PATH,DEVICE.PLATFORM_VERSION_STR))
		else
			table.insert(proguard_option,string.format("-libraryjars %s",ENV.CLASSPATH))
		end
		libpath = CHANNEL:LibPath()
		if ((CHANNEL.OBFUSCAT_LIBS~="1") and libpath and libpath ~="") then
			table.insert(proguard_option,string.format("-injars %s",libpath))
		end
		table.insert(proguard_option,string.format("-outjars %s\\",dstdir))

		table.insert(proguard_option,string.format("-keep public class * extends javax.microedition.midlet.MIDlet"))
		table.insert(proguard_option,string.format("-printmapping %s\\mapping.txt",ENV.PP_SRC_RAW))
		table.insert(proguard_option,string.format("-useuniqueclassmembernames"))
		table.insert(proguard_option,string.format("-dontusemixedcaseclassnames"))	-- ���룬����preverify ����ͨ��
		table.insert(proguard_option,string.format("-keepdirectories"))

		table.insert(proguard_option,string.format("-ignorewarnings"))	--�������Ϊ��android

		if (DEVICE.PLATFORM ~="Android") then
			table.insert(proguard_option,"-flattenpackagehierarchy")
		end

		if (extern_opts~=nil) then
			for k,v in pairs(extern_opts) do
				table.insert(proguard_option,string.format("%s",v))
			end
		end

		-- ��proguard.pro�ļ��ϲ�
		local mycfg = ENV.PP_SRC_RAW .. "\\proguardTemplate.pro"
		lfs.copyfile(string.format("\"%s\\Proguard\\proguardTemplate.pro\"",ENV.TOOLS_PATH),ENV.PP_SRC_RAW)
		WriteTable(proguard_option,mycfg,true)
		-- ִ��proguard
		os.execute(string.format("call \"%s\\bin\\java.exe\" -jar \"%s\\Proguard\\v451\\lib\\proguard.jar\" @%s",
			ENV.JDK_PATH,
			ENV.TOOLS_PATH,
			mycfg
			))
end

function Java.preverify(srcdir,dstdir)

	if (DEVICE.PLATFORM =="Android") then
		lfs.xcopy(srcdir,dstdir)
	else
		ShowInfo("...preverify...\n")
		pushd()
		lfs.chdir(srcdir)
		local PREVERYFY_OPTION = ""
		if DEVICE.USE_CLDC11 then
			ShowInfo("preverify Using CLDC 1.1")
			PREVERYFY_OPTION = "-target CLDC1.1"
		else
			if DEVICE.USE_CLDC10 then
				ShowInfo("preverify Using CLDC 1.0")
				PREVERYFY_OPTION = "-target CLDC1.0"
			else
				ShowError("preverify No CLDC Specification\n")
			end
		end
				local classpath = ENV.CLASSPATH
		os.execute(string.format("call \"%s\\bin\\preverify\" %s -classpath %s;%s -d \"%s\" \"%s\"",
			ENV.WTK,PREVERYFY_OPTION,classpath,CHANNEL:LibPath(),dstdir,srcdir))
		popd()
	end
end


function XMLItem(labelname,params,info)

	local result = {xarg = params,label=labelname}
	if (info==nil) then
		result.empty=1
	else
		table.insert(result,info)
	end
	return result
end
function XMLAddSub(xml,item)
	table.insert(xml,item)
end


function CreateCLASSPATH ()
	local xml = {"<\?xml version=\"1.0\" encoding=\"UTF-8\"\?>"}
	local  classpath =XMLItem("classpath",{})
	-- ����"con"���͵�classpath
	if (DEVICE.PLATFORM =="Android") then
		table.insert(classpath,XMLItem("classpathentry",{path="com.android.ide.eclipse.adt.ANDROID_FRAMEWORK",kind="con"}))
	else
		table.insert(classpath,XMLItem("classpathentry",{kind="con",path="org.elipse.mtj.JavaMEContainer/J2ME Wireless Toolkit 2.2/DefaultColorPhone"}))
	end
	-- ����"output"����
	table.insert(classpath,XMLItem("classpathentry",{kind="output",path="bin"}))
	-- ����"lib"����
	for _,v in pairs(CHANNEL:Lib()) do
		table.insert(classpath,XMLItem("classpathentry",{kind="lib",path=v}))
	end
	-- ����Android���Դ����
	--[[
	if (DEVICE.PLATFORM =="Android") then
		table.insert(classpath,XMLItem("classpathentry",{path=ENV.JAVALIB.."\\android_wrapper.jar",kind="lib"}))
	end
]]--

	-- ����Դ��������
	table.insert(classpath,XMLItem("classpathentry",{excluding="**/.svn/**",kind="src",path= "pb_common_src",}))
	table.insert(classpath,XMLItem("classpathentry",{excluding="**/.svn/**",kind="src",path= "pb_special_src",}))
	table.insert(classpath,XMLItem("classpathentry",{excluding="**/.svn/**",kind="src",path= "_wrapper_common_src"}))
	table.insert(classpath,XMLItem("classpathentry",{excluding="**/.svn/**",kind="src",path= "_wrapper_special_src",}))
	table.insert(classpath,XMLItem("classpathentry",{excluding="**/.svn/**",kind="src",path= "_JYLib",}))
	table.insert(classpath,XMLItem("classpathentry",{excluding="**/.svn/**",kind="src",path= "pb_dataOut",}))
	table.insert(classpath,XMLItem("classpathentry",{excluding="**/.svn/**",kind="src",path= "pb_dataSrc",}))
	table.insert(classpath,XMLItem("classpathentry",{excluding="**/.svn/**",kind="src",path= "pp_dataOut",}))
	table.insert(classpath,XMLItem("classpathentry",{excluding="**/.svn/**",kind="src",path= "pp_dataSrc",}))
	-- ����Android���Դ����
	if (DEVICE.PLATFORM =="Android") then
		table.insert(classpath,XMLItem("classpathentry",{excluding="**/.svn/**",kind="src",path= "src",}))
		table.insert(classpath,XMLItem("classpathentry",{excluding="**/.svn/**",kind="src",path= "gen",}))
		table.insert(classpath,XMLItem("classpathentry",{excluding="**/.svn/**",kind="src",path= "j2me_wrapper_source",}))
		table.insert(classpath,XMLItem("classpathentry",{excluding="**/.svn/**",kind="src",path= "j2me_wrapper_data",}))
	end

	table.insert(xml,classpath)
	return xml
end
function CreateMTJ()
	ShowInfo("Creating MTJ")
	local xml = {"<\?xml version=\"1.0\" encoding=\"UTF-8\"\?>"}
	local mtjMetadata = XMLItem("mtjMetadata",{jad="N73.jad",version="1.2.1.v201006161022"})

	local mtjdevice = XMLItem("device",{group="J2ME Wireless Toolkit 2.2",name="DefaultColorPhone"})
	local signing = XMLItem("signing",{projectSpecific="false",signProject="false"})
	table.insert(signing,XMLItem("alias",{}))


	local device = XMLItem("device",{group="J2ME Wireless Toolkit 2.2",name="DefaultColorPhone"})

	local symbolSet = XMLItem("symbolSet",{name="DefaultColorPhone"})


			TableToMTJSymbolSet(DEVICE,symbolSet)
			TableToMTJSymbolSet(CHANNEL.PREPROCESS,symbolSet)
			local sp_device_define = CHANNEL.PREPROCESS.DEVICE[MAKE.DEVICE_MODEL]
			if sp_device_define then
				TableToMTJSymbolSet(sp_device_define,symbolSet)
			end

--	local workspaceSymbolSet = XMLItem("workspaceSymbolSet",{name="Memory3M"})
--	local workspaceSymbolSet = XMLItem("workspaceSymbolSet",{name=""})

	local configuration = XMLItem("configuration",{active="true",name="DefaultColorPhone"})
	table.insert(configuration,device)
	table.insert(configuration,symbolSet)
	table.insert(configuration,workspaceSymbolSet)

	local configurations = XMLItem("configurations",{})
	table.insert(configurations,configuration)

	table.insert(mtjMetadata,mtjdevice)
	table.insert(mtjMetadata,signing)
	table.insert(mtjMetadata,configurations)

	table.insert(xml,mtjMetadata)
	return xml
end
function CreateDebugSetting(prjname)
--[[
x:\Offical\Projects\J2MEFast\_Standard\trunk\workspace\.metadata\.plugins\org.eclipse.debug.core\.launches\KEmulator.launch
]]--
	Content =
[[
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<launchConfiguration type="org.eclipse.jdt.launching.localJavaApplication">
<listAttribute key="org.eclipse.debug.core.MAPPED_RESOURCE_PATHS">
<listEntry value="/BBX_6101_SuperCar"/>
</listAttribute>
<listAttribute key="org.eclipse.debug.core.MAPPED_RESOURCE_TYPES">
<listEntry value="4"/>
</listAttribute>
<listAttribute key="org.eclipse.jdt.launching.CLASSPATH">
<listEntry value="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#13;&#10;&lt;runtimeClasspathEntry containerPath=&quot;org.eclipse.jdt.launching.JRE_CONTAINER&quot; path=&quot;1&quot; type=&quot;4&quot;/&gt;&#13;&#10;"/>
<listEntry value="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#13;&#10;&lt;runtimeClasspathEntry externalArchive=&quot;C:/Java/KEmulator/KEmulator.jar&quot; path=&quot;3&quot; type=&quot;2&quot;/&gt;&#13;&#10;"/>
</listAttribute>
<booleanAttribute key="org.eclipse.jdt.launching.DEFAULT_CLASSPATH" value="false"/>
<stringAttribute key="org.eclipse.jdt.launching.MAIN_TYPE" value="emulator.Emulator"/>
<stringAttribute key="org.eclipse.jdt.launching.PROGRAM_ARGUMENTS" value="-cp &quot;bin&quot; -midlet &quot;cn.thirdgwin.app.GameMIDlet&quot;"/>
<stringAttribute key="org.eclipse.jdt.launching.PROJECT_ATTR" value="BBX_N73_CDSC_KTPD"/>
</launchConfiguration>

]]
	local xml = {[[<?xml version="1.0" encoding="UTF-8" standalone="no"?>]]}

	local launchConfiguration = XMLItem("launchConfiguration",{type="org.eclipse.jdt.launching.localJavaApplication"})

	local listAttribute = XMLItem("listAttribute",{key="org.eclipse.debug.core.MAPPED_RESOURCE_PATHS"})
		table.insert(listAttribute,XMLItem("listEntry",{value="/" ..prjname}))
	table.insert(launchConfiguration,listAttribute)

	local listAttribute = XMLItem("listAttribute",{key="org.eclipse.debug.core.MAPPED_RESOURCE_TYPES"})
		table.insert(listAttribute,XMLItem("listEntry",{value="4"}))
	table.insert(launchConfiguration,listAttribute)


	local listAttribute = XMLItem("listAttribute",{key="org.eclipse.jdt.launching.CLASSPATH"})
		table.insert(listAttribute,XMLItem("listEntry",{value="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#13;&#10;&lt;runtimeClasspathEntry externalArchive=&quot;"..ENV.KEMULATOR.."&quot; path=&quot;3&quot; type=&quot;2&quot;/&gt;&#13;&#10;"}))
		table.insert(listAttribute,XMLItem("listEntry",{value="&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;&#13;&#10;&lt;runtimeClasspathEntry id=&quot;org.eclipse.jdt.launching.classpathentry.defaultClasspath&quot;&gt;&#13;&#10;&lt;memento exportedEntriesOnly=&quot;false&quot; project=&quot;"..prjname.."&quot;/&gt;&#13;&#10;&lt;/runtimeClasspathEntry&gt;&#13;&#10;"}))
	table.insert(launchConfiguration,listAttribute)

	local booleanAttribute = XMLItem("booleanAttribute",{key="org.eclipse.jdt.launching.DEFAULT_CLASSPATH",value="false"})
	table.insert(launchConfiguration,booleanAttribute)

	local stringAttribute = XMLItem("stringAttribute",{key="org.eclipse.jdt.launching.MAIN_TYPE",value="emulator.Emulator"})
	table.insert(launchConfiguration,stringAttribute)

	local stringAttribute = XMLItem("stringAttribute",{key="org.eclipse.jdt.launching.PROGRAM_ARGUMENTS",value="-cp &quot;bin&quot; -midlet &quot;"..MAKE.PACKAGE_NAME..".GameMIDlet&quot; -device &quot;" .. DEVICE.BRAND .. " " .. DEVICE.MODEL .. "&quot;"})
	table.insert(launchConfiguration,stringAttribute)
	local stringAttribute = XMLItem("stringAttribute",{key="org.eclipse.jdt.launching.PROJECT_ATTR",value=prjname})
	table.insert(launchConfiguration,stringAttribute)

	table.insert(xml,launchConfiguration)

	return xml
end
function ImportToWorkspace(workspacepath,prjpath,theprjname)
	bpack=string.pack
	bunpack=string.unpack
	theprjname = "URI//file:/" .. prjpath
	theprjname = string.gsub (theprjname, "(\\)","/")
	local header = bpack("b17",0x40,0xB1,0x8B,0x81,0x23,0xBC,0x00,0x14,0x1A,0x25,0x96,0xE7,0xA3,0x93,0xBE,0x1E,0x00)
	local bodylen = bpack("b1",string.len(theprjname))
	local tail   = bpack("b20",0x00,0x00,0x00,0x00,0xC0,0x58,0xFB,0xF3,0x23,0xBC,0x00,0x14,0x1A,0x51,0xF3,0x8C,0x7B,0xBB,0x77,0xC6)
	fout,msg = io.open(workspacepath .."/.location","wb")
	fout:seek("end")
	fout:write(header)
	fout:write(bodylen)
	fout:write(theprjname)
	fout:write(tail)
	fout:flush()
	fout:close()
end


function CreateProject()
	local xml = {"<\?xml version=\"1.0\" encoding=\"UTF-8\"\?>"}

	local projectDescription = XMLItem("projectDescription",{})
	table.insert(projectDescription,XMLItem("name",{},string.format("%s_%s_%s",MAKE.CHANNEL_NAME,MAKE.DEVICE_MODEL,CHANNEL.PACK_NAME)))
	table.insert(projectDescription,XMLItem("comment",{},""))
	table.insert(projectDescription,XMLItem("projects",{},""))

		local buildSpec = XMLItem("buildSpec",{})
		local buildCommand

		buildCommand = XMLItem("buildCommand",{})
			table.insert(buildCommand,XMLItem("name",{},"org.eclipse.mtj.core.preprocessor"))
			table.insert(buildCommand,XMLItem("arguments",{}))
		table.insert(buildSpec,buildCommand)

	if(DEVICE.PLATFORM=="Android") then
		buildCommand = XMLItem("buildCommand",{})
			table.insert(buildCommand,XMLItem("name",{},"com.android.ide.eclipse.adt.ResourceManagerBuilder"))
			table.insert(buildCommand,XMLItem("arguments",{}))
		table.insert(buildSpec,buildCommand)

		buildCommand = XMLItem("buildCommand",{})
			table.insert(buildCommand,XMLItem("name",{},"com.android.ide.eclipse.adt.PreCompilerBuilder"))
			table.insert(buildCommand,XMLItem("arguments",{}))
		table.insert(buildSpec,buildCommand)
	end

		buildCommand = XMLItem("buildCommand",{})
			table.insert(buildCommand,XMLItem("name",{},"org.eclipse.jdt.core.javabuilder"))
			table.insert(buildCommand,XMLItem("arguments",{}))
		table.insert(buildSpec,buildCommand)

	if(DEVICE.PLATFORM=="Android") then
		buildCommand = XMLItem("buildCommand",{})
			table.insert(buildCommand,XMLItem("name",{},"com.android.ide.eclipse.adt.ApkBuilder"))
			table.insert(buildCommand,XMLItem("arguments",{}))
		table.insert(buildSpec,buildCommand)
	else
		-- ��Eclipse �У�������������Eclipse�в�����Ҫ
		if (false) then
			buildCommand = XMLItem("buildCommand",{})
				table.insert(buildCommand,XMLItem("name",{},"org.eclipse.mtj.core.preverifier"))
				table.insert(buildCommand,XMLItem("arguments",{}))
			table.insert(buildSpec,buildCommand)

			buildCommand = XMLItem("buildCommand",{})
				table.insert(buildCommand,XMLItem("name",{},"org.eclipse.mtj.core.packageBuilder"))
				table.insert(buildCommand,XMLItem("arguments",{}))
			table.insert(buildSpec,buildCommand)
		end
	end
	table.insert(projectDescription,buildSpec)

	local natures = XMLItem("natures",{})

	if(DEVICE.PLATFORM=="Android") then
		table.insert(natures,XMLItem("nature",{},"com.android.ide.eclipse.adt.AndroidNature"))
		table.insert(natures,XMLItem("nature",{},"org.eclipse.jdt.core.javanature"))
	else
		table.insert(natures,XMLItem("nature",{},"org.eclipse.jdt.core.javanature"))
	end
	table.insert(natures,XMLItem("nature",{},"org.eclipse.mtj.core.preprocessingNature"))
	table.insert(natures,XMLItem("nature",{},"org.eclipse.mtj.core.nature"))

	table.insert(projectDescription,natures)

---------------------

	local link
	local linkedResources = XMLItem("linkedResources",{})

	if(DEVICE.PLATFORM=="Android") then
		link = XMLItem("link",{})
			table.insert(link,XMLItem("name",{},"j2me_wrapper_source"))
			table.insert(link,XMLItem("type",{},"2"))
			table.insert(link,XMLItem("location",{},string.gsub (ENV.J2MEWRAPPER .. "\\master_src", "(\\)","/")))
		table.insert(linkedResources,link)

		link = XMLItem("link",{})
			table.insert(link,XMLItem("name",{},"j2me_wrapper_data"))
			table.insert(link,XMLItem("type",{},"2"))
			table.insert(link,XMLItem("location",{},string.gsub (ENV.J2MEWRAPPER .. "\\master_data", "(\\)","/")))
		table.insert(linkedResources,link)

	end

	link = XMLItem("link",{})
		table.insert(link,XMLItem("name",{},"pb_dataOut"))
		table.insert(link,XMLItem("type",{},"2"))
		table.insert(link,XMLItem("location",{},string.gsub (ENV.BLD_DATA_OUT, "(\\)","/")))
	table.insert(linkedResources,link)

	link = XMLItem("link",{})
		table.insert(link,XMLItem("name",{},"pb_dataSrc"))
		table.insert(link,XMLItem("type",{},"2"))
		table.insert(link,XMLItem("location",{},string.gsub (ENV.BLD_DATA_H, "(\\)","/")))
	table.insert(linkedResources,link)

	link = XMLItem("link",{})
		table.insert(link,XMLItem("name",{},"pp_dataOut"))
		table.insert(link,XMLItem("type",{},"2"))
		table.insert(link,XMLItem("location",{},string.gsub (ENV.PP_DATA_OUT, "(\\)","/")))
	table.insert(linkedResources,link)

	link = XMLItem("link",{})
		table.insert(link,XMLItem("name",{},"pp_dataSrc"))
		table.insert(link,XMLItem("type",{},"2"))
		table.insert(link,XMLItem("location",{},string.gsub (ENV.PP_DATA_H, "(\\)","/")))
	table.insert(linkedResources,link)

	link = XMLItem("link",{})
		table.insert(link,XMLItem("name",{},"pb_common_src"))
		table.insert(link,XMLItem("type",{},"2"))
		table.insert(link,XMLItem("location",{},string.gsub (ENV.COMMON_SRC, "(\\)","/")))
	table.insert(linkedResources,link)

	link = XMLItem("link",{})
		table.insert(link,XMLItem("name",{},"pb_special_src"))
		table.insert(link,XMLItem("type",{},"2"))
		table.insert(link,XMLItem("location",{},string.gsub (ENV.SPECIFIC_SRC, "(\\)","/")))
	table.insert(linkedResources,link)

	link = XMLItem("link",{})
		table.insert(link,XMLItem("name",{},"_wrapper_common_src"))
		table.insert(link,XMLItem("type",{},"2"))
		table.insert(link,XMLItem("location",{},string.gsub (ENV.JYWRAPPER_COMMON_SRC, "(\\)","/")))
	table.insert(linkedResources,link)

	link = XMLItem("link",{})
		table.insert(link,XMLItem("name",{},"_wrapper_special_src"))
		table.insert(link,XMLItem("type",{},"2"))
		table.insert(link,XMLItem("location",{},string.gsub (ENV.JYWRAPPER_SPECIFIC_SRC, "(\\)","/")))
	table.insert(linkedResources,link)

	link = XMLItem("link",{})
		table.insert(link,XMLItem("name",{},"_JYLib"))
		table.insert(link,XMLItem("type",{},"2"))
		table.insert(link,XMLItem("location",{},string.gsub (ENV.JYLIB_SRC, "(\\)","/")))
	table.insert(linkedResources,link)
	table.insert(projectDescription,linkedResources)

	table.insert(xml,projectDescription)
	return xml
end
function CreateAndroidProject()
	local xml = {"<\?xml version=\"1.0\" encoding=\"UTF-8\"\?>"}
	local projectDescription = XMLItem("projectDescription",{})
	table.insert(projectDescription,XMLItem("name",{},string.format("%s",MAKE.CHANNEL_NAME)))
	table.insert(projectDescription,XMLItem("comment",{},""))
	table.insert(projectDescription,XMLItem("projects",{},""))

	local buildSpec = XMLItem("buildSpec",{})
	local buildCommand

	buildCommand = XMLItem("buildCommand",{})
	table.insert(buildCommand,XMLItem("name",{},"com.android.ide.eclipse.adt.ResourceManagerBuilder"))
	table.insert(buildCommand,XMLItem("arguments",{}))
	table.insert(buildSpec,buildCommand)

	buildCommand = XMLItem("buildCommand",{})
	table.insert(buildCommand,XMLItem("name",{},"com.android.ide.eclipse.adt.PreCompilerBuilder"))
	table.insert(buildCommand,XMLItem("arguments",{}))
	table.insert(buildSpec,buildCommand)

	buildCommand = XMLItem("buildCommand",{})
	table.insert(buildCommand,XMLItem("name",{},"org.eclipse.jdt.core.javabuilder"))
	table.insert(buildCommand,XMLItem("arguments",{}))
	table.insert(buildSpec,buildCommand)

	buildCommand = XMLItem("buildCommand",{})
	table.insert(buildCommand,XMLItem("name",{},"com.android.ide.eclipse.adt.ApkBuilder"))
	table.insert(buildCommand,XMLItem("arguments",{}))
	table.insert(buildSpec,buildCommand)
	table.insert(projectDescription,buildSpec)

	local natures = XMLItem("natures",{})
	table.insert(natures,XMLItem("nature",{},"com.android.ide.eclipse.adt.AndroidNature"))
	table.insert(natures,XMLItem("nature",{},"org.eclipse.jdt.core.javanature"))
	table.insert(projectDescription,natures)

	table.insert(xml,projectDescription)
	return xml
end
function CreateAndroidManifest(folder)
-- portrait landscape
	text = string.format([[
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="%s.%s"
    android:versionName="1.0.0"
    android:versionCode="100">
    <uses-sdk android:minSdkVersion="%d" />
    <application android:icon="@drawable/icon" android:label="@string/app_name">
        <activity android:name=".JoinGame"
			android:label="@string/MIDlet_Name"
			android:screenOrientation="%s"
			android:configChanges="orientation|keyboard|keyboardHidden|navigation"
		>
		<intent-filter>
                	<action android:name="android.intent.action.MAIN" />
	                <category android:name="android.intent.category.LAUNCHER" />
       	</intent-filter>
        </activity>
    </application>
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission xmlns:android="http://schemas.android.com/apk/res/android" android:name="android.permission.FULLSCREEN"/>
</manifest>
		]],
		MAKE.PACKAGE_NAME,
		CHANNEL.PACK_NAME,
		DEVICE.PLATFORM_VERSION,
		DEVICE.SCREEN_ORIENTATION
		)
		text = iconv(text,"GBK","UTF-8")
	WriteString(string.format("%s\\AndroidManifest.xml",folder),text)
end
function CreateAndroidProperties(folder)
	WriteString(string.format("%s\\default.properties",folder),string.format("target=%s\nproguard.config=proguard.cfg",DEVICE.PLATFORM_VERSION_STR))
-- ���proguard ���ã�������Android 2.3���ϵ���Android����
-- Android ��IDE�б���ʹ�������ʽ�����л�����
-- �������еĻ�����ʽ��δ���
	proguardcfg = [[
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
]]
	WriteString(string.format("%s\\proguard.cfg",folder),proguardcfg)

end
function CreateAndroidDirectory(folder)

	CreateAndroidManifest(folder)
	CreateAndroidProperties(folder)
	--��������Ŀ¼
	lfs.mkdir(string.format("%s\\src\\cn\\thirdgwin\\app\\%s", folder, CHANNEL.PACK_NAME))
	-- дJoinGame.java
	JoinGame =
	[[
package cn.thirdgwin.app.%s;

import cn.thirdgwin.app.*;
public class JoinGame extends GameMIDlet {
	public JoinGame() {
		super();
	}
	public void startApp()
	{
		try{
			super.startApp();
		}catch(Exception e){}
	}

	public void destroyApp(boolean arg0) {
		try{
			super.destroyApp(arg0);
		}
		catch (Exception E){}
	}
	public void pauseApp()
	{
		try{
			super.pauseApp();
		}
		catch (Exception E){}
	}

	public void PauseGame()
	{
		try{
//			super.PauseGame();
		}
		catch (Exception E){}
	}

	public void StartGame() {
		try{
//			super.StartGame();
		}
		catch (Exception E){}
	}
}
	]]
	JoinGame = string.format(JoinGame, CHANNEL.PACK_NAME)
	JoinGame = iconv(JoinGame,"GBK","UTF-8")
	WriteString(string.format(folder .. "\\src\\cn\\thirdgwin\\app\\%s\\JoinGame.java", CHANNEL.PACK_NAME),JoinGame)
	--������ԴĿ¼
	lfs.mkdir(string.format("%s\\assets", folder))
	--����genĿ¼
	lfs.mkdir(string.format("%s\\gen", folder))
	--����resĿ¼
	lfs.mkdir(string.format("%s\\res\\drawable", folder))
	lfs.mkdir(string.format("%s\\res\\layout", folder))
	lfs.mkdir(string.format("%s\\res\\values", folder))
	lfs.mkdir(string.format("%s\\bin", folder))

	--lfs.copyfile(string.format("%s\\icon.png",ENV.BLD_DATA_OUT_AND), ENV.WORKSPACE.."\\"..MAKE.CHANNEL_NAME.."\\"..MAKE.DEVICE_BRAND.."_"..MAKE.DEVICE_MODEL.."\\res\\drawable")
	ShowInfo(string.format("����icon.png��Դ��Ŀ¼��%s",folder.."\\res\\drawable"))
	lfs.copyfile(string.format("%s\\icon.png",ENV.BLD_DATA_OUT_AND), folder.."\\res\\drawable")
	ShowInfo(string.format("����menu������Դ��Ŀ¼��%s",folder.."\\res\\drawable"))
	--os.execute("call pause")
	--����menuר���ļ��е�ָ��λ��
	lfs.copyfile(ENV.BLD_DATA_OUT_AND_MENURES, folder.."\\res\\drawable")
	lfs.copyfile(ENV.BLD_DATA_OUT_AND_SOUNDRES, folder.."\\assets")
	--os.execute("call pause")
	--ɾ��ר���ļ���
	lfs.rmdir(ENV.BLD_DATA_OUT_AND_MENURES)
	-- дmain.xml
	mainxml =
	[[
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    >
<TextView
    android:layout_width="fill_parent"
    android:layout_height="wrap_content"
    />
</LinearLayout>
	]]
	mainxml = iconv(mainxml,"GBK","UTF-8")
	WriteString(folder .. "\\res\\layout\\main.xml",mainxml)
	-- дstrings.xml
	mainxml = tabletoxml(CreateAndroidStrings())
	mainxml = iconv(mainxml,"GBK","UTF-8")
	mainxml = string.format(mainxml, CHANNEL.GAME_NAME, CHANNEL.GAME_NAME)
	WriteString(folder .. "\\res\\values\\strings.xml",mainxml)
end
function CreateAndroidStrings()
	local xml = {"<\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"\?>"}
	local classpath =XMLItem("resources",{})
	table.insert(classpath,XMLItem("string",{name="MIDlet_Name"},{"%s"}))
	table.insert(classpath,XMLItem("string",{name="app_name"},{"%s"}))
	table.insert(xml,classpath)
	table.insert(xml,resources)
	return xml
end
