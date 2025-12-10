$(document).ready(function () {
    $('.datatable').DataTable({
        responsive: true,
        order: [],
        language: {
            "decimal": "",
            "emptyTable": "No hay información",
            "info": "Mostrando _START_ a _END_ de _TOTAL_ Entradas",
            "infoEmpty": "Mostrando 0 to 0 of 0 Entradas",
            "infoFiltered": "(Filtrado de _MAX_ total entradas)",
            "infoPostFix": "",
            "thousands": ",",
            "lengthMenu": "Mostrar _MENU_ registros",
            "loadingRecords": "Cargando...",
            "processing": "Procesando...",
            "search": "Buscar:",
            "zeroRecords": "Sin resultados encontrados",
            "paginate": {
                "first": "Primero",
                "last": "Último",
                "next": "Siguiente",
                "previous": "Anterior"
            }
        },
        columnDefs: [
            { className: "px-6 py-2", targets: "_all" },
            { orderable: false, targets: -1 }
        ],
        initComplete: function () {
            $('.dt-search input').addClass('border-gray-300 focus:border-primary focus:ring-primary');
            $('.dt-length select').addClass('border-gray-300 rounded-lg focus:border-primary focus:ring-primary ml-2 mr-2');
        }
    });
});
