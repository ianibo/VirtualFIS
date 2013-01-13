<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
<body>

  <div class="navbar navbar-static-top">
    <div class="navbar-inner">
      <div class="container">
        <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">Menu</a>
        <div class="nav-collapse">
          <!-- Navigation links starts here -->
          <ul class="nav">
            <!-- Main menu -->
            <li><g:link controller="home" action="index">Home</g:link></li>
          </ul>
        </div>
      </div>
    </div>
  </div>

  <g:if test="${flash.message}">
  <div class="content">
    <div class="container"><div class="row"><div class="well span12" style="text-align:center;">
      <bootstrap:alert class="alert-info">${flash.message}</bootstrap:alert>
    </div></div></div>
  </div>
  </g:if>

  <g:if test="${flash.error}">
  <div class="content">
    <div class="container"><div class="row"><div class="well span12" style="text-align:center;">
      <bootstrap:error class="alert-info">${flash.error}</bootstrap:error>
    </div></div></div>
  </div>
  </g:if>



  <div class="content">
    <div class="container">
      <div class="row">
        <div class="span9">
          <div style="text-align:center;">
            <g:form action="index" method="get" class="form-inline">
              Postcode: <input name="postcode" placeholder="Enter your postcode..." type="text" />
              Keywords: <input name="keywords" placeholder="Keywords, eg Childcare..." type="text" />
              <button class='btn'>Go!</button>
            </g:form>
          </div>
        </div>
        <div class="span3">
        </div>
      </div>
    </div>
  </div>
 


</body>
</html>
