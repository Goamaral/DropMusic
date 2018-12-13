<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Edit song</h1>
<s:form action="album_song_edit_post" >
    <s:textfield value="song.name" label='Name(%{song.name})' />
    <s:textfield value="song.info" label='Info(%{song.info})' />
    <s:submit value="Update" />
</s:form>
</body>
</html>