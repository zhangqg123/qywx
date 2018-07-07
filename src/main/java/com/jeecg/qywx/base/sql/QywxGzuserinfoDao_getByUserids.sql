SELECT * FROM qywx_gzuserinfo
<#if userids ?exists && userids ?length gt 0>
	where userid in (${userids})
</#if> 