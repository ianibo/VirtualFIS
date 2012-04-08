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
        <li> <g:link controller="home" action="index">${user.username}</g:link> <span class="divider">/</span> </li>
        <li class="active">Manage Membership Requests</li>
      </ul>
    </div>

    <div class="row-fluid">
      <div>
        <div class="well">
          <h2>Manage Pending Membership Requests</h2>

          <table class="table table-striped table-bordered table-condensed">
            <thead>
              <tr>
                <th>User</th>
                <th>Email</th>
                <th>Organisation</th>
                <th>Role</th>
                <th>Status</th>
                <th>Date Requested</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${pendingRequests}" var="req">
                <tr>
                  <td>${req.person.username}</td>
                  <td>${req.person.email}</td>
                  <td>${req.org.name}</td>
                  <td>${req.role}</td>
                  <td><g:message code="cv.membership.status.${req.status}"/></td>
                  <td><g:formatDate format="dd MMMM yyyy" date="${req.dateRequested}"/></td>
                  <td><g:link controller="admin" action="actionMembershipRequest" params="${[req:req.id, act:'approve']}" class="btn" >Approve</g:link>
                      <g:link controller="admin" action="actionMembershipRequest" params="${[req:req.id, act:'deny']}" class="btn" >Deny</g:link></td>
                </tr>
              </g:each>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </body>
</html>
