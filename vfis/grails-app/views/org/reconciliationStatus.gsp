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
          <div>Found ${hitcount} records, showing page ${pageno+1} of ${maxpages}</div>
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
                <td>
                  <g:if test="${r.targetRecord}">
                    <g:link controller="content" action="edit" id="${r.targetRecord}">${r.displayText} (${r.docid})</g:link>
                  </g:if>
                  <g:else>
                    ${r.displayText} (${r.docid})
                  </g:else>
                </td>
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
                  <div class="btn-group">
                    <g:if test="${r.targetRecord}">
                      <g:link controller="content" action="edit" id="${r.targetRecord}" class="btn btn-primary btn-small">Edit</g:link>
                    </g:if>
                  </div>
                </td>
              </tr>
            </g:each>
          <tbody>
        </table>
        <div class="pagination pagination-right">
          <ul>
            <li><g:link controller="org" action="reconciliationStatus" id="${params.id}">First</g:link></li>
            <g:if test="${pageno > 0}">
              <li><g:link controller="org" action="reconciliationStatus" id="${params.id}" params="${[pageno:pageno-1]}">Prev</g:link></li>
            </g:if>
            <g:each in="${(pagstart..<pagend)}" var="p">
              <g:if test="${pageno==p}">
                <li class="active"><g:link controller="org" action="reconciliationStatus" id="${params.id}" params="${[pageno:p]}">${p+1}</g:link></li>
              </g:if>
              <g:else>
                <li><g:link controller="org" action="reconciliationStatus" id="${params.id}" params="${[pageno:p]}">${p+1}</g:link></li>
              </g:else>
            </g:each>
            <g:if test="${pageno < maxpages }">
              <li><g:link controller="org" action="reconciliationStatus" id="${params.id}" params="${[pageno:pageno+1]}">Next</g:link></li>
            </g:if>
            <li><g:link controller="org" action="reconciliationStatus" id="${params.id}" params="${[pageno:maxpages-1]}">Last</g:link></li>
          </ul>
        </div>

      </div>
    </div>

  </body>
</html>
