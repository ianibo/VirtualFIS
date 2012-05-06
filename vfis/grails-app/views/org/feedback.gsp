<!doctype html>
<html>
  <head>
    <meta name="layout" content="bootstrap"/>
    <title>VFIS Home</title>
  </head>

  <body>
    <div class="row-fluid">
        <ul class="breadcrumb">
        <li> <g:link path="/">Virtual FIS</g:link> <span class="divider">/</span> </li>
        <li> <g:link controller="org" action="dashboard" id="${org.id}">${org.name}</g:link> </li>
      </ul>
    </div>

    <!--http://www.w3resource.com/twitter-bootstrap/forms-tutorial.php-->
    <!-- http://webdesign.tutsplus.com/tutorials/htmlcss-tutorials/stepping-out-with-bootstrap-from-twitter/ -->
    <!-- http://stackoverflow.com/questions/9534249/twitter-bootstrap-form-inline-stretch -->
    <!-- http://jsfiddle.net/CdNef/ -->
    <!-- http://datatables.net/blog/Microsoft_CDN -->
    <div class="row-fluid">
      <div>
        <h1>${org.name} Dashboard</h1>
      </div>
    </div>

    <div class="row-fluid">

      <div class="span12">
        <div class="well">
          <h2><g:link action="feedback" id="${params.id}">End User Feedback</g:link></h2>
           <table class="table table-striped table-bordered table-condensed">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>From</th>
                  <th>Category</th>
                  <th>Status</th>
                  <th>Message</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                <g:each in="${feedback}" var="f">
                  <tr>
                    <td style="white-space: nowrap"><g:formatDate format="dd-MMM-yyyy" date="${f.messageTimeStamp}"/></td>
                    <td>${f.contactEmail} / ${f.contactName}</td>
                    <td>${f.category}</td>
                    <td>${f.status}</td>
                    <td>${f.message}</td>
                    <td>
                     <g:link controller="org" action="issue" id="${params.id}" params="${[issue:f.id, act:'resolved']}" class="btn" >Resolved</g:link>
                     <g:link controller="org" action="issue" id="${params.id}" params="${[issue:f.id, act:'spam']}" class="btn" >Spam</g:link>
                    </td>
                  </tr>
                </g:each>
              </tbody>
           </table>
        </div>
      </div>
    </div>

  </body>
</html>
