$(() => {

    let deliverySelect = $('#deliveryMethodSelect');

    deliverySelect.change(function () {
        let deliveryCost = parseFloat($(this).children("option:selected").val());
        let productsCost = parseFloat($('#sumProductsCost').text());
        $("#sumDeliveryCost").text(deliveryCost);
        let sum = deliveryCost + productsCost;
        $('#totalValue').text(sum);
    });

});
