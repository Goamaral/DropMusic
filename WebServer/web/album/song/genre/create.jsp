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

<s:form action="album_song_genre_create_post">
    <s:textfield name="genre.name" label="Name" />
    <s:submit value="Create"/>
</s:form>
</body>
</html>