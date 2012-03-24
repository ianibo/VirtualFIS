<!doctype html>
<html>
  <head>
    <meta name="layout" content="bootstrap"/>
    <title>VFIS Home</title>
  </head>

  <body>
    <div class="row-fluid">
        <ul class="breadcrumb">
        <li>
          <g:link controller='home' action='index'>FIS Home</g:link> <span class="divider">/</span>
        </li>
        <li class="active">Memberships</li>
      </ul>
    </div>

    <div class="row-fluid">
      <div class="well">
        <h1>Memberships</h1>
        <h2>Request new membership</h2>
        <p>Type the name of an organisation below. New organisations are created after approval by a system administrator. Requests to join existing
           organisations will be referred to the user managing that organisation. If you feel you should be the administrator of an organisation
           please contact OFS for suppot. Please ensure that the email address you have registered with and confirmed matches the organisation you
           are attempting to join/create</p>
        <g:form controller="home" action="processJoinRequest">
          <g:select name="org"
                    from="${com.k_int.vfis.Organisation.list()}"
                    optionKey="id"
                    optionValue="name">
          </g:select>
        </g:form>
        <form>
      </div>
    </div>
  </body>
</html>
