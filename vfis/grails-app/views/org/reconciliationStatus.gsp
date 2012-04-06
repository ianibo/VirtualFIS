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
            Currently reconciling records from OFS. ${progress} ${reconciliation.job?.max} / ${reconciliation.job?.start}
            <div class="progress progress-striped active">
              <div class="bar" style="width: ${(int)progress}%;"></div>
            </div><br/>
            Currently active reconciliation : ${reconciliation}
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
              <th>Record identifier</th>
              <th>Record Last Modified</th>
              <th>Changes Detected</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${records}" var="r">
              <tr>
                <td>${r.docid}</td>
                <td>${r.lastmod}</td>
                <td>
                  <ul>
                    <g:each in="${r.changes}" var="c">
                      <li>${c.desc}</li>
                    </g:each>
                  </ul>
                </td>
                <td></td>
              </tr>
            </g:each>
          <tbody>
        </table>
      </div>
    </div>

  </body>
</html>
