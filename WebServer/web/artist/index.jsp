<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<s:include value="/websocket.jsp" />

<h1>Artists</h1>
<s:include value="/artist/partials/index.jsp" />

<s:if test="%{current_user.isEditor}" >
    <p><a href="<s:url action="artist_create" />">Create artist</a></p>
</s:if>

<p><a href="<s:url action="artist_search" />">Search songs</a></p>
<p><a href="<s:url action="user_dashboard" />">Back</a></p>
</body>
</html>