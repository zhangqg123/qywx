select * from qywx_gzuserinfo qg where 
concat(',',qg.department,',') regexp concat(',(',replace(:qywxGzuserinfo.departmentSql,',','|'),'),')
