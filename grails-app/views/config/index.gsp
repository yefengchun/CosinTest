<%--
  Created by IntelliJ IDEA.
  User: alexa
  Date: 22.12.2015
  Time: 20:28
--%>
<html>
<head>
    <meta name="layout" content="config"/>
    <title><g:message code="default.config.label"/></title>
</head>

<body>
<div id="home" class="col-md-10" role="home">
    <div id="page-body" role="home">
        <div id="intro">
            <h1 class="content-expand">Application Status</h1>
            <ul class="expand-content">
                <li>Environment: ${grails.util.Environment.current.name}</li>
                <li>App profile: ${grailsApplication.config.grails?.profile}</li>
                <li>App version: <g:meta name="info.app.version"/></li>
                <li>Grails version: <g:meta name="info.app.grailsVersion"/></li>
                <li>Groovy version: ${GroovySystem.getVersion()}</li>
                <li>JVM version: ${System.getProperty('java.version')}</li>
                <li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
            </ul>

            <h1 class="content-expand">Artefacts</h1>
            <ul class="expand-content">
                <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
                <li>Domains: ${grailsApplication.domainClasses.size()}</li>
                <li>Services: ${grailsApplication.serviceClasses.size()}</li>
                <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
            </ul>

        </div>

        <h1 class="content-expand">Installed Plugins</h1>
        <ul class="expand-content">
            <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
                <li>${plugin.name} - ${plugin.version}</li>
            </g:each>
        </ul>
    </div>
    </div>
</div>                                                                          <!-- end row -->
</body>
</html>