<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'blue.css')}" type="text/css">
    <g:layoutHead/>
    <r:layoutResources />
  </head>
  <body>

    <header>
      <div class="container">
        <div class="row">
          <div class="span4">
            <!-- Logo and site link -->
            <div class="logo">
              <h1><a href="http://www.localchatter.com/">Local Chatter<span class="color">.</span></a></h1>
              <p>Helping Connect public information with local commiunities</p>
            </div>
          </div>
          <div class="span4 offset4">
            <div class="list">
              <div class="address">
                <i class="icon-share"></i> Facebook, Google+
              </div>
              <hr />
              <!-- Add your address here -->
              <div class="address">
                <i class="icon-home"></i> For local authorities
              </div>
              <div class="address">
                <i class="icon-home"></i> For information owners
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>

    <g:layoutBody/>


    <footer>
      <div class="container">
        <div class="row">

          <div class="span4">
             <!-- Widget 1 -->
             <div class="widget">
                <h4>Twitter</h4>
                <!-- Too add twitter widget, goto "js" folder and open "custom.js" file and search for the word "ashokramesh90". This is my twitter username. Just replace the word with your twitter username. You are done. -->
                <div class="tweet"></div>
             </div>
          </div>

          <div class="span4">
             <!-- widget 2 -->
             <div class="widget">
                <h4>Recent Posts</h4>
                   <ul>
                      <li><a href="#">Sed eu leo orci, in rhoncus puru condimentum gravida metus</a></li>
                      <li><a href="#">Etiam at in rhoncus puru nulla ipsum, in rhoncus purus</a></li>
                      <li><a href="#">Fusce vel magnain rhoncus puru faucibus felis dapibus facilisis</a></li>
                   </ul>
             </div>
          </div>

          <div class="span4">
             <!-- Widget 3 -->
             <div class="widget">
                <h4>Categories</h4>
                <ul>
                   <li><a href="#">Condimentum</a></li>
                   <li><a href="#">Etiam at</a></li>
                   <li><a href="#">Fusce vel</a></li>
                   <li><a href="#">Vivamus</a></li>
                   <li><a href="#">Pellentesque</a></li>
                </ul>
             </div>
          </div>
      
          <div class="span12"><hr /><p class="copy">
          <!-- Copyright information. You can remove my site link. -->
               Copyright &copy; <a href="#">Your Site</a> | Designed by <a href="http://responsivewebinc.com">Responsive Web Design</a></p></div>
        </div>
      </div>
    </footer>               

    <g:javascript library="application"/>
    <r:layoutResources />
  </body>
</html>
