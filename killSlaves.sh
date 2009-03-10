#!/bin/bash
# Author: Jeff Prouty
#
# Ensure public key authentication with attu is setup
# Warning: THIS WILL KILL ALL EXISTING JAVA PROCESSES BEING RUN ON THE HOSTS UNDER YOUR USER NAME
#

cseHost=".cs.washington.edu"
jarFile="donut.jar"

function printUsage () {
	printf "Usage: %s: -u username <hosts>+\n" $(basename $0) >&2
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

# username must be specified
if [[ !($uflag) ]]; then
	printUsage
fi

# There must be atleast 1 host to kill!
if (( $# < 1 )); then
	printUsage
fi

for shortName in ${*}; do
	host="${username}@${shortName}${cseHost}"
	echo "Killing all java processes: ${host}"
	ssh ${host} "killall java"
done