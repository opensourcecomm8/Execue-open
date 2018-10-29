/*
 * When the DOM loads, initialize the form to upload 
 * the files using an AJAX "like" call rather than a form submit.
 * 
 * NOTE : handleUploadRequest() and handleUploadResponse(returnedData)
 *  methods should be implemented by the caller.
 *  handleUploadRequest() method is for any pre-upload manipulations
 *  handleUploadResponse(returnedData) method is for handling 
 *    the upload response. returnedData is a JSON object., so upload response
 *    from the server should be JSON and the content type for the response
 *    should be 'text/html'
 */
function ajaxUpload(formId, bodyId, actionName) {
  // Get a reference to the form we are going to be hooking into.
  var jUploadForm = $("#" + formId);

  /*
   * Attach an event to the submit method. Instead of submitting the actual form
   * to the primary page, we are going to be submitting the form to a hidden
   * iFrame that we dynamically create.
   */
  jUploadForm.submit(function(objEvent) {
    var jThis = $(this);

    /*
     * place holder for pre upload manipulations This method should be
     * implemented by the caller
     */
    handleUploadRequest();

    /*
     * Create a unique name for our iFrame. We can do this by using the tick
     * count from the date.
     */
    var iFrameName = ("uploader" + (new Date()).getTime());

    /*
     * Create an iFrame with the given name that does not point to any page - we
     * can use the address "about:blank" to get this to happen.
     */
    var jInnerFrame = $("<iframe name=\"" + iFrameName
        + "\" src=\"about:blank\" />");

    /*
     * We now have an iFrame that is not attached to the document. Before we
     * attach it, let's make sure it will not be seen.
     */
    jInnerFrame.css("display", "none");

    /*
     * Since we submitting the form to the iFrame, we will want to be able to
     * get back data from the form submission. To do this, we will have to set
     * up an event listener for the LOAD event of the iFrame.
     */
    jInnerFrame.load(function(objEvent) {
      /*
       * Get a reference to the body tag of the loaded iFrame. We are doing to
       * assume that this element will contain our return data in JSON format.
       */
      var objUploadBody = window.frames[iFrameName].document
          .getElementsByTagName("body")[0];

      /*
       * Get a jQuery object of the body so that we can have better access to
       * it.
       */
      var jBody = $(objUploadBody);

      /*
       * Assuming that our return data is in JSON format, evaluate the body html
       * to get our return data.
       */
      var returnedData = eval("(" + jBody.html() + ")");

      /*
       * handle the returned data from server accordingly. callers should
       * implement this method call
       */
      handleUploadResponse(returnedData);

      /*
       * Remove the iFrame from the document. Because FireFox has some issues
       * with "Infinite thinking", let's put a small delay on the frame removal.
       */
      setTimeout(function() {
        jInnerFrame.remove();
      }, 100); // End of setTimeout()

      }); // End of jInnerFrame.load()

      // Attach to body.
      $("#" + bodyId).append(jInnerFrame);

      /*
       * Now that our iFrame it totally in place, hook up the frame to post to
       * the iFrame.
       */
      jThis.attr("action", actionName).attr("method", "post").attr("enctype",
          "multipart/form-data").attr("encoding", "multipart/form-data").attr(
          "target", iFrameName);

    }); // End of jUploadForm.submit()
} // End of function ajaxUpload()
