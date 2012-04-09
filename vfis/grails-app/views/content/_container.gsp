<g:each in="content" var="item">
  <g:render template="/content/dynamicComponent" model="[root:record, layoutnode:item]"/>
</g:each>
