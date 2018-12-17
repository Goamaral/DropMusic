<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>

<h1>Dashboard</h1>

<p><a href="<s:url action="album_index" />">Albums</a></p>
<p><a href="<s:url action="artist_index" />">Artists</a></p>
<s:if test="%{current_user.isEditor}" >
    <p><a href="<s:url action="user_promote" />">Promote user to editor</a></p>
</s:if>

<s:if test="%{uid != null}" >
    <p><a href="<s:url action="user_associate_dropbox" />">Associate with dropbox</a></p>
</s:if>


<p><a href="<s:url action="user_logout" />">Logout</a></p>

</body>
</html>