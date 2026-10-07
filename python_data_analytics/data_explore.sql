<< EOF
-- Show table schema 
\d+ retail;

-- Show first 10 rows
SELECT * FROM retail limit 10;

-- Check # of records
SELECT COUNT(*) FROM retail;

-- number of clients (e.g. unique client ID)
SELECT COUNT(DISTINCT customer_id) FROM retail;

-- invoice data range (e.g. max/min dates)
SELECT invoice_date,
	min ()
	max ()
FROM retail
-- Q1: Show first 10 rows
SELECT * FROM retail LIMIT 10;

-- Q2: Check # of records
SELECT COUNT(*) FROM retail;

-- Q3: number of clients (e.g. unique client ID)
SELECT COUNT(DISTINCT customer_id) FROM retail;

-- Q4: invoice date range (e.g. max/min dates)
SELECT MIN(invoice_date), MAX(invoice_date) FROM retail;

-- Q5: number of SKU/merchants (e.g. unique stock code)
SELECT COUNT(DISTINCT stock_code) FROM retail;

-- Q6: Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)
SELECT AVG(invoice_total)
FROM (
    SELECT invoice_no, SUM(quantity * unit_price) AS invoice_total
    FROM retail
    GROUP BY invoice_no
    HAVING SUM(quantity * unit_price) > 0
) AS valid_invoices;

-- Q7: Calculate total revenue (e.g. sum of unit_price * quantity)
SELECT SUM(unit_price * quantity) FROM retail;

-- Q8: Calculate total revenue by YYYYMM
SELECT 
    CAST(EXTRACT(YEAR FROM invoice_date) * 100 + EXTRACT(MONTH FROM invoice_date) AS INTEGER) AS yyyymm,
    SUM(unit_price * quantity) AS sum
FROM retail
GROUP BY yyyymm
ORDER BY yyyymm;



EOF
