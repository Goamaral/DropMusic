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
<p><b>Name </b><s:property value="album.name" /></p>
<p><b>Rating </b><s:property value="album.rating" />/5</p>
<p><b>Info </b><s:property value="album.info" /></p>
<p><b>Artists </b><s:property value="artistsString" /></p>
<p><b>Genres </b><s:property value="genresString" /></p>
<p><b>Release date </b><s:property value="album.releaseDateString" /></p>

<p><a href="<s:url action="album_critics" />?album_id=<s:property value="album.id" />">Critics</a></p>
<p><a href="<s:url action="album_songs" />?album_id=<s:property value="album.id" />">Songs</a></p>

<s:if test="%{current_user.isEditor}">
    <p><a href="<s:url action="album_edit" />?album_id=<s:property value="album.id" />">Edit album</a></p>
    <p><a href="<s:url action="album_delete" />?album_id=<s:property value="album.id" />">Delete album</a></p>
</s:if>

<a href="<s:url action="album_index" />">Back</a>

</body>
</html>