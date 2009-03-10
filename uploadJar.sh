#!/bin/bash
# Author: Jeff Prouty
#
# Builds latest donut.jar then
# Uploads donut.jar into your home dir in attu (which is your home dir on all linux lab machines)
#

attu="attu.cs.washington.edu"
jarFile="donut.jar"

function printUsage () {
	printf "Usage: %s: -u username\n" $(basename $0) >&2
	exit 2
}

uflag=
while getopts 'u:' OPTION
do
	case $OPTION in
	u)  uflag=1
		username="$OPTARG"
		;;
	?)	printUsage
		;;
	esac
done
shift $(($OPTIND - 1))

# Both username and ports must be specified
if [[ !($uflag) ]]; then
	printUsage
fi

ant create_jar
scp ${jarFile} ${username}@${attu}: