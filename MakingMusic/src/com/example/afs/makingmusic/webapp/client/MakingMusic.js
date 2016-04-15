"use strict";
// See
// https://javascriptweblog.wordpress.com/2010/12/07/namespacing-in-javascript/
var makingMusic = makingMusic || {};
(function() {
  var refreshIntervalMillis = 1000;
  this.initializeProperties = function(json) {
    var response = JSON.parse(json);
    var properties = response.properties;
    for ( var name in properties) {
      var value = properties[name];
      if (name === "maximum-concurrent-notes") {
        var input = document.getElementById(name);
        if (input.value == input.defaultValue) {
          input.value = value;
          input.defaultValue = value;
        }
      } else if (name.match(/^instrument-/)) {
        document.getElementById(name).checked = (value === "true");
      }
    }
  }
  this.onClick = function(event) {
    var target = event.target;
    var id = target.id;
    var match = id.match(/^instrument-(.+)/);
    if (match !== null) {
      var value = target.checked;
      this.setProperty(id, value);
    }
    match = id.match(/^property-(.+)/);
    if (match !== null) {
      var name = match[1];
      var input = document.getElementById(name);
      var value = input.value;
      this.setProperty(name, value);
      input.defaultValue = value;
    }
    match = id.match(/^tab-(.+)/);
    if (match !== null) {
      var name = match[1];
      this.selectTab(name);
    }

  }
  this.onLoad = function() {
    this.selectTab("home");
    document.getElementById("property-form").reset();
    setInterval(this.onPoll.bind(this), refreshIntervalMillis);
    //this.onPoll();
  }
  this.onPoll = function() {
    this.refreshImage();
    this.refreshProperties();
  }
  this.selectTab = function(name) {
    var i = null, tab = null;
    var tabs = document.querySelectorAll(".tab");
    for (i = 0; i < tabs.length; i++) {
      tab = tabs[i];
      console.log(tab);
      if (tab.id == name) {
        tab.style['display'] = 'flex';
      } else {
        tab.style['display'] = 'none';
      }
    }
  }
  this.setProperty = function(name, value) {
    var httpRequest = new XMLHttpRequest();
    httpRequest.open("POST", "rest/v1/properties/" + name + "/" + value, true);
    httpRequest.send();
  }
  this.refreshImage = function() {
    var currentFrame = document.getElementById("current-frame");
    currentFrame.src = "currentFrame.jpg?t=" + new Date().getTime();
  }
  this.refreshProperties = function() {
    var self = this;
    var httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = function() {
      if (httpRequest.readyState == 4 && httpRequest.status == 200) {
        self.initializeProperties(httpRequest.responseText);
      }
    };
    httpRequest.open("GET", "rest/v1/properties", true);
    httpRequest.send();
  }
}).apply(makingMusic);
