@echo off
pushd
cd ..
cd ..
set REVIEW_VERSION=TRUE
make.lua review encodedata_off pb_clean pb_data pb_build pp_clean pp_data pp_build workspace %1 %2 %3
popd
pause