# Compatibility
The provided sql files have only been tested with IBM DB2 and Apache Derby. MS SQL Server and Oracle are untested. We welcome pull requests regarding table and view creation files for all compatible databases.

# Db2 Testing commands
`./sqllib/bin/db2` run db2 command line
`connect to db2inst1`
`list database directory` list databases
`select schemaname from syscat.schemata` show db schemas
