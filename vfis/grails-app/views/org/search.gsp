<!doctype html>
<html>
  <head>
    <meta name="layout" content="bootstrap"/>
    <title>${org.name} - Search</title>
  </head>

  <body>
    <div class="row-fluid">
        <ul class="breadcrumb">
        <li> <g:link path="/">Virtual FIS</g:link> <span class="divider">/</span> </li>
        <li> <g:link controller="org" action="dashboard" id="${org.id}">${org.name}</g:link> <span class="divider">/</span> </li>
        <li> <g:link controller="org" action="reconcileOfs" id="${org.id}">Search</g:link> </li>
      </ul>
    </div>

    <div class="row-fluid">
      <g:form class="well form-inline" action="search" controller="org" id="${params.id}" method="get">
          <div class="control-group">
            <label class="control-label" for="titlesearchinput">Record Title : </label>
            <input id="titlesearchinput "type="text" class="search-query" name="q" placeholder="Search" value="${params.q}">
            <label class="control-label" for="typesearchinput">Record Type : </label>
            <select id="typesearchinput" name="rectype" value="params.rectype"><option value="All">All</option><option value="ECD">ECD</option><option value="FSD">FSD</option></select>
            <button type="submit" class="btn btn-primary"><i class="icon-search icon-white"></i> Search</button>
          </div>
      </g:form>
    </div>

    <div class="row-fluid">
      <div class="well">

        <table class="table table-striped table-bordered table-condensed">
          <div>Found ${hitcount} records, showing page ${pageno+1} of ${maxpages}</div>
          <thead>
            <tr>
              <th>Record</th>
              <th>Type</th>
              <th>Title</th>
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
                <td>
                  <g:if test="${r.type='OFS:Service'}">ECD</g:if>
                  <g:else>FSD</g:else>
                </td>
                <td>${r.src.DC_Title}</td>
                <td>
                  <div class="btn-group">
                    <g:link controller="content" action="edit" id="${r._id}" class="btn btn-primary btn-small"><i class="icon-edit icon-white"></i> Edit</g:link>
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
