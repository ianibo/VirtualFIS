<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Localchatter.info"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <r:require modules="bootstrap, bootstrap-popover, lc "/> <!-- see bootstrap-js for all js res-->

    <g:layoutHead/>
    <r:layoutResources />

    <!-- Moved to resources
      <link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" type="text/css">
      <link rel="stylesheet" href="${resource(dir: 'css', file: 'blue.css')}" type="text/css">
      <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-editable.css')}" type="text/css">
      <script src="${resource(dir: 'js', file: 'bootstrap-editable.min.js')}"></script>
      <script src="${resource(dir: 'js', file: 'moment.min.js')}"></script>
    -->

    <script type="text/javascript">
      var _gaq = _gaq || [];
      _gaq.push(['_setAccount', '${grailsApplication.config.analytics.code}']);
      _gaq.push(['_trackPageview']);
      (function() {
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
      })();
    </script>

  </head>
  <body>

    <header>
      <div class="container">
        <div class="row">
          <div class="span4">
            <!-- Logo and site link -->
            <div class="logo">
              <h1><a href="http://www.localchatter.info/">LocalChatter<span class="color">.</span>Info</a></h1>
              <p>Helping Connect public information with local commiunities</p>
            </div>
          </div>
          <div class="span4 offset4">
            <div class="list">

              <div class="address">
                <!-- AddThis Button BEGIN -->
                <div class="addthis_toolbox addthis_default_style ">
                  <a class="addthis_button_preferred_1"></a>
                  <a class="addthis_button_preferred_2"></a>
                  <a class="addthis_button_preferred_3"></a>
                  <a class="addthis_button_preferred_4"></a>
                  <a class="addthis_button_compact"></a>
                  <a class="addthis_counter addthis_bubble_style"></a>
                </div>
                <script type="text/javascript">var addthis_config = {"data_track_addressbar":true};</script>
                <script type="text/javascript" src="//s7.addthis.com/js/300/addthis_widget.js#pubid=ra-513ceb995a05bdf7"></script>
              <!-- AddThis Button END -->
              </div>
              <hr />
              <!-- Add your address here -->
              <div class="address">
                <i class="icon-home"></i> <a href="http://blog.localchatter.info/?page_id=4">For local authorities</a>
              </div>
              <div class="address">
                <i class="icon-home"></i> <a href="http://blog.localchatter.info/?page_id=6">For information owners</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>

    <div class="navbar navbar-static-top">
      <div class="navbar-inner">
        <div class="container">
          <div class="nav-collapse">
            <!-- Navigation links starts here -->
            <ul class="nav">
              <!-- Main menu -->
              <li><g:link controller="home" action="index">Home</g:link></li>
            </ul>
            <ul class="nav pull-right">
               <sec:ifLoggedIn>
                 <li><g:link controller="logout">Logout</g:link></li>
               </sec:ifLoggedIn>
            </ul>
          </div>
        </div>
      </div>
    </div>


    <g:layoutBody/>


    <footer>
      <div class="container">
        <div class="row">

          <div class="span4">
             <!-- Widget 1 -->
             <div class="widget">
                <h4>Twitter : <a href="http://twitter.com/lcdotinfo">lcdotinfo</a></h4>
                <div class="tweet"></div>
             </div>
          </div>

          <div class="span4">
             <!-- widget 2 -->
             <div class="widget">
                <h4>Links</h4>
                   <ul>
                      <li><a href="http://blog.localchatter.info/?page_id=4">For local authorities and information providers</a></li>
                      <li><a href="http://blog.localchatter.info/?page_id=6">For record owners</a></li>
                   </ul>
             </div>
          </div>

          <div class="span4">
             <!-- Widget 3 -->
             <div class="widget">
                <h4>Categories</h4>
                <ul>
                   <li><a href="/">Global Search</a></li>
                   <li><a href="/">Childcare Only</a></li>
                </ul>
             </div>
          </div>
      
          <div class="span12"><hr /></div>
        </div>
      </div>
    </footer>               

    <g:javascript library="application"/>
    <r:layoutResources />

  <script language="JavaScript">
    jQuery(function($){
       $(".tweet").tweet({
          username: "lcdotinfo",
          join_text: "auto",
          avatar_size: 0,
          count: 3,
          auto_join_text_default: "we said,",
          auto_join_text_ed: "we",
          auto_join_text_ing: "we were",
          auto_join_text_reply: "we replied to",
          auto_join_text_url: "we were checking out",
          loading_text: "loading tweets...",
          template: "{text}"
       });
    });
  </script>
  </body>
</html>
