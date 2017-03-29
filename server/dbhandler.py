import pymysql

class DBHandler:
    def __init__(self, host, user, password, db):
        self.host = host
        self.user = user
        self.password = password
        self.database = db
        self.conn = None
        self.cursor = None

    def connect(self):
        try:
            self.conn = pymysql.connect(
                host=self.host, 
                user=self.user, 
                passwd=self.password, 
                db=self.database
            )
        except Exception as e:
            print(e.args)
            return False

        self.cursor = self.conn.cursor()
        
        try:
            self.cursor.execute("SELECT VERSION();")
            if self.cursor.fetchone():
                return True
            else:
                return False
        except Exception as e:
            print(e.args)

        return False
    
    def commit(self):
        if self.conn is None:
            return
        
        self.conn.commit()

    def close(self):
        if self.cursor is None:
            return
        self.cursor.close()
        if self.conn is None:
            return
        self.conn.close()
    
    def execute(self, query):
        if self.cursor is None:
            return None
        
        try:
            self.cursor.execute(query)
            return self.cursor.fetchall()
        except Exception as e:
            print(e.args)
            return None