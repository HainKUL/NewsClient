<?php

global $connection;
define("DB_HOST","studev.groept.be");
    define("DB_USER","a17_sd606");
    define("DB_PASSWORD","a17_sd606");
    define("DB_DATABASE","a17_sd606");

    $connection = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

    if(mysqli_connect_errno()){
        die("Database connnection failed " . "(" .
            mysqli_connect_error() . " - " . mysqli_connect_errno() . ")"
                );
    }
// $upload_path = 'Image/'; //this is our upload folder
// $server_ip = 'http://a17-sd606.studev.groept.be/'; //Getting the server ip
$upload_url = 'http://a17-sd606.studev.groept.be/Image/'; //upload url

//response array
$response = array();


if($_SERVER['REQUEST_METHOD']=='POST'){

    //checking the required parameters from the request
    if(isset($_POST['caption']))
    {

        $caption = $_POST['caption'];
        $fileinfo = pathinfo($_FILES['image']['name']);//getting file info from the request
        $extension = $fileinfo['extension']; //getting the file extension
        $file_url = $upload_url . getFileName() . '.' . $extension; //file url to store in the database
        $file_path = $upload_path . getFileName() . '.'. $extension; //file path to upload in the server
        $img_name = getFileName() . '.'. $extension; //file name to store in the database

        try{
            move_uploaded_file($_FILES['image']['tmp_name'],$file_path); //saving the file to the uploads folder;

            //adding the path and name to database
            $sql = "INSERT INTO Photos(photo_name, photo_url, caption) ";
            $sql .= "VALUES ('{$img_name}', '{$file_url}', '{$caption}');";

            if(mysqli_query($connection,$sql)){
                //filling response array with values
                $response['error'] = false;
                $response['photo_name'] = $img_name;
                $response['photo_url'] = $file_url;
                $response['caption'] = $caption;
            }
            //if some error occurred
        }catch(Exception $e){
            $response['error']=true;
            $response['message']=$e->getMessage();
        }
        //displaying the response
        echo json_encode($response);

        //closing the connection
        mysqli_close($connection);
    }else{
        $response['error'] = true;
        $response['message']='Please choose a file';
    }
}

/*
We are generating the file name
so this method will return a file name for the image to be uploaded
*/
function getFileName(){
    global $connection;

    $sql = "SELECT max(id) as id FROM Photos";
    $result = mysqli_fetch_array(mysqli_query($connection, $sql));

    if($result['id']== null)
        return 1;
    else
        return ++$result['id'];

    mysqli_close($connection);
}
?>
