#Branches:
The code for each user story (or task unit) must be written on individual branches. For instance, if I am adding a new module to the User Interface, I will create a new branch from the repository and work from there. This may seem like it presents a lot of overhead, but in this way there is an organized structure. As long as branches are kept up to date, this avoids clashing code, and too many unrelated Pull Requests from a single branch. Changes can be tracked easier which will allow for better story maintenance. Individuals working on the same functionality may checkout the relevant branch and collaborate remotely. 

Branches need to follow a set of naming conventions. They should inform the team on its purpose. They should not contain any numbers, whitespaces, or symbols (except for ‘-’).

Branches should also utilize prefixes. This determines the type of branch created, which further clues developers in on its intended functionality. The prefixes should include any of the following:

feat/
prefixed to a branch with the intention of adding, removing, or modifying a working feature to the application

bugfix/
prefixed to a branch with the intention of fixing a bug in the code-base

hotfix/
prefixed to a branch with the intention of fixing a critical issue with a temporary solution

test/
prefixed to a branch with the intention of exploration or testing

Therefore, branches should model after the following format:

prefix/branch-name
##Example: bugfix/search-function-logic
