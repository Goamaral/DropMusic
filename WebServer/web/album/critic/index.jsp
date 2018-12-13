<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Critics</h1>

<s:if test="%{#critics.size() == 0}" >
    <p>No critics available</p>
</s:if>
<s:else>
    <ul>
        <s:iterator value="critics">
            <li>
                <a href="<s:url action="album_critic_show" ><s:param name="critic_id"><s:property value="id"/></s:param></s:url>">
                    By <s:property value="author_username" /> with <s:property value="rating" /> /5 rating
                </a>
            </li>
        </s:iterator>
    </ul>
</s:else>

<p><a href="<s:url action="album_critic_create" />">Add critic</a></p>
</body>
</html>