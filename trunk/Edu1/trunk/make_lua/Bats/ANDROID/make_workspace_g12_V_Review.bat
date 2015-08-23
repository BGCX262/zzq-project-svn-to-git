@echo off
pushd
cd ..
cd ..
set REVIEW_VERSION=TRUE
make.lua review encodedata_off pb_clean pb_data workspace SC HTC G12_V_Review
popd
pause