$('#tecnologiaSelect').select2({
    placeholder: 'Selecione uma tecnologia',
    tags: true,
    createTag: function (params) {
        return {
            id: params.term,
            text: params.term,
            value: params.term,
            newOption: true
        };
    },
    templateResult: function (data) {
        var $result = $("<span></span>");

        $result.text(data.text);

        if (data.newOption) {
            $result.append(" <em>(Novo)</em>");
            $result.html($('<strong>', {html: $result.html()}));
        }

        return $result;
    },
    insertTag: function (data, tag) {
        data.push(tag);
    }
});

document.getElementById('formularioPropriedade').addEventListener('submit', function (event) {
    event.preventDefault();
    cadastrarPropriedade();
});

function removerCurso(id) {
    var dataTable = $('#cursosTable').DataTable();
    dataTable.row($("#" + id)).remove().draw();

}

function removerFornecedor(id) {
    const dataTable = $('#fornecedorTable').DataTable();
    dataTable.row($("#" + id)).remove().draw();
}

function removerTecnologia(id) {
    const dataTable = $('#tecnologiasTable').DataTable();
    dataTable.row($("#" + id)).remove().draw();
}

function adicionarTecnologia() {
    const select = document.getElementById("tecnologiaSelect");

    const text = select.options[select.selectedIndex].text;
    const id = select.options[select.selectedIndex].value;

    if (text !== "") {
        var tecnologias = document.getElementsByClassName("tecnologiaAdd")
        var flag = 0
        for (tecnologia of tecnologias) {
            if (tecnologia.id === id) {
                flag = 1
            }
        }
        if (flag === 0) {
            var table = new DataTable("#tecnologiasTable")
            table.row
                .add([
                    text,
                    `<button class="squared-button delete"
                                 onclick="removerTecnologia('${id}');">
                             <i class="fa-solid fa-trash"></i>
                         </button>`
                ])
                .draw(false);

            const linhas = document.getElementById("tecnologiasTable").children[1].children;
            for (let linha of linhas) {

                if (linha.children[0].innerText === text) {
                    linha.setAttribute("id", id)
                    linha.className = 'tecnologiaAdd'
                    linha.children[0].setAttribute("id", id)
                    linha.children[0].className = `sorting_1 tecnologiaID`
                    break;
                }

            }

        }
    }

}

function adicionarFornecedores() {
    const supplierChecks = document.getElementsByClassName("fornecedorADDcheck");

    for (const supplierCheck of supplierChecks) {
        if (supplierCheck.checked) {
            if (!isSupplierAlreadyAdded(supplierCheck)) {
                const supplierInfo = getSupplierInfo(supplierCheck.id);
                addSupplierToTable(supplierCheck.id, supplierInfo);
            }
        }
    }
}

function isSupplierAlreadyAdded(supplierCheck) {
    const existingSupplierRows = document.getElementsByClassName("fornecedorID");
    for (const row of existingSupplierRows) {
        if (row.id === supplierCheck.id) {
            return true;
        }
    }
    return false;
}

function getSupplierInfo(supplierId) {
    return Array.from(document.getElementsByClassName(`fornecedorADD ${supplierId}`)).map(info => info.innerText);
}

function addSupplierToTable(supplierId, supplierInfo) {
    const table = new DataTable("#fornecedorTable");
    table.row
        .add([
            supplierInfo[0],
            supplierInfo[1],
            supplierInfo[2],
            supplierInfo[3],
            getDeleteButtonHtml(supplierId)
        ])
        .draw(false);

    updateRowAttributes(supplierId, supplierInfo[0]);
}

function getDeleteButtonHtml(supplierId) {
    return `<button class="squared-button delete" onclick="removeSupplier(${supplierId});">
              <i class="fa-solid fa-trash"></i>
            </button>`;
}

function updateRowAttributes(supplierId, supplierName) {
    const rows = document.getElementById("fornecedorTable").children[1].children;
    for (const row of rows) {
        if (row.children[0].innerText === supplierName) {
            row.id = supplierId;
            row.children[0].id = supplierId;
            row.children[0].className = 'sorting_1 fornecedorID';
            break;
        }
    }
}

function adicionarCursos() {
    const selects = document.getElementsByClassName("cursoADDcheck");
    for (let check of selects) {
        if (check.checked) {
            if (!isCursoAlreadyAdded(check.id)) {
                const infos = getCursoInfos(check.id);
                addCursoToTable(infos, check.id);
                updateCursoRow(infos, check.id);
            }
        }
    }
}

function isCursoAlreadyAdded(checkId) {
    const cursosSelect = document.getElementsByClassName("cursoID");
    for (let curso of cursosSelect) {
        if (curso.id === checkId) {
            return true;
        }
    }
    return false;
}

function getCursoInfos(checkId) {
    return document.getElementsByClassName(`cursoADD ${checkId}`);
}

function addCursoToTable(infos, checkId) {
    var table = new DataTable(`#cursosTable`);
    table.row
        .add([
            infos[0].innerText,
            infos[1].innerText,
            infos[2].innerText,
            infos[3].innerText,
            `<button class="squared-button delete" onclick="removerCurso(${checkId});">
                 <i class="fa-solid fa-trash"></i>
             </button>`
        ])
        .draw(false);
}

function updateCursoRow(infos, checkId) {
    const linhas = document.getElementById("cursosTable").children[1].children;
    for (let linha of linhas) {
        if (linha.children[0].innerText === infos[0].innerText) {
            linha.setAttribute("id", checkId);
            linha.children[0].setAttribute("id", checkId);
            linha.children[0].className = "sorting_1 cursoID";
            break;
        }
    }
}

function cadastrarPropriedade() {
    cursosIds = []
    allCursos = []
    cursos = document.getElementsByClassName("cursoID")

    for (let item of cursos) {
        cursosIds.push(item.id)
    }

    for (let cursoId of cursosIds) {
        allCursos.push({
            id: parseInt(cursoId),
            nome: null,
            conteudo: null,
            professor: null,
            duracao: null,
        })
    }

    fornecedoresIds = []
    allFornecedores = []
    fornecedores = document.getElementsByClassName("fornecedorID")

    for (let item of fornecedores) {
        fornecedoresIds.push(item.id)
    }

    for (let fornecedorId of fornecedoresIds) {
        allFornecedores.push({
            id: parseInt(fornecedorId),
            nome: null,
            email: null,
            nicho: null,
            qualidade: null,
        })
    }

    tecnologiasIds = []
    allTecnologias = []
    tecnologias = document.getElementsByClassName("tecnologiaID")

    for (let item of tecnologias) {
        tecnologiasIds.push({id: item.id, nome: item.innerText})
    }

    for (let tecnologiaId of tecnologiasIds) {
        allTecnologias.push({
            id: parseInt(tecnologiaId.id),
            nome: tecnologiaId.nome,
            observacao: null,
        })
    }


    $.ajax({
        url: '/propriedade',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            idPropriedade: document.getElementById('id').value,
            nomePropriedade: document.getElementById('nome').value,
            email: document.getElementById('email').value,
            status: parseInt(document.getElementById('status').value),
            cpf: document.getElementById('cpf').value,
            cnpj: document.getElementById('cnpj').value,
            telefone: document.getElementById('telefone').value,
            celular: document.getElementById('celular').value,
            rua: document.getElementById('rua').value,
            bairro: document.getElementById('bairro').value,
            cidade: document.getElementById('cidade').value,
            uf: document.getElementById('uf').value,
            latitude: document.getElementById('latitude').value,
            longitude: document.getElementById('longitude').value,
            nomeProdutor: document.getElementById('nomeProdutor').value,
            cursos: allCursos,
            fornecedores: allFornecedores,
            tecnologias: allTecnologias
        }),
        success: function (response) {
            window.location.href = '/propriedade';
        },
        error: function (xhr, status, error) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Ocorreu um erro ao cadastrar a propriedade.'
            });
        }
    });
}

function handleDelete(id) {
    $.ajax({
        url: '/propriedade/delete/' + id,
        type: 'POST',
        data: {_method: 'POST'},
        success: function () {
            window.location.href = '/propriedade';
        },
        error: function (xhr, status, error) {
            console.error(error);
        }
    });
}
