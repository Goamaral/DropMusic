<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Create genre</h1>

<s:include value="/general/errors.jsp" />

<s:form action="album_song_genre_create_post">
    <s:hidden name="album_id" />
    <s:hidden name="song_id" />
    <s:textfield name="genre.name" label="Name" />
    <s:submit value="Create"/>
</s:form>

<p><a href='<s:url action="album_song_genre_add" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />'>
    Back
</a></p>
</body>
</html>