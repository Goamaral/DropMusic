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

<h1>Critics</h1>

<s:if test="%{critics.size() == 0}" >
    <p>No critics available</p>
</s:if>
<s:else>
    <ul>
        <s:iterator value="critics" var="critic">
            <li>
                <a href="<s:url action="album_critic_show" />?album_id=<s:property value="album_id" />&critic_id=<s:property value="#critic.id" />">
                    By <s:property value="#critic.author.username" /> with <s:property value="#critic.rating" />/5 rating
                </a>
            </li>
        </s:iterator>
    </ul>
</s:else>

<p><a href="<s:url action="album_critic_create" />?album_id=<s:property value="album_id" />">Add critic</a></p>
<p><a href="<s:url action="album_show" />?album_id=<s:property value="album_id" />">Back</a></p>
</body>
</html>