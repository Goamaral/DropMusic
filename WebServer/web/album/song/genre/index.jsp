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

<h1>Song genres</h1>
<p><b>Genres </b><s:property value="song.genres" /></p>


<s:if test="%{current_user.isEditor}" >
    <p><a href="<s:url action="album_song_genre_add" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />">
        Add genre
    </a></p>
    <p><a href="<s:url action="album_song_genre_remove" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />">
        Remove genre
    </a></p>
</s:if>

<p><a href='<s:url action="album_song_show" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />'>
    Back
</a></p>
</body>
</html>