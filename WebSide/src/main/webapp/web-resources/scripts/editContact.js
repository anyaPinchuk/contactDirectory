var modalPhoto = document.getElementById('myModalPhoto');
var btnPhoto = document.getElementById("photo");
var span = document.getElementsByClassName("close")[0];
btnPhoto.onclick = function () {
    modalPhoto.style.display = "block";
};
span.onclick = function () {
    modalPhoto.style.display = "none";
};

////=-------------photo------------///

function savePhoto(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById("photo").src = e.target.result;
        };
        reader.readAsDataURL(input.files[0]);
    }
    span.click();
}

//-------------Handlers--------//