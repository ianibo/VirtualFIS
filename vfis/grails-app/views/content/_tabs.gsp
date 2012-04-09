<div class="tabbable">

  <ul class="nav nav-tabs">
    <g:each in="${layoutnode.tabs}" var="t">
      <li class="active"><a href="#${t.id}" data-toggle="tab">${t.label}</a></li>
    </g:each>
  </ul>

  <div class="tab-content">
    <g:each in="${layoutnode.tabs}" var="t">
      <div class="tab-pane active" id="${t.id}">
        <p>
          <g:each in="${t.content}" var="item">
            <g:render template="/content/dynamicComponent" model="[root:record, layoutnode:item]"/>
          </g:each>
        </p>
      </div>
    </g:each>
  </div>

</div>
