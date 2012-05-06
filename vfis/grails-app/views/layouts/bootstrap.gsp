<%@ page import="org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes" %>
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title><g:layoutTitle default="${meta(name: 'app.name')}"/></title>
    <meta name="description" content="Virtual FIS System">
    <meta name="author" content="Knowledge Integration Ltd / Open Family Services">

    <meta name="viewport" content="initial-scale = 1.0">

    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <r:require modules="scaffolding"/>

    <!-- Le fav and touch icons -->
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="72x72" href="${resource(dir: 'images', file: 'apple-touch-icon-72x72.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-114x114.png')}">

    <g:layoutHead/>
    <r:layoutResources/>
  </head>

  <body>


<div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
    <div class="container">
      <a class="brand" href="#">OFS Virtual FIS</a>
      <div class="nav-collapse">
        <ul class="nav">
          <li<%= request.forwardURI == "${createLink(uri: '/')}" ? ' class="active"' : '' %>><a href="${createLink(uri: '/')}">Home</a></li>
          <sec:ifAllGranted roles="ROLE_USER">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">Actions <b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li><g:link controller="Home" action="index">My Dashboard</g:link></li>

                <g:if test="${org}">
                  <li><hr/></li>
                  <li><g:link controller="org" action="dashboard" id="${org.id}"><b>Selected Org: ${org.name}</b></g:link></li>
                  <li><g:link controller="org" action="search" id="${org.id}">Search</g:link></li
                  <li><g:link controller="org" action="reconciliationStatus" id="${org.id}">OFS Reconciliation</g:link></li>
                </g:if>

                <sec:ifAllGranted roles="ROLE_ADMIN">
                  <li><hr/></li>
                  <li><g:link controller="admin" action="membershipRequests">Manage Membership Requests</g:link></li>
                </sec:ifAllGranted>
              </ul>
            </li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">Your Organisations<b class="caret"></b></a>
              <ul class="dropdown-menu">
                <g:each in="${user?.adminAssociations}" var="assoc">
                  <li><g:link controller="Org" action="dashboard" id="${assoc.org.id}">${assoc.org.name}</g:link></li>
                </g:each>
                <li><g:link controller="Home" action="memberships">Request and Manage Memberships</g:link></li>
              </ul>
            </li>
          </sec:ifAllGranted>
        </ul>
        <ul class="nav pull-right">
          <li>
            <form class="navbar-search pull-right" action="">
              <input type="text" class="search-query span2" placeholder="Search">
            </form>
          </li>
          <sec:ifNotLoggedIn><li><g:link controller="login" action="auth">Login</g:link></li><li><g:link controller="register" action="index">Register</g:link></li></sec:ifNotLoggedIn>
          <sec:ifAllGranted roles="ROLE_USER"><li><a href="Logout">Logout (<sec:username/>)</a></li></sec:ifAllGranted>
        </ul>
      </div><!-- /.nav-collapse -->
    </div><!-- /.container -->
  </div><!-- /.navbar-inner -->
</div><!-- /.navbar -->

    <div class="container-fluid">
      <g:layoutBody/>
    </div>

    <r:layoutResources/>

  </body>
</html>
