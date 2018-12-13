<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<h1>Albums</h1>
<s:if test="%{#albums.size() == 0}" >
    <p>No albums available</p>
</s:if>
<s:else>
    <ul>
    <s:iterator value="albums" var="album">
        <li>
            <a href="<s:url action="album_show" ><s:param name="id"><s:property value="#album.id"/></s:param></s:url>">
                <s:property value="#album.name"/>
            </a>
        </li>
    </s:iterator>
    </ul>
</s:else>

<s:if test="%{#current_user.isEditor}" >
    <p><a href="<s:url action="album_create" />">Create album</a></p>
</s:if>

<a href="<s:url action="album_search" />">Search songs</a>
</body>
</html>