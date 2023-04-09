#!/usr/bin/env python

import os

print("****************************************************************")
print(" Merge Develop Into Each Feature Branch, and Push to the Cloud")
print("****************************************************************")
print("")

# Get the current branch name
original_branch = os.popen('git rev-parse --abbrev-ref HEAD').read().strip()
print("Current branch is: " + original_branch)

# Stash any changes if present
stash_output = os.popen('git stash').read()
stashed_changes = len(stash_output.split('\n')) > 1
if stashed_changes:
	print("Stashed changes.")

# Check out the develop branch and do a pull
ckoutput = os.system('git checkout develop')

if 'up to date' not in ckoutput:
    os.system('git pull')
    print("Checked out develop branch and did a pull.")

    # Find the list of feature branches
    feature_branches = os.popen('git branch -r --list \*feature*').read().split('\n')
    feature_branches = [x.replace('origin/','') for x in feature_branches]

    # For each feature branch, merge the develop branch into it and push
    for feature_branch in feature_branches:
        os.system('git checkout ' + feature_branch + ' && git merge develop && git push')
        print("Merged develop branch into " + feature_branch + " and pushed.")

# Change back to the original branch and pop any stashed changes
os.system('git checkout ' + original_branch)
print("Changed back to the original branch.")
if stashed_changes:
	os.system('git stash pop')
	print("Popped any stashed changes.")

