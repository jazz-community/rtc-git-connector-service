# this script is for testing that schema, views and migrations
# are valid db2 syntax.

# run scripts with db user and against the default database
su - db2inst1
db2 connect to db2inst1

# create reporting schemas
/home/db2inst1/sqllib/bin/db2 CREATE SCHEMA RIODS
/home/db2inst1/sqllib/bin/db2 CREATE SCHEMA RIDW

# create commit tables
/home/db2inst1/sqllib/bin/db2 -tvf /var/custom/tables/create_commit_table.sql
/home/db2inst1/sqllib/bin/db2 -tvf /var/custom/tables/create_commit_lookup_table.sql
# create issue tables
/home/db2inst1/sqllib/bin/db2 -tvf /var/custom/tables/create_issue_table.sql
/home/db2inst1/sqllib/bin/db2 -tvf /var/custom/tables/create_issue_assignee_table.sql
/home/db2inst1/sqllib/bin/db2 -tvf /var/custom/tables/create_issue_relation_table.sql
# create merge request tables
/home/db2inst1/sqllib/bin/db2 -tvf /var/custom/tables/create_merge_request_table.sql
/home/db2inst1/sqllib/bin/db2 -tvf /var/custom/tables/create_merge_request_assignee_table.sql
/home/db2inst1/sqllib/bin/db2 -tvf /var/custom/tables/create_merge_request_relation_table.sql
