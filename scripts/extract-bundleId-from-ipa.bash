#!/bin/bash

# Comment out all prompts that say #COMMENT to only retrieve value

# Variables
# echo "setting variables and printing..."  #COMMENT
dirWork=$(pwd)/bundle-to-test/ios
actualFile=$1
extension=.$(basename $actualFile | cut -d '.' -f2)
filename=$(basename -a -s $extension $actualFile)
zipFile=$dirWork/$filename.zip
payloadDir=$dirWork/Payload

# Convert IPA/APP to ZIP
if [[ $extension = '.ipa' ]]
then
  cp $actualFile $zipFile
  # Unzip file to Payload directory
  # echo "testing zip archive.." #COMMENT
  cd $dirWork
  unzip -t $zipFile | grep silent
  # echo "unzipping archive.." #COMMENT
  unzip -o $zipFile | grep silent
  cd Payload
  cd $(eval ls) # This should only return the app directory
elif [[ $extension = '.app' ]]
then
  cd $actualFile
fi

# read contents of plist and only return value of bundleId
export CREATEDBUNDLEID=$(/usr/libexec/PlistBuddy -c "Print :CFBundleIdentifier" Info.plist) # Prints value of key and stores in variable
echo $CREATEDBUNDLEID

# cleanup
if [[ $extension = '.ipa' ]]
then
  rm -r $dirWork/Payload
  rm -r $zipFile
fi
