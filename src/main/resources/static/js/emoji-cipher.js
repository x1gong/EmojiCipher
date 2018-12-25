/**
 * http://www.emojicipher.com
 * Copyright 2018 x1gong
 * Released under the MIT license
 * http://opensource.org/licenses/MIT
 */

$(function() {

  var COPY_WARNING = "<strong>Failed</strong> to copy the text. Please copy manually.";
  var ENCRYPT_WARNING = "Please enter something to encrypt";
  var ENCRYPT_ERROR = "Failed to encrypt the text";
  var DECRYPT_WARNING = "Please enter something to decrypt";
  var DECRYPT_ERROR = "<strong>Failed</strong> to decrypt the text.";

  $("#success_close").click(function () {
    $("#success_copy_alert").hide('fade');
  });

  $("#fail_close").click(function () {
    $("#warning_alert").hide('fade');
  });

  $("#error_close").click(function () {
    $("#error_alert").hide('fade');
  });
 
  $("#text_copy").click(function () {
    copyToClipboard("#text_input");
  });

  $("#emoji_copy").click(function () {
    copyToClipboard("#emoji_output");
  });

  $("#clear_btn").click(function () {
    $("#text_input").val("");
    $("#emoji_output").val("");
  });

  $("#encrypt_btn").click(function () {
    var text = $("#text_input").val().trim();
    if (text) {
      var jsonObj = {
        text: text
      };
      $.ajax({
        url:"/encrypt",
        type: 'POST',
        data: JSON.stringify(jsonObj),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function(data){
      	  $("#emoji_output").val(data.result);
      	},
      	error: function(err) {
      	  showError(ENCRYPT_ERROR);
      	}
      });
    } else {
      showWarning(ENCRYPT_WARNING);
    }
  });

  $("#decrypt_btn").click(function () {
    var text = $("#emoji_output").val().trim();
    if (text) {
      var jsonObj = {
        text: text
      };
      $.ajax({
        url:"/decrypt",
        type: 'POST',
        data: JSON.stringify(jsonObj),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function(data){
          $("#text_input").val(data.result);
        },
        error: function(err) {
          showError(DECRYPT_ERROR);
        }
      });
    } else {
      showWarning(DECRYPT_WARNING);
    }
  });

  function copyToClipboard(textID) {
    $(textID).select();
    if (document.execCommand("copy")) {
      $("#success_copy_alert").hide("hide");
      $("#success_copy_alert").show("fade");
      window.setTimeout(function() {
          $("#success_copy_alert").hide('fade');
      }, 2000);
    } else {
      showWarning(COPY_WARNING);
    }
  }


  function showWarning(message) {
    $("#warning_alert").hide("hide");
    document.getElementById("warning_text").innerHTML = message;
    $("#warning_alert").show("fade");
    window.setTimeout(function() {
        $("#warning_alert").hide('fade');
    }, 2000);
  }

  function showError(message) {
    $("#error_alert").hide("fade");
    document.getElementById("error_text").innerHTML = message;
    $("#error_alert").show("fade");
    window.setTimeout(function() {
        $("#error_alert").hide('fade');
    }, 2000);
  }
 
});


