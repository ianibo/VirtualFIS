<html>
   <head>
      <meta name="layout" content="main"/>
      <title>LocalChatter.info: ${record.source.title} via ${record.source.owner}</title>
      <r:require modules="bootstrap"/>

    <g:if test="${record.source.position && record.source.position.lat && record.source.position.lon}">
      <meta property="ICBM" name="ICBM" content="${record.source.position.lat}, ${record.source.position.lon}" />
      <meta property="geo.position" name="geo.position" content="${record.source.position.lat}, ${record.source.position.lon}" />
      <meta property="og:latitude" content="${record.source.position.lat}" />
      <meta property="og:longitude" content="${record.source.position.lon}" />
      <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
    </g:if>

    <meta property="geo.region" name="geo.region" content="${record.source.orig.address.toString()}" />
    <meta property="geo.placename" name="geo.placename" content="${record.source?.orig.address?.streetAddress?.toString()}" />

    <!-- OGP Properties -->
    <meta property="og:title" content="${record.source.orig.name}" />
    <meta property="og:description" content="${record.source.orig.description}" />
    <meta property="og:type" content="activity" />
    <meta property="og:url" content="http://localchatter.info/entry/${record.source.shortcode}" />
    <meta property="og:site_name" content="LocalChatter.info" />

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
      <div class="container"><div class="row"><div class="span12">
        <bootstrap:alert class="alert-info">${flash.message}</bootstrap:alert>
      </div></div></div>
    </div>
  </g:if>


  <div class="content">
    <div class="container">
      <div class="row">
        <div class="span9" itemscope itemtype="${record.source.__schema}">

          <g:if test="${record.source.position && record.source.position.lat && record.source.position.lon }">
          <div id="rightpanel" style="float:right; width:250px;">
            <div id="map" style="width: 250px; height: 250px;"></div>
            <div style="text-align: center; margin-top:15px; width: 250px;">Please contact the provider for exact location</div>
          </div>
          </g:if>

          <h1 itemprop="name">${record.source.orig.name}</h1>
          <p itemprop="description">${record.source.orig.description}</p>

          <dl>
    
            <g:if test="${record.source.additionalInfo}">
            <dt>Additional Information</dt>
            <dd>${record.source.orig.additionalInfo}</dd>
            </g:if>

            <dt>Address</dt>
            <dd itemprop="address">
              ${record.source.orig.address.streetAddress}<br/>
              ${record.source.orig.address.postcode}
            </dd>
          </dl>
          <dl>

            <dt>Privacy</dt>
            <dd>
              <g:if test="${record.source.orig.privacyLevel=='PublicListing'}">Public Listing.</g:if>
            </dd>
            <dt>Original Source Record Provided by</dt>
            <dd>${record.source.owner} (No local updates, corrections or edits yet)</dd>
            <dt>Ownership</dt>
            <dd>Not currently claimed</dd>

            <g:if test="${record.source.orig.certs}">
              <dt>Certificates, Accreditations and Authorised Links</dt>
              <dd>
                <table class="table table-bordered">
                  <thead>
                    <tr> <th>from</th> <th>Link</th> <th>Id</th></tr>
                  </thead>
                  <tbody>
                    <g:each in="${record.source.certs}" var="c">
                      <tr><td>${c.cert}</td><td><a href="${c.url}">${c.uri}</a></td><td>${c.id}</td></tr>
                    </g:each>
                  </tbody>
                </table>
              </dd>
            </g:if>
          </dl>
        </div>
        <div class="span3">
          <g:render template="addpanel" contextPath="../templates"/>
        </div>
      </div>
    </div>
  </div>
 

    <g:if test="${record.source.position && record.source.position.lat && record.source.position.lon }">
      <script type="text/javascript">
      //<![CDATA[

      function map2() {
        var myLatlng = new google.maps.LatLng(${record.source.position.lat},${record.source.position.lon});

        var myOptions = {
           zoom: 15,
           center: myLatlng,
           mapTypeId: google.maps.MapTypeId.ROADMAP
        }

        var map = new google.maps.Map(document.getElementById("map"), myOptions);

        <g:if test="${record.source.orig.privacyLevel=='PublicListing'}">
        var marker = new google.maps.Marker({
             position: myLatlng, 
             map: map, 
             title:"${record.source.title}"
        });   
        marker.setMap(map);  
        </g:if>

      }

      map2();
      //]]>
      </script>
    </g:if>


</body>
</html>
