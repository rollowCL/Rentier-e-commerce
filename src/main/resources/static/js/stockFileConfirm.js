$(() => {
    let stockFileConfirm = $('#stockFileConfirm');
    stockFileConfirm.on('click', function (event) {
        let stockFile = $('#stockFile').val();
        let fileName= (stockFile.substring(stockFile.lastIndexOf('\\') + 1, stockFile.length));
        if (!window.confirm("Czy na pewno chcesz zaktualizować stan magazynowy z pliku " + fileName)) {
            event.preventDefault();
        }

    })

});
