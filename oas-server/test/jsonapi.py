# -*- coding: utf-8 -*-

import requests
import json

def post(url,data,token=""):
  headers = {'content-type': 'application/json;charset=utf8'}
  if len(token) > 0 :
    headers['token'] = token;
  response=requests.post(url,data=json.dumps(data),headers=headers)
  return response.json()

