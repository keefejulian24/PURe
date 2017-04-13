from requestToAPI import RequestToAPI
from dbhandler import DBHandler
import datetime
import json

class APIManager:
    def __init__ (self, server, username, password, dbname):
        self.db = DBHandler(server, username, password, dbname)
        self.apiData = json.load(open('api-key.json', 'r'))
        self.req = RequestToAPI(self.apiData)

    def connectDB(self):
        self.db.connect()
    
    def closeDB(self):
        self.db.close()

    def fetchUVI(self, date):
        data = self.req.getData(self.apiData['uvi'], date)
        return data['items']
    
    def fetchPSI(self, date):
        data = self.req.getData(self.apiData['psi'], date)
        return data['region_metadata'], data['items']
    
    def fetchPM25(self, date):
        data = self.req.getData(self.apiData['pm25'], date)
        return data['region_metadata'], data['items']
    
    def createPrediction(self, date):
        for i in range(24):
            self.db.execute("INSERT INTO UVI VALUES('{}', 0);".format(str(date + datetime.timedelta(hours=i))))
            self.db.execute("INSERT INTO PSI VALUES('{}', 0, 0, 0, 0, 0, 0);".format(str(date + datetime.timedelta(hours=i))))        
            self.db.execute("INSERT INTO PM25 VALUES('{}', 0, 0, 0, 0, 0);".format(str(date + datetime.timedelta(hours=i))))
            self.db.commit()
        
        for i in range(24):
            query = "UPDATE UVI SET value = (SELECT IF(ISNULL(t.a), 0, round(t.a)) FROM (SELECT avg(value) AS a FROM UVI WHERE HOUR(TIME(date)) = {} ORDER BY date DESC LIMIT 100) t) WHERE HOUR(TIME(date)) = {} AND date >= '{}';".format(i, i, str(date))
            self.db.execute(query)
            for type in ['south', 'north', 'central', 'west', 'east']:
                query = "UPDATE PSI SET {} = (SELECT * FROM (SELECT ROUND(AVG({})) FROM PSI WHERE HOUR(TIME(date)) = {} ORDER BY date DESC LIMIT 10) t) WHERE HOUR(TIME(date)) = {} AND date > '{}'".format(type, type, i, i, str(date))
                self.db.execute(query)
                query = "UPDATE PM25 SET {} = (SELECT * FROM (SELECT ROUND(AVG({})) FROM PM25 WHERE HOUR(TIME(date)) = {} ORDER BY date DESC LIMIT 10) t) WHERE HOUR(TIME(date)) = {} AND date > '{}'".format(type, type, i, i, str(date))
                self.db.execute(query)
            
            for type in ['south', 'north', 'central', 'west', 'east']:
                query = "UPDATE PSI SET {} = (SELECT * FROM (SELECT {} FROM PSI WHERE HOUR(TIME(date)) = 23 ORDER BY date DESC LIMIT 1) t) WHERE HOUR(TIME(date)) = 0 AND date > '{}'".format(type, type, str(date))
                self.db.execute(query)
                query = "UPDATE PM25 SET {} = (SELECT * FROM (SELECT {} FROM PM25 WHERE HOUR(TIME(date)) = 23 ORDER BY date DESC LIMIT 1) t) WHERE HOUR(TIME(date)) = 0 AND date > '{}'".format(type, type, str(date))
                self.db.execute(query)
            self.db.commit()
        
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