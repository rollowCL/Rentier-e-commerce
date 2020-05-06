$(() => {
    let context = $('#contextPath').val();
    let buttons = $('.action-button');

    buttons.each(function () {

        $(this).on("click", function () {
            let imageId = $(this).data('id');
            let action = $(this).data('action');
            let mainImage = $(this).data('main');
            let row = $(this).parent().parent();

            if (action === "DELETE") {
                if (mainImage === true) {
                    window.alert("Nie możesz usunąć zdjęcia głównego");

                } else {

                    let confirmed = window.confirm("Czy na pewno chcesz usunąć to zdjęcie?");
                    if (confirmed === true) {
                        $.ajax({url: context + "/image/del/" + imageId, method: action})
                            .done(function () {
                                row.remove();
                            });
                        return true;
                    } else {
                        return false;
                    }

                }

            } else if (action === "PUT") {

                if (mainImage) {
                    window.alert("To zdjęcie jest już ustawione jako główne");
                } else {

                    let confirmed = window.confirm("Czy na pewno chcesz ustawić to zdjęcie jako główne?");
                    if (confirmed === true) {
                        $.ajax({url: context + "/image/main/" + imageId, method: action})
                            .done(function () {

                                buttons.each(function() {
                                    if ($(this).data('id') === imageId) {
                                        $(this).data('main', true);
                                    } else {
                                        $(this).data('main', false);
                                    }
                                });

                                let icons = $('#image-table-body i');
                                icons.each(function () {
                                    if ($(this).data('id') === imageId) {
                                        $(this).removeClass("icon-line-square-cross");
                                        $(this).addClass("icon-line-square-check");
                                    } else {
                                        $(this).removeClass("icon-line-square-check");
                                        $(this).addClass("icon-line-square-cross");

                                    }
                                });
                            });
                        return true;
                    } else {
                        return false;
                    }

                }


            }

        });

    });

});
