var searchContactBtn = document.getElementById("searchContactBtn");
searchContactBtn.addEventListener("click", function () {
    var form = document.forms.searchForm;
    var element = document.getElementById("notifyDiv");
    var dateFrom = form.dateFrom;
    var dateTo = form.dateTo;
    if(validate(element, form.firstName, form.surname, form.thirdName, form.citizenship, form.status, form.country, form.city,
        form.address, form.indexCode) && validDate(element, dateFrom) && validDate(element, dateTo)) {
        form.submit();
    }
});