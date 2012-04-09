<g:if test="${layoutnode.type=='tabs'}"><g:render template="/content/tabs" model="[root:record, layoutnode:layoutnode]"/></g:if>
<g:elseif test="${layoutnode.type=='text'}"><g:render template="/content/text" model="[root:record, layoutnode:layoutnode]"/></g:elseif>
<g:elseif test="${layoutnode.type=='link'}"><g:render template="/content/link" model="[root:record, layoutnode:layoutnode]"/></g:elseif>
<g:else>Unhandled type ${layoutnode?.type}</g:else>
