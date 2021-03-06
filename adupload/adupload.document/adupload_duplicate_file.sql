/************************************************************
 * Code formatted by SoftTree SQL Assistant ?v6.5.278
 * Time: 2017/5/8 17:01:29
 ************************************************************/

/****** Script for SelectTopNRows command from SSMS  ******/

-- 广告上传查找重复

SELECT  * FROM (
	SELECT 
		SourceID
		,COUNT(SourceID) AS co
	FROM   [SouFunAD].[dbo].[SlFile]
	WHERE SourceID <> ''
	GROUP BY SourceID
) b
INNER JOIN [SouFunAD].[dbo].[SlFile]
ON [SlFile].SourceID = b.SourceID
WHERE b.co > 1 --AND b.SourceID = '134c7cc9742d477fb997b85adb73ba59'
ORDER BY [SouFunAD].[dbo].[SlFile].UpdateTime DESC
--------------------------------------------------

SELECT DISTINCT 
	[SouFunAD].[dbo].[SlFile].SourceID
	,[SouFunAD].[dbo].[SlFile].ProjectName
	,[SouFunAD].[dbo].[SlFile].SourceURL
	,[SouFunAD].[dbo].[SlFile].SourceSize
	,[SouFunAD].[dbo].[SlFile].UploadUsername
	--,[SouFunAD].[dbo].[SlFile].UpdateTime
FROM (
SELECT SourceID,COUNT(SourceID) AS co
FROM   [SouFunAD].[dbo].[SlFile]
WHERE SourceID <> ''
GROUP BY SourceID
) b
INNER JOIN [SouFunAD].[dbo].[SlFile]
ON [SlFile].SourceID = b.SourceID
WHERE b.co > 1
ORDER BY 
	--[SouFunAD].[dbo].[SlFile].UpdateTime DESC
	[SouFunAD].[dbo].[SlFile].SourceID
	,[SouFunAD].[dbo].[SlFile].ProjectName
	,[SouFunAD].[dbo].[SlFile].SourceURL
	,[SouFunAD].[dbo].[SlFile].SourceSize
	,[SouFunAD].[dbo].[SlFile].UploadUsername
	

[SouFunAD].[dbo].[SlFile].UpdateTime DESC
--------------------------------------------------

SELECT  COUNT(b.SourceID) FROM (
	SELECT SourceID,COUNT(SourceID) AS co
	FROM   [SouFunAD].[dbo].[SlFile]
	GROUP BY SourceID
) b
WHERE b.co = 1
