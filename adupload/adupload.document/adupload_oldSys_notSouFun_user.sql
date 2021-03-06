/****** Script for SelectTopNRows command from SSMS  ******/

--广告上传旧系统中非soufun的账户
SELECT b.UserName,mu.email,b.co,b.adtime from 
( 
SELECT UserName,COUNT(UserName) AS co,MAX(AddTime) AS adtime 
  FROM [SouFunAD].[dbo].[Log_Operation]
GROUP BY UserName
) b
INNER JOIN Mgr_User AS mu WITH (NOLOCK)
ON b.UserName = mu.mgrName
where charindex('soufun.com' , mu.email) = 0
ORDER BY adtime DESC

