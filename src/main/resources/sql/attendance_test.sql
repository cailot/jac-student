-- Teacher
INSERT INTO jae.Teacher (accountNumber,address,bank,branch,bsb,email,endDate,firstName,lastName,memo,phone,startDate,state,superMember,superannuation,tfn,title,vitNumber) VALUES (67890,'123 Belmore Rd','Anz','13','012-345','jy171114@gmail.com',NULL,'Jin','Seo',NULL,'0433195038','2023-11-11','1','39404TS','VicSuper',123456,'mr','VT393443');

-- Teacher_Class
INSERT INTO jae.Teacher_Class (teacherId,clazzId) VALUES (1,1);
INSERT INTO jae.Teacher_Class (teacherId,clazzId) VALUES (1,27);
INSERT INTO jae.Teacher_Class (teacherId,clazzId) VALUES (1,51);

-- Invoice
INSERT INTO jae.Invoice (amount,credit,discount,info,paidAmount,paymentDate,registerDate) VALUES (843,0,0,NULL,0,'2023-11-11','2023-11-11');
INSERT INTO jae.Invoice (amount,credit,discount,info,paidAmount,paymentDate,registerDate) VALUES (700,0,0,NULL,0,'2023-11-11','2023-11-11');
INSERT INTO jae.Invoice (amount,credit,discount,info,paidAmount,paymentDate,registerDate) VALUES (709.99,1,0,NULL,0,'2023-11-11','2023-11-11');
INSERT INTO jae.Invoice (amount,credit,discount,info,paidAmount,paymentDate,registerDate) VALUES (700,0,0,NULL,0,'2023-11-11','2023-11-11');
INSERT INTO jae.Invoice (amount,credit,discount,info,paidAmount,paymentDate,registerDate) VALUES (555,0,0,NULL,0,'2023-11-11','2023-11-11');

-- Enrolment
INSERT INTO jae.Enrolment (cancellationReason,cancelled,credit,discount,endWeek,info,old,registerDate,startWeek,clazzId,invoiceId,studentId) VALUES (NULL,0,0,'0',31,NULL,0,'2023-11-11',22,1,1,130055);
INSERT INTO jae.Enrolment (cancellationReason,cancelled,credit,discount,endWeek,info,old,registerDate,startWeek,clazzId,invoiceId,studentId) VALUES (NULL,0,0,'0',26,NULL,0,'2023-11-11',17,1,2,130365);
INSERT INTO jae.Enrolment (cancellationReason,cancelled,credit,discount,endWeek,info,old,registerDate,startWeek,clazzId,invoiceId,studentId) VALUES (NULL,0,0,'100%',26,NULL,0,'2023-11-11',17,5,2,130365);
INSERT INTO jae.Enrolment (cancellationReason,cancelled,credit,discount,endWeek,info,old,registerDate,startWeek,clazzId,invoiceId,studentId) VALUES (NULL,0,1,'0',30,NULL,0,'2023-11-11',20,1,3,134174);
INSERT INTO jae.Enrolment (cancellationReason,cancelled,credit,discount,endWeek,info,old,registerDate,startWeek,clazzId,invoiceId,studentId) VALUES (NULL,0,0,'100%',30,NULL,0,'2023-11-11',20,5,3,134174);
INSERT INTO jae.Enrolment (cancellationReason,cancelled,credit,discount,endWeek,info,old,registerDate,startWeek,clazzId,invoiceId,studentId) VALUES (NULL,0,0,'0',21,NULL,0,'2023-11-11',12,1,4,134264);
INSERT INTO jae.Enrolment (cancellationReason,cancelled,credit,discount,endWeek,info,old,registerDate,startWeek,clazzId,invoiceId,studentId) VALUES (NULL,0,0,'100%',21,NULL,0,'2023-11-11',12,5,4,134264);
INSERT INTO jae.Enrolment (cancellationReason,cancelled,credit,discount,endWeek,info,old,registerDate,startWeek,clazzId,invoiceId,studentId) VALUES (NULL,0,0,'0',31,NULL,0,'2023-11-11',25,1,5,134357);
INSERT INTO jae.Enrolment (cancellationReason,cancelled,credit,discount,endWeek,info,old,registerDate,startWeek,clazzId,invoiceId,studentId) VALUES (NULL,0,0,'100%',31,NULL,0,'2023-11-11',25,5,5,134357);

-- Attendance
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-08','Wednesday',NULL,'Y','22',1,130055);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-15','Wednesday',NULL,'O','23',1,130055);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-22','Wednesday',NULL,'O','24',1,130055);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-29','Wednesday',NULL,'O','25',1,130055);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-12-06','Wednesday',NULL,'O','26',1,130055);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-12-13','Wednesday',NULL,'O','27',1,130055);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-12-20','Wednesday',NULL,'O','28',1,130055);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-12-27','Wednesday',NULL,'O','29',1,130055);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2024-01-03','Wednesday',NULL,'O','30',1,130055);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2024-01-10','Wednesday',NULL,'O','31',1,130055);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-10-04','Wednesday',NULL,'Y','17',1,130365);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-10-11','Wednesday',NULL,'O','18',1,130365);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-10-18','Wednesday',NULL,'O','19',1,130365);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-10-25','Wednesday',NULL,'O','20',1,130365);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-01','Wednesday',NULL,'O','21',1,130365);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-08','Wednesday',NULL,'O','22',1,130365);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-15','Wednesday',NULL,'O','23',1,130365);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-22','Wednesday',NULL,'O','24',1,130365);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-29','Wednesday',NULL,'O','25',1,130365);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-12-06','Wednesday',NULL,'O','26',1,130365);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-10-25','Wednesday',NULL,'O','20',1,134174);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-01','Wednesday',NULL,'O','21',1,134174);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-08','Wednesday',NULL,'O','22',1,134174);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-15','Wednesday',NULL,'O','23',1,134174);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-22','Wednesday',NULL,'O','24',1,134174);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-29','Wednesday',NULL,'O','25',1,134174);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-12-06','Wednesday',NULL,'O','26',1,134174);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-12-13','Wednesday',NULL,'O','27',1,134174);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-12-20','Wednesday',NULL,'O','28',1,134174);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-12-27','Wednesday',NULL,'O','29',1,134174);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2024-01-03','Wednesday',NULL,'O','30',1,134174);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-08-30','Wednesday',NULL,'O','12',1,134264);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-09-06','Wednesday',NULL,'O','13',1,134264);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-09-13','Wednesday',NULL,'O','14',1,134264);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-09-20','Wednesday',NULL,'O','15',1,134264);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-09-27','Wednesday',NULL,'O','16',1,134264);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-10-04','Wednesday',NULL,'O','17',1,134264);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-10-11','Wednesday',NULL,'O','18',1,134264);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-10-18','Wednesday',NULL,'O','19',1,134264);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-10-25','Wednesday',NULL,'O','20',1,134264);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-01','Wednesday',NULL,'O','21',1,134264);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-11-29','Wednesday',NULL,'O','25',1,134357);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-12-06','Wednesday',NULL,'O','26',1,134357);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-12-13','Wednesday',NULL,'O','27',1,134357);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-12-20','Wednesday',NULL,'O','28',1,134357);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2023-12-27','Wednesday',NULL,'O','29',1,134357);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2024-01-03','Wednesday',NULL,'O','30',1,134357);
INSERT INTO jae.Attendance (attendDate,day,info,status,week,clazzId,studentId)
 VALUES ('2024-01-10','Wednesday',NULL,'O','31',1,134357);
/*
-- Outstanding
INSERT INTO jae.Outstanding (amount,info,paid,paymentId,registerDate,remaining,invoiceId) VALUES (765.00,NULL,65.00,2,'2023-11-11',700.00,2);
INSERT INTO jae.Outstanding (amount,info,paid,paymentId,registerDate,remaining,invoiceId) VALUES (555.00,NULL,55.00,5,'2023-11-11',500.00,5);

-- Payment
INSERT INTO jae.Payment (amount,info,method,registerDate,total,invoiceId) VALUES (843.00,NULL,'cash','2023-11-11',843.00,1);
INSERT INTO jae.Payment (amount,info,method,registerDate,total,invoiceId) VALUES (65.00,NULL,'cash','2023-11-11',765.00,2);
INSERT INTO jae.Payment (amount,info,method,registerDate,total,invoiceId) VALUES (709.99,NULL,'bank','2023-11-11',709.99,3);
INSERT INTO jae.Payment (amount,info,method,registerDate,total,invoiceId) VALUES (700.00,NULL,'cash','2023-11-11',700.00,4);
INSERT INTO jae.Payment (amount,info,method,registerDate,total,invoiceId) VALUES (55.00,NULL,'cheque','2023-11-11',555.00,5);
*/