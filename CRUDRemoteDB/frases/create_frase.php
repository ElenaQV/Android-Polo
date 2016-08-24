<?php
 

$response = array();
 

if (isset($_POST['autor']) && isset($_POST['frase']) && isset($_POST['tipoFrase'])&& isset($_POST['rating'])) {
 
    $autor = $_POST['autor'];
	$frase = $_POST['frase'];
    $tipoFrase = $_POST['tipoFrase'];
    $rating = $_POST['rating'];
 
    
    require_once __DIR__ . '/db_connect.php';
 
    
    $db = new DB_CONNECT();
 
    
    $result = mysql_query("INSERT INTO frases(autor, frase, tipoFrase,rating) VALUES('$autor', '$frase', '$tipoFrase','$rating')");
 
    
    if ($result) {
        
        $response["success"] = 1;
        $response["message"] = "Frase creada exitosamente.";
 
        
        echo json_encode($response);
    } else {
        
        $response["success"] = 0;
        $response["message"] = "¡Error!.";
 
        
        echo json_encode($response);
    }
} else {
    
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    
    echo json_encode($response);
}
?>