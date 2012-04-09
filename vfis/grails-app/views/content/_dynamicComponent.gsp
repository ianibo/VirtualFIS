<g:if test="${layoutnode.type=='tabs'}"><g:render template="/content/tabs" model="[root:record, layoutnode:layout]"/></g:if>
<g:elseif test="${layoutnode.type=='text'}"><g:render template="/content/text" model="[root:record, layoutnode:layout]"/></g:elseif>
<g:else>Unhandled type ${layoutnode?.type}</g:else>
