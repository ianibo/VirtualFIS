<html>
   <head>
      <meta name="layout" content="main"/>
      <title>LocalChatter.info - Share something new with your local community</title>

      <meta name="description" content="Use localchatter to search for community improved information from trusted local sources. You will information collected from local authorities and other trusted sources, imrpved and refined by the community. Content includes child care, registered Childminders and Family Information, Activities,  OfSTED registered Childcare and Family Information Service’s across England."/>
      <meta name="dc.description" content="Use localchatter to search for community improved information from trusted local sources. You will information collected from local authorities and other trusted sources, imrpved and refined by the community. Content includes child care, registered Childminders and Family Information, Activities,  OfSTED registered Childcare and Family Information Service’s across England."/>
      <meta name="og.description" content="Use localchatter to search for community improved information from trusted local sources. You will information collected from local authorities and other trusted sources, imrpved and refined by the community. Content includes child care, registered Childminders and Family Information, Activities,  OfSTED registered Childcare and Family Information Service’s across England."/>

      <meta property="dc.title" name="dc.title" content="Search the UK Directory of approved services including child care, registered Childminders and Family Information"/>
      <meta property="title" name="title" content="Search the UK Directory of approved services including child care, registered Childminders and Family Information"/>
      <meta property="og:title" name="og:title" content="Search the UK Directory of approved services including child care, registered Childminders and Family Information"/>

      <meta property="keywords" name="keywords" content="Family, Childcare, Search, Information, Positive Activities, Events, Organisations, child care, babysitters, childminders, registered childminders, localchatter, Family Information Services"/>

   </head>
<body>

  <g:if test="${flash.message}">
    <div class="content">
      <div class="container"><div class="row"><div class="span12">
        <bootstrap:alert class="alert-info">${flash.message}</bootstrap:alert>
      </div></div></div>
    </div>
  </g:if>

  <g:if test="${flash.error}">
    <div class="content">
      <div class="container"><div class="row"><div class="span12">
        <bootstrap:alert class="alert-error">${flash.error}</bootstrap:alert>
      </div></div></div>
    </div>
  </g:if>


  <div class="content">
    <div class="container">
      <div class="row">
        <div class="span9">
          <p>${proposed_name} in ${proposed_postcode} looks like something we don't already know about</p>
        </div>
        <div class="span3">
          <ul>
            <li>Additional Information</li>
            <li>Rules for adding things</li>
            <li>What can I share</li>
            <li>There isn't a template for the thing I want to describe</li>
          </ul>
        </div>
      </div>
    </div>
  </div>

  <script>
    $(function() {
      $.fn.editable.defaults.mode = 'popup';
      $(".hiddenDateInput").editable({
        url: function(params) {
          $("#"+$(this).data('hiddenid')).val(params.value);
        }
      });
    });
  </script>

</body>
</html>
