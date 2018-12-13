<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Album</h1>
<b>Name</b><p><s:property value="current_album.name" /></p>
<b>Rating</b><p><s:property value="current_album.rating" />/5</p>
<b>Info</b><p><s:property value="current_album.info" />/5</p>
<b>Artists</b><p><s:property value="artists" /></p>
<b>Genres</b><p><s:property value="genres" /></p>
<b>Release date</b><p><s:property value="current_album.releaseDateString" /></p>

<p><a href="<s:url action="album_critics" />">Critics</a></p>
<p><a href="<s:url action="album_songs" />">Songs</a></p>

<s:if test="%{#current_user.isEditor}">
    <p><a href="<s:url action="album_edit" />">Edit album</a></p>
    <p><a href="<s:url action="album_delete" />">Delete album</a></p>
</s:if>

</body>
</html>