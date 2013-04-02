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


  <div class="content">
    <div class="container">
      <div class="row">
        <div class="span9">
          <p>Want to share information about a local service, one-off event or locally owned independent business? GREAT!</p>
          <p>Please use our recurring event form to tell us about an event that repeats</p>
          <form>
            <fieldset>
              <legend>Legend</legend>
              <label>Where can we find the service, event or business?</label>
              <input type="text" name="proposed_postcode" placeholder="Postcode of service, event or business">
              <span class="help-block">We only add informaiton about places people can actually walk into, please tell us where people can find the service, event or business</span>
              <label>Whats the name of the service, event or business?</label>
              <input type="text" placeholder="Name of the service, event or business" name="proposed_name"/>
              <label>If this is an event, please tell us the start time</label>
              <a data-type="date" id="datepicker"></a>
              <label>Continue</label>
              <button type="submit" class="btn">Next -></button>
            </fieldset>
          </form>
          <p>Whats the postcode of the location? <input type="text" name="proposed_postcode"/></p>
          <p>Whats the name of the service, event or  </p>
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
      $("#datepicker").editable();
    });
  </script>

</body>
</html>
