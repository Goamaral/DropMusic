<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Albums</h1>
<s:include value="/album/partials/index.jsp" />

<s:if test="%{current_user.isEditor}" >
    <p><a href="<s:url action="album_create" />">Create album</a></p>
</s:if>

<p><a href="<s:url action="album_search" />">Search songs</a></p>
<p><a href="<s:url action="user_dashboard" />">Back</a></p>
</body>
</html>