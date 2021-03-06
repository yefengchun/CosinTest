<div class="footer navbar">
    <div class="container">
        <ul class="nav nav-tabs nav-justified">
            <li class="${controllerName == null ? 'active' : ''}">
                <a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
            </li>
            <li class="${actionName == 'imprint' ? 'active' : ''}">
                <a href="${createLink(uri: '/common/imprint')}"><g:message code="imprint.label"/> </a>
            </li>
            <li class="${actionName == 'disclaimer' ? 'active' : ''}">
                <g:link controller="common" action="disclaimer">
                    <g:message code="disclaimer.label"/>
                </g:link>
            </li>
            <li role="presentation" class="disabled">
                <a href="#"><g:message code="app.version.label"/> <g:meta name="info.app.version"/></a>
            </li>
            <li role="presentation" class="disabled">
                <a href="#"><g:formatDate date="${new Date()}" format="dd-MMM-yyyy"/></a>
            </li>
            <li role="presentation">
                <a href="https://grails.org/" target="_blank"><g:message code="info.grailsVersion.label"/> <g:meta
                        name="info.app.grailsVersion"/></a>
            </li>
        </ul>
    </div>
</div>