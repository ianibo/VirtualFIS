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
        <li> <g:link controller='home' action='index'>User Home</g:link> <span class="divider">/</span> </li>
        <li class="active">Memberships</li>
      </ul>
    </div>

    <div class="row-fluid">
      <div class="span12">
        <h1>Administrative memberships</h1>
      </div>
    </div>

    <div class="row-fluid">
      <div class="span6">
        <div class="well">
          <h2>Existing Memberships</h2>

          <table class="table table-striped table-bordered table-condensed">
            <thead>
              <tr>
                <th>Organisation</th>
                <th>Role</th>
                <th>Status</th>
                <th>Date Requested / Actioned</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${user.adminAssociations}" var="assoc">
                <tr>
                  <td>${assoc.org.name}</td>
                  <td>${assoc.role}</td>
                  <td><g:message code="cv.membership.status.${assoc.status}"/></td>
                  <td><g:formatDate format="dd MMMM yyyy" date="${assoc.dateRequested}"/> / <g:formatDate format="dd MMMM yyyy" date="${assoc.dateActioned}"/></td>
                  <td><button class="btn">Remove</button></td>
                </tr>
              </g:each>
            </tbody>
          </table>
        </div>
      </div>

      <div class="span6">
        <div class="well">
          <h2>Request new membership</h2>
          <p>Select an organisation and a role below. Requests to join existing
             organisations will be referred to the administrative users of that organisation. If you feel you should be the administrator of an organisation
             please contact OFS for suppot. Please ensure that the email address you have registered with and confirmed matches the organisation you
             are attempting to join/create</p>
  
          <g:form controller="home" action="processJoinRequest" form class="form-search">
            <g:select name="org"
                      from="${com.k_int.vfis.Organisation.list()}"
                      optionKey="id"
                      optionValue="name"
                      class="input-medium">
            </g:select>
            <g:select name="role" from="${['Administrator', 'Staff']}"/>
            <button class="btn" data-complete-text="Request Membership" type="submit">Request Membership</button>
          </g:form>
          <form>
        </div>
      </div>
    </div>
  </body>
</html>
