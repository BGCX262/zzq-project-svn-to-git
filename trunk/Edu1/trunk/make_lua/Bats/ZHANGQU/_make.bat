@echo off
pushd
cd ..
cd ..
make.lua encodedata_off pb_clean pb_data pp_clean pp_data pb_build  pp_build workspace %1 %2 %3
popd
pause