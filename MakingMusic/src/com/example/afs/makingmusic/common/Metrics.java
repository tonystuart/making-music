// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

public class Metrics {

  private String cpu;
  private int frames;
  private int images;
  private int instruments;
  private int items;
  private int notes;
  private int pages;
  private int properties;
  private int redirects;
  private int temperature;
  private String uptime;

  public String getCpu() {
    return cpu;
  }

  public int getFrames() {
    return frames;
  }

  public int getImages() {
    return images;
  }

  public int getInstruments() {
    return instruments;
  }

  public int getItems() {
    return items;
  }

  public int getNotes() {
    return notes;
  }

  public int getPages() {
    return pages;
  }

  public int getProperties() {
    return properties;
  }

  public int getRedirects() {
    return redirects;
  }

  public int getTemperature() {
    return temperature;
  }

  public String getUptime() {
    return uptime;
  }

  public void setCpu(String cpu) {
    this.cpu = cpu;
  }

  public void setFrames(int frames) {
    this.frames = frames;
  }

  public void setImages(int images) {
    this.images = images;
  }

  public void setInstruments(int instruments) {
    this.instruments = instruments;
  }

  public void setItems(int items) {
    this.items = items;
  }

  public void setNotes(int notes) {
    this.notes = notes;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }

  public void setProperties(int properties) {
    this.properties = properties;
  }

  public void setRedirects(int redirects) {
    this.redirects = redirects;
  }

  public void setTemperature(int temperature) {
    this.temperature = temperature;
  }

  public void setUptime(String uptime) {
    this.uptime = uptime;
  }

  @Override
  public String toString() {
    return "Metrics [cpu=" + cpu + ", frames=" + frames + ", images=" + images + ", instruments=" + instruments + ", items=" + items + ", notes=" + notes + ", pages=" + pages + ", properties=" + properties + ", redirects=" + redirects + ", temperature=" + temperature + ", uptime=" + uptime + "]";
  }

}
