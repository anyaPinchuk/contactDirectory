function deleteContact(){
    var url = "deleteContact";
    var params = "lorem=ipsum&name=alpha";
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);

    xhr.setRequestHeader("Content-type", "");

    xhr.send(params);
}

function addPhone() {
    console.log("hi");
    window.open('addPhone.html',"width=800,height=750,resizable=yes,scrollbars=yes");
}

