<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Add song</h1>

<s:form action="album_song_create_post">
    <s:textfield name="song.name" label="Name" />
    <s:textfield name="song.info" label="Info" />
    <s:submit value="Add"/>
</s:form>
</body>
</html>