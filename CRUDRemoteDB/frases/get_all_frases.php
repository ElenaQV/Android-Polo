<?php
 
$response = array();
 

require_once __DIR__ . '/db_connect.php';
 

$db = new DB_CONNECT();
 

$result = mysql_query("SELECT * FROM frases") or die(mysql_error());
 

if (mysql_num_rows($result) > 0) {
    
    $response["frases"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        
        $frase = array();
        $frase["idFrase"] = $row["idFrase"];
        $frase["autor"] = $row["autor"];
        $frase["frase"] = $row["frase"];
		$frase["tipoFrase"] = $row["tipoFrase"];
		$frase["rating"] = $row["rating"];
        $frase["created_at"] = $row["created_at"];
        $frase["updated_at"] = $row["updated_at"];
 
        
        array_push($response["frases"], $frase);
    }
    
    $response["success"] = 1;
 
    
    echo json_encode($response);
} else {
    
    $response["success"] = 0;
    $response["message"] = "Frases no  encontradas";
 
    
    echo json_encode($response);
}
?>