#!/bin/bash

iproxy_pid=$(pgrep iproxy)
xcodebuild_pid=$(pgrep xcodebuild)

[ -z $iproxy_pid ] && echo "iproxy is not running" || kill -9 $iproxy_pid
[ -z $xcodebuild_pid ] && echo "xcodebuild is not running" || kill -9 $xcodebuild_pid

if [[ -z "$1" ]]
then
  echo "No Device udid is provided for the targeted build"
else
  udid=$1

  # Start WDA Server
  xcodebuild clean build -project $HOME/mobile/WebDriverAgent/WebDriverAgent.xcodeproj -target WebDriverAgentLib -destination id=$udid -configuration Debug IPHONEOS_DEPLOYMENT_TARGET=11.4 >> $HOME/mobile/WebDriverAgent/build.log 2>&1 &
  wait
  xcodebuild clean build -project $HOME/mobile/WebDriverAgent/WebDriverAgent.xcodeproj -target WebDriverAgentRunner -destination id=$udid -configuration Debug IPHONEOS_DEPLOYMENT_TARGET=11.4 >> $HOME/mobile/WebDriverAgent/build.log 2>&1 &
  wait
  xcodebuild test -project $HOME/mobile/WebDriverAgent/WebDriverAgent.xcodeproj -scheme WebDriverAgentRunner -destination id=$udid -configuration Debug >> $HOME/mobile/WebDriverAgent/build.log 2>&1 &

  # Start iproxy
  iproxy 8100 8100 > $HOME/mobile/WebDriverAgent/iproxy.log 2>&1 &
fi
