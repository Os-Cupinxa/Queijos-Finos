$(document).ready(function () {
    $('#cursosTable').DataTable({
        scrollY: '56vh',
        scrollCollapse: true,
        language: {
            "decimal": ",",
            "thousands": ".",
            "emptyTable": "Nenhum dado disponível na tabela",
            "info": "Mostrando _START_ até _END_ de _TOTAL_ registros",
            "infoEmpty": "Mostrando 0 até 0 de 0 registros",
            "infoFiltered": "(filtrado de _MAX_ registros no total)",
            "infoPostFix": "",
            "lengthMenu": "Mostrando _MENU_ registros",
            "loadingRecords": "Carregando...",
            "processing": "Processando...",
            "search": "",
            "searchPlaceholder": "Pesquisar propriedade",
            "zeroRecords": "Nenhum registro encontrado",
            "paginate": {
                "first": "Primeiro",
                "last": "Último",
                "next": "Próximo",
                "previous": "Anterior"
            },
            "aria": {
                "sortAscending": ": ativar para ordenar a coluna de forma crescente",
                "sortDescending": ": ativar para ordenar a coluna de forma decrescente"
            }
        }
    });
});
