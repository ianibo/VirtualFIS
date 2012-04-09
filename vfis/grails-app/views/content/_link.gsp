<div class="well">
  <g:if test="${layoutnode.linklabel != null}">
    <h3>${layoutnode.linklabel}
  </g:if>
  <g:each in="${layoutnode.content}" var="item">
    <g:render template="/content/dynamicComponent" model="[root:record, layoutnode:item]"/>
  </g:each>
</div>
