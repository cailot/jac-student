-- Debug query for testGroup=3, grade=11
-- Current time: 2026-02-18 19:43:57
-- This query matches the JPQL query used in getTestSchedule4Explanation

SET @testGroup = '3';
SET @grade = '11';
SET @now = '2026-02-18 19:43:57';

-- 1. Check what testGroup values exist in the database
SELECT DISTINCT 
    testGroup,
    CAST(testGroup AS CHAR) as testGroup_char,
    CAST(testGroup AS SIGNED) as testGroup_numeric,
    COUNT(*) as count
FROM TestSchedule
WHERE active = 1
GROUP BY testGroup
ORDER BY testGroup;

-- 2. Check what grade values exist in the database
SELECT DISTINCT 
    grade,
    CAST(grade AS CHAR) as grade_char,
    CAST(grade AS SIGNED) as grade_numeric,
    COUNT(*) as count
FROM TestSchedule
WHERE active = 1
GROUP BY grade
ORDER BY grade;

-- 3. Check all active TestSchedule records with testGroup=3 (exact match)
SELECT 
    id,
    fromDatetime,
    toDatetime,
    grade,
    testGroup,
    week,
    explanationFromDatetime,
    explanationToDatetime,
    active,
    registerDate,
    CASE 
        WHEN testGroup = '3' THEN 'EXACT_MATCH'
        WHEN CAST(testGroup AS CHAR) = '3' THEN 'CHAR_MATCH'
        WHEN CAST(testGroup AS SIGNED) = 3 THEN 'NUMERIC_MATCH'
        ELSE 'NO_MATCH'
    END as match_type
FROM TestSchedule
WHERE active = 1
AND (
    testGroup = '3' 
    OR CAST(testGroup AS CHAR) = '3'
    OR CAST(testGroup AS SIGNED) = 3
)
ORDER BY registerDate DESC;

-- 4. Check all active TestSchedule records with grade=11 (exact match)
SELECT 
    id,
    fromDatetime,
    toDatetime,
    grade,
    testGroup,
    week,
    explanationFromDatetime,
    explanationToDatetime,
    active,
    registerDate,
    CASE 
        WHEN grade = '11' THEN 'EXACT_MATCH'
        WHEN CAST(grade AS CHAR) = '11' THEN 'CHAR_MATCH'
        WHEN CAST(grade AS SIGNED) = 11 THEN 'NUMERIC_MATCH'
        ELSE 'NO_MATCH'
    END as match_type
FROM TestSchedule
WHERE active = 1
AND (
    grade = '11' 
    OR CAST(grade AS CHAR) = '11'
    OR CAST(grade AS SIGNED) = 11
)
ORDER BY registerDate DESC;

-- 5. Exact query matching JPQL: testGroup='3' AND grade='11' AND now BETWEEN explanation windows
SELECT 
    id,
    fromDatetime,
    toDatetime,
    grade,
    testGroup,
    week,
    explanationFromDatetime,
    explanationToDatetime,
    active,
    registerDate,
    CASE 
        WHEN @now BETWEEN explanationFromDatetime AND explanationToDatetime THEN 'IN_WINDOW'
        WHEN @now < explanationFromDatetime THEN 'BEFORE_WINDOW'
        WHEN @now > explanationToDatetime THEN 'AFTER_WINDOW'
        WHEN explanationFromDatetime IS NULL OR explanationToDatetime IS NULL THEN 'NULL_WINDOW'
    END as time_status
FROM TestSchedule
WHERE testGroup = @testGroup
AND grade = @grade
AND active = 1
ORDER BY registerDate DESC;

-- 6. Check with numeric comparison (in case DB stores as numbers)
SELECT 
    id,
    fromDatetime,
    toDatetime,
    grade,
    testGroup,
    week,
    explanationFromDatetime,
    explanationToDatetime,
    active,
    registerDate,
    CASE 
        WHEN @now BETWEEN explanationFromDatetime AND explanationToDatetime THEN 'IN_WINDOW'
        WHEN @now < explanationFromDatetime THEN 'BEFORE_WINDOW'
        WHEN @now > explanationToDatetime THEN 'AFTER_WINDOW'
        WHEN explanationFromDatetime IS NULL OR explanationToDatetime IS NULL THEN 'NULL_WINDOW'
    END as time_status
FROM TestSchedule
WHERE CAST(testGroup AS SIGNED) = CAST(@testGroup AS SIGNED)
AND CAST(grade AS SIGNED) = CAST(@grade AS SIGNED)
AND active = 1
ORDER BY registerDate DESC;

-- 7. Check all records that match testGroup=3 OR grade=11 (to see if data exists at all)
SELECT 
    id,
    fromDatetime,
    toDatetime,
    grade,
    testGroup,
    week,
    explanationFromDatetime,
    explanationToDatetime,
    active,
    registerDate,
    CASE 
        WHEN CAST(testGroup AS SIGNED) = 3 THEN 'MATCHES_TESTGROUP'
        WHEN CAST(grade AS SIGNED) = 11 THEN 'MATCHES_GRADE'
        WHEN CAST(testGroup AS SIGNED) = 3 AND CAST(grade AS SIGNED) = 11 THEN 'MATCHES_BOTH'
        ELSE 'NO_MATCH'
    END as match_status
FROM TestSchedule
WHERE active = 1
AND (
    CAST(testGroup AS SIGNED) = 3 
    OR CAST(grade AS SIGNED) = 11
)
ORDER BY registerDate DESC
LIMIT 50;

-- 8. Check explanation windows for testGroup=3, grade=11 (regardless of current time)
SELECT 
    id,
    testGroup,
    grade,
    week,
    explanationFromDatetime,
    explanationToDatetime,
    DATEDIFF(explanationToDatetime, explanationFromDatetime) as window_days,
    CASE 
        WHEN NOW() BETWEEN explanationFromDatetime AND explanationToDatetime THEN 'CURRENTLY_ACTIVE'
        WHEN NOW() < explanationFromDatetime THEN 'FUTURE'
        WHEN NOW() > explanationToDatetime THEN 'PAST'
        WHEN explanationFromDatetime IS NULL OR explanationToDatetime IS NULL THEN 'NULL'
    END as window_status
FROM TestSchedule
WHERE (
    CAST(testGroup AS SIGNED) = 3 
    OR testGroup = '3'
    OR CAST(testGroup AS CHAR) = '3'
)
AND (
    CAST(grade AS SIGNED) = 11 
    OR grade = '11'
    OR CAST(grade AS CHAR) = '11'
)
AND active = 1
ORDER BY explanationFromDatetime DESC;
