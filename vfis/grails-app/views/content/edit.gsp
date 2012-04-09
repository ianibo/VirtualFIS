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

    <div class="row-fluid">
      <div class="well">
        <form class="form-horizontal">
          <g:render template="/content/dynamicComponent" model="[root:record, layoutnode:layout]"/>
        </form>
      </div>
    </div>

  </body>
</html>
