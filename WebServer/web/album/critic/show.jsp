<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Critic</h1>

<b>Album</b><p><s:property value="current_critic.album_name" /></p>
<b>Rating</b><p><s:property value="current_critic.rating" />/5</p>
<b>Justification</b><p><s:property value="current_critic.justification" /></p>
</body>
</html>