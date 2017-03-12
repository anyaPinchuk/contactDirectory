function deleteContact(){
    var url = "deleteContact";
    var params = "lorem=ipsum&name=alpha";
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);

    xhr.setRequestHeader("Content-type", "");

    xhr.send(params);
}

