<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="container">
    <s:if test="%{artists.size() == 0}" >
        <p>No artists available</p>
    </s:if>
    <s:else>
        <ul>
            <s:iterator value="artists" var="artist">
                <li>
                    <a href="<s:url action="artist_show" />?artist_id=<s:property value="#artist.id"/>">
                        <s:property value="#artist.name"/>
                    </a>
                </li>
            </s:iterator>
        </ul>
    </s:else>
</div>