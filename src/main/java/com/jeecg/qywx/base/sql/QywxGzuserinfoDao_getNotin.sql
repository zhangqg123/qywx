select * from qywx_gzuserinfo qg where NOT EXISTS (SELECT 1 FROM notice_article_record nar 
WHERE nar.userid = qg.userid and nar.articleid = ${articleid} )
and concat(',',qg.department,',') regexp concat(',(',replace(:qywxGzuserinfo.departmentSql,',','|'),'),')
order by qg.department