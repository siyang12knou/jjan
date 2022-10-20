import 'dart:convert';

import "package:http/http.dart";

class HttpClient {

  Client client = Client();
  Map<String, String> headers = {};

  Future<Response> head(Uri url, {Map<String, String> headers}) async {
    Map<String, String> sendHeader = {};
    if(headers != null) {
      sendHeader.addAll(headers);
    }
    sendHeader.addAll(this.headers);

    Response response = await client.head(url, headers: sendHeader);
    updateCookie(response);
    return response;
  }

  Future<Response> get(Uri url, {Map<String, String> headers}) async {
    Map<String, String> sendHeader = {};
    if(headers != null) {
      sendHeader.addAll(headers);
    }
    sendHeader.addAll(this.headers);

    Response response = await client.get(url, headers: sendHeader);
    updateCookie(response);
    return response;
  }

  Future<Response> post(Uri url,
      {Map<String, String> headers, Object body, Encoding encoding}) async {
    Map<String, String> sendHeader = {};
    if(headers != null) {
      sendHeader.addAll(headers);
    }
    sendHeader.addAll(this.headers);

    Response response = await client.post(url, headers: sendHeader, body: body, encoding: encoding);
    updateCookie(response);
    return response;
  }

  Future<Response> put(Uri url,
      {Map<String, String> headers, Object body, Encoding encoding}) async {
    Map<String, String> sendHeader = {};
    if(headers != null) {
      sendHeader.addAll(headers);
    }
    sendHeader.addAll(this.headers);

    Response response = await client.put(url, headers: sendHeader, body: body, encoding: encoding);
    updateCookie(response);
    return response;
  }

  Future<Response> patch(Uri url,
      {Map<String, String> headers, Object body, Encoding encoding}) async {
    Map<String, String> sendHeader = {};
    if(headers != null) {
      sendHeader.addAll(headers);
    }
    sendHeader.addAll(this.headers);

    Response response = await client.patch(url, headers: sendHeader, body: body, encoding: encoding);
    updateCookie(response);
    return response;
  }

  Future<Response> delete(Uri url,
      {Map<String, String> headers, Object body, Encoding encoding}) async {
    Map<String, String> sendHeader = {};
    if(headers != null) {
      sendHeader.addAll(headers);
    }
    sendHeader.addAll(this.headers);

    Response response = await client.delete(url, headers: sendHeader, body: body, encoding: encoding);
    updateCookie(response);
    return response;
  }

  void updateCookie(Response response) {
    String rawCookie = response.headers['set-cookie'];
    if (rawCookie != null) {
      int index = rawCookie.indexOf(';');
      headers['cookie'] =
      (index == -1) ? rawCookie : rawCookie.substring(0, index);
    }
  }
}