#!/bin/bash
# Author: Jeff Prouty
#
# Ensure public key authentication with attu is setup
#

cseHost=".cs.washington.edu"
jarFile="donut.jar"

function printUsage () {
	printf "Usage: %s: -u username -p \"<ports >+\" [-H knownHost -P knownPort] <hosts>+\n" $(basename $0) >&2
	exit 2
}

uflag=
pflag=
Hflag=
Pflag=
while getopts 'u:p:H:P:' OPTION
do
	case $OPTION in
	u)  uflag=1
		username="$OPTARG"
		;;
	p)	pflag=1
		ports="$OPTARG"
		;;
	H)	Hflag=1
		knownHost="$OPTARG"
		;;
	P)	Pflag=1
		knownPort="$OPTARG"
		;;
	?)	printUsage
		;;
	esac
done
shift $(($OPTIND - 1))

# Both username and ports must be specified
if [[ !($uflag && $pflag) ]]; then
	printUsage
fi

# Either the knownHost and knownPort should both be set, or both unset
if [[ $Hflag != $Pflag ]]; then
	printUsage
fi

# There must be atleast 1 host to connect to!
if (( $# < 1 )); then
	printUsage
fi

for shortName in $*; do
	host="${shortName}${cseHost}"
	unset hostCommands
	for port in $ports; do	
		if [ -z "$knownHost" ]; then
			knownHost=$host
			knownPort=$port
			
			echo "Starting the first donut server: ${username}@${host}:${port}"
			
		 	hostCommands+="(java -jar ${jarFile} --port ${port} &) && "
		else
			echo "Joining: ${username}@${host}:${port}"
			
			hostCommands+="(java -jar ${jarFile} --port ${port} --known-host ${knownHost} --known-port ${knownPort} &) && "
		fi
	done
	
	ssh -o ConnectTimeout=2 -f "${username}@${host}" "${hostCommands[@]}true"
done