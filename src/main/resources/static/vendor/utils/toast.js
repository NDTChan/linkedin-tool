(function ($) {
    $.fn.hsToast = function (event, options) {
        // This is the easiest way to have default options.
        const settings = $.extend({
            // These are the defaults.
            typeOpt: "bg-primary",
            placement: "top-0 end-0"
        }, options);

        const toastPlacementExample = document.querySelector('.toast-placement-ex');
        let selectedType, selectedPlacement, toastPlacement;

        if (event === 'show') {
            if (toastPlacement) {
                toastDispose(toastPlacement);
            }
            selectedType = settings.typeOpt;
            selectedPlacement = settings.placement.split(' ');

            toastPlacementExample.classList.add(selectedType);
            DOMTokenList.prototype.add.apply(toastPlacementExample.classList, selectedPlacement);
            toastPlacement = new bootstrap.Toast(toastPlacementExample);
            toastPlacement.show();
        }

        if (event === 'hide') {
            toastDispose(toastPlacement);
        }


        // Dispose toast when open another
        function toastDispose(toast) {
            if (toast && toast._element !== null) {
                if (toastPlacementExample) {
                    toastPlacementExample.classList.remove(selectedType);
                    DOMTokenList.prototype.remove.apply(toastPlacementExample.classList, selectedPlacement);
                }
                toast.dispose();
            }
        }

    };
}(jQuery));