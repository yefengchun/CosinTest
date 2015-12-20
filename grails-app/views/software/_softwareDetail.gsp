<table class="table table-striped">
    <tbody>
        <f:with bean="software">
            <f:display property="softwareName"/>
            <f:display property="softwareVersion"/>
            <f:display property="softwareVendor"/>
            <f:display property="system"/>
            </f:with>
        </tbody>
</table>
<g:form controller="software" id="${software.id}">
    <g:actionSubmit action="edit" class="btn btn-primary"
                    value="${message(code: 'default.button.edit.label', default: 'Edit')}"/>
</g:form>
<hr/>
<h3><g:message code="qualification.list.label"/> </h3>
%{--Display the sorted qualifications with the newest Qualification on Top--}%
<g:render template="/layouts/listQualifications" model="[model: software.qualifications.sort { it.qualificationDate }.reverse()]"/>
<hr/>
<g:render template="/layouts/addQualification"/>