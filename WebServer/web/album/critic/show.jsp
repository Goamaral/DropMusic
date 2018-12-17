<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<s:include value="/websocket.jsp" />

<h1>Critic</h1>

<s:include value="/general/errors.jsp" />

<p><b>Album </b><s:property value="critic.album.name" /></p>
<p><b>Rating </b><s:property value="critic.rating" />/5</p>
<p><b>Justification </b><s:property value="critic.justification" /></p>

<p><a href="<s:url action="album_critics" />?album_id=<s:property value="album_id" />">Back</a></p>
</body>
</html>