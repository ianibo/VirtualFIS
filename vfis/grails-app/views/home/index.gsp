<!doctype html>
<html>
  <head>
    <meta name="layout" content="bootstrap"/>
    <title>VFIS Home</title>
  </head>

  <body>
    <div class="row-fluid">
      <ul class="breadcrumb">
        <li> Virtual FIS <span class="divider">/</span> </li>
        <li class="active">User Home</li>
      </ul>
    </div>

    <div class="row-fluid">
      <div>
        <h1>User Dashboard</h1>
      </div>
    </div>

    <div class="row-fluid">
      <div class="well">
        <h2>Alerts</h2>
         <table class="table table-striped table-bordered table-condensed">
            <thead>
              <tr>
                <th>Date</th>
                <th>From</th>
                <th>Category</th>
              </tr>
              <tr>
                <th>Message</th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${feedback}" var="f">
                <tr>
                  <td><g:formatDate format="yyyy-MM-dd" date="${f.messageTimeStamp}"/></td>
                  <td>${f.contactEmail} / ${f.contactName}</td>
                  <td>${f.category}</td>
                </tr>
                <tr>
                  <td>${f.message}</td>
                </tr>
              </g:each>
            </tbody>
         </table>
      </div>
    </div>

    <div class="row-fluid">
      <div class="span6">
        <div class="well">
          <h2>Records you manage / own</h2>
        </div>
      </div>

      <div class="span6">
        <div class="well">
          <h2>Collections you manage / own</h2>
        </div>
      </div>
    </div>

  </body>
</html>
