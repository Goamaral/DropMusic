<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Add critic</h1>

<s:form action="album_critic_create_post">
    <s:textfield name="critic.rating" label="Rating(x/5)" />
    <s:textfield name="critic.justification" label="Justification" />
    <s:submit value="Add"/>
</s:form>
</body>
</html>