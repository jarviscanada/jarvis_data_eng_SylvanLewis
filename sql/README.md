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


## Practice Queries


**Insert a new facility**
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES (9, 'Spa', 20, 30, 100000, 800);

** Insert a new facility with calculated ID**
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
SELECT (SELECT MAX(facid) FROM cd.facilities) + 1, 'Spa', 20, 30, 100000, 800;
** Update existing data **
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE facid = 1;
** Delete all bookings **
DELETE FROM cd.bookings;
** Delete a member **
DELETE FROM cd.members
WHERE memid = 37;

## Basics

** Filter Facilities by cost**
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost > 0 
AND membercost < (monthlymaintenance / 50.0);
** Filter facilities by name **
SELECT * FROM cd.facilities
WHERE name LIKE '%Tennis%';
** Filter facilities by ID list **
SELECT * FROM cd.facilities
WHERE facid IN (1, 5);
** Filter members by join date **
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-01 0:00';
** Combine results with UNION **
SELECT surname FROM cd.members
UNION
SELECT surname FROM cd.facilities;

## Joins

** Join to find member booking times **
SELECT starttime
FROM cd.bookings
INNER JOIN cd.members
ON cd.members.memid = cd.bookings.memid
WHERE cd.members.surname LIKE 'Farrell'
AND cd.members.firstname LIKE 'David';
** Join with specific date range **
SELECT bks.starttime AS start, facs.name AS name
FROM cd.facilities facs
INNER JOIN cd.bookings bks
    ON facs.facid = bks.facid
WHERE facs.name IN ('Tennis Court 2', 'Tennis Court 1') 
    AND bks.starttime >= '2012-09-21' 
    AND bks.starttime < '2012-09-22'
ORDER BY bks.starttime;
** Self-join to find recommenders **
SELECT DISTINCT recs.firstname AS firstname, recs.surname AS surname
FROM cd.members mems
INNER JOIN cd.members recs
    ON recs.memid = mems.recommendedby
ORDER BY surname, firstname;

## Aggregation

** Count recommendations **
SELECT recommendedby, COUNT(*)
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;
** Total slots booked per facility **
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
GROUP BY facid
ORDER BY facid;
** Total member count **
SELECT 
    COUNT(*) OVER(), 
    firstname, 
    surname
FROM cd.members
ORDER BY joindate;
** Facilities by total slots **
SELECT facid, total
FROM (
    SELECT 
        facid, 
        SUM(slots) AS total, 
        RANK() OVER (ORDER BY SUM(slots) DESC) AS rank
    FROM cd.bookings
    GROUP BY facid
) AS ranked_facilities
WHERE rank = 1;

## String

** Format member names **
SELECT surname || ', ' || firstname AS name
FROM cd.members;
** Regex Phonenumbers with parentheses **
SELECT memid, telephone 
FROM cd.members 
WHERE telephone ~ '[()]'
ORDER BY memid;
** Count members by first letter of surname **
SELECT 
    SUBSTR(surname, 1, 1) AS letter, 
    COUNT(*) AS count 
FROM cd.members 
GROUP BY letter 
ORDER BY letter;
```
