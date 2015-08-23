for %%i in (BBX_*.bat) do (
pushd %CD%
	call %%i
popd
	)
	