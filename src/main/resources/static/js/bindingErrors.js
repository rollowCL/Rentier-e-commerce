$(() => {

    let errors = $(".error");

    let formElements = $(" .form-control");

    $.each(formElements, function (key, value) {

        let name = value.name + ".errors";

        for (let error of errors) {

            if (error.id === name) {

                $(this).css("border-color", "red");

            }


        }
    })


});