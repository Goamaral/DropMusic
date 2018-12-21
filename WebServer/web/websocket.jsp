<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>

<div id="notification"></div>

<script>
    var user_id = <s:property value="current_user.id" />
    var host = "ws://" + window.location.host + "/WebServer/ws"
    console.log(host);

    if ('WebSocket' in window) {
        window.websocket = new WebSocket(host)
    } else if ('MozWebSocket' in window) {
        window.websocket = new MozWebSocket(host)
    }

    console.log("SS")
    console.log("Websocket " + window.websocket)

    window.websocket.onopen = function() {
        console.log("CONNECTED")
    }

    window.websocket.onclose = function() {
        console.log("CLOSE")
    }

    window.websocket.onmessage = function(ev) {
        console.log("MESSAGE " + ev.data)
        var message_parts = ev.data.split("|")
        if (user_id == message_parts[0]) {
            $("#notification").append("<p>" + message_parts[1] + "</p>")
        }
    }
</script>