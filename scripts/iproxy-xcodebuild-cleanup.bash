#!/bin/bash

iproxy_pids=$(pgrep iproxy)
xcodebuild_pids=$(pgrep xcodebuild)

[ -x $iproxy_pids ] && echo "iproxy is not running" || pkill -9 iproxy
[ -x $xcodebuild_pids ] && echo "xcodebuild is not running" ||  pkill -9 xcodebuild
