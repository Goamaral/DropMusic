<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Songs</h1>

<s:include value="/general/errors.jsp" />

<s:if test="%{songs.size() == 0}" >
    <p>No songs available</p>
</s:if>
<s:else>
    <ul>
        <s:iterator value="songs" var="song">
            <li>
                <a href='<s:url action="album_song_show" />?album_id=<s:property value="album_id" />&song_id=<s:property value="#song.id" />'>
                    <s:property value="#song.name" /> by <s:property value="#song.artists" />
                </a>
            </li>
        </s:iterator>
    </ul>
</s:else>

<s:if test="%{current_user.isEditor}" >
    <p><a href='<s:url action="album_song_create" />?album_id=<s:property value="album_id" />'>Add song</a></p>
</s:if>

<p><a href='<s:url action="album_show" />?album_id=<s:property value="album_id" />'>Back</a></p>
</body>
</html>