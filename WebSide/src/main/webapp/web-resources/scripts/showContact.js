window.onload = function () {
    var rows_phones = document.getElementsByClassName("rows_phones");
    var rows_attach = document.getElementsByClassName("rows_attach");
    if (rows_phones.length === 0) {
        document.querySelector("#phones").style.display = "none";
    }
    if (rows_attach.length === 0) {
        document.querySelector("#attachments").style.display = "none";
    }
};