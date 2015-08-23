@echo off
pushd
cd ..
cd ..
set REVIEW_VERSION=TRUE
make.lua review encodedata_off pb_clean pb_data workspace BBX HTC G7_Review
popd
pause