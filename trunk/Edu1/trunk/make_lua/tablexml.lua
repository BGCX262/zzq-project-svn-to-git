-- Table 和 XML 相互转换
-- Table 的[1] 是XML的标题
-- 其他元素是 {lable="",{K=v }} 格式
function tabletoxml(x)
    -- list of selfclosing tags,,eg  <br /> and is correct for xhtml,html is different
    local selfclosing = {meta=true, input=true, img=true, br=true, hr=true}
    local out=""

    -- bracflag is to do with writing more human readable xhtml by writing newlines

    if x.label then
        if bracflag==true then out=out.."\n" bracflag=false end
        out=out.."<".. x.label  -- open tag such as <div
        -- any parameters inside the tag head are in an xarg list
        -- such as <div id="fred" >
    end
    if x.xarg then
        for r,t in pairs(x.xarg) do -- do xarg tags and maybe close tag
            out=out.." ".. r.."=\"".. t .. "\""
            -- eg. style="mystyle"
        end
        if selfclosing[x.label] then
            out=out.." /" -- self closing such as  />, note space first for bugged browsers
        end
        out=out..">"
        bracflag=true
    end
    for v,w in pairs(x) do
        if type(w) == "table" then
            if v~="xarg" then -- skip xarg, already done first
                retout=tabletoxml(w) -- is table, recurse
                out=out..retout
            end
        else
            if v ~="label" and v ~= "empty" then
                bracflag=false
                out=out..w -- anything else, this does the legwork for plain output
            end
        end
    end
    -- close named tag. note the following is after return from recursing deeper levels
    -- so that nesting is correct
    -- x.label and any x.xarg are still valid here for this level
    -- at this point all information about a tag/label are known, we are at end of item
    if x.label and (true~= selfclosing[x.label]) then
        if bracflag==true then out=out.."\n" bracflag=false end
        out=out.."</" .. x.label .. ">"  -- such as </div>
        bracflag=true
    end
    -- return from recursion or final return
    return out  -- create a long string with newlines which is xhtml page, probably W3C legal
end

--==========================================
--now the matching xml to table routine from the Lua archive
--====== xml 2 table =========
function parseargs(s)
  local arg = {}
  string.gsub(s, "([%-%w]+)=([\"'])(.-)%2", function (w, _, a)
    arg[w] = a
  end)
  return arg
end

function xmltotable(s)
  local stack = {}
  local top = {}
  table.insert(stack, top)
  local ni,c,label,xarg, empty
  local i, j = 1, 1
  while true do
    ni,j,c,label,xarg, empty = string.find(s,
"<(%/?)([%w%:_%-]+)(.-)(%/?)>", i)
    if not ni then break end
    local text = string.sub(s, i, ni-1)
    if not string.find(text, "^%s*$") then
      table.insert(top, text)
    end
    if empty == "/" then  -- empty element tag
      table.insert(top, {label=label, xarg=parseargs(xarg), empty=1})
    elseif c == "" then   -- start tag
       top = {label=label, xarg=parseargs(xarg)}
      table.insert(stack, top)   -- new level
    else  -- end tag
      local toclose = table.remove(stack)  -- remove top
      top = stack[#stack]
      if #stack < 1 then
        error("nothing to close with "..label)
      end
      if toclose.label ~= label then
        error("trying to close "..toclose.label.." with "..label)
      end
      table.insert(top, toclose)
    end
    i = j+1
  end
  local text = string.sub(s, i)
  if not string.find(text, "^%s*$") then
    table.insert(stack[stack.n], text)
  end
  if #stack > 1 then
    error("unclosed "..stack[stack.n].label)
  end
  return stack[1]
end
