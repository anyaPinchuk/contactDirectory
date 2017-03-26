var searchContactBtn = document.getElementById("searchContactBtn");
searchContactBtn.addEventListener("click", function () {
    var form = document.forms.searchForm;
    var flag = false;
    if ((form.firstName != "") && (form.surname != "") && (form.thirdName != "") && (form.dateOfBirth != "") && (form.citizenship = !"") &&
        (flag.status != "") && (form.country != "") && (form.city != "") && (form.address != "") && (form.indexCode != "")) {
        flag = true;
    } else alert("please fill at least one field");
    var date = form.dateOfBirth;
    if (date.value.match(/^(0?[1-9]|[12][0-9]|3[01])[\-](0?[1-9]|1[012])[\-]\d{4}$/) && date != "") {
        var strings = date.value.split("-");
        date.value = strings[2] + "-" + strings[1] + "-" + strings[0];
    } else {
        //alert("field 'date' is wrong")
    }
    if (flag) {
        form.submit();
}
});