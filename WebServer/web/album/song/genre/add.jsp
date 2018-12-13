<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Add genre</h1>

<s:if test="%{#geners.size() == 0}" >
    <p>No genres available</p>
</s:if>
<s:else>
    <ul>
        <s:iterator value="genres">
            <li>
                <a href="<s:url action="album_song_genre_add_post" ><s:param name="genre_id"><s:property value="id"/></s:param></s:url>">
                    <s:property value="name" />
                </a>
            </li>
        </s:iterator>
    </ul>
</s:else>

<s:if test="%{#current_user.isEditor}" >
    <p><a href="<s:url action="album_song_genre_create" />">Create genre</a></p>
</s:if>
</body>
</html>