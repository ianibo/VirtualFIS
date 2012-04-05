<!doctype html>
<html>
  <head>
    <meta name="layout" content="bootstrap"/>
    <title>${org.name} - Reconcile Records With OFS</title>
  </head>

  <body>
    <div class="row-fluid">
        <ul class="breadcrumb">
        <li> <g:link path="/">Virtual FIS</g:link> <span class="divider">/</span> </li>
        <li> <g:link controller="org" action="dashboard" id="${org.id}">${org.name}</g:link> <span class="divider">/</span> </li>
        <li> <g:link controller="org" action="reconcileOfs" id="${org.id}">Reconcile OFS</g:link> </li>
      </ul>
    </div>

    <div class="row-fluid">
      <div>
        <h1>${org.name} - Reconcile Records from OFS...</h1>
        <g:if test="${reconciliation?.active == true}">
          Currently active reconciliation : ${reconciliation}
        </g:if>
        <g:else>
          no currently active reconciliation <g:link controller="org" action="requestReconciliation" id="${params.id}" class="btn">Start</g:link>
        </g:else>
      </div>
    </div>

  </body>
</html>
