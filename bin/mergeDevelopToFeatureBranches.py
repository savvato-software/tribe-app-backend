import subprocess

print("****************************************************************")
print(" Merge Develop Into Each Feature Branch, and Push to the Cloud")
print("****************************************************************")
print("")

# Get the current branch name
original_branch = subprocess.run(['git', 'rev-parse', '--abbrev-ref', 'HEAD'], stdout=subprocess.PIPE).stdout.decode('utf-8').strip()
print("Current branch is: " + original_branch)

# Stash any changes if present
stash_output = subprocess.run(['git', 'stash'], stdout=subprocess.PIPE).stdout.decode('utf-8')
stashed_changes = len(stash_output.split('\n')) > 1
if stashed_changes:
    print("Stashed changes.")


# Check out the develop branch and do a pull
ckoutput = subprocess.run(['git','checkout','develop'], stdout=subprocess.PIPE).stdout.decode('utf-8')

if 'up to date' not in ckoutput:
    subprocess.run(['git','pull'])
    print("Checked out develop branch and did a pull.")

# Find the list of feature branches
feature_branches = subprocess.run(['git', 'branch', '-r', '--list', '\*feature*'], stdout=subprocess.PIPE).stdout.decode('utf-8').split('\n')
feature_branches = [x.replace('origin/','') for x in feature_branches]

# For each feature branch, merge the develop branch into it and push
for feature_branch in feature_branches:
    subprocess.run(['git', 'checkout', feature_branch], stdout=subprocess.PIPE).stdout.decode('utf-8')
    subprocess.run(['git', 'merge', 'develop'], stdout=subprocess.PIPE).stdout.decode('utf-8')
    subprocess.run(['git', 'push'], stdout=subprocess.PIPE).stdout.decode('utf-8')
    print("Merged develop branch into " + feature_branch + " and pushed.")

# Change back to the original branch and pop any stashed changes
subprocess.run(['git', 'checkout', original_branch], stdout=subprocess.PIPE).stdout.decode('utf-8')
print("Changed back to the original branch.")
if stashed_changes:
    subprocess.run(['git', 'stash', 'pop'], stdout=subprocess.PIPE).stdout.decode('utf-8')
    print("Popped any stashed changes.")
