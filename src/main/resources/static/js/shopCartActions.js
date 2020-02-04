$(() => {

    let sizeSelect = $('#sizeSelect');
    let quantity = $('#quantity');
    let addToCartButton = $('#addToCartButton');


    sizeSelect.on('change', function (event) {

        if(sizeSelect.val() !== '0') {

            let option = sizeSelect.children(`option[value=${sizeSelect.val()}]`);
            quantity.attr('max', option.data('max'));

        }

    });

    addToCartButton.on('click', function (event) {

        if(sizeSelect.val() === '0') {
            event.preventDefault();
            alert("Musisz wybrać rozmiar");

        } else {
            let option = sizeSelect.children(`option[value=${sizeSelect.val()}]`);

            if (quantity.val() > option.data('max')) {
                event.preventDefault();
                alert("Brak w magazynie takiej ilości w tym rozmiarze");
            }

        }

    });

});