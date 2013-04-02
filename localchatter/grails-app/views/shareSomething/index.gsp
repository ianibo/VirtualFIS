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

          <g:form class="form-horizontal" action="check">
            <div class="control-group">
              <label class="control-label" for="proposed_postcode">Postocde</label>
              <div class="controls">
                <input type="text" name="proposed_postcode" placeholder="Postcode of service, event or business" value="${params.proposed_postcode}">
              </div>
            </div>
            <div class="control-group">
              <label class="control-label" for="proposed_name">Name</label>
              <div class="controls">
	        <input type="text" placeholder="Name of the service, event or business" name="proposed_name" value="${params.proposed_name}"/>
              </div>
            </div>
            <div class="control-group">
              <label class="control-label" for="proposed_event_date">Event Date</label>
              <input type="hidden" name="event_date" id="event_date" value="${params.event_date}"/>
              <div class="controls">
                <a data-type="date" class="hiddenDateInput" data-hiddenid="event_date">${params.event_date}</a>
              </div>
            </div>
            <div class="control-group">
              <div class="controls">
                <button type="submit" class="btn">Next -></button>
              </div>
            </div>
          </g:form>
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
