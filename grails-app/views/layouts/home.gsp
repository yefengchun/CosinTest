<!doctype html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>COSIN &raquo COmputerized Software INventory<g:layoutTitle default="Welcome"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%-- TODO
  Individuelle Scripts auswählen, damit nicht alle aufschlagen!!! --%>
    <asset:javascript src="jquery-2.1.3.js"/>
    <asset:javascript src="bootstrap-all.js"/>
    <asset:stylesheet src="bootstrap-all.css"/>
    <asset:stylesheet src="font-awesome-all.css"/>
    <%-- Noch mehr Gelaber --%>
    <g:layoutHead/>

</head>

<body>

<g:render template="/navigation/navMain"/>                                          <!-- Navigation Bar Top -->

    <g:layoutBody/>                                                             <!-- Main Content  -->
                                                                                    <!-- end container -->
<g:render template="/navigation/footer"/>
<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
<asset:javascript src="fadeAndToggle.js"/>
</body>
</html>