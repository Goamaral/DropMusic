<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Song genres</h1>
<b>Genres</b><p><s:property value="song.genres" /></p>


<s:if test="%{#current_user.isEditor}" >
    <p><a href="<s:url action="album_song_genre_add" />">Add genre</a></p>
    <p><a href="<s:url action="album_song_genre_remove" />">Remove genre</a></p>
</s:if>
</body>
</html>