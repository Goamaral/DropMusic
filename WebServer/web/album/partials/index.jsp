<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="container">
    <s:if test="%{albums.size() == 0}" >
        <p>No albums available</p>
    </s:if>
    <s:else>
        <ul>
            <s:iterator value="albums" var="album">
                <li>
                    <a href="<s:url action="album_show" />?id=<s:property value="#album.id"/>">
                        <s:property value="#album.name"/>
                    </a>
                </li>
            </s:iterator>
        </ul>
    </s:else>
</div>