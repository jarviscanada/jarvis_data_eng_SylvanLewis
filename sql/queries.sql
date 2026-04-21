SELECT * FROM cd.facilities;

INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES (9, 'Spa', 20, 30, 100000, 800);

INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
SELECT (SELECT MAX(facid) FROM cd.facilities) + 1, 'Spa', 20, 30, 100000, 800;

UPDATE cd.facilities
SET initialoutlay = 10000
WHERE facid = 1;

DELETE FROM cd.bookings;

DELETE FROM cd.members
WHERE memid = 37;

SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost > 0 
AND membercost < (monthlymaintenance / 50.0);

SELECT * FROM cd.facilities
WHERE name LIKE '%Tennis%';

SELECT * FROM cd.facilities
WHERE facid in (1,5);

SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-01 0:00';

SELECT surname FROM cd.members
UNION
SELECT name FROM cd.facilities;

SELECT starttime
FROM cd.bookings
INNER JOIN cd.members
ON cd.members.memid = cd.bookings.memid
WHERE cd.members.surname LIKE 'Farrell'
AND cd.members.firstname LIKE 'David';

SELECT bks.starttime AS start, facs.name AS name
FROM cd.facilities facs
INNER JOIN cd.bookings bks
    ON facs.facid = bks.facid
WHERE facs.name IN ('Tennis Court 2', 'Tennis Court 1') 
    AND bks.starttime >= '2012-09-21' 
    AND bks.starttime < '2012-09-22'
ORDER BY bks.starttime;

SELECT mems.firstname AS memfname, mems.surname AS memsname, recs.firstname AS recfname, recs.surname AS recsname
FROM cd.members mems
LEFT OUTER JOIN cd.members recs
    ON recs.memid = mems.recommendedby
ORDER BY memsname, memfname;   

SELECT DISTINCT recs.firstname AS firstname, recs.surname AS surname
FROM cd.members mems
INNER JOIN cd.members recs
    ON recs.memid = mems.recommendedby
ORDER BY surname, firstname;

SELECT DISTINCT mems.firstname || ' ' || mems.surname AS member,
    (SELECT recs.firstname || ' ' || recs.surname AS recommender 
     FROM cd.members recs 
     WHERE recs.memid = mems.recommendedby)
FROM cd.members mems
ORDER BY member;

SELECT recommendedby, count(*)
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;

SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
GROUP BY facid
ORDER BY facid;

SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE starttime >= '2012-09-01' 
  AND starttime < '2012-10-01'
GROUP BY facid
ORDER BY SUM(slots);

SELECT facid, EXTRACT(month FROM starttime) AS month, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE starttime >= '2012-01-01' AND starttime < '2013-01-01'
GROUP BY facid, month
ORDER BY facid, month;

SELECT mems.surname, mems.firstname, mems.memid, MIN(bks.starttime) AS starttime
FROM cd.members mems
INNER JOIN cd.bookings bks 
    ON mems.memid = bks.memid
WHERE bks.starttime >= '2012-09-01'
GROUP BY mems.surname, mems.firstname, mems.memid
ORDER BY mems.memid;

SELECT 
    COUNT(*) OVER(), 
    firstname, 
    surname
FROM cd.members
ORDER BY joindate;

SELECT 
    ROW_NUMBER() OVER(ORDER BY joindate), 
    firstname, 
    surname
FROM cd.members
ORDER BY joindate;

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

SELECT surname || ', ' || firstname AS name
FROM cd.members;

SELECT memid, telephone 
FROM cd.members 
WHERE telephone ~ '[()]'
ORDER BY memid;

SELECT 
    SUBSTR(surname, 1, 1) AS letter, 
    COUNT(*) AS count 
FROM cd.members 
GROUP BY letter 
ORDER BY letter;




















