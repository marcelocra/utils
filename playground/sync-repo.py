#!/usr/bin/env python3
#
# Long running script to be used as a cron job.

import os
import argparse


def d(to_print, show=False):
    if not show:
        return None

    print(to_print)
    return to_print


def log(to_log):
    print("hey")

parser = argparse.ArgumentParser(
    prog="sync-repo",
    description="Sync the local repository with the remote repository.",
    epilog="This script is intended to be used as a cron job.",
)
parser.add_argument("local-directory")

args = parser.parse_args()
d(args, show=True)

HOME = os.environ["HOME"]
d(HOME, show=True)

# stderr and stdout will be sent to this log file.
logfile_path = os.path.join(HOME, ".sync-repo.log.txt")
d(logfile_path, show=True)
