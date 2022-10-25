#!/bin/bash

if [[ -z "$1" || -z "$2" || -z "$3" || -z "$4" ]]
then
  echo "no udid, development team, provisioning profile or bundle id was given"
else
  udid=$1
  dev_team=$2
  bundle_id=$3
  provision_profile=$4
  proxy_port=$5

  # Start WDA Server
  xcodebuild clean build -project $HOME/mobile/WebDriverAgent/WebDriverAgent.xcodeproj -target WebDriverAgentLib -destination id=$udid -configuration Debug DEVELOPMENT_TEAM=$dev_team IPHONEOS_DEPLOYMENT_TARGET=11.4 CODE_SIGN_STYLE=Manual > $HOME/mobile/WebDriverAgent/build.log 2>&1 &
  wait
  xcodebuild clean build -project $HOME/mobile/WebDriverAgent/WebDriverAgent.xcodeproj -target WebDriverAgentRunner -destination id=$udid -configuration Debug PROVISIONING_PROFILE=$provision_profile DEVELOPMENT_TEAM=$dev_team PRODUCT_BUNDLE_IDENTIFIER=$bundle_id IPHONEOS_DEPLOYMENT_TARGET=11.4 CODE_SIGN_STYLE=Manual >> $HOME/mobile/WebDriverAgent/build.log 2>&1 &
  wait
  xcodebuild test -project $HOME/mobile/WebDriverAgent/WebDriverAgent.xcodeproj -scheme WebDriverAgentRunner -destination id=$udid -configuration Debug DEVELOPMENT_TEAM=$dev_team CODE_SIGN_STYLE=Manual PROVISIONING_PROFILE=$provision_profile >> $HOME/mobile/WebDriverAgent/build.log 2>&1 &

  # Start iproxy
  iproxy $proxy_port $proxy_port > $HOME/mobile/WebDriverAgent/iproxy.log 2>&1 &
fi
