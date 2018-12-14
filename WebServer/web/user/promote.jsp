<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Promote user to editor</h1>

<s:include value="/general/errors.jsp" />

<s:if test="%{users.size() == 0}">
    <p>No normal users available</p>
</s:if>
<s:else>
    <s:iterator value="users" var="user" >
        <a href="<s:url action="user_promote_post" ><s:param name="id"><s:property value="#user.id"/></s:param></s:url>">
            <s:property value="#user.username"/>
        </a>
    </s:iterator>
</s:else>

<p><a href="<s:url action="user_dashboard" />">Dashboard</a></p>
</body>
</html>