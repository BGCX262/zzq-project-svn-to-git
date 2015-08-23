for %%i in (ZHANGQU_*.bat) do (
pushd %CD%
	call %%i
popd
	)
	