#!/bin/bash

# Please run from project level

SCRIPTPATH="$( cd "$(dirname "$0")" ; pwd -P )"

p12_name=""
provision_name=""
prj_dir=$(pwd)

echo -e "\033[31mFor Asia Pacific, please enter number 3 if your device is added after 12/09/2020. Otherwise, please enter 2:\033[32m \033[0m"
PS3='Please select your location number: '
options=("London" "AsiaPacific" "bak_AsiaPacific" "mAuthDev")
select opt in "${options[@]}"
do
    case $opt in
        "London")
            p12_name="MobileX.p12"
            provision_name="Development_uk.co.hsbc.enterprise.hsbcukmobilebanking.mobileprovision"
            break
            ;;
        "AsiaPacific")
            p12_name="hsbc-mobile-dev-us.p12"
            provision_name="Mobilebanking_Allcapabilities.mobileprovision"
            break
            ;;
        "bak_AsiaPacific")
            p12_name="hsbc-mobile-dev-us.p12"
            provision_name="bakMobilebanking_Allcapabilities.mobileprovision"
            break
            ;;
        "mAuthDev")
            p12_name="secure-access-mauth.p12"
            provision_name="MAuthDev.mobileprovision"
            break
            ;;
        *) echo "invalid option $REPLY";;
    esac
done

WDA_LOCAL_PATH=~/mobile/WebDriverAgent
git clone https://github.com/appium/WebDriverAgent.git $WDA_LOCAL_PATH
cd $WDA_LOCAL_PATH
mkdir -p Resources/WebDriverAgent.bundle

cd $SCRIPTPATH

security unlock-keychain login.keychain-db
security import $p12_name -k login.keychain-db

open $provision_name
openssl smime -inform der -verify -noverify -in $provision_name > tmp.plist
/usr/libexec/PlistBuddy -c 'Print :UUID' tmp.plist > ../src/main/resources/provisioning.txt
/usr/libexec/PlistBuddy -c 'Print :Entitlements:com.apple.developer.team-identifier' tmp.plist >> ../src/main/resources/provisioning.txt
app_identifier="$(/usr/libexec/PlistBuddy -c 'Print :Entitlements:application-identifier' tmp.plist)"
echo ${app_identifier: 11 } >> ../src/main/resources/provisioning.txt
rm tmp.plist

# Modify WDA Runner Xcode project with HSBC bundle id, Code Signing style, Dev Team and Provisioning profile
cd ~/mobile/WebDriverAgent
emptyData='""'
provisionId=$(sed -n 1p "${prj_dir}/src/main/resources/provisioning.txt")
devTeamId=$(sed -n 2p "${prj_dir}/src/main/resources/provisioning.txt")
comBundleId=$(sed -n 3p "${prj_dir}/src/main/resources/provisioning.txt")
projectProperties="${emptyData}; CODE_SIGN_STYLE = Manual; DEVELOPMENT_TEAM = ${devTeamId}; PROVISIONING_PROFILE_SPECIFIER = ${provisionId}"

# Replacing multiple project build setting with default WDA Bundle-Id
sed "s!com.facebook.WebDriverAgentRunner!${projectProperties}!" WebDriverAgent.xcodeproj/project.pbxproj > temp.pbxproj
cat temp.pbxproj > WebDriverAgent.xcodeproj/project.pbxproj
rm temp.pbxproj

# Updating CFBundleIdentifier in WebdriverAgentRunner - info.plist
plutil -replace CFBundleIdentifier -string ${comBundleId} ./WebdriverAgentRunner/info.plist