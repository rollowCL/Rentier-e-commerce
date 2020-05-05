$(() => {

    let errors = $(".error");

    let formElements = $(" .form-control");

    $.each(formElements, function (key, value) {

        let name = value.id + ".errors";
        console.log("Name: " + name);

        for (let error of errors) {
            console.log("Error_id: " + error.id);
            if (error.id === name) {

                $(this).css("border-color", "red");

            }


        }
    })


});
