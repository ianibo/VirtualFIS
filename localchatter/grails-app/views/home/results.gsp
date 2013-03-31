<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
      <meta name="description" content="Use localchatter to search for community improved information from trusted local sources. You will information collected from local authorities and other trusted sources, imrpved and refined by the community"/>

      <!-- Ask search engines not to index the search results pages, it looks horrible in google -->
      <meta name="robots" CONTENT="noindex, follow">
   </head>
<body>

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
      <div class="row-fluid">
        <div class="span8">
          <div class="row-fluid" style="text-align:center;">
              <g:form action="index" method="get" class="form-inline">
                Postcode: <input name="postcode" placeholder="Enter your postcode..." type="text" value="${params.postcode}" />
                Keywords: <input name="q" placeholder="Keywords, eg Childcare..." type="text"  value="${params.q}"/>
                <button class='btn'>Go!</button>
              </g:form>
          </div>
          <div class="row-fluid">
            <div class="facetFilter span3">
              <g:each in="${facets}" var="facet">
                <div>
                  ${facet.key}
                  <ul>
                    <g:each in="${facet.value}" var="v">
                      <li>
                        <g:set var="fname" value="facet:${facet.key+':'+v.term}"/>
                        <g:link controller="home" action="index" params="${params+[fname:'Y']}">${v.display}</g:link> (${v.count})
                        <g:if test="${params[fname]=='Y'}">Tick</g:if>
                      </li>
                    </g:each>
                  </ul>
                </div>
              </g:each>
            </div>
            <div class="span9">
              <g:if test="${hits?.totalHits}">Results ${params.offset+1} to ${params.lastrec} of ${hits?.totalHits}</g:if>

              <ul>
                <g:each in="${hits}" var="res">
                  <li>
                    <strong><g:link controller="entry" id="${res.source.shortcode}">${res.source.orig.name}</g:link></strong><br/>
                    ${res.source.orig.description}<br/>
                    <g:if test="${params.postcode}">Distance from ${params.postcode} : ${res.sortValues[0].round(2)} ${dunit}<br/></g:if>
                    Information Source: ${res.source.owner}
 
                  </li>
                </g:each>
              </ul>
              <g:paginate action="index" controller="home" params="${params}" next="Next" prev="Prev" maxsteps="10" total="${hits.totalHits}" class="pagination-right"/>
            </div>
          </div>
        </div>
        <div class="span4">
          <g:render template="addpanel" contextPath="../templates"/>
        </div>
      </div>
    </div>

  </div>


</body>
</html>
