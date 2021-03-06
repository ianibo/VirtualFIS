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

      <div class="span4">
        <div class="well">
          <h2><g:link action="feedback" id="${params.id}">End User Feedback</g:link></h2>
           <table class="table table-striped table-bordered table-condensed">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>From</th>
                  <th>Category</th>
                  <th>Status</th>
                </tr>
                <tr>
                  <th colspan="4">Message</th>
                </tr>
              </thead>
              <tbody>
                <g:each in="${feedback}" var="f">
                  <tr>
                    <td><g:formatDate format="dd-MMM-yyyy" date="${f.messageTimeStamp}"/></td>
                    <td>${f.contactEmail} / ${f.contactName}</td>
                    <td>${f.category}</td>
                    <td>${f.status}</td>
                  </tr>
                  <tr>
                    <td colspan="4">${f.message}</td>
                  </tr>
                </g:each>
              </tbody>
           </table>
        </div>
      </div>

      <div class="span4">
        <div class="well">
          <h2>Records</h2>
          <h3>ECD Records</h3>
          System currently holds X ECD records
          <h3>FSD Records</h3>
          System currently holds Y ECD records
        </div>
      </div>

      <div class="span4">
        <div class="well">
          <h2>Reconciliation</h2>
          <h3>OFS</h3>
          No outstanding issues
          <h3>OFSTED</h3>
          No outstanding issues
        </div>
      </div>



    </div>

  </body>
</html>
