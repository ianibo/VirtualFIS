<!doctype html>
<html>
  <head>
    <meta name="layout" content="bootstrap"/>
    <title>Edit Record</title>
  </head>

  <body>
    <div class="row-fluid">
        <ul class="breadcrumb">
        <li> <g:link path="/">Virtual FIS</g:link> <span class="divider">/</span> </li>
        <li> <g:link controller="content" action="edit" id="${params.id}">Edit Record</g:link> </li>
      </ul>
    </div>

    <g:if test="${flash.message != null}">
      <div class="alert alert-success">
        <a class="close" data-dismiss="alert">×</a>
        <h4 class="alert-heading">Success!</h4>
        <g:message code="${flash.message}" args="${flash.args}" default="${flash.default}"/>
      </div>
    </g:if>

    <div class="row-fluid">
      <div class="well">
        <g:form controller="content" action="save" class="form-horizontal" id="${params.id}">
          <g:render template="/content/dynamicComponent" model="[root:record, layoutnode:layout,parentpath:'src']"/>
          <button class="btn" data-complete-text="Save" type="submit">Save</button>
        </g:form>
      </div>
    </div>

  </body>
</html>
