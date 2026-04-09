#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

hostname=$(hostname -f)
lscpu_out=$(lscpu)

cpu_number=$(echo "$lscpu_out" | grep -E "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | grep -E "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | grep -E "^Model name:" | awk -F ':' '{print $2}' | xargs)
cpu_mhz=$(echo "$lscpu_out" | grep -E "^CPU MHz:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_out" | grep -E "^L2 cache:" | awk '{print $3}' | sed 's/K//' | xargs)
total_mem=$(vmstat -s | grep "total memory" | awk '{print $1}' | xargs)

timestamp=$(date -u "+%Y-%m-%d %H:%M:%S")

cpu_mhz=${cpu_mhz:-0}
l2_cache=${l2_cache:-0}

insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, timestamp) VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, $total_mem, '$timestamp');"

export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?
