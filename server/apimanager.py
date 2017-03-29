from requestToAPI import RequestToAPI
from dbhandler import DBHandler
import datetime
import json

class APIManager:
    def __init__ (self):
        self.db = DBHandler('localhost', 'root', None, 'PURE')
        self.apiData = json.load(open('api-key.json', 'r'))
        self.req = RequestToAPI(self.apiData)
        self.db.connect()

    def fetchUVI(self, date):
        data = self.req.getData(self.apiData['uvi'], date)
        return data['items']
    
    def fetchPSI(self, date):
        data = self.req.getData(self.apiData['psi'], date)
        return data['region_metadata'], data['items']
    
    def fetchPM25(self, date):
        data = self.req.getData(self.apiData['pm25'], date)
        return data['region_metadata'], data['items']

    def storeUVI(self, data):
        if len(data) == 0 or 'index' not in data[-1]:
            return
        for item in data[-1]['index']:
            date = self.convertDatetime(item['timestamp'])
            query = "INSERT INTO UVI VALUES('{}', {}) ON DUPLICATE KEY UPDATE value={}".format(str(date), item['value'], item['value'])
            self.db.execute(query)
        
        self.db.commit()
        return
    
    def storePSI(self, data, regions=["south", "north", "central", "west", "east", "national"]):
        for item in data:
            date = self.convertDatetime(item['timestamp'])
            reading = item['readings']['psi_twenty_four_hourly']
            parameters = [str(date)]
            query = "INSERT INTO PSI VALUES('{}'" + ", {}" * len(regions) + ") ON DUPLICATE KEY UPDATE "

            for region in regions:
                parameters.append(reading[region])
                query += region + "=" + str(reading[region]) + ", "

            query = (query[:-2] + ";").format(*parameters)
            self.db.execute(query)
        
        self.db.commit()
        return
    
    def storePM25(self, data, regions=["south", "north", "central", "west", "east"]):
        for item in data:
            date = self.convertDatetime(item['timestamp'])
            reading = item['readings']['pm25_one_hourly']
            parameters = [str(date)]
            query = "INSERT INTO PM25 VALUES('{}'" + ", {}" * len(regions) + ") ON DUPLICATE KEY UPDATE "

            for region in regions:
                parameters.append(reading[region])
                query += region + "=" + str(reading[region]) + ", "

            query = (query[:-2] + ";").format(*parameters)
            self.db.execute(query)
        
        self.db.commit()
        return
    
    def convertDatetime(self, datestr):
        return datetime.datetime.strptime(datestr[:-6], '%Y-%m-%dT%H:%M:%S')