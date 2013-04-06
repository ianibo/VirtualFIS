<div class="">
  <div class="navbar">
    <div class="navbar-inner">
      <a class="brand" href="#">${layoutInfo.name}</a>
    </div>
  </div>

  <dl class="dl-horizontal">
    <g:each in="${layoutInfo.fields}" var="f">
      <g:if test="${f.type=='string'}">
        <div class="control-group">
          <dt>${f.label}</dt>
          <dd>${o[f.label]}</dd>
        </div>
      </g:if>
      <g:if test="${f.type=='integer'}">
        <div class="control-group">
          <dt>${f.label}</dt>
          <dd>${o[f.label]}</dd>
        </div>
      </g:if>
      <g:if test="${f.type=='object'}">
        <div class="control-group">
          <dt>${f.label}</dt>
          <dd>
            <g:render template="objectPanel" contextPath="../templates" model="[o:o[f.name],layoutInfo:f.layout]"/>
          </dd>
        </div>
      </g:if>
    </g:each>
  </dl>
</div>
