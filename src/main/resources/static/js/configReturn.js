$(() => {

    let tab = "#" + $('#tab').val();
    console.log(tab);
    $('a[href="' + tab + '"]').click();

});
