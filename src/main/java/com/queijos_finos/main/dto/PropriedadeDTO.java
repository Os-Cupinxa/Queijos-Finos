package com.queijos_finos.main.dto;

import com.queijos_finos.main.model.Contrato;
import com.queijos_finos.main.model.Tecnologias;

import java.util.List;

public record PropriedadeDTO(String name, String owner, String city, String state, Integer status, String latitude,
                             String longitude, List<Contrato> contractsActive, List<Tecnologias> tecnologiasList) {
}
