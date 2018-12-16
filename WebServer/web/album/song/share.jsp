<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Share song</h1>

<p><a href='<s:url action="album_song_show" />?album_id=<s:property value="album_id" />&song_id=<s:property value="song_id" />'>Back</a></p>

</body>
</html>