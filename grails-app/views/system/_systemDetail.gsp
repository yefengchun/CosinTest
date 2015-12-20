<%--
  Created by IntelliJ IDEA.
  User: Alexander
  Date: 15.10.2015
  Time: 18:05
--%>
<table class="table table-striped">

    <tbody>
        <f:with bean="system">
            <f:display property="systemName"/>
            <f:display property="systemDepartment"/>
            <f:display property="systemOwner"/>
            <f:display property="processOwner"/>
            <f:display property="area"/>
        </f:with>
    </tbody>
</table>
<g:form controller="system" id="${system.id}">
    <g:actionSubmit action="edit" class="btn btn-primary"
                    value="${message(code: 'default.button.edit.label', default: 'Edit')}"/>
</g:form>
<%-- Wenn das Template ausserhalb von System aufgerufen wird, sparen wir uns den anderen Kram
    TODO: Verstecken beim Aufruf, bei Bedarf aufklappen.
--%>
<g:if test="${controllerName.compareToIgnoreCase('system')==0}">
    <%-- Get the software of the system --%>
    <h3><g:message code="software.label"/></h3>
    <g:render template="/software/softwareList"/>
    <%-- Get the computers of the system --%>
    <h3><g:message code="system.servers.label"/></h3>
    <g:render template="/computer/computerList" model="${[model: system.servers]}"/>
    <h3><g:message code="system.clients.label"/></h3>
    <g:render template="/computer/computerList" model="${[model: system.clients]}"/>
</g:if>