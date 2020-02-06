$(() => {

    let returnToTag = "#" + $('#returnToTag').text();

    let index = $('#tabs-profile a[href=returnToTag]').parent().index();
    $('#tabs-profile').tabs({ active: index - 2});


});