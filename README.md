# RTC Git Connector Service
RTC Git Connector Service provides the server side functionality for [RTC Git Connector](https://github.com/jazz-community/rtc-git-connector). This includes providing custom link styles for Git Artifacts, Rich Hover capability for these links across RTC and extensions for DCC and Report Builder to enable reporting of Git information.

# Table of Contents

-   [Setup](#setup)
    -   [Prerequisites](#prerequisites)
    -   [Installation](#installation)
-   [Reporting](#reporting)
    -   [Prerequisites](#prerequisites-1)
    -   [Deployment](#deployment)
    -   [Update](#update)
    -   [Example](#example)
-   [Contributing](#contributing)
-   [Licensing](#licensing)

# Setup
Deploying this service only makes sense in conjunction with the [RTC Git Connector](https://github.com/jazz-community/rtc-git-connector). Detailed instructions for installing RTC Git Connector can be taken from it's [readme](https://github.com/jazz-community/rtc-git-connector/blob/master/README.md).

## Prerequisites
RTC Git Connector Service requires that [Secure User Property Store for RTC](https://github.com/jazz-community/rtc-secure-user-property-store) has been **installed** and **properly configured**. This is a compulsory dependency and RTC Git Connector Service will not work at all without the Secure User Property Store being available.

RTC Git Connector Service has been developed, tested and deployed on RTC versions above 6.0.3.
reporting

## Installation
Please refer to the [Releases page](https://github.com/jazz-community/rtc-git-connector-service/releases) for current stable releases. The plugin can be installed like any other rtc update-site, for detailed instructions, you can follow [these steps](https://github.com/jazz-community/rtc-create-child-item-plugin#installation).

# Reporting
To enable the reporting capabilities of the RTC Git Connector Service, additional deployment steps are necessary. DCC requires additional TTL files and additional tables in the RIODS database. Report Builder requires additional TTL files and additional views in the RIDW database.

If you have previously deployed another version of RTC Git Connector Service with reporting capabilites and wish to upgrade to a new version, please follow the instructions in the [Update](#update) section.

**Currently, only IBM DB2 and Apache Derby have been tested and are known to work.** Other databases should work with the provided SQL files, but have not been tested.

## Prerequisites
1. The plugin has been deployed as described in [Installation](#installation).
2. CREATE/DROP privileges for tables in the RIODS database and views in the RIDW database. Usually, the user that was used during Jazz setup when creating the data warehouse configuration should have these privileges.
3. File access for adding TTL files to the dcc and rs metadata folders.
4. A SQL Client / database administration tool for making the required changes. Any tool supporting the database you are using will work. [DBeaver Community](https://dbeaver.io/) was used during development and is known to work with Apache Derby as well as IBM DB2. The [Example](#example) provided in this read me use the DB2 command line client and the windows command line.

## Deployment
This section gives a high level description of deploying the required reporting components. A detailed [Example](#example) is also provided in a later section.

### Prepare Database
The required SQL Files are in the `reporting/sql_db2_derby/` folder of this repository or the released update site zip file, split up by subfolders for tables and views. Please note that the order in which the scripts are executed is important, and should be followed as is presented here.

1. Using a SQL Client / database administration tool, connect to the Data Warehouse Database.
2. Run the `create_commit_table.sql` file to create the `RIODS.GIT_COMMIT` table.
3. Run the `create_commit_lookup_table.sql` file to create the `RIODS.GIT_COMMIT_LOOKUP` table.
3. Run the `create_ìssue_table.sql` file to create the `RIODS.GIT_ÌSSUE` table.
3. Run the `create_ìssue_table_assignee.sql` file to create the `RIODS.GIT_ÌSSUE_ASSIGNEE` table.
3. Run the `create_ìssue_table_relation.sql` file to create the `RIODS.GIT_ÌSSUE_RELATION` table.
3. Run the `create_merge_request_table.sql` file to create the `RIODS.GIT_MERGE_REQUEST` table.
3. Run the `create_merge_request_table_assignee.sql` file to create the `RIODS.GIT_MERGE_REQUEST_ASSIGNEE` table.
3. Run the `create_merge_request_table_relation.sql` file to create the `RIODS.GIT_MERGE_REQUEST_RELATION` table.
4. Run the `create_commit_view.sql` file to create the `RIDW.VW_GIT_COMMIT` view.
5. Run the `create_commit_lookup_view.sql` file to create the `RIDW.VW_GIT_COMMIT_LOOKUP` view.

### Deploy Data Collection Files

1. Copy all ttl files from the `reporting/dcc` folder to the `server/conf/dcc/mapping` folder of your RTC installation.
2. Head to the dcc start page, eg. `https://your-rtc-server/dcc/web`. Import the data collection definitions.

![Collection Job Import](https://github.com/jazz-community/rtc-git-connector-service/blob/master/documentation/dcc_load_jobs.png)

3. Verify that the Git DCC Jobs have been loaded successfully. The Jobs should be listed in the ODS Data Collection section.

![Job Import Verification](https://github.com/jazz-community/rtc-git-connector-service/blob/master/documentation/dcc_jobs_loaded.png)

### Deploy Report Builder Definitions
1. Copy all ttl files from the `reporting/rs` folder to the `server/conf/rs/metadata` folder of your RTC installation.
2. Import report definitions: 
    1. Go to the admin section of report builder and click on Data Sources

![Report Builder Admin View](https://github.com/jazz-community/rtc-git-connector-service/blob/master/documentation/rs_admin_section.png)

    2. Choose the Rational Data Warehouse Data Source

![Report Builder Data Sources](https://github.com/jazz-community/rtc-git-connector-service/blob/master/documentation/rs_data_sources.png)

    3. Reload definitions

![Report Builder Resource Refresh](https://github.com/jazz-community/rtc-git-connector-service/blob/master/documentation/rs_refresh-source.png)

3. Verify that the report definitions have been loaded sucessfully: After the successful update of the metamodel, you should see the git artifacts when creating a reporting and selecting "Choose an artifact"

![Report Builder Showing Git Commit](https://github.com/jazz-community/rtc-git-connector-service/blob/master/documentation/rs_choose_artifact.png)

## Update
If you have previously deployed the reporting capabilities, please follow these instructions **before** cleanly deploying by following the instructions in the [Deployment](#deployment) section. This step is required to avoid inconsistencies in the reporting process, and will ensure that data is reliably stored and reported against.

**Running these scripts will delete all! data.** However, that is not an issue as running dcc again will collect the same data when running a full import.

Run the `drop_before_update.sql` located in the `sql_db2_derby` folder with a sql tool of your choice.

Now, continue with the steps in the [Deployment](#deployment) section to create a fresh deployment of the reporting capabilities.

## Example
This example shows how deployment of the reporting capabilities can be done with RTC and DB2 hosted on Windows.

### Prepare Database

1. Start a DB2 command line window as the user who created the Jazz database.
2. Change directory to the sql scripts of this package.

`cd /d d:\IBM\BT-Updatesites\reporting\sql_db2_derby`

3. Connect to Data Warehouse Database

`db2 connect to <database>`

4. Drop existing views and tables if they exist

`db2 -tvf .\drop_before_update.sql`

5. Create the tables and views in the correct order

```
db2 -tvf .\tables\create_commit_table.sql
db2 -tvf .\tables\create_commit_lookup_table.sql
db2 -tvf .\tables\create_issue_table.sql
db2 -tvf .\tables\create_issue_assginee_table.sql
db2 -tvf .\tables\create_issue_relation_table.sql
db2 -tvf .\tables\create_merge_request_table.sql
db2 -tvf .\tables\create_merge_request_assginee_table.sql
db2 -tvf .\tables\create_merge_request_relation_table.sql
db2 -tvf .\views\create_commit_view.sql
db2 -tvf .\views\create_commit_lookup_view.sql
db2 -tvf .\views\create_issue_view.sql
db2 -tvf .\views\create_issue_assignee_view.sql
db2 -tvf .\views\create_issue_relation_view.sql
db2 -tvf .\views\create_merge_request_view.sql
db2 -tvf .\views\create_merge_request_assignee_view.sql
db2 -tvf .\views\create_merge_request_relation_view.sql
```

6. Disconnect the command line session

`db2 disconnect <database>`

### Deploy Data Collection Files

1. Copy the files in `reporting/dcc` to `server/conf/dcc/mapping`

`cp .\dcc\* D:\IBM\JazzLiberty_DCC_606\server\conf\dcc\mapping`

2. Import data collection definitions as shown in the [instructions](#deploy-data-collection-files)
    1. Open DCC Web, eg. from `https://jazz-home-uri/dcc/web`
    2. Click the load button

### Deploy Report Builder Definitions

1. Copy the files in `reporting/rs` to `server/conf/rs/metadata`

`cp .\rs\* D:\IBM\JazzLiberty_RS_606\server\conf\rs\metadata`

2. Reload the report definitions as shown in the [instructions](#deploy-report-builder-definitions)
    1. Open report builder, eg. from `https://jazz-home-uri/rs/reports`
    2. Go to the Admin View
    3. Open Data Source
    4. Choose the *Rational Data Warehouse* Data Source
    5. Click refresh

# Contributing
Please use the [Issue Tracker](https://github.com/jazz-community/rtc-git-connector-service/issues) of this repository to report issues or suggest enhancements.

For general contribution guidelines, please refer to [CONTRIBUTING.md](https://github.com/jazz-community/rtc-git-connector-service/blob/master/CONTRIBUTING.md)

# Licensing
Copyright (c) Siemens AG. All rights reserved.<br>
Licensed under the [MIT](LICENSE) License.
