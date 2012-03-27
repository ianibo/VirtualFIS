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
      <div class="span6">
        <div class="well">
          <h2>Search</h2>
        </div>
      </div>

      <div class="span6">
        <div class="well">
          <h2>Details</h2>
        </div>
      </div>
    </div>

  </body>
</html>
