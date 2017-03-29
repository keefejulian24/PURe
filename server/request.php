<?php 
	class DBHandler {
		private $dbHost = "localhost";
		private $dbUser = "pure";
		private $dbPassword = "";
		private $dbName = "pure";
		private $dbConn;
		
		public function connect() {
			$this->dbConn = new mysqli(
										$this->dbHost, 
										$this->dbUser, 
										$this->dbPassword,
										$this->dbName
							);
			if ($this->dbConn->connect_error) {
				die("Connection failed: ".$this->dbConn->connect_error);
			}
		}
		
		public function close() {
			if (!isset($this->dbConn)) 
				return;
			$this->dbConn->close();
		}
		
		public function execute($query) {
			$result = $this->dbConn->query($query);
			if (!$result) {
				die("Error: ".$this->dbConn->error);
			} else return $result;
		}
	}
?>
