<!doctype html>
<html>
  <head>
    <meta name="layout" content="bootstrap"/>
    <title>VFIS Home</title>
  </head>

  <body>
    <div class="row-fluid">
        <ul class="breadcrumb">
        <li> <g:link path="/">Virtual FIS</g:link> <span class="divider">/</span> </li>
        <li> <g:link controller="org" action="dashboard" id="${org.id}">${org.name}</g:link> <span class="divider">/</span> </li>
        <li> <g:link controller="org" action="details" id="${org.id}">Details</g:link> </li>
      </ul>
    </div>

    <!--http://www.w3resource.com/twitter-bootstrap/forms-tutorial.php-->
    <!-- http://webdesign.tutsplus.com/tutorials/htmlcss-tutorials/stepping-out-with-bootstrap-from-twitter/ -->
    <!-- http://stackoverflow.com/questions/9534249/twitter-bootstrap-form-inline-stretch -->
    <!-- http://jsfiddle.net/CdNef/ -->
    <!-- http://datatables.net/blog/Microsoft_CDN -->
    <div class="row-fluid">
      <div>
        <h1>${org.name} Edit Details</h1>
      </div>
    </div>

    <g:form action="details" id="${org.id}" method="POST" class="well">
      <input type="hidden" name="id" value="${org.id}"/>
      <div class="row-fluid">
        <div class="span4">
          <div>
            <fieldset>
              <legend>Internal Information</legend>

              <div class="control-group">
                Identifier: ${org.identifier}
              </div>

              <div class="control-group">
                  Short Code: ${org.shortCode}
              </div>

              <div class="control-group">
                Subscription Type: ${org.subscriptionType?.description}
              </div>

              <legend>Core Details</legend>
              <div class="control-group">
                <label class="control-label" for="provname">Provider Name</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="provname" name="provname" value="${org.name}">
                  <p class="help-block">The text to be shown hyperlinked to your website after the "Via ..." line in records</p>
                </div>
              </div>

              <div class="control-group">
                <label class="control-label" for="website">Website</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="website" name="website" value="${org.website}">
                  <p class="help-block">The site linked to by your provider name (above)</p>
                </div>
              </div>

              <div class="control-group">
                <label class="control-label" for="provemail">Provider Contact Email Address</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="provemail" name="provemail" value="${org.contactEmail}">
                  <p class="help-block">The text which will be displayed to end users as an email address alongside your records</p>
                </div>
              </div>

              <div class="control-group">
                <label class="control-label" for="disclaimer">Disclaimer</label>
                <div class="controls">
                  <textarea class="input-xlarge" id="disclaimer" name="sourceDisclaimer" rows="3">${org.sourceDisclaimer}</textarea>
                  <p class="help-block">The block of text shown after provider name, IE: "via -provider-name- --disclaimer text here--"</p>
                </div>
              </div>
            </fieldset>
          </div>
        </div>
        <div class="span4">
          <div>
            <fieldset>

              <legend>Additional Information</legend>

              <div class="control-group">
                <label class="control-label" for="office">Office</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="office" name="office" value="${org.office}">
                </div>
              </div>

              <div class="control-group">
                <label class="control-label" for="thoroughfare">Thoroughfare</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="thoroughfare" name="thoroughfare" value="${org.thoroughfare}">
                </div>
              </div>

              <div class="control-group">
                <label class="control-label" for="dependentThoroughfare">dependentThoroughfare</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" name="dependentThoroughfare" id="dependentThoroughfare" value="${org.dependentThoroughfare}">
                </div>
              </div>

              <div class="control-group">
                <label class="control-label" for="locality">locality</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="locality" name="locality" value="${org.locality}">
                </div>
              </div>

              <div class="control-group">
                <label class="control-label" for="dependentLocality">dependentLocality</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" name="dependentLocality" id="dependentLocality" value="${org.dependentLocality}">
                </div>
              </div>

              <div class="control-group">
                <label class="control-label" for="region">region</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="region" name="region" value="${org.region}">
                </div>
              </div>

              <div class="control-group">
                <label class="control-label" for="postcode">postcode</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="postcode" name="postcode" value="${org.postcode}">
                </div>
              </div>
            </fieldset>
          </div>
        </div>
        <div class="span4">
          <div>
            <fieldset>
              <legend>Contact Info</legend>

              <div class="control-group">
                <label class="control-label" for="description">description</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="description" name="description" value="${org.description}">
                </div>
              </div>

              <div class="control-group">
                <label class="control-label" for="contactEmail">contactEmail</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="contactEmail" name="contactEmail" value="${org.contactEmail}">
                </div>
              </div>

              <div class="control-group">
                <label class="control-label" for="contactTelephone">contactTelephone</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" name="contactTelephone" id="contactTelephone" value="${org.contactTelephone}">
                </div>
              </div>

              <div class="control-group">
                <label class="control-label" for="contactFax">contactFax</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" name="contactFax" id="contactFax" value="${org.contactFax}">
                </div>
              </div>
            </fieldset>
          </div>
        </div>
      </div>
      <div class="row-fluid">
        <div class="span6">
          <input type="submit" class="btn-primary" value="Update Details"/>
        </div>
      </div>
    </g:form>

  </body>
</html>
