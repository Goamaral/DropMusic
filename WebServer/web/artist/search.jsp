<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DropMusic</title>
</head>

<body>
<s:include value="/websocket.jsp" />

<h1>Search</h1>
<input type="text" id="query"><button id="query_button">Search</button>

<s:include value="/artist/partials/search.jsp" />

<s:include value="/general/errors.jsp" />

<p><a href="<s:url action="album_index" />">Back</a></p>

<script>
    window.onload = function() {
        $("#query_button").click(function() {
            $.post("<s:url action="artist_search_post" />", { query: $("#query").val() }, function(data) {
                $("#container").html(data)
            })
        })
    }
</script>

</body>
</html>