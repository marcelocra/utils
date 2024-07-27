# vim:fdm=marker:fmr={{{,}}}:fdl=0:fen
#
# This is an interesting posix script from the Mojo folks at Modular.
#
# For my observations and learnings, search for the string 'Notes {' in folded
# sections (if you use (neo)vim).
#
# I downloaded it on 26jul24/21h05, directly from the provided install command
# from their getting started page, which curl from the following url:
#
#   https://get.modular.com/
#
# Yet, the script is either incorrect or the shell does something very weird at
# the end: they use a Python-like __main__ thingy, but the function doesn't
# close. o.O
#
# I'll take another look some other day, in case they simply uploaded the wrong
# file.

echo 'DO NOT RUN THIS SCRIPT, IT IS ONLY MEANT AS A REFERENCE'
exit 1

#!/bin/sh
##===----------------------------------------------------------------------===##
# # This file is Modular Inc proprietary. #
##===----------------------------------------------------------------------===##

set -eu

maybe_sudo() {
	if [ "$(whoami)" = "root" ]; then
		"$@"
	elif type sudo >/dev/null; then
		sudo -E "$@"
	else
		echo "Sorry, either root or 'sudo' is required."
		return 1
	fi
}

remote_script() {
	# Prefer curl over wget, but wget is available by default. We shouldn't need
	# any fallback path where packages are installed. Note that for older shells
	# we do not have pipefail available, therefore ensure that on failure we pipe
	# in a failure that will result in failure of the function.

    # Notes {{{
    #
    # In the next conditional, they download a shell script to pipe id to the
    # bash command. The interesting bit is that if the download fails, they pipe
    # `echo "exit 1"` instead!
    #
    # I didn't know those curl and wget options, so from their man pages:
    #
    # }}}

	if type curl >/dev/null; then
		(curl -1sLf "$1" || echo "exit 1") | maybe_sudo bash
	elif type wget >/dev/null; then
		(wget -q -O - "$1" || echo "exit 1") | maybe_sudo bash
	else
		echo "Sorry, one of 'curl' or 'wget' is required."
		return 1
	fi
}

# The main function of the script. Wrap in a function so an imcomplete download
# will not result in a partially executed script.

__main__() {
	# If the user is using WSL1, tell them that they should use WSL2 instead.
	# "Microsoft" in the uname is captialized on WSL1 and lowercase on WSL2. This is
	# a semi-reliable way to detect WSL1.

	if [ "$(uname -r | grep -c Microsoft)" -eq 1 ]; then
		echo "It looks like you are using WSL1 which is unsupported by Modular."
		echo "We recommend switching your Linux distribution to run with WSL2 instead."
		echo "See https://learn.microsoft.com/en-us/windows/wsl/install#upgrade-version-from-wsl-1-to-wsl-2 to learn how to switch to WSL2."
		exit 1
	fi

# Reliably detecting whether a system is Deb-based or RPM-based can be tricky,
# as both tools can be installed on a system. Complex cases may need to fall
# back to manual setup, however it should be the case Debian-deriviatives have
# /etc/debian_version, while this file should not be present in
# non-Debian-based systems.

if [ "$(uname)" = "Linux" ] && [ -f /etc/debian_version ]; then
	remote_script "https://dl.modular.com/${URL_SLUG:-public/installer}/setup.deb.sh"
	maybe_sudo apt install -yq --reinstall modular
elif [ "$(uname)" = "Linux" ] && type rpm >/dev/null && type dnf >/dev/null; then
	remote_script "https://dl.modular.com/${URL_SLUG:-public/installer}/setup.rpm.sh"
	maybe_sudo dnf -yq install modular || \
		(maybe_sudo dnf -q list installed modular >/dev/null 2>&1 \
		&& maybe_sudo dnf -yq reinstall modular)
elif [ "$(uname)" = "Linux" ] && type rpm >/dev/null && type yum >/dev/null; then
	remote_script "https://dl.modular.com/${URL_SLUG:-public/installer}/setup.rpm.sh"
	maybe_sudo yum -yq install modular || \
		(maybe_sudo yum -q list installed modular >/dev/null 2>&1 \
		&& maybe_sudo yum -yq reinstall modular)
elif [ "$(uname)" = "Darwin" ]; then
	UNAME_MACHINE="$(/usr/bin/uname -m)"
	if [ "${UNAME_MACHINE}" = "arm64" ]; then
		if [ -z "$(command -v brew)" ]; then
			echo "Homebrew not detected. Please install homebrew from https://brew.sh/"
			exit 1
		else
			echo "Updating Homebrew" brew update echo "Installing/upgrading Modular using Homebrew"
			if brew ls --versions modular >/dev/null; then
				# if modular is already installed, lets upgrade it.
				brew upgrade modularml/packages/modular
			else
				brew install modularml/packages/modular
			fi
		fi
	else
		echo "Sorry, modular is not currently supported on Intel based Macs. Please visit https://www.modular.com/ to learn about supported platforms."
		exit 1
	fi
else
	echo "Sorry, this system is not recognized. Please visit https://www.modular.com/ to learn about supported platforms."
	exit 1
fi

cat <
