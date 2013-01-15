<?php
if(isset($_POST['action'])){
	mysql_connect("localhost","root","w5684123");
	mysql_set_charset("utf8");
	mysql_select_db("zoo");
	if($_POST['action']=="commenter"){
		if(isset($_POST['animal'])&&isset($_POST['nom'])&&isset($_POST['contenu'])){
			mysql_query("INSERT INTO commentaires(animal, utilisateur, commentaire) VALUES(".$_POST['animal'].",'".$_POST['nom']."','".$_POST['contenu']."');") or die("commenter error: ".mysql_error());
			echo "success";
		}
		else
		echo "params error";
	}else if($_POST['action']=="favori"){
		if(isset($_POST['animal']))
		mysql_query("UPDATE animaux SET favori=favori+1 WHERE _id=".$_POST['animal'].";") or die("favori error: ".mysql_error());
	}

}else
echo "params error";
?>