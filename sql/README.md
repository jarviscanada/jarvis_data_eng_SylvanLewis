# Introduction

# SQL Queries

###### Table Setup (DDL)

```sql
CREATE TABLE cd.members (
    memid INTEGER PRIMARY KEY,
    surname VARCHAR(200),
    firstname VARCHAR(200),
    address VARCHAR(300),
    zipcode INTEGER,
    telephone VARCHAR(20),
    recommendedby INTEGER,
    joindate TIMESTAMP,
    FOREIGN KEY (recommendedby) REFERENCES cd.members(memid)
);

CREATE TABLE cd.facilities (
    facid INTEGER PRIMARY KEY,
    name VARCHAR(100),
    membercost NUMERIC,
    guestcost NUMERIC,
    initialoutlay NUMERIC,
    monthlymaintenance NUMERIC
);

CREATE TABLE cd.bookings (
    bookid INTEGER PRIMARY KEY,
    facid INTEGER,
    memid INTEGER,
    starttime TIMESTAMP,
    slots INTEGER,
    FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
    FOREIGN KEY (memid) REFERENCES cd.members(memid)
);
