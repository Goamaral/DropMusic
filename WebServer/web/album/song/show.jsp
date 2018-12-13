<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Song</h1>

<b>Name</b><p><s:property value="song.name" /></p>
<b>Info</b><p><s:property value="song.info" /></p>
<b>Artists</b><p><s:property value="song.artists" /></p>
<b>Genres</b><p><s:property value="song.genres" /></p>

<s:if test="%{#current_user.isEditor}" >
    <p><a href="<s:url action="album_song_artists" />">Artists</a></p>
    <p><a href="<s:url action="album_song_genres" />">Genres</a></p>
    <p><a href="<s:url action="album_song_edit" />">Edit song</a></p>
    <p><a href="<s:url action="album_song_delete" />">Delete song</a></p>
</s:if>

<p><a href="<s:url action="album_song_upload" />">Upload</a></p>
<p><a href="<s:url action="album_song_download" />">Download</a></p>
<p><a href="<s:url action="album_song_share" />">Share</a></p>
</body>
</html>