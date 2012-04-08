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
          <div class="well">
            Currently reconciling records from OFS. ${(int)progress}% completed - ${reconciliation.job?.start} out of ${reconciliation.job?.max} total records
            <div class="progress progress-striped active">
              <div class="bar" style="width: ${(int)progress}%;"></div>
            </div>
          <div>
        </g:if>
        <g:else>
          no currently active reconciliation <g:link controller="org" action="requestReconciliation" id="${params.id}" class="btn">Start</g:link>
        </g:else>
      </div>
    </div>
    <br/>
    <div class="row-fluid">
      <div class="well">
        <table class="table table-striped table-bordered table-condensed">
          <thead>
            <tr>
              <th>Record</th>
              <th>Last Modified</th>
              <th>Changes Detected</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${records}" var="r">
              <tr>
                <td>${r.displayText} (${r.docid})</td>
                <td><g:formatDate format="dd MMMM HH:mm" date="${r.lastmod}"/></td>
                <td>
                  <ul>
                    <g:each in="${r.changes}" var="c">
                      <li>${c.desc} 
                      <g:if test="${c.status=='autoaccepted'}">
                        <span class="label label-success">Auto Accepted</span>
                      </g:if>
                      <g:else>
                        <span class="label label-important">${c.status}</span>
                      </g:else>
                      </li>
                    </g:each>
                  </ul>
                </td>
                <td>
                  <g:if test="${c.targetRecord}">
                    <g:link controller="content" action="edit" id="${c.targetRecord}" class="button">Edit</g:link>
                  </g:if>
                </td>
              </tr>
            </g:each>
          <tbody>
        </table>
      </div>
    </div>

  </body>
</html>
