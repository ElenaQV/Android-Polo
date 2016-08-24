<?php
 

$response = array();
 

require_once __DIR__ . '/db_connect.php';
 

$db = new DB_CONNECT();
 

if (isset($_GET["idFrase"])) {
    $idFrase = $_GET['idFrase'];
 
    
    $result = mysql_query("SELECT * FROM frases WHERE idFrase = $idFrase");
 
    if (!empty($result)) {
        
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $frase = array();
            $frase["idFrase"] = $result["idFrase"];
            $frase["autor"] = $result["autor"];
            $frase["frase"] = $result["frase"];
            $frase["tipoFrase"] = $result["tipoFrase"];
			$frase["rating"] = $result["rating"];
            $frase["created_at"] = $result["created_at"];
            $frase["updated_at"] = $result["updated_at"];            
            $response["success"] = 1;
 
            
            $response["frase"] = array();
 
            array_push($response["frase"], $frase);
 
            
            echo json_encode($response);
        } else {
            
            $response["success"] = 0;
            $response["message"] = "Frase no  encontrada"; 
            
            echo json_encode($response);
        }
    } else {
        
        $response["success"] = 0;
        $response["message"] = "Frase no  encontrado";
 
        
        echo json_encode($response);
    }
} else {
    
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    
    echo json_encode($response);
}
?>