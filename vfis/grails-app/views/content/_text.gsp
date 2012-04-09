<div class="control-group">
  <label class="control-label" for="input01">${layoutnode.label}</label>
  <div class="controls">
    <input name="${parentpath}.${layoutnode.propname}" 
           type="text" 
           class="input-xlarge" 
           id="${parentpath}.${layoutnode.propname}"
           value="${org.apache.commons.beanutils.PropertyUtils.getProperty(root, parentpath+'.'+layoutnode.propname)}">
    <g:if test="${layoutnode.helptext}">
      <p class="help-block">${layoutnode.helptext}</p>
    </g:if>
  </div>
</div>
