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

<s:if test="%{#songs.size() == 0}" >
    <p>No songs available</p>
</s:if>
<s:else>
    <ul>
        <s:iterator value="songs">
            <li>
                <a href="<s:url action="album_song_show" ><s:param name="song_id"><s:property value="id"/></s:param></s:url>">
                    <s:property value="name" /> by <s:property value="artists" /> /5 rating
                </a>
            </li>
        </s:iterator>
    </ul>
</s:else>

<s:if test="%{#current_user.isEditor}" >
    <p><a href="<s:url action="album_song_create" />">Add song</a></p>
</s:if>
</body>
</html>