#!/usr/bin/env sh
#
# Long running script to be used as a cron job.

# stderr and stdout will be sent to a log file.
logfile_path="${HOME}/.sync-repo.log.txt"

# Create the log file if it doesn't exist.
if [ -f $logfile_path ]; then
    touch $logfile_path
fi

log() {
    echo "$1" >> $logfile_path
}

log "-------------------- $(date +%F_%T | tr ':' '-') --------------------"

if [ -z "$1" ]; then
    log 'Please, provide the path to a local git repository.'
    log '---------------------------------------------------------------------'
    exit 1
fi

if [ -d "$1" ]; then
    log "'$1' is not a directory. Please, provide a local valid one."
    log '---------------------------------------------------------------------'
    exit 1
fi

# Go to the folder and log it.
cd "$1"
log "Running in '$1'"

# Get git status.
curr_state="$(git status --porcelain)"

# Check if the status is empty and exit if so, as there's
# nothing to do.
if [ -z "$curr_state" ]; then
    log 'Clean repo, nothing to do.'
    exit 0
fi

run_or_exit() {
    if ! $1; then
        exit 1
    fi
}
    
run_or_exit "git add -A >>$logfile_path 2>&1"
run_or_exit "git commit -m 'update' >>$logfile_path 2>&1"
run_or_exit "git push >>$logfile_path 2>&1h"

