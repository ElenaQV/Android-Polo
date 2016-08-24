<?php
 
$response = array();
 

if (isset($_POST['idFrase'])) {
    $idFrase = $_POST['idFrase'];
 
    
    require_once __DIR__ . '/db_connect.php';
 
   
    $db = new DB_CONNECT();
 
    
    $result = mysql_query("DELETE FROM frases WHERE idFrase = $idFrase");
 
    
    if (mysql_affected_rows() > 0) {
        
        $response["success"] = 1;
        $response["message"] = "Frase actualmente actualizado";
 
        
        echo json_encode($response);
    } else {
        
        $response["success"] = 0;
        $response["message"] = "Frase no  encontrado";
 
        
        echo json_encode($response);
    }
} else {
    
    $response["success"] = 0;
    $response["message"] = "Campos requeridos";
 
    
    echo json_encode($response);
}
?>