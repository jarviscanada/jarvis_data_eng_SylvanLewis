# Linux Cluster Monitoring Agent

This project is designed to monitor static hardware specifications and dynamic resource usage, ranging across a cluster of Rocky Linux 9 nodes. By deploying bash scripts as agents on these nodes, the system collects then centralizes them within the PostgreSQL database. This project totals as a reliable way to track system health over time, based on continuous tracked data.

# Introduction

The Linux Cluster Monitoring Agent was developed as a Minimal Viable Product (MVP), solving the problem of monitoring a convoluted cluster of servers. Focusing on data collection and data storage, for analysis. While Resource Usage is collected and stored routinely, the Hardware Information is collected once per host. Bash was utilized as the primary scripting language, developing monitoring agents, and direct data extraction from system files (lscpu, proc/meminfo). Docker’s implementation containerized the PostgreSQL database, providing portable deployment environment access across different host machines. PostgreSQL was the central relational database. Version control was managed through the local command line Git, and the cloud-based platform that hosts this project is Github; all whilst following a meticulous Gitflow workflow. Crontab was used for automatic periodic execution of the scripts, transforming them into the necessary monitoring application.

# Quick Start

Create a psql container using psql_docker.sh specified with db_username and db_password

bash /scripts/psql_docker.sh create db_username db_password

Start a psql instance using psql_docker.sh

Psql -h localhost -p 5432 -U postgres -d host_agent -f sql/ddl.sql

Create tables using ddl.sql

bash scripts/host_info.sh localhost 5432 host_agent postgres password

Insert hardware specs data into the DB using host_info.sh

bash scripts/host_info.sh localhost 5432 host_agent postgres password

Insert hardware usage data into the DB using host_usage.sh

bash scripts/host_usage.sh localhost 5432 host_agent postgres password

Crontab setup
crontab -e 
* * * * * bash <path>/host_usage.sh psql_host psql_port db_name psql_user psql_password

# Implementation

Firstly, Docker is used to create a psql container. Bash script (psql_docker.sh) was developed to manage this container (create, start, stop). Once the PostgreSQL database was running, a Data Definition Language (DDL) script, ddl.sql was created to the host_agent database and create the host_info and host_usage tables, cementing the relational structure necessary for data storage.

# Architecture 





# Scripts

- 	./scripts/psql_docker.sh
Derived from Docker; creates, starts, and stops a psql instance.

The two Bash scripts which were developed to as monitoring agents:

-	./scripts/host_info.sh 
	Inserts Linux Host Hardware Specifications within the “host_info” table.

This script captures status hardware data. It utilizes Linux utilities such as lscpu, and /proc/meminfo to gather Hardware Specifications. This data is then formatted into a SQL INSERT statement and pushed to the database.


-	./scripts/host_usage.sh
	Inserts Linux Host Resource Usage Data within the “host_usage” table.
	
This script captures dynamic resource usage. It uses vmstat, and df to extract Resource Usage. This data is then queried to the database.

- 	./sql/ddl.sql
	Contains the Data Definition Language (DDL) commands to create host_agent database. 

- 	./sql/queries.sql
	SQL reports, used to solve business problems.

Finally, the scripts are transformed into a continuous monitoring service. By utilizing Crontab, the host_usage.sh script is scheduled to run every minute ( * * * * *) This automation confirms that the database receives scheduled performance data, automating the process, and allowing the system to capture usage data effectively.

# Database Modeling

host_info:
id: SERIAL (PK), Unique Identifier for each host
hostname: VARCHAR, Fully qualified domain name
cpu_number: INT, Number of CPU cores
cpu_architecture: VARCHAR, x86_64
cpu_model: INT, Total RAM in KB
cpu_mhz: TIMESTAMP, Time of registration
total_mem: INT, Total RAM in KB
timestamp: TIMESTAMP, Time of Registration

host_usage:
	timestamp: TIMESTAMP, Time of Registration
	host_id: INT(FK), Reference to host_info.id
	memory_free: INT, Available RAM in MB
cpu_idle: INT, Percentage of idle CPU	
	cpu_kernel: INT, Percentage of CPU used by kernel
	disk_io: INT, Number of disks currently in I/O
	disk_available: INT, Available disk space in MB

Test - Streamlined using the following technologies.

Bash Scripts: Executed from the CLI. Utilized Linux exit codes (echo $?) to ensure validity. 

	DDL/SQL: The ddl.sql file was executed against the Dockerized PostgreSQL instance. The schema was validated by logging into the psql shell.

# Deployment

	Github: The source code was hosted on Github, allowing the repository to be cloned seamlessly onto the target Linux host nodes.
	
	Docker: The database was deployed using Docker, allowing the RDBMS to be utilized.

	Crontab: Application automation was deployed by configuring the local crontab daemon on each host node to execute the monitoring agents within the background.

# Improvements

Alerts: Integrate an email update to notify the team if the server has issues.

Quarterly insights: Drawing insights to analyze 3-month periods, to captivate the lifecycle of data.

Allow regional data mapping: To draw historical insights, to see if the machines regional base impacts the application








