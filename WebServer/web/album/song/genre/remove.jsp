<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Remove genre</h1>

<s:include value="/general/errors.jsp" />

<s:if test="%{genres.size() == 0}" >
    <p>No genres available</p>
</s:if>
<s:else>
    <ul>
        <s:iterator value="genres" var="genre">
            <li>
                <a href='<s:url action="album_song_genre_remove_post" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />&genre_id=<s:property value="#genre.id" />'>
                    <s:property value="#genre.name" />
                </a>
            </li>
        </s:iterator>
    </ul>
</s:else>

<p><a href='<s:url action="album_song_genres" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />'>
    Back
</a></p>

</body>
</html>