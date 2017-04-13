<?php
	include "request.php";
	if (!isset($_GET["type"])) {
		echo "[]";
		return;
	}
	
	$arr = array("UVI", "PSI", "PM25");
	$db = new DBHandler();
	
	$db->connect();
	
	if (in_array($_GET["type"], $arr)) {
		$query = "SELECT date";
		
		if (in_array($_GET["type"], array("PSI", "PM25"))) {
			$region = "central";
			if (isset($_GET["region"]))
				if (in_array($_GET["region"], array("south", "north", "central", "west", "east")))
					$region = $_GET["region"];
			
			$query .= ", " . $region;
		} 
		
		if ($_GET["type"] == "UVI") $query .= ", value";
		
		
		$query .= " AS value FROM " . $_GET["type"];
		$opt = false;
		if (isset($_GET["start"])) {
			if (!$opt) {
				$opt = true;
				$query .= " WHERE";
			} else $query .= " AND";
			$query .= " '" . $_GET["start"] . "' <= date";
		}
		
		if (isset($_GET["end"])) {
			if (!$opt) {
				$opt = true;
				$query .= " WHERE";
			} else $query .= " AND";
			$query .= " date <= '" . $_GET["end"] . "'";
		}
		
		$query .= ";";
		
		$res = $db->execute($query);
		$ret = array();
		while ($row = $res->fetch_assoc()) {
			$ret[] = $row;
		}
		
		echo json_encode(array("data"=>$ret, "type"=>$_GET["type"]));
	} else echo "[]";
	
	$db->close();
?>