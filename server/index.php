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
		$query = "SELECT * FROM " . $_GET["type"];
		
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
		
		echo json_encode($ret);
	} else echo "[]";
	
	$db->close();
?>