-- getTestSchedule4Explanation: explanation list within explanation window
-- Replace @testGroup, @grade, @now with your test values before running in MySQL Workbench.

-- Example values (edit these):
-- SET @testGroup = '4';           -- 3=EDU, 4=ACER
-- SET @grade   = 'TT6';           -- or '11' if DB stores code
-- SET @now     = '2026-02-18 18:00:00';  -- current time in schedule window

SET @testGroup = '4';
SET @grade     = 'TT6';
SET @now       = NOW();  -- or e.g. '2026-02-18 18:00:00'

SELECT
    t.id,
    t.fromDatetime,
    t.toDatetime,
    t.grade,
    t.testGroup,
    t.week,
    t.info,
    t.active,
    t.registerDate,
    t.explanationFromDatetime,
    t.explanationToDatetime
FROM TestSchedule t
WHERE (
    t.testGroup = '0'
    OR t.testGroup LIKE CONCAT('%,', @testGroup, ',%')
    OR t.testGroup LIKE CONCAT(@testGroup, ',%')
    OR t.testGroup LIKE CONCAT('%,', @testGroup)
    OR t.testGroup = @testGroup
)
AND (
    t.grade = '0'
    OR t.grade LIKE CONCAT('%,', @grade, ',%')
    OR t.grade LIKE CONCAT(@grade, ',%')
    OR t.grade LIKE CONCAT('%,', @grade)
    OR t.grade = @grade
)
AND (@now BETWEEN t.explanationFromDatetime AND t.explanationToDatetime)
AND (t.active = true);

-- ---------------------------------------------------------------------------
-- If your DB uses snake_case column names, use this version instead:
-- ---------------------------------------------------------------------------
/*
SELECT
    t.id,
    t.from_datetime,
    t.to_datetime,
    t.grade,
    t.test_group,
    t.week,
    t.info,
    t.active,
    t.register_date,
    t.explanation_from_datetime,
    t.explanation_to_datetime
FROM TestSchedule t
WHERE (
    t.test_group = '0'
    OR t.test_group LIKE CONCAT('%,', @testGroup, ',%')
    OR t.test_group LIKE CONCAT(@testGroup, ',%')
    OR t.test_group LIKE CONCAT('%,', @testGroup)
    OR t.test_group = @testGroup
)
AND (
    t.grade = '0'
    OR t.grade LIKE CONCAT('%,', @grade, ',%')
    OR t.grade LIKE CONCAT(@grade, ',%')
    OR t.grade LIKE CONCAT('%,', @grade)
    OR t.grade = @grade
)
AND (@now BETWEEN t.explanation_from_datetime AND t.explanation_to_datetime)
AND (t.active = true);
*/
