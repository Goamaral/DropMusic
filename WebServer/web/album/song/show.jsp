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

<h1>Song</h1>

<s:include value="/general/errors.jsp" />

<p><b>Name </b><s:property value="song.name" /></p>
<p><b>Info </b><s:property value="song.info" /></p>
<p><b>Artists </b><s:property value="song.artists" /></p>
<p><b>Genres </b><s:property value="song.genres" /></p>

<s:if test="%{current_user.isEditor}" >
    <p><a href='<s:url action="album_song_artists" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />'>
        Artists
    </a></p>
    <p><a href="<s:url action="album_song_genres" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />">
        Genres
    </a></p>
    <p><a href="<s:url action="album_song_edit" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />">
        Edit song
    </a></p>
    <p><a href="<s:url action="album_song_delete" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />">
        Delete song
    </a></p>
</s:if>

<p><a href="<s:url action="album_song_upload" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />">
    Upload
</a></p>
<p><a href="<s:url action="album_song_download" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />">
    Download
</a></p>
<p><a href="<s:url action="album_song_share" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />">
    Share
</a></p>

<p><a href='<s:url action="album_songs" />?album_id=<s:property value="album_id" />'>Back</a></p>
</body>
</html>