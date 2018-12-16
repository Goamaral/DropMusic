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

<s:include value="/general/errors.jsp" />

<s:form action="album_song_create_post">
    <s:hidden name="album_id" />
    <s:textfield name="song.name" label="Name" />
    <s:textfield name="song.info" label="Info" />
    <s:submit value="Add"/>
</s:form>

<p><a href='<s:url action="album_songs" />?album_id=<s:property value="album_id" />'>Back</a></p>

</body>
</html>