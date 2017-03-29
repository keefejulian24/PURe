import requests
import json
import datetime

class RequestToAPI:
    def __init__(self, apiData):
        self.PATH = apiData['path']
        self.KEY = apiData['api-key']
    
    def getData(self, state, date):
        r = requests.get(
            self.PATH + state, 
            headers = {'api-key': self.KEY}, 
            params = {'date': str(date)[0:10]}
        )

        ret = r.json()

        if 'api_info' not in ret or 'status' not in ret['api_info']:
            return None

        if ret['api_info']['status'] == 'healthy':
            return ret
        else:
            return None # something wrong happened
