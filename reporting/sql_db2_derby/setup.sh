# this script is for testing that schema, views and migrations
# are valid db2 syntax.

# create reporting databases
db2 CREATE DATABASE RIODS
db2 CREATE DATABASE RIDW

# create commit tables
db2 -tvf tables/create_commit_table.sql
db2 -tvf tables/create_commit_lookup_table.sql
# create issue tables
db2 -tvf tables/create_issue_table.sql
db2 -tvf tables/create_issue_assignee_table.sql
db2 -tvf tables/create_issue_relation_table.sql
# create merge request tables
db2 -tvf tables/create_merge_request_table.sql
db2 -tvf tables/create_merge_request_assignee_table.sql
db2 -tvf tables/create_merge_request_relation_table.sql
