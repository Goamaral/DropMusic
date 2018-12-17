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

<h1>Add critic</h1>

<s:include value="/general/errors.jsp" />

<s:form action="album_critic_create_post">
    <s:hidden name="album_id" />
    <s:textfield name="critic.rating" label="Rating(x/5)" />
    <s:textfield name="critic.justification" label="Justification" />
    <s:submit value="Add"/>
</s:form>

<p><a href="<s:url action="album_critics" />?album_id=<s:property value="album_id" />">Back</a></p>
</body>
</html>