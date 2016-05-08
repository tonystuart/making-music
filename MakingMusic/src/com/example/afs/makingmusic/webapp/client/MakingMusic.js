"use strict";
// See
// https://javascriptweblog.wordpress.com/2010/12/07/namespacing-in-javascript/
var makingMusic = makingMusic || {};
(function() {
  var refreshIntervalMillis = 1000;
  this.initializeInstruments = function(propertyCache) {
    var properties = propertyCache.properties;
    document.getElementById("instrument-form").reset();
    for ( var name in properties) {
      var value = properties[name];
      if (name.match(/^instrument-/)) {
        document.getElementById(name).checked = (value === "true");
      } else if (name.match(/^enum-/)) {
        document.getElementById(name + "-" + value).checked = true;
      }
    }
  }
  this.initializeMetrics = function(metrics) {
    var table = document.createElement("TABLE");
    for (var name in metrics) {
      var value = metrics[name];
      var row = table.insertRow();
      var nameCell = row.insertCell(0);
      var valueCell = row.insertCell(1);
      nameCell.innerHTML = name;
      valueCell.innerHTML = value;
    }
    var metricsContainer = document.getElementById("metrics-container");
    metricsContainer.innerHTML = "";
    metricsContainer.appendChild(table);
  }
  this.initializeSettings = function(propertyCache) {
    var properties = propertyCache.properties;
    for ( var name in properties) {
      var value = properties[name];
      if (name === "maximum-concurrent-notes") {
        var input = document.getElementById(name);
        if (input.value == input.defaultValue) {
          input.value = value;
          input.defaultValue = value;
        }
      }
    }
  }
  this.initializeTab = function(requestedTab, json) {
    var response = JSON.parse(json);
    switch (requestedTab) {
    case "instruments":
      this.initializeInstruments(response);
      break;
    case "metrics":
      this.initializeMetrics(response);
      break;
    case "settings":
      this.initializeSettings(response);
      break;
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
    match = id.match(/^enum-(.+)/);
    if (match !== null) {
      var name = target.name;
      var value = target.value;
      this.setProperty(name, value);
    }
    match = id.match(/^tab-(.+)/);
    if (match !== null) {
      var name = match[1];
      this.selectTab(name);
    }
  }
  this.onLoad = function() {
    this.selectTab("home");
    document.getElementById("instrument-form").reset();
    setInterval(this.onPoll.bind(this), refreshIntervalMillis);
  }
  this.onPoll = function() {
    switch (this.currentTab) {
    case "home":
      this.refreshImage();
      break;
    case "info":
      break;
    case "instruments":
      this.refreshTab();
      break;
    case "metrics":
      this.refreshTab();
      break;
    case "settings":
      this.refreshTab();
      break;
    }
  }
  this.selectTab = function(name) {
    var i = null, tab = null;
    var tabs = document.querySelectorAll(".tab");
    for (i = 0; i < tabs.length; i++) {
      tab = tabs[i];
      if (tab.id == name) {
        tab.style['display'] = 'flex';
      } else {
        tab.style['display'] = 'none';
      }
    }
    this.currentTab = name;
    this.onPoll();
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
  this.refreshTab = function() {
    var self = this;
    var requestedTab = this.currentTab;
    var httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = function() {
      if (httpRequest.readyState == 4 && httpRequest.status == 200) {
        self.initializeTab(requestedTab, httpRequest.responseText);
      }
    };
    httpRequest.open("GET", "rest/v1/" + requestedTab, true);
    httpRequest.send();
  }
}).apply(makingMusic);
