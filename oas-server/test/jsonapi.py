# -*- coding: utf-8 -*-

import requests
import json

def post(url,data,token=""):
  headers = {'content-type': 'application/json;charset=utf8'}
  if len(token) > 0 :
    headers['token'] = token;
  
  s=json.dumps(data)
  data=json.loads(s)
  response=requests.post(url,data,headers=headers)
  return response.json()

