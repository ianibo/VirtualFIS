<div class="">

  <div class="navbar">
    <div class="navbar-inner">
      <a class="brand" href="#">${layoutInfo.label}</a>
    </div>
  </div>

  <table class="table table-condensed">
    <thead>
      <tr>
        <g:each in="${def.columns}" var="c">
          <th>${c.label}</th>
        </g:each>
      </tr>
    </thead>
    <tbody>
      <g:each in="${o}" var="r">
        <tr>
          <g:each in="${def.columns}" var="c">
            <td>${r[c.name]}</td>
          </g:each>  
        </tr>
      </g:each>
    </tbody>
  </table>
</div>
