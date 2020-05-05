$(() => {

    let images = $('.product-multi-image');
    let mainImage = $('#mainImage');

    images.each(function(index) {
        $(this).on("click", function(){
            currentMainSrc = mainImage.attr('src');
            newMainSrc = $(this).attr('src');

            mainImage.attr('src', newMainSrc);
            $(this).attr('src', currentMainSrc);
        });

    });

});
