<?php
 

$response = array();
 

if (isset($_POST['idFrase']) && isset($_POST['autor']) && isset($_POST['frase']) && isset($_POST['tipoFrase'])&& isset($_POST['rating'])) {
 
    $idFrase = $_POST['idFrase'];
    $autor = $_POST['autor'];
    $frase = $_POST['frase'];
    $tipoFrase = $_POST['tipoFrase'];
	$rating = $_POST['rating'];
 
    
    require_once __DIR__ . '/db_connect.php';
 
  
    $db = new DB_CONNECT();
 
    
    $result = mysql_query("UPDATE frases SET autor = '$autor', frase = '$frase', tipoFrase = '$tipoFrase', rating = '$rating' WHERE idFrase = $idFrase");
 
    
    if ($result) {
        
        $response["success"] = 1;
        $response["message"] = "Frase correctamente actualizada.";
 
        
        echo json_encode($response);
    } else {
 
    }
} else {
    
    $response["success"] = 0;
    $response["message"] = "Campo requerido";
 
    
    echo json_encode($response);
}
?>