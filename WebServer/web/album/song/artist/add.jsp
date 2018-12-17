<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Add artist</h1>

<s:include value="/general/errors.jsp" />

<s:if test="%{artists.size() == 0}" >
    <p>No artists available</p>
</s:if>
<s:else>
    <ul>
        <s:iterator value="artists" var="artist">
            <li>
                <a href='<s:url action="album_song_artist_add_post" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />&artist_id=<s:property value="artist_id" />'>
                    <s:property value="#artist.name" />
                </a>
            </li>
        </s:iterator>
    </ul>
</s:else>

<p><a href='<s:url action="album_song_artists" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />'>
    Back
</a></p>

</body>
</html>